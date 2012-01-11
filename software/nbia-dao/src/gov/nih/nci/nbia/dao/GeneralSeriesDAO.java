package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.EquipmentDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.util.SiteData;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

public interface GeneralSeriesDAO  {

	public Collection<String> findProjectsOfVisibleSeries() throws DataAccessException;

	public Collection<EquipmentDTO> findEquipmentOfVisibleSeries() throws DataAccessException;


	public Collection<String> findDistinctBodyPartsFromVisibleSeries()throws DataAccessException;

	public Collection<String> findDistinctModalitiesFromVisibleSeries()throws DataAccessException;


    public List<SeriesDTO> getDataForSeries(Integer seriesPkId) throws DataAccessException;

	/**
	 * This returns the series objects by their primary keys.  This method
	 * does NOT look at authorization of any kind.
	 */
	public List<SeriesDTO> findSeriesBySeriesPkId(Collection<Integer> seriesPkIds) throws DataAccessException;

	/**
	 * Return all the series for a given list of series instance UIDs IGNORING
	 * authorization.
	 */
	public List<SeriesDTO> findSeriesBySeriesInstanceUID(List<String> seriesIds)throws DataAccessException;

	/**
	 * Return all the series for a given list of patients, but only when
	 * the series are authorized.
	 */
	public List<SeriesDTO> findSeriesByPatientId(List<String> patientIDs,
			                                     List<SiteData> authorizedSites,
			                                     List<String> authroizedSeriesSecurityGroups) throws DataAccessException;


	/**
	 * Return all the series for a given list of studies, but only when
	 * the series are authorized.
	 */
	public List<SeriesDTO> findSeriesByStudyInstanceUid(List<String> studyInstanceUids,
			                                            List<SiteData> authorizedSites,
			                                            List<String> authroizedSeriesSecurityGroups)throws DataAccessException;

	/**
	 * Return all the series for a given list of series instance UIDs, but only when
	 * the series are authorized.
	 */
	public List<SeriesDTO> findSeriesBySeriesInstanceUID(List<String> seriesIds,
			                                             List<SiteData> authorizedSites,
			                                             List<String> authorizedSeriesSecurityGroups) throws DataAccessException;
	
	public SeriesDTO getGeneralSeriesByPKid(Integer seriesPkId) throws DataAccessException;
}
