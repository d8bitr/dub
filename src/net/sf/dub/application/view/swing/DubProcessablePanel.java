/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 12:40:10
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DubProcessablePanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.7  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.6  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 * Revision 1.5  2005/08/05 11:32:52  dgm
 * direktzugriff auf properties unterbunden
 *
 * Revision 1.4  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.3  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.2  2005/08/04 12:27:43  dgm
 * configurationsverhalten geändert, process refactored
 *
 * Revision 1.1  2005/08/04 11:59:01  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 */
package net.sf.dub.application.view.swing;

import net.sf.dub.application.controller.ApplicationController;
import net.sf.dub.application.data.Configuration;
import net.sf.dub.miniframework.controller.ProcessException;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;
import net.sf.dub.miniframework.view.swing.ProcessablePanel;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class DubProcessablePanel extends ProcessablePanel {

	public DubProcessablePanel(ProcessableController controller, ApplicationFrame application, String headerText) {
		super(controller, application, headerText);
		setPanelBase(this.getClass().getPackage());
		setControllerBase(ApplicationController.class.getPackage());
	}

	public Configuration getConfiguration() {
		return (Configuration)getController().getData();
	}

	public void onNext() throws ProcessException {
		// Bei bedarf überschreiben
	}
	
	public void onBack() {
		// Bei bedarf überschreiben
	}

}
