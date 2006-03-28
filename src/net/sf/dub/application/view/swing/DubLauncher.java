/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.09.2005 - 09:29:26
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DubLauncher.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/09/02 11:53:48  dgm
 * developer splash
 *
 * Revision 1.1  2005/09/02 09:12:11  dgm
 * deployment verbessern
 *
 */
package net.sf.dub.application.view.swing;

import javax.swing.UIManager;

import net.sf.dub.application.controller.ApplicationController;
import net.sf.dub.application.resources.DubResourcesAnchor;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.ImageFactory;
import net.sf.dub.miniframework.view.swing.SplashScreen;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;


/**
 * Ausgelagerte Applikations-Initialisierung um Startklasse klein zu halten und auslagern zu können
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class DubLauncher {
	
	public DubLauncher() {
		//nada
	}
	
	public void initialise() {
		// Cli
		// Logger
		
		// I18n
		Messages.addResourceBundle(DubResourcesAnchor.class, "i18n.messages"); //$NON-NLS-1$
		// Splash
		ImageFactory.setBaseClass(DubResourcesAnchor.class, true);
		final SplashScreen splash = new SplashScreen(ImageFactory.getImage("splashimage.jpg")); //$NON-NLS-1$
		splash.setMinimumAppearanceTime(System.getProperty("jnlp.developer", "false").equalsIgnoreCase("true") ?  2 : 2500);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		splash.setStatusText(Messages.get("Dub.loading") + " .."); //$NON-NLS-1$ //$NON-NLS-2$
		splash.setVisible(true);
		// L&F
		splash.setStatusText(Messages.get("Dub.loading") + " Look and Feel"); //$NON-NLS-1$ //$NON-NLS-2$
		setLaF();
		// ApplicationController
		splash.setStatusText(Messages.get("Dub.loading") + " Application Frame"); //$NON-NLS-1$ //$NON-NLS-2$
		Thread worker = new Thread() {
			public void run() {
				ApplicationController application = new ApplicationController(null);
				DubApplicationFrame frame = new DubApplicationFrame(application);
				splash.dispose();
				frame.setVisible(true);
				application.start();
			}
		};
		worker.start();
	}

	public void setLaF() {
		try {
			PlasticLookAndFeel.setMyCurrentTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
		}
		catch (Exception e) {
			// Gibts eben vorerst kein L&F
			e.printStackTrace();
		}
	}

}
