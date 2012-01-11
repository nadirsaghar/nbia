package gov.nih.nci.nbia.beans.populator;

import gov.nih.nci.nbia.basket.ExternalDataBasket;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.Arrays;
import java.util.List;


public class DataBasketPopulatorMgBean extends AbstractPopulatorMgBean {

	public String[] getPatients() {
		return patients;
	}

	public void setPatients(String[] patients) {
		this.patients = patients;
	}

	public String[] getStudies() {
		return studies;
	}

	public void setStudies(String[] studies) {
		this.studies = studies;
	}

	public String[] getSerieses() {
		return serieses;
	}

	public void setSerieses(String[] serieses) {
		this.serieses = serieses;
	}


	//preconditino: parameterMap is not null
	protected void populateImpl() throws Exception {
		List<String> patientList = null;
		List<String> studyList = null;
		List<String> seriesList = null;

		if (patients != null && patients.length > 0)
		{
			patientList = Arrays.asList(patients);
		}
		if (studies != null && studies.length > 0)
		{
			studyList = Arrays.asList(studies);
		}
		if (serieses != null && serieses.length > 0)
		{
			seriesList = Arrays.asList(serieses);
		}

		ExternalDataBasket rsh = new ExternalDataBasket();
		List<SeriesSearchResult> sDto = rsh.getSeriesDTOList(patientList,
				                                             studyList,
				                                             seriesList,
				                                             BeanManager.getSecurityBean().getAuthorizationManager());

		if (sDto != null && sDto.size() > 0)
		{
			BasketBean dataBasket = (BasketBean) BeanManager.getBasketBean();
			dataBasket.getBasket().addSeries(sDto);
		}
	}

	/////////////////////////////////////////PRIVATE////////////////////////////////

	private String[] patients;

	private String[] studies;

	private String[] serieses;

}

