/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 10.08.2005 - 16:10:29
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:46 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DefaultSqlMoreConnection.java,v $
 * Revision 1.1  2006/03/28 15:49:46  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/29 14:37:54  dgm
 * i18n fortgeführt
 *
 * Revision 1.1  2005/08/11 14:06:26  dgm
 * SqlMore angepasst (Puh), komplett die Connection und die Ausgabe austauschbar gemacht, in gui integriert (wenn gleich noch nicht im thread)
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
public class DefaultSqlMoreConnection implements SqlMoreConnection {
	
	private DbUtil util;
	private ConnectionData data;
	private Console console;
	
	public DefaultSqlMoreConnection(DbUtil util, Console console) {
		this.util = util;
		this.console = console;
	}

	
	protected Connection connection;
	
	public Connection getConnection() {
		return connection;
	}


	public void disconnect() throws SQLException {
		if ((connection != null) && !connection.isClosed()) {
			connection.close();
		}
	}


	public int getVendor() {
		return data.getVendor();
	}

	public static final String CONNECT_FMT_MSG = "Connect string format is:\n  vendor:user_name/password@host_name/port:database_name\n" //$NON-NLS-1$
		+ "  where /port is optional\n" + "  and vendor in\n" + "    ora == Oracle\n" + "    mss == Micorosft SQL Server\n" + "    mys == MySQL\n"    //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		+ "    hsq == hSqlDB\n"; //$NON-NLS-1$

	protected void connect(String logonString) throws SQLException {
		ConnectionData result = parseLogonString(logonString);
		if (result != null) {
			data = result;
		}
		connect(data.getVendor(), data.getUsername(), data.getPassword(), data.getHost(), data.getPort(), data.getSid());
	}
	
	public ConnectionData parseLogonString(String logonString) {
		ConnectionData result = new ConnectionData();
		// determine the token offsets
		int v_db_un = logonString.indexOf(':');
		int v_un_pw = logonString.indexOf('/');
		int v_pw_ht = logonString.indexOf('@');
		int v_ht_pt = logonString.lastIndexOf('/');
		int v_ht_sd = logonString.lastIndexOf(':');
		if (v_db_un == -1 || v_un_pw == -1 || v_pw_ht == -1 || v_ht_sd == -1) {
			console.printerr("Invalid connect string: [" + logonString + "]\n"); //$NON-NLS-1$ //$NON-NLS-2$
			console.printerr(CONNECT_FMT_MSG);
			return null;
		}
		// tokenize the credentials string
		// check for a parsing error
		if (v_db_un < v_un_pw) {
			String v_vendor_str = logonString.substring(0, v_db_un).toLowerCase();
			if (v_vendor_str.charAt(0) == 'o')
				result.setVendor(SqlMoreConnection.VENDOR_ORACLE);
			else if (v_vendor_str.charAt(0) == 'm' && v_vendor_str.charAt(1) == 's')
				result.setVendor(SqlMoreConnection.VENDOR_MSSQLSERVER);
			else if (v_vendor_str.charAt(0) == 'm' && v_vendor_str.charAt(1) == 'y')
				result.setVendor(SqlMoreConnection.VENDOR_MYSQL);
			else if (v_vendor_str.charAt(0) == 'h')
				result.setVendor(SqlMoreConnection.VENDOR_HSQLDB);
			else
				result.setVendor(SqlMoreConnection.VENDOR_UNKNOWN);
		}
		// we hit the host : SID separator
		else {
			console.printerr("Invalid vendor in connect string: [" + logonString + "]\n");  //$NON-NLS-1$//$NON-NLS-2$
			console.printerr(CONNECT_FMT_MSG);
			return null;
		}
		result.setUsername(logonString.substring(v_db_un + 1, v_un_pw));
		result.setPassword(logonString.substring(v_un_pw + 1, v_pw_ht));

		// this portiion of the code is a little tricky, we conditionally
		// detect the presence of a port in the connect string
		if (v_ht_pt > v_pw_ht) { // we have a port specified
			result.setHost(logonString.substring(v_pw_ht + 1, v_ht_pt));
			String v_port_str = null;
			try {
				v_port_str = logonString.substring(v_ht_pt + 1, v_ht_sd);
				result.setPort(Integer.parseInt(v_port_str));
			}
			catch (NumberFormatException e_num) {
				console.printerr("Invalid port in connect string: [" + v_port_str + "]\n\n");  //$NON-NLS-1$//$NON-NLS-2$
				return null;
			}
		}
		// we hit the username / password separator, no port specified
		else {
			result.setHost(logonString.substring(v_pw_ht + 1, v_ht_sd));
		}
		result.setSid(logonString.substring(v_ht_sd + 1));
		return result;
	}

	public void connect(int vendor, String username, String password, String host, int port, String sid) throws SQLException {
		// print it out
		if (SqlMore.DEBUG) {
			console.println(username + "/" + password + "@" + host + "/" + port + ":" + sid);  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
		}
		util.init(vendor, username, password, sid, host, port);
		connection = util.createConnection(util.getDriverString(), username, password, vendor);
	}


	protected boolean disconnect(Connection p_conn) throws SQLException {
		// cleanup any existing connections
		if (p_conn != null && !p_conn.isClosed()) {
			p_conn.close();
			p_conn = null;
		}

		return true;
	}

}

