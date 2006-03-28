/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 09:55:19
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:22 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ApplicationController.java,v $
 * Revision 1.1  2006/03/28 15:49:22  danielgalan
 * inital import
 *
 * Revision 1.7  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.6  2005/08/10 09:54:38  dgm
 * connection schliessen beim beenden
 *
 * Revision 1.5  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.4  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.3  2005/08/04 11:59:01  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.2  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 * Revision 1.1  2005/07/14 14:42:52  dgm
 * initialer import für dub
 *
 */
package net.sf.dub.application.controller;

import net.sf.dub.application.data.Configuration;
import net.sf.dub.application.db.Database;
import net.sf.dub.miniframework.controller.AbstractController;
import net.sf.dub.miniframework.controller.Controller;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ApplicationController extends AbstractController {
	
	private Configuration configuration = new Configuration();
	private Database database = new Database();

	public ApplicationController(Controller parentController) {
		super(parentController);
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public Database getDatabase() {
		return database;
	}

	public void stop() {
		database.close();
	}

}
