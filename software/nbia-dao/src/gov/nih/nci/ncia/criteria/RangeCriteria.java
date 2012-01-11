/**
* $Id: RangeCriteria.java 10592 2009-10-22 16:52:55Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.13  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;


/**
 * Interface implemented by criteria classes that
 * represent ranges of values
 *
 * @author NCIA Team
 */
public interface RangeCriteria {
    public RangeData getRangeData();
}
