package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.ImgObsCharacteristicQuantificationDTO;

import java.util.Collection;
import org.springframework.dao.DataAccessException;

public interface AimImgObsCharacteristicQuantificationDAO {

	public Collection<String> findAllQuantificationNames() throws DataAccessException;

	public Collection<String> findAllValuesByName(String quantificationName) throws DataAccessException;

    public Collection<ImgObsCharacteristicQuantificationDTO> findAllQuantifications() throws DataAccessException;
}