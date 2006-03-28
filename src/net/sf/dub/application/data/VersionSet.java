/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 03.08.2005 - 13:55:43
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersionSet.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.8  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.7  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.6  2005/08/09 14:24:48  dgm
 * Anzeige für Versionen, Aufräumen bevor neue Versionen aus der db geladen werden
 *
 * Revision 1.5  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.4  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.3  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.2  2005/08/04 15:34:29  dgm
 * Configuration laden
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.1  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 */
package net.sf.dub.application.data;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersionSet {
	
	private TreeMap versions;

	public VersionSet() {
		versions = new TreeMap();
	}
	
	public void addVersion(VersionBean version) {
		if ((version != null) && !versions.containsValue(version)) {
			//versions.put(version.getVersionName(), version); -> wurde nicht genommen, da die TreeMap sonst nicht die "natural order" benutzt
			versions.put(version, version);
		}
	}
	
	public boolean existsVersion(String name) {
		Iterator iterKeys = versions.keySet().iterator();
		while (iterKeys.hasNext()) {
			VersionBean version = (VersionBean)iterKeys.next();
			if (version.getVersionName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existsVersion(int versionMajor, int versionMinor, int versionMicro) {
		return existsVersion(VersionBean.generateName(versionMajor, versionMinor, versionMicro));
	}
	
	public VersionBean getVersion(String name) {
		Iterator iterKeys = versions.keySet().iterator();
		while (iterKeys.hasNext()) {
			VersionBean version = (VersionBean)iterKeys.next();
			if (version.getVersionName().equals(name)) {
				return version;
			}
		}
		return null;
	}
	
	public VersionBean getVersion(int versionMajor, int versionMinor, int versionMicro) {
		return getVersion(VersionBean.generateName(versionMajor, versionMinor, versionMicro));
	}
	
	public Iterator getIterator() {
		return versions.values().iterator();
	}
	
	public int getVersionCount() {
		return versions.size();
	}
	
	public VersionBean getVersion(int index) {
		Iterator iter = getIterator();
		for (int i = 0; i <= index; i++) {
			if (iter.hasNext()) {
				VersionBean version = (VersionBean)iter.next();
				if (i == index) {
					return version;
				}
			}
		}
		return null;
	}
	
	public void removeVersion(VersionBean version) {
		versions.remove(version);
	}
	
}
