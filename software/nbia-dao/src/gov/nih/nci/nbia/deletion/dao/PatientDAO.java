package gov.nih.nci.nbia.deletion.dao;

import gov.nih.nci.nbia.deletion.DeletionAuditPatientInfo;
import gov.nih.nci.nbia.exception.DataAccessException;

public interface PatientDAO {

	public void removePatient(Integer patientId) throws DataAccessException;
	public int getTotalStudiesInPatient(Integer patientId) throws DataAccessException;
	public boolean checkPatientNeedToBeRemoved(Integer patientId, Integer count) throws DataAccessException;
	public DeletionAuditPatientInfo getDeletionAuditPatientInfo(Integer PatientId);
}
