package gov.nih.nci.nbia.dbadapter;

import gov.nih.nci.nbia.domain.operation.AnnotationOperationInterface;
import gov.nih.nci.nbia.domain.operation.CTImageOperationInterface;
import gov.nih.nci.nbia.domain.operation.GeneralEquipmentOperationInterface;
import gov.nih.nci.nbia.domain.operation.GeneralImageOperationInterface;
import gov.nih.nci.nbia.domain.operation.ImageSubmissionHistoryOperationInterface;
import gov.nih.nci.nbia.domain.operation.PatientOperationInterface;
import gov.nih.nci.nbia.domain.operation.SeriesOperationInterface;
import gov.nih.nci.nbia.domain.operation.StudyOperationInterface;
import gov.nih.nci.nbia.domain.operation.TrialDataProvenanceOperationInterface;
import gov.nih.nci.nbia.internaldomain.CTImage;
import gov.nih.nci.nbia.internaldomain.GeneralEquipment;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.Patient;
import gov.nih.nci.nbia.internaldomain.Study;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;
import gov.nih.nci.nbia.internaldomain.TrialDataProvenance;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.rsna.ctp.pipeline.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ImageStorage extends HibernateDaoSupport{
	private String md5 = "";

	@Autowired
	private TrialDataProvenanceOperationInterface tdpo;
	@Autowired
	private PatientOperationInterface po;
	@Autowired
	private StudyOperationInterface so;
	@Autowired
	private GeneralEquipmentOperationInterface geo;
	@Autowired
	private SeriesOperationInterface serieso;
	@Autowired
	private GeneralImageOperationInterface gio;
	@Autowired
	private CTImageOperationInterface ctio;
	@Autowired
	private ImageSubmissionHistoryOperationInterface imageSubmissionHistoryOperation;
	@Autowired
	private AnnotationOperationInterface ao;

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Status storeDicomObject(Map numbers,
			                       String fileName,
			                       long fileSize,
			                       boolean visibility) {
        TrialDataProvenance tdp=null;
        try {
			tdp = (TrialDataProvenance)tdpo.validate(numbers);
			getHibernateTemplate().saveOrUpdate(tdp);
        }catch(Exception e) {
            log.error("Exception in TrialDataProvenanceOperation " + e);
            errors.put("TrialDataProvenance", e.getMessage());
            return Status.FAIL;
        }

        Patient patient=null;
        try {
			po.setTdp(tdp);
			po.setSite(null);  ///we dont care about this....
			patient = (Patient)po.validate(numbers);
			getHibernateTemplate().saveOrUpdate(patient);
        }catch(Exception e) {
            log.error("Exception in PatientOperation " + e);
            return Status.FAIL;
        }
        Study study=null;
        try {
			so.setPatient(patient);
			study = (Study)so.validate(numbers);
			getHibernateTemplate().saveOrUpdate(study);
        }catch(Exception e) {
            log.error("Exception in StudyOperation " + e);
            return Status.FAIL;
        }
        GeneralEquipment equip=null;
        try {
			equip = (GeneralEquipment)geo.validate(numbers);
			getHibernateTemplate().saveOrUpdate(equip);
        }catch(Exception e) {
            log.error("Exception in GeneralEquipmentOperation " + e);
            return Status.FAIL;
        }
        GeneralSeries series=null;
        try {
			serieso.setEquip(equip);
			serieso.setPatient(patient);
			serieso.setStudy(study);
			series = (GeneralSeries)serieso.validate(numbers);
			getHibernateTemplate().saveOrUpdate(series);
			ao.updateAnnotation(series);
        }catch(Exception e) {
            log.error("Exception in SeriesOperation " + e);
            return Status.FAIL;
        }
        GeneralImage gi=null;
        try {
			gio.setDataProvenance(tdp);
			gio.setPatient(patient);
			gio.setSeries(series);
			gio.setStudy(study);
			gi = (GeneralImage)gio.validate(numbers);

		    if (fileName != null) {
		        gi.setFilename(fileName);
		    }
		    gi.setDicomSize(Long.valueOf(fileSize));
		    gi.setMd5Digest(md5);
		    getHibernateTemplate().saveOrUpdate(gi);
        }catch(Exception e) {
            log.error("File " + gi.getFilename()+ " " + e);
            e.printStackTrace();
            return Status.FAIL;
        }

        try {
			ctio.setGeneralImage(gi);
			CTImage ct = (CTImage)ctio.validate(numbers);
			getHibernateTemplate().saveOrUpdate(ct);

		}catch(Exception e) {
			log.error("Exception in CTImageOperation " + e);
            errors.put("CTImage", e.getMessage());
            return Status.FAIL;
		}

		try {
			 imageSubmissionHistoryOperation.setProperties(gio.isReplacement(),
						                              tdp,
						                              patient,
					                                  study,
					                                  series);

			SubmissionHistory imageSubmissionHistory =
				(SubmissionHistory)imageSubmissionHistoryOperation.validate(numbers);
			getHibernateTemplate().saveOrUpdate(imageSubmissionHistory);
		}
		catch(Exception e) {
			log.error("Exception in ImageSubmissionHistoryOperation " + e);
            //it is my belief that this is totally useless but will follow pattern till overall analysis is done
            errors.put("ImageSubmissionHistory", e.getMessage());
            //getHibernateTemplate().getSessionFactory().getCurrentSession().getTransaction().rollback();
            return Status.FAIL;
        }

        if(errors.size()> 0) {
            System.out.println("Total numbers of errors: " + errors.size());
            return Status.FAIL;
        }
        return Status.OK;
    }

    private Map<String,String> errors = new HashMap<String,String>();

    private Logger log = Logger.getLogger(ImageStorage.class);
}
