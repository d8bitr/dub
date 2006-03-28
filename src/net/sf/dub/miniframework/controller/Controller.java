/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 09:20:28
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: Controller.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.3  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 * Revision 1.2  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 * Revision 1.1  2005/07/14 14:42:52  dgm
 * initialer import für dub
 *
 */
package net.sf.dub.miniframework.controller;

import net.sf.dub.miniframework.data.DataContainer;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public interface Controller {
	
	public Controller getParent();
	public Controller getRoot();
	public void start();
	public void stop();
	public DataContainer getData();

}

