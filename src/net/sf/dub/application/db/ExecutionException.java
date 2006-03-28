/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 08.08.2005 - 12:53:14
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ExecutionException.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 */
package net.sf.dub.application.db;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ExecutionException extends Exception {

	public ExecutionException(String message) {
		super(message);
	}
	
	public ExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

}
