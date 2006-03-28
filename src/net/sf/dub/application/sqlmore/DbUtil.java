package net.sf.dub.application.sqlmore;

import java.sql.*;
import java.util.logging.Logger;
import java.util.Calendar;
import java.io.Reader;
import java.io.IOException;


/**
 * Database functionality abstraction utilities.
 *
 * Supported drivers:
 * - Oracle
 * - PostgreSQL
 * - MySQL (incomplete)
 * - hsql DB (incomplete)
 * - DB2 (incomplete)
 *
 * Copyright (c) 2001-2004 Marc Lavergne. All rights reserved.
 *
 * The following products are free software; Licensee can redistribute and/or 
 * modify them under the terms of	the GNU Lesser General Public License
 * (http://www.gnu.org/copyleft/lesser.html) as published by the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-	1307  USA; 
 * either version 2.1 of the	license, or any later version:
 */
public class DbUtil {

	// JDBC Datatypes:
	/** static int identifier for JDBC Blob datatype */
	public static final int JDBC_BLOB = 1;
	/** static int identifier for JDBC Byte Array datatype */
	public static final int JDBC_BYTE_ARRAY = 2;
	/** static int identifier for JDBC Date datatype */
	public static final int JDBC_DATE = 3;
	/** static int identifier for JDBC int datatype */
	public static final int JDBC_INTEGER = 4;
	/** static int identifier for JDBC String datatype */
	public static final int JDBC_STRING = 5;

	private String m_database;

	public DbUtil() {
		super();
	}
	
	public String getDriverString() {
		return m_database;
	}
	/**
	 * Initializes a database driver according to vendor specific rules, using a default host and port
	 * @param p_vendor int representing a static database vendor value
	 * @param p_username String containing the database username for the application to use
	 * @param p_password String containing the database password for p_username
	 * @param p_database String containing the database instance name
	 * @throws SQLException on any problems encoutered at the database level
	 * @return boolean with true if driver successfully initialized, otherwise false
	 */
	public boolean init(int p_vendor, String p_username, String p_password, String p_database) throws SQLException {
		return init(p_vendor, p_username, p_password, p_database, "127.0.0.1"); //$NON-NLS-1$
	}

