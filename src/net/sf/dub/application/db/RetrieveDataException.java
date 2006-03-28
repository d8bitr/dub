/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 09:20:05
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: RetrieveDataException.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 */
package net.sf.dub.application.db;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class RetrieveDataException extends Exception {

	public RetrieveDataException(String message) {
		super(message);
	}
	
	public RetrieveDataException(String message, Throwable cause) {
		super(message, cause);
	}

}

