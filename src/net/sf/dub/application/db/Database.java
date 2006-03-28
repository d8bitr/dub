/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 07:05:20
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: Database.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.4  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.3  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.2  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.1  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 */
package net.sf.dub.application.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import net.sf.dub.miniframework.util.Messages;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class Database {
	
	protected Connection connection;
	
	public Database() {
		
	}
	
	public void connect(String server, String port, String sid, String username, String password) throws ConnectionFailedException {
		close();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); //$NON-NLS-1$
			//jdbc:oracle:thin:@<HOST>:<PORT>:<SID>
			String url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + sid;   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(false);
		}
		catch (Exception ex) {
			throw new ConnectionFailedException(Messages.get("Database.error.connect"), ex); //$NON-NLS-1$
		}
	}
	
	public void close() {
		try {
			if (isConnected()) {
				connection.close();
			}
		}
		catch (Exception ex) {
			// Aus irgendeinem Grund kann die Connection nicht geschlossen werden
			ex.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		try {
			return (connection != null) && !connection.isClosed();
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	public Vector getPlainQueryData(String sql) throws RetrieveDataException {
		if (isConnected()) {
			try {
				Statement stm = connection.createStatement();
				ResultSet rs = stm.executeQuery(sql);
				Vector result = new Vector();
				while (rs.next()) {
					Vector row = new Vector();
					int columnCount = rs.getMetaData().getColumnCount();
					for (int i = 0; i < columnCount; i++) {
						row.add(rs.getObject(i + 1));
					}
					result.add(row);
				}
				rs.close();
				stm.close();
				return result;
			}
			catch (Exception ex) {
				String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
				throw new RetrieveDataException(Messages.get("Database.error.data") + ":" + lineSeparator + sql, ex); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return null;
	}
	
	public void execute(String sql) throws ExecutionException {
		if (isConnected()) {
			try {
				Statement stm = connection.createStatement();
				stm.executeUpdate(sql);
				stm.close();
			}
			catch (SQLException sex) {
				String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
				throw new ExecutionException(Messages.get("Database.error.execute") + ":" + lineSeparator + sql, sex); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	public void commit() throws ExecutionException {
		if (isConnected()) {
			try {
				connection.commit();
			}
			catch (SQLException sex) {
				throw new ExecutionException(Messages.get("Database.error.commit"), sex); //$NON-NLS-1$
			}
		}
	}

}

