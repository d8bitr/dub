/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.08.2005 - 16:13:37
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:22 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: UpdateController.java,v $
 * Revision 1.1  2006/03/28 15:49:22  danielgalan
 * inital import
 *
 * Revision 1.10  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.9  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.8  2005/09/09 15:18:09  dgm
 * update zeit und benutzer anzeigen
 *
 * Revision 1.7  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.6  2005/08/12 12:59:00  dgm
 * updaten in thread
 *
 * Revision 1.5  2005/08/11 14:06:26  dgm
 * SqlMore angepasst (Puh), komplett die Connection und die Ausgabe austauschbar gemacht, in gui integriert (wenn gleich noch nicht im thread)
 *
 * Revision 1.4  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
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

import java.io.Writer;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import net.sf.dub.application.data.VersionBean;
import net.sf.dub.application.db.ExecutionException;
import net.sf.dub.application.db.RetrieveDataException;
import net.sf.dub.application.sqlmore.OracleConnection;
import net.sf.dub.application.sqlmore.SqlParser;
import net.sf.dub.miniframework.controller.Controller;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class UpdateController extends DubController {
	
	private SqlParser parser;

	public UpdateController(Controller parentController) {
		super(parentController);
	}

	public String getProcessStepBack() {
		return "Versiontable"; //$NON-NLS-1$
	}

	public void onNext() {
		//
	}

	public void onBack() {
		//
	}
	
	public void initializeSqlParser(String sqlFileContent, Writer writer) throws SQLException {
		if (parser != null) {
			closeSqlParser();
		}
		OracleConnection sqlConnection = new OracleConnection();
		sqlConnection.connect(getConfiguration().getProperty("database.name"), //$NON-NLS-1$
		                      getConfiguration().getProperty("database.password"), //$NON-NLS-1$
		                      getConfiguration().getProperty("database.server"), //$NON-NLS-1$
		                      Integer.parseInt(getConfiguration().getProperty("database.port")), //$NON-NLS-1$
		                      getConfiguration().getProperty("database.sid")); //$NON-NLS-1$
		parser = new SqlParser(sqlConnection, sqlFileContent, writer);
		
	}
	
	public void executeParsedSql(int index) throws SQLException {
		if (parser == null) {
			return;
		}
		parser.executeSql(index);
	}
	
	public String getParsedSql(int index) {
		return parser.getParsedSql(index);
	}
	
	public int getParsedSqlCount() {
		return parser.getParsedSqlCount();
	}
	
	public void closeSqlParser() throws SQLException {
		if (parser != null) {
			parser.close();
		}
	}
	
	public int getParsedSqlStatements() {
		return parser.getParsedSqlCount();
	}
	

	public void updateDatabaseVersion(VersionBean version) throws ExecutionException {
		String tablename = getConfiguration().getProperty("update.table"); //$NON-NLS-1$
		
		Vector resultMaxId;
		try {
			resultMaxId = getDatabase().getPlainQueryData("select max(id) from " + tablename); //$NON-NLS-1$
		}
		catch (RetrieveDataException e) {
			throw new ExecutionException(e.getMessage(), e);
		}
		int maxId = ((BigDecimal)((Vector)resultMaxId.get(0)).get(0)).intValue() + 1;
		
		getDatabase().execute("insert into " + tablename + " (id, version_major, version_minor, version_micro, remark, database_update_date, update_by) " + //$NON-NLS-1$ //$NON-NLS-2$
		                      "values (" + maxId + ", " + version.getVersionMajor() + ", "+ version.getVersionMinor() + ", " + version.getVersionMicro() + ", 'Finished', sysdate, '" + System.getProperty("user.name") + "')");  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		getDatabase().commit();
		version.setInDatabase(true);
		version.setUpdateBy(System.getProperty("user.name")); //$NON-NLS-1$
		version.setDatabaseUpdateDate(new Date(System.currentTimeMillis()));
	}

}

