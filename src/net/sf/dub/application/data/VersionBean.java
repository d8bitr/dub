/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 03.08.2005 - 13:48:09
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersionBean.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.10  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.9  2005/09/20 08:19:13  dgm
 * aufgeräumt
 *
 * Revision 1.8  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.7  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.6  2005/08/09 15:46:14  dgm
 * bugfix beim laden des configurationsfiles, anzeige update erweitert
 *
 * Revision 1.5  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.4  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.3  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
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

import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersionBean implements Comparable {
	
	private int id;
	private int versionMajor;
	private int versionMinor;
	private int versionMicro;
	private String remark;
	private Date databaseUpdateDate;
	private String updateBy;
	
	private boolean inFile;
	private boolean inDatabase;
	
	private TreeMap versionFiles = new TreeMap();
	
	public VersionBean() {
	}

	public void addFile(String name, String text) {
		versionFiles.put(name, text);
	}
	
	public Iterator getFileValueIterator() {
		return versionFiles.values().iterator();
	}
	
	public Iterator getFileKeyIterator() {
		return versionFiles.keySet().iterator();
	}
	
	public TreeMap getFiles() {
		return versionFiles;
	}
	
	public int getFilesCount() {
		return versionFiles.size();
	}
	
	public String getVersionFileName(int index) {
		Iterator iter = versionFiles.keySet().iterator();
		for (int i = 0; i <= index; i++) {
			if (iter.hasNext()) {
				String result = (String)iter.next();
				if (i == index) {
					return result;
				}
			}
		}
		return null;
	}

	public String getVersionFileContent(int index) {
		Iterator iter = versionFiles.values().iterator();
		for (int i = 0; i <= index; i++) {
			if (iter.hasNext()) {
				String result = (String)iter.next();
				if (i == index) {
					return result;
				}
			}
		}
		return null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getUpdateBy() {
		return updateBy;
	}
	
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
	public Date getDatabaseUpdateDate() {
		return databaseUpdateDate;
	}
	
	public void setDatabaseUpdateDate(Date updateDatabase) {
		this.databaseUpdateDate = updateDatabase;
	}
	
	public int getVersionMajor() {
		return versionMajor;
	}
	
	public void setVersionMajor(int versionMajor) {
		this.versionMajor = versionMajor;
	}
	
	public int getVersionMicro() {
		return versionMicro;
	}
	
	public void setVersionMicro(int versionMicro) {
		this.versionMicro = versionMicro;
	}
	
	public int getVersionMinor() {
		return versionMinor;
	}
	
	public void setVersionMinor(int versionMinor) {
		this.versionMinor = versionMinor;
	}

	public int compareTo(Object o) {
		if (o.getClass().isAssignableFrom(VersionBean.class)) {
			VersionBean otherVersion = (VersionBean)o;
			if (getVersionMajor() > otherVersion.getVersionMajor()) {
				return 1;
			}
			else if (getVersionMajor() < otherVersion.getVersionMajor()) {
				return -1;
			}
			
			if (getVersionMinor() > otherVersion.getVersionMinor()) {
				return 1;
			}
			else if (getVersionMinor() < otherVersion.getVersionMinor()) {
				return -1;
			}

			if (getVersionMicro() > otherVersion.getVersionMicro()) {
				return 1;
			}
			else if (getVersionMicro() < otherVersion.getVersionMicro()) {
				return -1;
			}
		}
		return 0;
	}
	
	public String getVersionName() {
		return generateName(getVersionMajor(), getVersionMinor(), getVersionMicro());
	}
	
	public String toString() {
		return getVersionName();
	}

	public boolean isInDatabase() {
		return inDatabase;
	}

	public void setInDatabase(boolean inDatabase) {
		this.inDatabase = inDatabase;
	}

	public boolean isInFile() {
		return inFile;
	}

	public void setInFile(boolean inFile) {
		this.inFile = inFile;
	}
	
	public static String generateName(int versionMajor, int versionMinor, int versionMicro) {
		return versionMajor + "." + versionMinor + "." + versionMicro;  //$NON-NLS-1$//$NON-NLS-2$
	}

}
