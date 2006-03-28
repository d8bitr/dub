/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 23.08.2005 - 16:29:20
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: Messages.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 */
package net.sf.dub.miniframework.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import net.sf.dub.miniframework.resources.ResourcesAnchor;




/**
 * Läd I18N Resourcen aus einer properties Datei
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class Messages {
	
	private static Messages instance;
	private Vector resourceBundles = new Vector();

	private Messages() {
	}
	
	
	public static void addResourceBundle(String baseName) {
		getInstance().addResourceBundleIntern(baseName);
	}
	
	public static void addResourceBundle(Class baseClass, String name) {
		getInstance().addResourceBundleIntern(baseClass, name);
	}
	
	
	private void addResourceBundleIntern(String baseName) {
		resourceBundles.add(ResourceBundle.getBundle(baseName));
	}
	
	private void addResourceBundleIntern(Class baseClass, String name) {
		resourceBundles.add(ResourceBundle.getBundle(baseClass.getPackage().getName() + "." + name)); //$NON-NLS-1$
	}
	
	
	private static Messages getInstance() {
		if (instance == null) {
			instance = new Messages();
			instance.addResourceBundleIntern(ResourcesAnchor.class, "i18n.messages"); //$NON-NLS-1$
		}
		return instance;
	}
	
	private String getMessage(String key) {
		String result = null; 
		for (int bundle = resourceBundles.size() - 1; bundle >= 0; bundle--) {
			ResourceBundle resourceBundle = (ResourceBundle)resourceBundles.get(bundle);
			try {
				result = resourceBundle.getString(key);
			}
			catch (MissingResourceException mrex) {
			}
			if (result != null) {
				return result;
			}
		}
		return "?[" + key + "]?"; //$NON-NLS-1$ //$NON-NLS-2$
	}


	public static String get(String key) {
		return getInstance().getMessage(key);
	}
	
}
