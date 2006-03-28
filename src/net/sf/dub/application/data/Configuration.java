/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.08.2005 - 14:30:50
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: Configuration.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.7  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.6  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.5  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.4  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.3  2005/08/05 11:32:52  dgm
 * direktzugriff auf properties unterbunden
 *
 * Revision 1.2  2005/08/04 15:34:29  dgm
 * Configuration laden
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.1  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 */
package net.sf.dub.application.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import net.sf.dub.miniframework.data.DataContainer;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class Configuration implements DataContainer {
	
	private Properties properties = new Properties();
	private VersionSet versions = new VersionSet();

	public Configuration() {
		//
	}

	public Object get(Object key) {
		return properties.get(key);
	}
	
	public void clearProperties() {
		properties.clear();
		// default settings
		properties.setProperty("update.table", "dub");  //$NON-NLS-1$//$NON-NLS-2$
	}
	
	public void loadProperties(InputStream inputStream) throws IOException {
		properties.load(inputStream);
	}

	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public VersionSet getVersions() {
		return versions;
	}
	
	public void clearVersionsInFile() {
		Iterator iterVersions = versions.getIterator();
		while (iterVersions.hasNext()) {
			VersionBean currentVersion = (VersionBean)iterVersions.next();
			if (currentVersion.isInFile()) {
				iterVersions.remove();
			}
		}
	}

}
