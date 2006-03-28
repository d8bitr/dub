/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 15.07.2005 - 17:46:34
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: SplashScreen.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.1  2005/08/01 21:29:36  dgm
 * gui
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import javax.swing.JWindow;

/**
 * Quick'n'Dirty SplashScreen
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class SplashScreen extends JWindow {
	
	private Image image;
	private long minimumAppearanceTime;
	private long timeStart;
	private String statusText = ""; //$NON-NLS-1$
	
	public SplashScreen(Image image) {
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(image, 0);
		try {
			tracker.waitForID(0);
		}
		catch (InterruptedException ex) {
			// nada
		}
		this.image = image;
		setSize(this.image.getWidth(this), this.image.getHeight(this));
		// zentrieren
		Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimScreen.width / 2) - (getSize().width / 2),  (dimScreen.height / 2) - (getSize().height / 2));
		timeStart = System.currentTimeMillis();
	}


	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(image, 0, 0, this);
		g.drawString(statusText, 8, image.getHeight(this) - 8);
		/*
		if (System.getProperty("javawebstart.version") != null) {
			g.drawString("Version " + getClass().getPackage().getImplementationVersion(), 2, image.getHeight(this) - 22);
		}
		else {
			g.drawString("Version " + getClass().getPackage().getImplementationVersion(), 10, image.getHeight(this) - 10);
		}
		*/
	}
	
	public void setMinimumAppearanceTime(long minimumAppearanceTime) {
		this.minimumAppearanceTime = minimumAppearanceTime;
	}
	
	public void setStatusText(String text) {
		statusText = text;
		repaint();
	}

	public void dispose() {
        long timeEnd = System.currentTimeMillis();
        while((timeEnd - timeStart) <= minimumAppearanceTime) {
            try {
                Thread.sleep(50);
            }
            catch (InterruptedException ex) {
            }
            timeEnd = System.currentTimeMillis();
        }

        super.dispose();
    }

}
