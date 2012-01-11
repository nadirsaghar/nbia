/**
* $Id: CurationStatusDateCriteria.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.7  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.6  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;

import java.util.Date;


public class CurationStatusDateCriteria extends TransientCriteria {
    
    private Date curationStatusDate;

    public boolean isEmpty() {
        return (getCurationStatusDate() == null);
    }

    public Date getCurationStatusDate() {
        return curationStatusDate;
    }

    public void setCurationStatusDate(Date curationStatusDate) {
        this.curationStatusDate = curationStatusDate;
    }
}
