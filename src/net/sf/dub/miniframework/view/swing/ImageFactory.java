/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 15.07.2005 - 16:59:43
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ImageFactory.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.5  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.4  2005/09/19 15:02:31  dgm
 * framework muss auch daten laden könne, schon besser, aber immer noch suboptimal
 *
 * Revision 1.3  2005/09/09 15:17:20  dgm
 * warten auf images via mediatracker
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

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import net.sf.dub.miniframework.resources.ResourcesAnchor;



/**
 * Stellt Zentral gepflegte ImageResourcen zur verfügung
 * 
 * @author  tentacle
 * @version $Revision: 1.1 $
 */
public class ImageFactory {
	
	private static final String FRAMEWORK_KEY = "frameworkImage"; //$NON-NLS-1$

	private Map images = new Hashtable();
	private Class baseClass = ImageFactory.class;
	private boolean usesImagesSubdirectory = true;
	private JLabel dummyLabel;
	private MediaTracker tracker;
	
	private static ImageFactory instance;

	private ImageFactory() {
		// konstruktor soll nicht von außen aufgerufen werden
		// So nen schiet, der Mediatracker braucht eine blöde Component
		dummyLabel = new JLabel();
		tracker = new MediaTracker(dummyLabel);
	}
	
	public static void setBaseClass(Class baseClass, boolean usesImagesSubdirectory) {
		getInstance().baseClass = baseClass;
		getInstance().usesImagesSubdirectory = usesImagesSubdirectory;
	}
	
	private static ImageFactory getInstance() {
		if (instance == null) {
			instance = new ImageFactory();
		}
		return instance;
	}
	
	public static Icon getIcon(String filename) {
		return new ImageIcon(getImage(filename));
	}
	
	public static Icon getIcon(String filename, boolean isFrameworkResource) {
		return new ImageIcon(getImage(filename, isFrameworkResource));
	}

	public static Image getImage(String filename) {
		return getImage(filename, false);
	}
	
	public static Image getImage(String filename, boolean isFrameworkResource) {
		String key = isFrameworkResource ? FRAMEWORK_KEY + filename : filename;
		ImageFactory factory = getInstance();
		if (!factory.images.containsKey(key)) {
			URL urlImage = null;
			if (isFrameworkResource) {
				urlImage =  ResourcesAnchor.class.getResource("images/" + filename);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			}
			else {
				urlImage = factory.baseClass.getResource((factory.usesImagesSubdirectory ? "images" : "") + "/" + filename);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			}
			Image image = Toolkit.getDefaultToolkit().getImage(urlImage);
			factory.tracker.addImage(image, 0);
			try {
				factory.tracker.waitForAll();
			}
			catch (Exception ex) {
				// nada
			}
			factory.images.put(filename, image);
			return image;
		}
		return (Image)getInstance().images.get(key);
	}

}
