/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 14:42:45
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:22 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ConfigurationController.java,v $
 * Revision 1.1  2006/03/28 15:49:22  danielgalan
 * inital import
 *
 * Revision 1.11  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.10  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.9  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.8  2005/08/05 11:32:52  dgm
 * direktzugriff auf properties unterbunden
 *
 * Revision 1.7  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.6  2005/08/04 15:34:29  dgm
 * Configuration laden
 *
 * Revision 1.5  2005/08/04 12:27:43  dgm
 * configurationsverhalten geändert, process refactored
 *
 * Revision 1.4  2005/08/04 11:59:01  dgm
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
package net.sf.dub.application.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.dub.application.data.Configuration;
import net.sf.dub.application.data.ConfigurationLoader;
import net.sf.dub.application.data.InvalidConfigurationFile;
import net.sf.dub.application.db.ConnectionFailedException;
import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.controller.ProcessException;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ConfigurationController extends DubController {

	public ConfigurationController(Controller parentController) {
		super(parentController);
	}

	public String getProcessStepBack() {
		return "Start"; //$NON-NLS-1$
	}

	public String getProcessStepNext() {
		return "Versiontable"; //$NON-NLS-1$
	}

	public void onNext() throws ProcessException {
		String server = getConfiguration().getProperty("database.server"); //$NON-NLS-1$
		String port = getConfiguration().getProperty("database.port"); //$NON-NLS-1$
		String sid = getConfiguration().getProperty("database.sid"); //$NON-NLS-1$
		String username = getConfiguration().getProperty("database.name"); //$NON-NLS-1$
		String password = getConfiguration().getProperty("database.password"); //$NON-NLS-1$
		try {
			getDatabase().connect(server, port, sid, username, password);
		}
		catch (ConnectionFailedException cfex) {
			throw new ProcessException(cfex.getMessage(), cfex);
		}
	}

	public void onBack() {
		//
	}

	public void loadConfiguration(String filename, Configuration configuration) throws FileNotFoundException, InvalidConfigurationFile, IOException {
		ConfigurationLoader loader = new ConfigurationLoader();
		loader.loadConfiguration(filename, getConfiguration());
	}

}
