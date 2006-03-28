/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 09:55:29
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ConfigurationBean.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.4  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.3  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.2  2005/08/04 12:27:43  dgm
 * configurationsverhalten geändert, process refactored
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 */
package net.sf.dub.application.data;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ConfigurationBean {
	
	private String databaseName;
	private String databasePassword;
	private String databaseSid;
	private String databaseServer;
	private int databasePort;
	private String updateTable;
	private String updateAutomatically;

	public String getDatabaseName() {
		return databaseName;
	}
	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public String getDatabasePassword() {
		return databasePassword;
	}
	
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}
	
	public int getDatabasePort() {
		return databasePort;
	}
	
	public void setDatabasePort(int databasePort) {
		this.databasePort = databasePort;
	}
	
	public String getDatabaseSid() {
		return databaseSid;
	}
	
	public void setDatabaseSid(String databaseSid) {
		this.databaseSid = databaseSid;
	}
	
	public String getDatabaseServer() {
		return databaseServer;
	}
	
	public void setDatabaseServer(String databaseServer) {
		this.databaseServer = databaseServer;
	}
	
	public String getUpdateAutomatically() {
		return updateAutomatically;
	}
	
	public void setUpdateAutomatically(String updateAutomatically) {
		this.updateAutomatically = updateAutomatically;
	}
	
	public String getUpdateTable() {
		return updateTable;
	}
	
	public void setUpdateTable(String updateTable) {
		this.updateTable = updateTable;
	}
	
}
