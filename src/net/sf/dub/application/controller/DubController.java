/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 13:17:14
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:22 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DubController.java,v $
 * Revision 1.1  2006/03/28 15:49:22  danielgalan
 * inital import
 *
 * Revision 1.5  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.4  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.3  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.2  2005/08/04 15:34:29  dgm
 * Configuration laden
 *
 * Revision 1.1  2005/08/04 12:27:43  dgm
 * configurationsverhalten geändert, process refactored
 *
 */
package net.sf.dub.application.controller;

import net.sf.dub.application.data.Configuration;
import net.sf.dub.application.db.Database;
import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.data.DataContainer;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class DubController extends ProcessableController {
	
	public DubController(Controller parentController) {
		super(parentController);
	}
	
	public Configuration getConfiguration() {
		return (Configuration)getData();
	}
	
	public Database getDatabase() {
		ApplicationController application = ((ApplicationController)getRoot());
		return application.getDatabase();
	}

	public DataContainer getData() {
		ApplicationController application = ((ApplicationController)getRoot());
		return application.getConfiguration();
	}
	
}
