package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.QcSearchResultDTO;
import gov.nih.nci.nbia.dto.QcStatusHistoryDTO;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;


/**
 * 
 */
public interface QcStatusDAO {

	public List<QcSearchResultDTO> findSeries(String[] qcStatus,
			                                  List<String> collectionSites, 
			                                  String[] patients) throws DataAccessException;
		
		
	public List<QcSearchResultDTO> findSeries(String[] qcStatus,
            List<String> collectionSites, 
            String[] patients, Date fromDate, Date toDate, int maxRows) throws DataAccessException;

	public List<QcStatusHistoryDTO> findQcStatusHistoryInfo(List<String> seriesList)throws DataAccessException;


	public void updateQcStatus(List<String> seriesList,
			                   List<String> statusList, 
			                   String newStatus, 
			                   String userName,
			                   String comment) throws DataAccessException;


}
