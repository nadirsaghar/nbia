package gov.nih.nci.nbia.search;

import junit.framework.TestCase;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.StudySearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;

public class DrillDownFactoryTestCase extends TestCase {


	public void testGetDrillDownFailure() {
		//dont set sys property "drilldown.className"

		try {
			DrillDownFactory.getDrillDown();
			fail("shouldnt get here");
		}
		catch(Exception ex) {
			//should get here
		}

	}

	public void testGetDrillDown() {
		System.setProperty("drilldown.className",
				           "gov.nih.nci.nbia.search.DrillDownFactoryTestCase$FakeDrillDown");

		DrillDown dd1 = DrillDownFactory.getDrillDown();
		DrillDown dd2 = DrillDownFactory.getDrillDown();

		assertEquals(dd1, dd2);
		assertTrue(dd1 instanceof DrillDown);
	}

	public static class FakeDrillDown implements DrillDown {
		public StudySearchResult[] retrieveStudyAndSeriesForPatient(PatientSearchResult patientSearchResult) {
			return null;
		}

		public ImageSearchResult[] retrieveImagesForSeries(SeriesSearchResult seriesSearchResult) {
			return null;
		}

		public ImageSearchResultEx[] retrieveImagesForSeriesForAllVersion(SeriesSearchResult seriesSearchResult) {
			return null;
		}

		public ImageSearchResultEx[] retrieveImagesForSeriesEx(
				SeriesSearchResult seriesSearchResult) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
