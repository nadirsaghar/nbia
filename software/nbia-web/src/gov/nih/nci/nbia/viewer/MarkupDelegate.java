/**
* $Id: MarkupDelegate.java 14161 2011-04-06 18:25:17Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.4  2007/10/12 16:15:33  panq
* Removed unused code.
*
*/

package gov.nih.nci.nbia.viewer;

import gov.nih.nci.nbia.dto.MarkupDTO;
import gov.nih.nci.nbia.markup.MarkupManager;
import gov.nih.nci.nbia.util.SpringApplicationContext;

public class MarkupDelegate {

	private MarkupManager mgr;

    /**
     * Constructor
     */
    public MarkupDelegate() { 
        mgr = (MarkupManager)SpringApplicationContext.getBean("markupManager");
    }

    public void saveMarkup(String seriesUid, String loginName, String data){
        MarkupDTO dto = new MarkupDTO();
        dto.setMarkupData(data);
        dto.setSeriesUID(seriesUid);
        dto.setUsrLoginName(loginName);
        mgr.saveMarkup(dto);
    }
    
    public String getMarkups(String seriesUid) {
        MarkupDTO dto = new MarkupDTO();
        dto.setSeriesUID(seriesUid);
        return mgr.getMarkups(dto);
    }
    
    public boolean markupExist(String seriesUid) {
        MarkupDTO dto = new MarkupDTO();
        dto.setSeriesUID(seriesUid);
        return mgr.markupExist(dto);
    }
}
