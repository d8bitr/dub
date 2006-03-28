/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 14:45:08
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersionLoader.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.9  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.8  2005/09/09 15:18:17  dgm
 * update zeit und benutzer anzeigen
 *
 * Revision 1.7  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.6  2005/08/15 14:46:51  dgm
 * bugfix, es wurde stets die selbe tabelle abgefragt
 *
 * Revision 1.5  2005/08/09 14:24:48  dgm
 * Anzeige für Versionen, Aufräumen bevor neue Versionen aus der db geladen werden
 *
 * Revision 1.4  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.3  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.2  2005/08/07 21:43:46  dgm
 * validierung version_tabelle
 *
 * Revision 1.1  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 */
package net.sf.dub.application.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import net.sf.dub.application.db.Database;
import net.sf.dub.application.db.ExecutionException;
import net.sf.dub.application.db.RetrieveDataException;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersionLoader {
	
	private static final String NUMBER = "NUMBER"; //$NON-NLS-1$
	private static final String VARCHAR2 = "VARCHAR2"; //$NON-NLS-1$
	private static final String DATE = "DATE"; //$NON-NLS-1$
	
	public boolean existVersionTable(Database database, String tablename) throws RetrieveDataException {
		Vector resultExists = database.getPlainQueryData("select table_name from user_tables where upper(table_name) = upper('" + tablename + "')"); //$NON-NLS-1$ //$NON-NLS-2$
		if ((resultExists == null) || (resultExists.size() == 0)) {
			return false;
		}
		if (resultExists.get(0).getClass().isAssignableFrom(Vector.class)) {
			return ((Vector)resultExists.get(0)).get(0).toString().equalsIgnoreCase(tablename);
		}
		return false;
	}
	
	public boolean isVersionTableValid(Database database, String tablename) throws RetrieveDataException {
		Vector resultValid = database.getPlainQueryData("select column_name, data_type, nvl(data_precision, data_length) data_length, nullable from user_tab_columns where upper(table_name) = upper('" + tablename + "')"); //$NON-NLS-1$ //$NON-NLS-2$
		HashSet checkedFields = new HashSet(); 
		for (int columns = 0; columns < resultValid.size(); columns++) {
			Vector column = (Vector)resultValid.get(columns);
			String name = column.get(0).toString().toLowerCase();
			String datatype = column.get(1).toString();
			int dataLength = ((BigDecimal)column.get(2)).intValue();
			boolean nullable = column.get(3).toString().equalsIgnoreCase("Y") ? true : false; //$NON-NLS-1$
			
			if (checkField(name, "id", datatype, NUMBER, dataLength, 38, nullable, false)) { //$NON-NLS-1$
				checkedFields.add(name);
			}
			else if (checkField(name, "version_major", datatype, NUMBER, dataLength, 3, nullable, false)) { //$NON-NLS-1$
				checkedFields.add(name);
			}
			else if (checkField(name, "version_minor", datatype, NUMBER, dataLength, 3, nullable, false)) { //$NON-NLS-1$
				checkedFields.add(name);
			}
			else if (checkField(name, "version_micro", datatype, NUMBER, dataLength, 3, nullable, false)) { //$NON-NLS-1$
				checkedFields.add(name);
			}
			else if (checkField(name, "remark", datatype, VARCHAR2, dataLength, 255, nullable, true)) { //$NON-NLS-1$
				checkedFields.add(name);
			}
			else if (checkField(name, "database_update_date", datatype, DATE, dataLength, 0, nullable, false)) { //$NON-NLS-1$
				checkedFields.add(name);
			}
			else if (checkField(name, "update_by", datatype, VARCHAR2, dataLength, 255, nullable, false)) { //$NON-NLS-1$
				checkedFields.add(name);
			}
			
		}
		if(checkedFields.contains("id") && checkedFields.contains("version_major") && //$NON-NLS-1$ //$NON-NLS-2$
				checkedFields.contains("version_minor") && checkedFields.contains("version_micro") &&  //$NON-NLS-1$//$NON-NLS-2$
				checkedFields.contains("remark") && checkedFields.contains("database_update_date") && //$NON-NLS-1$ //$NON-NLS-2$
				checkedFields.contains("update_by")) { //$NON-NLS-1$
			return true;
		}
		return false;
	}
	
	private boolean checkField(String name, String nameExpected, String datatype, String datatypeExpected, int datalength, int datalengthExpected, boolean nullable, boolean nullableExpected) {
		if (!name.equalsIgnoreCase(nameExpected)) {
			return false;
		}
		if (!datatype.equalsIgnoreCase(datatypeExpected)) {
			return false;
		}
		if (!(datalength >= datalengthExpected)) {
			return false;
		}
		if (nullable != nullableExpected) {
			return false;
		}
		return true;
	}
	
	public void createVersiontable(Database db, String tablename) throws ExecutionException {
		db.execute("create table " + tablename + " (" + //$NON-NLS-1$ //$NON-NLS-2$
		           "id                   number(38) not null," + //$NON-NLS-1$
		           "version_major        number(3) not null," + //$NON-NLS-1$
		           "version_minor        number(3) not null," + //$NON-NLS-1$
		           "version_micro        number(3) not null," + //$NON-NLS-1$
		           "remark               varchar2(255)," + //$NON-NLS-1$
		           "database_update_date date not null," + //$NON-NLS-1$
		           "update_by            varchar2(255) not null" + //$NON-NLS-1$
		           ")"); //$NON-NLS-1$
		db.execute("comment on table " + tablename + " is 'This table contains the update version history of database updates'"); //$NON-NLS-1$ //$NON-NLS-2$
		db.execute("insert into " + tablename + " (id, version_major, version_minor, version_micro, remark, database_update_date, update_by) " + //$NON-NLS-1$ //$NON-NLS-2$
		           "values (0, 0, 0, 0, 'This table contains the update version history of database updates', sysdate, '" + System.getProperty("user.name") + "')");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		db.commit();
	}
	
	public void clearDatabaseResults(Configuration configuration) {
		Iterator iterVersions = configuration.getVersions().getIterator();
		HashSet deleteVersions = new HashSet();
		while (iterVersions.hasNext()) {
			VersionBean version = (VersionBean)iterVersions.next();
			if (!version.isInFile()) {
				deleteVersions.add(version);
			}
			else if (version.isInDatabase()) {
				version.setInDatabase(false);
				version.setId(0);
				version.setUpdateBy(null);
				version.setDatabaseUpdateDate(null);
			}
		}
		Iterator iterDelete = deleteVersions.iterator();
		while (iterDelete.hasNext()) {
			VersionBean version = (VersionBean)iterDelete.next();
			configuration.getVersions().removeVersion(version);
		}
	}

	public void loadVersions(Database database, Configuration configuration) throws RetrieveDataException {
		clearDatabaseResults(configuration);
		String versionTable = configuration.getProperty("update.table"); //$NON-NLS-1$
		Vector dbVersions = database.getPlainQueryData("select id, version_major, version_minor, version_micro, remark, database_update_date, update_by from " + //$NON-NLS-1$
		                                               versionTable + //$NON-NLS-1$
		                                               " order by version_major, version_minor, version_micro, database_update_date"); //$NON-NLS-1$
		VersionSet versions = configuration.getVersions();
		Iterator iterVersions = dbVersions.iterator();
		while (iterVersions.hasNext()) {
			Vector dbRow = (Vector)iterVersions.next();
			int id = ((BigDecimal)dbRow.get(0)).intValue();
			int versionMajor = ((BigDecimal)dbRow.get(1)).intValue();
			int versionMinor = ((BigDecimal)dbRow.get(2)).intValue();
			int versionMicro = ((BigDecimal)dbRow.get(3)).intValue();
			String remark = (String)dbRow.get(4);
			Date databaseUpdateDate = (Date)dbRow.get(5);
			String updateBy = (String)dbRow.get(6);
			VersionBean version = versions.getVersion(versionMajor, versionMinor, versionMicro);
			if (version != null) {
				version.setInDatabase(true);
				version.setDatabaseUpdateDate(databaseUpdateDate);
				version.setUpdateBy(updateBy);
			}
			else {
				//create
				VersionBean newVersion = new VersionBean();
				newVersion.setId(id);
				newVersion.setVersionMajor(versionMajor);
				newVersion.setVersionMinor(versionMinor);
				newVersion.setVersionMicro(versionMicro);
				newVersion.setRemark(remark);
				newVersion.setDatabaseUpdateDate(databaseUpdateDate);
				newVersion.setUpdateBy(updateBy);
				newVersion.setInDatabase(true);
				versions.addVersion(newVersion);
			}
		}
	}
	
}
