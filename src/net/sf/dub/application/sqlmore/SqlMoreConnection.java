/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 10.08.2005 - 14:47:06
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:46 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: SqlMoreConnection.java,v $
 * Revision 1.1  2006/03/28 15:49:46  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/11 14:06:26  dgm
 * SqlMore angepasst (Puh), komplett die Connection und die Ausgabe austauschbar gemacht, in gui integriert (wenn gleich noch nicht im thread)
 *
 * Revision 1.1  2005/08/10 13:59:36  dgm
 * aufräumarbeiten
 *
 */
package net.sf.dub.application.sqlmore;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public interface SqlMoreConnection {

	public static final int VENDOR_UNKNOWN = 0;
	public static final int VENDOR_POSTGRESQL = 1; //
	public static final int VENDOR_MYSQL = 2;
	public static final int VENDOR_ORACLE = 3;
	public static final int VENDOR_DB2 = 4; //
	public static final int VENDOR_HSQLDB = 5;
	public static final int VENDOR_SQLSERVER = 6; // // MS SQLServer databases
	public static final int VENDOR_MSSQLSERVER = 7; // MS SQLServer databases via the MS JDBC driver 
	public static final int VENDOR_SYBASE = 8; //

	public Connection getConnection();
	public void connect(int vendor, String username, String password, String host, int port, String sid) throws SQLException;
	public void disconnect() throws SQLException;
	public int getVendor();
	public ConnectionData parseLogonString(String logonString);

}
