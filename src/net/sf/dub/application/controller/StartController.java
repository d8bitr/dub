/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.08.2005 - 12:43:03
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:22 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: StartController.java,v $
 * Revision 1.1  2006/03/28 15:49:22  danielgalan
 * inital import
 *
 * Revision 1.6  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.5  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.4  2005/08/04 12:27:43  dgm
 * configurationsverhalten geändert, process refactored
 *
 * Revision 1.3  2005/08/04 11:59:01  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.2  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 * Revision 1.1  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 */
package net.sf.dub.application.controller;

import net.sf.dub.miniframework.controller.Controller;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class StartController extends DubController {

	public StartController(Controller parentController) {
		super(parentController);
	}

	public String getProcessStepNext() {
		return "Configuration"; //$NON-NLS-1$
	}

	public void onNext() {
		//
	}

	public void onBack() {
		//
	}

}

