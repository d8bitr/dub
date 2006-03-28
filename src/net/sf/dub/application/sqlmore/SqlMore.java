package net.sf.dub.application.sqlmore;

import java.io.ByteArrayInputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.*;


/**
 * Main application loop
 */
public class SqlMore extends Executor {

	public static final boolean DEBUG = false;
	public static final boolean HINTS = true;

	protected int lastExecutedOperator;
	protected String lastExecutedSql;

	public static final String HELP_MSG = "Available commands are:\n" + //$NON-NLS-1$
	                                      "  @<filename> -- run a SQL script\n" + //$NON-NLS-1$
	                                      "  connect <connect string> -- connect using the given string\n" + //$NON-NLS-1$
	                                      "  desc <segment name>\n" + //$NON-NLS-1$
	                                      "  disconnect -- disconnect the active connection\n" + //$NON-NLS-1$
	                                      "  / -- re-run the previous command\n" + //$NON-NLS-1$
	                                      "  s -- list the last DDL source in the command buffer\n" +  //$NON-NLS-1$
	                                      "  objects -- list the objects in the user's schema\n" + //$NON-NLS-1$
	                                      "  quit / exit -- quit the program\n" + //$NON-NLS-1$
	                                      "  source <procedure / view name> -- display the source code for the named object\n" + //$NON-NLS-1$
	                                      "  call <procedure> -- execute the given stored procedure\n" + //$NON-NLS-1$
	                                      "  tables -- list the tables in the user's schema\n" + //$NON-NLS-1$
	                                      "  find -- find the supplied value in the user's schema (string and numeric only)\n" + //$NON-NLS-1$
	                                      "  view -- list the views in the user's schema\n"; //$NON-NLS-1$


	
	public static void main(String args[]) {
		new SqlMore();
	}

	
	private DbUtil database;
	
	public SqlMore() {
		super();
		setWriter(new OutputStreamWriter(System.out));
		println("\nSql*More: Release 0.16.1 - Production on " + java.util.Calendar.getInstance().getTime().toString()); //$NON-NLS-1$
		println("\nJava Version " + System.getProperty("java.version") + " on " + System.getProperty("os.name") + " " + System.getProperty("os.version"));   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$ //$NON-NLS-6$
		init(System.in);
		database = new DbUtil();
		sqlConnection = new DefaultSqlMoreConnection(database, this);
		
		runSqlMore();

	}
	
	public SqlMore(SqlMoreConnection sqlConnection, String sqlFileContent, Writer outputWriter) {
		super();
		if (outputWriter != null) {
			setWriter(outputWriter);
		}
		this.sqlConnection = sqlConnection;
		ByteArrayInputStream bain = new ByteArrayInputStream(sqlFileContent.getBytes());
		init(bain);
	}
	
	public void runSqlMore() {
		int v_op;
		StringBuffer v_sql = new StringBuffer();
		while(true) {
			v_op = parseSql(v_sql);
			try {
				execSql(v_op, v_sql.toString());
			}
			catch (SQLException e_sql) {
				printerr("\nServer: Msg " + e_sql.getErrorCode() + "\n" + e_sql.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	private SqlMoreConnection sqlConnection;
	
	protected SqlMoreConnection getSqlConnection() {
		return sqlConnection;
	}
	
	protected Connection getConnection() {
		return getSqlConnection().getConnection();
	}
	
	protected int getVendor() {
		return getSqlConnection().getVendor();
	}


	
	protected void execSql(int operator, String sql) throws SQLException {
		if (operator != QUIT && operator != CONNECT && (operator > 1000) && (getConnection() == null || getConnection().isClosed())) {
			println("Not connected"); //$NON-NLS-1$
			return;
		}

		try {
			println(sql);
			switch (operator) {
				case NADA:
				case COMMENT:
					break;
				case SCRIPT:
					int resultOperator = NADA;
					String scritName = sql.trim().substring(1);
					Script script = new Script(scritName);
					StringBuffer sqlBuffer = new StringBuffer();
					while((resultOperator = script.parseSql(sqlBuffer)) != EOF) {
						execSql(resultOperator, sqlBuffer.toString());
					}
					script.deinit();
					break;
				case OUTPUT_ON:
					outputEnable(getConnection(), getVendor(), 1000000);
					break;
				case OUTPUT_OFF:
					outputDisable(getConnection(), getVendor());
					break;
				case HELP:
					print(HELP_MSG);
					break;
				case SOURCE_LIST:
					showSourceDDL(lastExecutedSql);
					break;
				case SESSION_LIST:
					showSessions(getConnection(), getVendor());
					break;
				case LOCK_LIST:
					showRowLocks(getConnection(), getVendor());
					break;
				case COMMIT:
					getConnection().commit();
					break;
				case ROLLBACK:
					getConnection().rollback();
					break;
				case DESC:
					if (sql.length() < 6)
						print(HELP_MSG);
					else {
						String v_table = sql.substring(5);
						descTable(getConnection(), v_table);
					}
					break;
				case SOURCE:
					if (sql.length() < 8)
						print(HELP_MSG);
					else {
						String v_table = sql.substring(7);
						sourceObject(getConnection(), getVendor(), v_table);
					}
					break;
				case CONNECT:
					// cleanup any existing connections
					getSqlConnection().disconnect();
					
					// Connect string format:vendor:user_name/password@host_name/port:database_name
					// isolate the credentials
					String v_creds = sql.substring(7).trim();
					
					if (v_creds.length() == 0) {
						print(DefaultSqlMoreConnection.CONNECT_FMT_MSG);
						return;
					}
					
					((DefaultSqlMoreConnection)getSqlConnection()).connect(v_creds);
					
					//getSqlConnection().connect(v_creds);
					break;
				case DISCONNECT:
					getSqlConnection().disconnect();
					println("Disconnected"); //$NON-NLS-1$
					break;
				case QUIT:
					break;
					// wird keine reaktion hervorrufen in unserer umgebung (wäre schlimm wenn ;)
				case OBJECTS:
					listObjects(getConnection(), getVendor(), sql);
					break;
				case TABLES:
					listTables(getConnection(), getVendor(), sql);
					break;
				case VIEWS:
					listViews(getConnection(), getVendor(), sql);
					break;
				case FIND:
					if (sql.length() < 6)
						print(HELP_MSG);
					else {
						String v_value = sql.substring(5);
						findValue(getConnection(), getVendor(), v_value);
						;
					}
					break;
				case SELECT:
					lastExecutedOperator = operator;
					lastExecutedSql = sql;
					executeQuery(getConnection(), sql);
					break;
				case INSERT:
				case UPDATE:
				case DELETE:
					lastExecutedOperator = operator;
					lastExecutedSql = sql;
					executeDML(getConnection(), operator, sql);
					break;
				case STOREDPROC:
					lastExecutedOperator = operator;
					lastExecutedSql = sql;
					executeStoredProc(getConnection(), getVendor(), sql);
					break;
				case SHOWERRORS:
					showErrorsStoredProc(getConnection(), getVendor(), lastExecutedSql);
					break;
				case REPEAT:
					execSql(lastExecutedOperator, lastExecutedSql);
					break;
				case EXPORT:
					if (sql.length() < 8)
						print(HELP_MSG);
					else {
						String v_sql = sql.substring(7);
						Export exp = new Export();
						exp.exportDIF(getConnection(), v_sql);
					}
					break;
				case DDL:
					lastExecutedOperator = operator;
					lastExecutedSql = sql;
					executeDDL(getConnection(), sql);
					break;
				default:
			}
		}
		catch (SQLException sex) {
			println(sex.getLocalizedMessage());
			throw sex;
		}
	}

}