	/**
	 * Initializes a database driver according to vendor specific rules using a default port
	 * @param p_vendor int representing a static database vendor value
	 * @param p_username String containing the database username for the application to use
	 * @param p_password String containing the database password for p_username
	 * @param p_database String containing the database instance name
	 * @param p_host String containing the hostname or IP address of the database server
	 * @throws SQLException on any problems encoutered at the database level
	 * @return boolean with true if driver successfully initialized, otherwise false
	 */
	public boolean init(int p_vendor, String p_username, String p_password, String p_database, String p_host) throws SQLException {
		switch (p_vendor) {
			case SqlMoreConnection.VENDOR_POSTGRESQL:
				return init(p_vendor, p_username, p_password, p_database, p_host, 5432);
			case SqlMoreConnection.VENDOR_MYSQL:
				return init(p_vendor, p_username, p_password, p_database, p_host, 3306);
			case SqlMoreConnection.VENDOR_ORACLE:
				return init(p_vendor, p_username, p_password, p_database, p_host, 1521);
			case SqlMoreConnection.VENDOR_HSQLDB:
				return init(p_vendor, p_username, p_password, p_database, p_host, 0);
			case SqlMoreConnection.VENDOR_SQLSERVER:
			case SqlMoreConnection.VENDOR_MSSQLSERVER:
				return init(p_vendor, p_username, p_password, p_database, p_host, 1433);
			default:
				throw new SQLException("DbUtil.init: Unsupported database vendor [" + p_vendor + "]");  //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	/**
	 * Initializes a database driver according to vendor specific rules
	 * @param p_vendor int representing a static database vendor value
	 * @param p_username String containing the database username for the application to use
	 * @param p_password String containing the database password for p_username
	 * @param p_database String containing the database instance name
	 * @param p_host String containing the hostname or IP address of the database server
	 * @param p_port int representing the port on which the database is listening
	 * @throws SQLException on any problems encoutered at the database level
	 * @return boolean with true if driver successfully initialized, otherwise false
	 */
	public boolean init(int p_vendor, String p_username, String p_password, String p_database, String p_host, int p_port) throws SQLException {
		// SqlMoreConnection.VENDOR_HSQLDB may send in a 0 port which effectively means a local database
		if (p_port == 0 && p_vendor != SqlMoreConnection.VENDOR_HSQLDB) {
			return init(p_vendor, p_username, p_password, p_database, p_host);
		}
		try {
			switch (p_vendor) {
				case SqlMoreConnection.VENDOR_POSTGRESQL:
					Class.forName("org.postgresql.Driver"); //$NON-NLS-1$
					m_database = "jdbc:postgresql://" + p_host + ":" + Integer.toString(p_port) + "/" + p_database; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					break;
				case SqlMoreConnection.VENDOR_MYSQL:
					Class.forName("com.mysql.jdbc.Driver"); //$NON-NLS-1$
					m_database = "jdbc:mysql://" + p_host + ":" + Integer.toString(p_port) + "/" + p_database;  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
					break;
				case SqlMoreConnection.VENDOR_ORACLE:
					// DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
					// This is for PRE-8.1.7.1 drivers only:
					//   Class.forName("oracle.jdbc.driver.OracleDriver");
					// it's now:
					Class.forName("oracle.jdbc.OracleDriver"); //$NON-NLS-1$
					// Other method:
					//   DriverManager.registerDriver((java.sql.Driver)Class.forName("oracle.jdbc.driver.OracleDriver").newInstance());
					m_database = "jdbc:oracle:thin:@" + p_host + ":" + Integer.toString(p_port) + ":" + p_database;  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
					//            m_database = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=" + p_host + ")(PORT=" + Integer.toString(p_port) + ")))(CONNECT_DATA=(SERVICE_NAME=" + p_database + ")(SERVER=DEDICATED)))";
					break;
				case SqlMoreConnection.VENDOR_HSQLDB:
					Class.forName("org.hsqldb.jdbcDriver"); //$NON-NLS-1$
					// NOTE: If the database is specified as '.' the DB will be in-memory only
					if (p_port == 0 || p_database.charAt(0) == '.')
						m_database = "jdbc:hsqldb:" + p_database; //$NON-NLS-1$
					else
						m_database = "jdbc:hsqldb:hsql//" + p_host + ":" + p_port; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case SqlMoreConnection.VENDOR_SQLSERVER:
					Class.forName("net.sourceforge.jtds.jdbc.Driver"); //$NON-NLS-1$
					m_database = "jdbc:jtds:sqlserver://" + p_host + ":" + Integer.toString(p_port) + "/" + p_database;  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
					break;
				case SqlMoreConnection.VENDOR_MSSQLSERVER:
					Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver"); //$NON-NLS-1$
					// we set "selectmethod=cursor" to allow multiple statements per connection
					// Error avoided: [SQLServer JDBC Driver]Can't start a cloned connection while in manual transaction mode.
					m_database = "jdbc:microsoft:sqlserver://" + p_host + ":" + Integer.toString(p_port) + ";databasename=" + p_database //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ ";selectmethod=cursor"; //$NON-NLS-1$
					break;
				default:
					throw new SQLException("DbUtil.init: Unsupported database vendor [" + p_vendor + "]");  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		catch (ClassNotFoundException e_cnf) {
			Logger.getAnonymousLogger().severe(e_cnf.toString());
			return false;
		}

		return true;
	}

	/**
	 * Creates a Connection object using an initialized database JDBC driver
	 * @return Connection object with an active database connection
	 * @throws SQLException on any problems encoutered at the database level
	 */
	public Connection createConnection(String database, String username, String password, int vendor) throws SQLException {
		Connection v_conn = null;

		try {
			// NOTE: If the app svr and db svr are on the same box, use a bequeath connection
			v_conn = DriverManager.getConnection(database, username, password);

			setConnectionDefaults(vendor, v_conn);
		}
		catch (SQLException e_sql) {
			throw e_sql;
		}

		return v_conn;
	}

	/**
	 * Sets default behavior for a JDBC Connection object
	 * @param p_conn on which to set defaults
	 * @throws SQLException on any problems encoutered at the database level
	 */
	public static final void setConnectionDefaults(int p_vendor, Connection p_conn) throws SQLException {
		try {
			// DB Specific Optimizations:
			switch (p_vendor) {
				case SqlMoreConnection.VENDOR_POSTGRESQL:
				case SqlMoreConnection.VENDOR_SQLSERVER:
				case SqlMoreConnection.VENDOR_MSSQLSERVER:
				case SqlMoreConnection.VENDOR_MYSQL:
				case SqlMoreConnection.VENDOR_HSQLDB:
				case SqlMoreConnection.VENDOR_ORACLE:
					// disable auto-commit
					p_conn.setAutoCommit(false);
					break;
				//case DB2:
				//break;
				default:
					throw new SQLException("DbUtil.setConnectionDefaults: Unsupported database vendor [" + p_vendor + "]");  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		catch (SQLException e_sql) {
			throw e_sql;
		}
	}

	/**
	 * static method for handling positional column gets for String values
	 * @param p_rset ResultSet object on which to perform get
	 * @param p_col int representing the column position in the ResultSet
	 * @return String containing the value at p_col, otherwise NULL
	 */
	public static final String getString(ResultSet p_rset, int p_col) {
		try {
			return p_rset.getString(p_col);
		}
		catch (SQLException e_sql) {
			return null;
		}
	}

	/**
	 * static method for handling positional column gets for int values
	 * @param p_rset ResultSet object on which to perform get
	 * @param p_col int representing the column position in the ResultSet
	 * @return int containing the value at p_col, otherwise -1
	 */
	public static final int getInt(ResultSet p_rset, int p_col) {
		try {
			return p_rset.getInt(p_col);
		}
		catch (SQLException e_sql) {
			return -1;
		}
	}

	/**
	 * static method for handling positional column gets for Blob values
	 * @param p_vendor int representing a static database vendor value
	 * @param p_rset ResultSet object on which to perform get
	 * @param p_col int representing the column position in the ResultSet
	 * @return byte[] containing the value at p_col, otherwise NULL
	 */
	public static final byte[] getBlob(int p_vendor, ResultSet p_rset, int p_col) {
		byte[] v_data = null;

		try {
			// conditional logic for BLOB fetching
			switch (p_vendor) {
				case SqlMoreConnection.VENDOR_POSTGRESQL:
					v_data = p_rset.getBytes(p_col);
					break;
				case SqlMoreConnection.VENDOR_ORACLE:
					Blob v_blob = p_rset.getBlob(p_col);
					v_data = v_blob.getBytes(1, (int)v_blob.length());
					break;
				default:
					throw new SQLException("DbUtil.getBlob: Unsupported database vendor [" + p_vendor + "]");  //$NON-NLS-1$//$NON-NLS-2$
			}

			return v_data;
		}
		catch (SQLException e_sql) {
			Logger.getAnonymousLogger().severe(e_sql.toString());
			return null;
		}
	}

	/**
	 * static method for handling positional column gets for Blob values for Oracle
	 * @param p_rset ResultSet object on which to perform get
	 * @param p_col int representing the column position in the ResultSet
	 * @return byte[] containing the value at p_col, otherwise NULL
	 */
	public static final byte[] getOracleBlob(ResultSet p_rset, int p_col) {
		return getBlob(SqlMoreConnection.VENDOR_ORACLE, p_rset, p_col);
	}

	/**
	 * static method for handling Oracle BLOB updates
	 * @param p_rset ResultSet object on which to perform get
	 * @param p_col int representing the column position in the ResultSet
	 * @return boolean with true if successful, otherwise false
	 */
	public static final boolean setOracleBlob(ResultSet p_rset, int p_col, byte[] p_data) {
		try {
			Blob v_blob = p_rset.getBlob(p_col);
			if (v_blob == null)
				return false;
			((oracle.sql.BLOB)v_blob).setBytes(1, p_data);
			return true;
		}
		catch (ClassCastException e_cast) {
			Logger.getAnonymousLogger().severe(e_cast.toString());
			return false;
		}
		catch (SQLException e_sql) {
			Logger.getAnonymousLogger().severe(e_sql.toString());
			return false;
		}
	}

	/**
	 * static method for fetching an Oracle CLOB value
	 * @param p_rset ResultSet object on which to perform get
	 * @param p_col int representing the column position in the ResultSet
	 * @return String containing the value at p_col, otherwise NULL
	 */
	public static final String getOracleClob(ResultSet p_rset, int p_col) {
		//    byte[] v_data = null;

		if (p_rset == null) {
			Logger.getAnonymousLogger().severe("NULL ResultSet detected"); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}

		try {
			Clob v_clob = p_rset.getClob(p_col);
			if (v_clob != null) {
				Reader v_stream;
				char[] v_buf = new char[(int)v_clob.length()];
				v_stream = v_clob.getCharacterStream();
				v_stream.read(v_buf);
				v_stream.close();
				return new String(v_buf);
			}
			return ""; //$NON-NLS-1$
		}
		catch (IOException e_io) {
			Logger.getAnonymousLogger().severe(e_io.toString());
			return null;
		}
		catch (SQLException e_sql) {
			Logger.getAnonymousLogger().severe(e_sql.toString());
			return null;
		}
	}

	/**
	 * static method for handling Oracle CLOB updates
	 * @param p_rset ResultSet object on which to perform get
	 * @param p_col int representing the column position in the ResultSet
	 * @return boolean with true if successful, otherwise false
	 */
	public static final boolean setOracleClob(ResultSet p_rset, int p_col, String p_data) {
		if (p_rset == null) {
			Logger.getAnonymousLogger().severe("NULL ResultSet detected"); //$NON-NLS-1$
			return false;
		}

		try {
			Clob v_clob = p_rset.getClob(p_col);
			if (v_clob == null)
				return false;
			((oracle.sql.CLOB)v_clob).setString(1, p_data);
			return true;
		}
		catch (SQLException e_sql) {
			Logger.getAnonymousLogger().severe(e_sql.toString());
			return false;
		}
	}

	/**
	 * static method for obtaining a Timestamp object with the current system time
	 * @return Timestamp object representing the current system time
	 */
	public static final Timestamp systimestamp() {
		return new Timestamp(Calendar.getInstance().getTime().getTime());
	}


	/** Obtains the default listener port for a given database vendor
	 * @return int with member JDBC generic datatype ID if known, otherwise JDBC_STRING
	 * @param p_vendor int representing a static database vendor value
	 * @throws SQLException  */
	public static final int getDefaultPort(int p_vendor) throws SQLException {
		switch (p_vendor) {
			case SqlMoreConnection.VENDOR_POSTGRESQL:
				return 5432;
			case SqlMoreConnection.VENDOR_MYSQL:
				return 3306;
			case SqlMoreConnection.VENDOR_ORACLE:
				return 1521;
			default:
				throw new SQLException("DbUtil.getDefaultPort: Unsupported database vendor [" + p_vendor + "]");  //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	/**
	 * static method for translating a vendor specific datatype ID
	 * (see ResultSetMetaDate.getColumnType()) to a generic JDBC type
	 * using the member JDBC datatype IDs. We require both the type ID and name
	 * because IDs can represent mutilple datatypes for a single vendor that don't
	 * have a mutil-faceted counterpart with another database vendor.
	 * NOTE: Vendor support is an ongoing development process ... currently
	 * only basic PostgreSQL and Oracle support exist.
	 * @param p_vendor int representing a static database vendor value
	 * @param p_vendor_type int representing a the vendor specific dataype ID
	 * @param p_vendor_name String representing a the vendor specific dataype name
	 * @return int with member JDBC generic datatype ID if known, otherwise JDBC_STRING
	 */
	public static final int getJdbcDatatypeID(int p_vendor, int p_vendor_type, String p_vendor_name) {
		switch (p_vendor) {
			case SqlMoreConnection.VENDOR_POSTGRESQL:
				switch (p_vendor_type) {
					case -2: // BYTE ARRAY (BLOB)
						return JDBC_BYTE_ARRAY;
					case 4: // OID or INTEGER
						if (p_vendor_name != null && p_vendor_name.compareTo("oid") == 0) //$NON-NLS-1$
							return JDBC_BLOB;
						return JDBC_INTEGER;
					case 91: // DATE -- contains ONLY a date component
						return JDBC_DATE;
					case 93: // TIMESTAMP -- contains BOTH a date and time component
						return JDBC_DATE;
					case 12: // VARCHAR
						return JDBC_STRING;
				}
				break;
			case SqlMoreConnection.VENDOR_ORACLE:
				switch (p_vendor_type) {
					case 2004: // BLOB
						return JDBC_BLOB;
					case 93: // DATE
						return JDBC_DATE;
					case 2: // INTEGER or NUMBER(38)
						return JDBC_INTEGER;
					case 12: // VARCHAR2
						return JDBC_STRING;
					case 1: // LONG
						return JDBC_STRING;
					case 4: // LONG RAW
						return JDBC_BLOB; // this is a guess, might be a byte array
				}
				break;
		}

		// by default return a JDBC_STRING since it is the most all-encompassing
		return JDBC_STRING;
	}

	/**
	 * Obtain the database vendor ID based on symantics from the JDBC connection metadata
	 * @param p_conn Connection object from which to extra metadata
	 * @return int with the database vendor ID
	 */
	public static int getDbVendorID(Connection p_conn) {
		try {
			String v_vendor = p_conn.getMetaData().getDatabaseProductName();

			if (v_vendor.compareToIgnoreCase("PostgreSQL") == 0) //$NON-NLS-1$
				return SqlMoreConnection.VENDOR_POSTGRESQL;
			else if (v_vendor.compareToIgnoreCase("Oracle") == 0) //$NON-NLS-1$
				return SqlMoreConnection.VENDOR_ORACLE;
			else if (v_vendor.compareToIgnoreCase("MySQL") == 0) //$NON-NLS-1$
				return SqlMoreConnection.VENDOR_MYSQL;
			else if (v_vendor.compareToIgnoreCase("HSQL Database Engine") == 0) //$NON-NLS-1$
				return SqlMoreConnection.VENDOR_HSQLDB;
			else if (v_vendor.compareToIgnoreCase("Microsoft SQL Server") == 0) //$NON-NLS-1$
				return SqlMoreConnection.VENDOR_MSSQLSERVER;
			else
				return SqlMoreConnection.VENDOR_UNKNOWN;
		}
		catch (SQLException e_sql) {
			Logger.getAnonymousLogger().severe(e_sql.toString());
			return SqlMoreConnection.VENDOR_UNKNOWN;
		}
	}

	/**
	 * Obtain the database vendor ID based on a string representation of the vendor name
	 * Valid values are: "PostgreSQL", "Oracle", "MySQL", "HSQL Database Engine"
	 * @param p_platform with the database platform
	 * @return int with the database vendor ID
	 */
	public static int getDbVendorID(String p_platform) {
		// make certain that spaces have been trimmed
		p_platform = p_platform.trim();

		if (p_platform.compareToIgnoreCase("PostgreSQL") == 0) //$NON-NLS-1$
			return SqlMoreConnection.VENDOR_POSTGRESQL;
		else if (p_platform.compareToIgnoreCase("Oracle") == 0) //$NON-NLS-1$
			return SqlMoreConnection.VENDOR_ORACLE;
		else if (p_platform.compareToIgnoreCase("MySQL") == 0) //$NON-NLS-1$
			return SqlMoreConnection.VENDOR_MYSQL;
		else if (p_platform.compareToIgnoreCase("HSQL Database Engine") == 0) //$NON-NLS-1$
			return SqlMoreConnection.VENDOR_HSQLDB;
		else if (p_platform.compareToIgnoreCase("MsSqlServer") == 0) //$NON-NLS-1$
			return SqlMoreConnection.VENDOR_SQLSERVER;
		else
			return SqlMoreConnection.VENDOR_UNKNOWN;
	}

	/**
	 * Obtain the database vendor driver version based on symantics from the JDBC connection metadata
	 * @param p_conn Connection object from which to extra metadata
	 * @return String with the database driver version
	 */
	public static String getDbDriverVer(Connection p_conn) {
		try {
			return p_conn.getMetaData().getDriverVersion();
		}
		catch (SQLException e_sql) {
			Logger.getAnonymousLogger().severe(e_sql.toString());
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Wrapper for closing a ResultSet without concern for SQL Exceptions
	 * @param p_rset ResultSet object to perform close on
	 */
	public static final void safeClose(ResultSet p_rset) {
		try {
			if (p_rset != null)
				p_rset.close();
			p_rset = null;
		}
		catch (SQLException e_sql) {
		}
	}

	/**
	 * Wrapper for closing a Statement array without concern for SQL Exceptions
	 * @param p_stmt Statement array to perform close on
	 */
	public static final void safeClose(Statement p_stmt[]) {
		if (p_stmt == null)
			return;

		int v_len = p_stmt.length;
		for (int i = 0; i < v_len; i++)
			safeClose(p_stmt[i]);
	}

	/**
	 * Wrapper for closing a Statement without concern for SQL Exceptions
	 * @param p_stmt Statement object to perform close on
	 */
	public static final void safeClose(Statement p_stmt) {
		try {
			if (p_stmt != null)
				p_stmt.close();
			p_stmt = null;
		}
		catch (SQLException e_sql) {
		}
	}

	/**
	 * static method for determining if a Connection object has been disconnected
	 * @param p_conn Connection object on which to perform checks
	 * @return boolean with true if disconnected, otherwise false
	 */
	public static final boolean isDisconnected(Connection p_conn) {
		if (p_conn == null)
			return true;

		try {
			return p_conn.isClosed();
		}
		catch (SQLException e_sql) {
			return true;
		}
	}

	/**
	 * static method for handling generic undo operations on a given Connection object
	 * @param p_conn Connection object on which to perform undo operations
	 */
	public static final void handleException(Connection p_conn) {
		if (p_conn == null)
			return;

		try {
			try {
				if (!isDisconnected(p_conn))
					p_conn.rollback();
			}
			catch (SQLException e_sql) {
				Logger.getAnonymousLogger().severe(e_sql.toString());
			}

			if (!isDisconnected(p_conn))
				p_conn.close();
		}
		catch (SQLException e_sql) {
			Logger.getAnonymousLogger().severe(e_sql.toString());
		}
	}

}
