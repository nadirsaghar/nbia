/**
* $Id: SeriesDescriptionCriteria.java 14146 2011-04-06 17:07:22Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2007/08/14 16:53:47  bauerd
* Removed the repopulate logic and cleaned up the class files
*
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.11  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.10  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 *
 *
 */
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;


/**
 * @author Ajay Gupta - NCICB/SAIC
 */
public class SeriesDescriptionCriteria extends SingleValuePersistentCriteria
                                       implements Serializable {
    private String seriesDescriptionValue;

    public SeriesDescriptionCriteria() {
        super();
    }

    /**
     */
    public String getSeriesDescriptionValue() {
        return seriesDescriptionValue;
    }

    /**
     */
    public void setSeriesDescriptionValue(String SeriesDescriptionValue) {
        if (SeriesDescriptionValue != null) {
            this.seriesDescriptionValue = SeriesDescriptionValue;
        }
    }

    public String getSingleValue() {
        return getSeriesDescriptionValue();
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        setSeriesDescriptionValue(attr.getAttributeValue());
    }

}
