/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 09:48:03
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: InvalidConfigurationFile.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 */
package net.sf.dub.application.data;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class InvalidConfigurationFile extends Exception {

	public InvalidConfigurationFile(String message) {
		super(message);
	}
	
	public InvalidConfigurationFile(String message, Throwable cause) {
		super(message, cause);
	}

}

