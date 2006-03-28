/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 15:05:48
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: InvalidVersionTableException.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 */
package net.sf.dub.application.data;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class InvalidVersionTableException extends Exception {

	public InvalidVersionTableException(String message) {
		super(message);
	}
	
	public InvalidVersionTableException(String message, Throwable cause) {
		super(message, cause);
	}

}
