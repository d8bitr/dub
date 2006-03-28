/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.08.2005 - 16:13:00
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:22 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersiontableController.java,v $
 * Revision 1.1  2006/03/28 15:49:22  danielgalan
 * inital import
 *
 * Revision 1.10  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.9  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.8  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.7  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.6  2005/08/07 21:43:46  dgm
 * validierung version_tabelle
 *
 * Revision 1.5  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
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

import net.sf.dub.application.data.VersionLoader;
import net.sf.dub.application.db.ExecutionException;
import net.sf.dub.application.db.RetrieveDataException;
import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.controller.ProcessException;
import net.sf.dub.miniframework.util.Messages;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersiontableController extends DubController {
	
	VersionLoader loader = new VersionLoader();

	public VersiontableController(Controller parentController) {
		super(parentController);
	}

	public String getProcessStepBack() {
		return "Configuration"; //$NON-NLS-1$
	}

	public String getProcessStepNext() {
		return "Update"; //$NON-NLS-1$
	}
	
	public boolean existVersionTable() throws RetrieveDataException {
		if (loader.existVersionTable(getDatabase(), getConfiguration().getProperty("update.table"))) { //$NON-NLS-1$
			return true;
		}
		return false;
	}
	
	public boolean isVersionTableValid() throws RetrieveDataException {
		if (loader.isVersionTableValid(getDatabase(), getConfiguration().getProperty("update.table"))) { //$NON-NLS-1$
			return true;
		}
		return false;
	}
	
	public void createVersiontable(String tablename) throws ExecutionException {
		loader.createVersiontable(getDatabase(), tablename);
	}

	public void onNext() throws ProcessException {
		try {
			if (!isVersionTableValid()) {
				String versionTable = getConfiguration().getProperty("update.table"); //$NON-NLS-1$
				throw new ProcessException(Messages.get("VersiontableController.info.invalidtable1") + " '" + versionTable + "' " + Messages.get("VersiontableController.info.invalidtable2"), null, true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		}
		catch (RetrieveDataException rdex) {
			throw new ProcessException(Messages.get("VersiontableController.error.loading"), rdex); //$NON-NLS-1$
		}
		try {
			loader.loadVersions(getDatabase(), getConfiguration());
		}
		catch (RetrieveDataException rdex) {
			throw new ProcessException(Messages.get("VersiontableController.error.merge"), rdex); //$NON-NLS-1$
		}
	}

	public void onBack() {
		//
	}

}

