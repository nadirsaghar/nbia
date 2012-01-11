package gov.nih.nci.nbia.servlet;

import gov.nih.nci.nbia.DownloadProcessor;
import gov.nih.nci.nbia.dto.AnnotationDTO;
import gov.nih.nci.nbia.dto.ImageDTO2;
import gov.nih.nci.security.util.StringEncrypter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author lethai
 *
 */
public class DownloadServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(DownloadServlet.class);

    public void doPost(HttpServletRequest request,
             HttpServletResponse response) throws ServletException,IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request,
              HttpServletResponse response) throws ServletException,IOException {
        String seriesUid = request.getParameter("seriesUid");
        String userId = request.getParameter("userId");
        String password = request.getHeader("password");
        Boolean includeAnnotation = Boolean.valueOf(request.getParameter("includeAnnotation"));
        Boolean hasAnnotation = Boolean.valueOf(request.getParameter("hasAnnotation"));
        String sopUids = request.getParameter("sopUids");

        logger.info("sopUids:"+sopUids);
        logger.info("seriesUid: " + seriesUid + " userId: " + userId + " includeAnnotation: " + includeAnnotation + " hasAnnotation: " + hasAnnotation);

        processRequest(response,
                       seriesUid,
                       userId,
                       password,
                       includeAnnotation,
                       hasAnnotation,
                       sopUids);
    }
    protected void processRequest(HttpServletResponse response,
            String seriesUid,
            String userId,
            String password,
            Boolean includeAnnotation,
            Boolean hasAnnotation,
            String sopUids) throws IOException{

        DownloadProcessor processor = new DownloadProcessor();
        List<AnnotationDTO> annoResults= new ArrayList<AnnotationDTO>();
        boolean hasAccess=false;

        try{
            password = decrypt(password);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        List<ImageDTO2> imageResults = processor.process(seriesUid, sopUids);
        if((imageResults == null) || imageResults.isEmpty() ){
            //no data for this series
            logger.info("no data for series: " +seriesUid);
        }else{
            hasAccess = processor.hasAccess(userId, password, imageResults.get(0).getProject(), imageResults.get(0).getSite(), imageResults.get(0).getSsg());
        }

        if(hasAccess){
            if(includeAnnotation && hasAnnotation){
                annoResults = processor.process(seriesUid);
            }
            sendResponse(response, imageResults, annoResults);
            //compute the size for this series
            long size = computeContentLength(imageResults, annoResults);
            try{
                processor.recordDownload(seriesUid, userId, size);
            }catch(Exception e){
                logger.error("Exception recording download "  + e.getMessage());
            }
        }
    }
    private void sendResponse(HttpServletResponse response,
            List<ImageDTO2> imageResults,
            List<AnnotationDTO> annoResults) throws IOException {

        TarArchiveOutputStream tos = new TarArchiveOutputStream(response.getOutputStream());

        try {
            long start = System.currentTimeMillis();

            logger.info("images size: " + imageResults.size() + " anno size: " + annoResults.size());

            sendImagesData(imageResults, tos);
            sendAnnotationData(annoResults, tos);

            logger.info("total time to send  files are " + (System.currentTimeMillis() - start)/1000 + " ms.");
        }
        finally {
            IOUtils.closeQuietly(tos);
        }
    }

    private void sendImagesData(List<ImageDTO2> imageResults,
                                TarArchiveOutputStream tos) throws IOException {

        InputStream dicomIn = null;
        for (ImageDTO2 imageDto : imageResults){
             String filePath = imageDto.getFileName();
             String sop = imageDto.getSOPInstanceUID();

             logger.info("filepath: " + filePath + " filename: " + sop);
             try {
                  File dicomFile = new File(filePath);
                  ArchiveEntry tarArchiveEntry = tos.createArchiveEntry(dicomFile, sop + ".dcm");

                  tos.putArchiveEntry(tarArchiveEntry);

                  dicomIn = new FileInputStream(dicomFile);

                  IOUtils.copy(dicomIn, tos);

                  tos.closeArchiveEntry();
             } finally {
                  IOUtils.closeQuietly(dicomIn);
                  logger.info("DownloadServlet Image transferred at " + new Date().getTime());
             }
        }
    }

    private void sendAnnotationData(List<AnnotationDTO> annoResults,
                                    TarArchiveOutputStream tos) throws IOException {
        InputStream annoIn = null;
        for(AnnotationDTO a : annoResults){
            String filePath = a.getFilePath();
            String fileName = a.getFileName();

            try {
                File annotationFile = new File(filePath);
                ArchiveEntry tarArchiveEntry = tos.createArchiveEntry(annotationFile, fileName);
                tos.putArchiveEntry(tarArchiveEntry);
                annoIn = new FileInputStream(annotationFile);
                IOUtils.copy(annoIn, tos);
                tos.closeArchiveEntry();
            }
            finally {
                IOUtils.closeQuietly(annoIn);
                logger.info("DownloadServlet Annotation transferred at "
                                   + new Date().getTime());
            }
        }
    }
    private String decrypt(String password) throws Exception{
        StringEncrypter decrypter = new StringEncrypter();
        if(password.equals("")){
            return "";
        }else{
            return decrypter.decrypt(password);
        }
    }
    private static long computeContentLength(List<ImageDTO2> imageResults,
                    List<AnnotationDTO> annoResults) {
        long contentSize=0;
        for(ImageDTO2 imageDto : imageResults){
            contentSize += imageDto.getDicomSize();
        }
        for(AnnotationDTO annoDto : annoResults){
            contentSize += annoDto.getFileSize();
        }
        return contentSize;
    }
}