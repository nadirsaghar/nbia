package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.nbia.beans.security.AnonymousLoginBean;
import gov.nih.nci.nbia.datamodel.IcefacesRowColumnDataModel;
import gov.nih.nci.nbia.datamodel.IcefacesRowColumnDataModelInterface;
import gov.nih.nci.nbia.search.DrillDown;
import gov.nih.nci.nbia.search.DrillDownFactory;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SlideShowUtil;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;


public class SeriesSearchResultBean implements IcefacesRowColumnDataModelInterface{

	/**
	 * This is the series that is being viewed currently.
	 */
	public SeriesSearchResult getSeries() {
		return series;
	}

	/**
	 * Drill down method from study to series.
	 */
	public void viewSeries(SeriesSearchResult theSeries) throws Exception {
		this.series = theSeries;

		DrillDown drillDown = DrillDownFactory.getDrillDown();
		imageSearchResults = drillDown.retrieveImagesForSeriesForAllVersion(theSeries);
		setImageList(Arrays.asList(imageSearchResults));
		icefacesDataModel = new IcefacesRowColumnDataModel(computeWrapperList(Arrays.asList(imageSearchResults)));
	}


	public DataModel getImageList() {
		return imageList;
	}


	public void setImageList(List<ImageSearchResultEx> imageList) {
		this.imageList = new ListDataModel(computeWrapperList(imageList));
	}

	/**
	 * better to put in component that accepts collection and spits out js
	 * but being as writing components is a big pain in the #$%^#$....
	 *
	 * <p>for each thumbnail in the imageList, write out the URL to the
	 * thumbnail inside a JavaScript array like such:
	 *
	 * ['http://xxx1', 'http://xxx2', ...]
	 */
	public String getImageSeriesJavascript() {
		return SlideShowUtil.getImageSeriesJavascriptEx(getUnwrappedImages());
	}

	/**
	 * The number of images in the found series
	 */
	public int getImagesInSeriesCount() {
		if (getSeries().getModality().equals("US")){
			return 0;
		}

		return getImages().size();
	}


	/**
	 * Adds the series for all the images shown on viewSeries.xhtml
	 * to the data basket.
	 */
	public String addCurrentSeriesToBasket() throws Exception {
		if(!testBasketLimitsForGuest()) {
			return null;
		}
		BasketBean dataBasket = BeanManager.getBasketBean();

		dataBasket.getBasket().addSeries(Collections.singletonList(series));

		return null;
	}


    /**
     * This is called a by a populator bean for the showDicom page
     */
    public void setTagInfo(List<DicomTagDTO> dicomTagInfoList) {
    	this.tagInfo = dicomTagInfoList;
    }


    /**
     * This is called by showDicom page
     */
    public List<DicomTagDTO> getTagInfo() {
        return tagInfo;
    }


    /**
     * This is called by showDicom page.  Return the SOP instance UID
     * of the first image being displayed.
     */
    public String getCurrDicomSopId() {
        return getImages().get(0).getSopInstanceUid();
    }

	public List<ImageResultWrapper> getImages() {
		return (List<ImageResultWrapper>)imageList.getWrappedData();
	}

	//////////////////////////////////////// icefaces image table for series image displaying ////////

    public ImageResultWrapper getCellValue() {
    	return icefacesDataModel.getCellValue();
    }


    public boolean getCellVisibility() {
    	return icefacesDataModel.getCellVisibility();
    }


    public int getFrameNum() {
    	if (icefacesDataModel.getCellVisibility() &&
    	    getSeries().getModality().equals("US") &&
    		getCellValue().getImageEx().getNameValuesPairs() != null) {
    		return Integer.parseInt(getCellValue().getImageEx().getNameValuesPairs().getValues()[0]);
       	}
    	return 0;
    }


	public DataModel getRowDataModel() {
		return icefacesDataModel.getRowDataModel();
	}

	public DataModel getColumnDataModel(){
		return icefacesDataModel.getColumnDataModel();
	}

	public String getImageLabel() {
		return icefacesDataModel.getImageLabel();
	}

	public boolean getShowPaginator() {
		return icefacesDataModel.getShowPaginator();
	}
	///////////////////////////////////////////PRIVATE/////////////////////////////////

    private List<DicomTagDTO> tagInfo;

	private SeriesSearchResult series;


	/**
	 * This is the model used for presenting the
	 * thumbnails.
	 */
	private DataModel imageList;


	/**
	 * show warning message when data basket and selected are not http for anonymous login
	 * @param fieldId
	 * @param selectedSize
	 */
	private static void showMessage(String fieldId, double selectedSize, double ftpLimit){
		DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(1);
		Object param[] = new Object[2];
		param[0]=nf.format(ftpLimit/1000) + " GB";
		param[1]=nf.format(selectedSize /1000) + " GB";
		MessageUtil.addErrorMessage(fieldId,
		                            "downloadWarningForGuest",
		                            param );
	}



	private List<ImageSearchResultEx> getUnwrappedImages() {
		List<ImageSearchResultEx> results = new ArrayList<ImageSearchResultEx>();
		for(ImageResultWrapper wrapper : getImages()) {
			results.add(wrapper.getImageEx());
		}
		return results;
	}

	private boolean testBasketLimitsForGuest() {
		BasketBean dataBasket = BeanManager.getBasketBean();
		List<ImageSearchResultEx> data = getUnwrappedImages();

		long size=0;
		double currentBasketSize = dataBasket.getBasket().calculateSizeInMB();
		double selectedSeriesSize=0;
		double ftpLimit = NCIAConfig.getFtpThreshold();
		AnonymousLoginBean anonymousLoginBean = BeanManager.getAnonymousLoginBean();
		if(anonymousLoginBean.getGuestLoggedIn()){
			Integer seriesId = data.get(0).getSeriesId();
			if(!dataBasket.getBasket().isSeriesInBasket(seriesId,
					                                    data.get(0).associatedLocation().getURL())){
				for(int i=0; i<data.size(); i++){
					size +=data.get(i).getSize();
				}
				selectedSeriesSize = Long.valueOf(size).doubleValue() / 1000000.0;

				double total = currentBasketSize + selectedSeriesSize;

				if(total >= ftpLimit){
					showMessage("MAINbody:imageForm:addSeriesToBasketButton", total, ftpLimit);
					return false;
				}
			}
		}
		return true;
	}

    public int getTotalImageCount()
    {
    	return imageSearchResults.length;
    }
	private IcefacesRowColumnDataModelInterface icefacesDataModel;

	private ImageSearchResultEx[] imageSearchResults;

	private static List<ImageResultWrapper> computeWrapperList(List<ImageSearchResultEx> imageList) {
		List<ImageResultWrapper> wrappers = new ArrayList<ImageResultWrapper>();
		for(ImageSearchResultEx result : imageList) {
			wrappers.add(new ImageResultWrapper(result));
		}
		return wrappers;
	}
}
