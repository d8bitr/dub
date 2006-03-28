/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 03.08.2005 - 15:14:59
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ProcessException.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.1  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 */
package net.sf.dub.miniframework.controller;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ProcessException extends Exception {
	
	private boolean consumed = false;
	
	public ProcessException(String message) {
		this(message, null);
	}
	
	public ProcessException(String message, Throwable cause) {
		this(message, cause, false);
	}
	
	public ProcessException(String message, Throwable cause, boolean consumed) {
		super(message, cause);
		this.consumed = consumed;
	}
	
	public boolean isConsumend() {
		return consumed;
	}

}

