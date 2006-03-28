/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 10.08.2005 - 14:56:50
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:46 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: OracleConnection.java,v $
 * Revision 1.1  2006/03/28 15:49:46  danielgalan
 * inital import
 *
 * Revision 1.5  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.4  2005/08/29 14:37:54  dgm
 * i18n fortgeführt
 *
 * Revision 1.3  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
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
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class OracleConnection implements SqlMoreConnection {
	
	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void disconnect() throws SQLException {
		if ((connection != null) && !connection.isClosed()) {
			connection.close();
		}
	}

	public int getVendor() {
		return VENDOR_ORACLE;
	}

	public void connect(int vendor, String username, String password, String host, int port, String sid) throws SQLException {
		connect(getVendor(), username, password, host, port, sid);
	}

	public void connect(String username, String password, String host, int port, String sid) throws SQLException {
		if ((connection != null) && !connection.isClosed()) {
			disconnect();
		}
		// jdbc:oracle:thin:@<HOST>:<PORT>:<SID>
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); //$NON-NLS-1$
		}
		catch (ClassNotFoundException e) {
			throw new SQLException("Driverclass 'oracle.jdbc.driver.OracleDriver' not found"); //$NON-NLS-1$
		}
		String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		connection = DriverManager.getConnection(url, username, password);
		connection.setAutoCommit(false);
	}

	public ConnectionData parseLogonString(String logonString) {
		return null;
	}

}

