package net.sf.dub.application.sqlmore;

import java.sql.*;
import sun.misc.Signal;
import sun.misc.SignalHandler;



/**
 * Main application loop
 *
 * Copyright (c) 2001-2004 Marc Lavergne. All rights reserved.
 *
 * The following products are free software; Licensee can redistribute and/or
 * modify them under the terms of	the GNU Lesser General Public License
 * (http://www.gnu.org/copyleft/lesser.html) as published by the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-	1307  USA;
 * either version 2.1 of the	license, or any later version:
 */
public class Executor extends Parser {

	// ***************************************************************************

	protected Statement m_stmt;
	protected boolean m_output = false;

	public static final int UNKNOWN = 1000;
	public static final int TABLE = 1001;
	public static final int VIEW = 1002;
	public static final int PROCEDURE = 1003;

	// ***************************************************************************

	private class sqlSignalHandler implements SignalHandler {

		public void handle(Signal p_sig) {
			try {
				if (m_stmt != null)
					m_stmt.cancel();
			}
			catch (SQLException e_sql) {
			}
		}
	}


	// ***************************************************************************

	public Executor() {
		// this doesn't actually allow us to preempt the shutdown event
		//    Runtime.getRuntime().addShutdownHook(new ControlCHandler());
		Signal.handle(new Signal("INT"), new sqlSignalHandler()); //$NON-NLS-1$
	}


	// ***************************************************************************

	private static String grep(String p_sql) {
		int v_grep_idx = p_sql.toLowerCase().indexOf(" grep "); //$NON-NLS-1$
		String v_grep = null;
		if (v_grep_idx > 0)
			v_grep = p_sql.substring(v_grep_idx + 6).trim();

		return v_grep;
	}


	// ***************************************************************************

	protected boolean listObjects(Connection p_conn, int p_db_vendor, String p_sql) throws SQLException {
		String v_grep = grep(p_sql);
		String v_sql;

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				v_sql = "select owner||'.'||object_name, object_type from all_objects"; //$NON-NLS-1$
				if (v_grep != null)
					v_sql += " where lower(object_name) like lower('%" + v_grep + "%')"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case SqlMoreConnection.VENDOR_MSSQLSERVER:
				v_sql = "select name, case type when 'U' then 'TABLE' when 'V' then 'VIEW' when 'P' then 'PROCEDURE' else 'OTHER' end from sysobjects"; //$NON-NLS-1$
				//        v_sql = "select name, case object_type when 'U' then 'TABLE' when 'V' then 'VIEW' when 'P' then 'PROCEDURE' else 'OTHER' end from sysobjects";
				if (v_grep != null)
					v_sql += " where lower(name) like lower('%" + v_grep + "%')";  //$NON-NLS-1$//$NON-NLS-2$
				break;
			case SqlMoreConnection.VENDOR_HSQLDB:
				v_sql = "select table_name, table_type from system_tables"; //$NON-NLS-1$
				break;
			default:
				printerr("Undefined operation for db vendor #" + p_db_vendor); //$NON-NLS-1$
				return false;
		}

		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		v_stmt = p_conn.prepareStatement(v_sql);
		v_rset = v_stmt.executeQuery();

		// print out a nice header
		println(" Name                                      Type  "); //$NON-NLS-1$
		println(" ----------------------------------------- ----------------------------"); //$NON-NLS-1$

		while(v_rset.next()) {
			print(" "); //$NON-NLS-1$
			for (int i = 0; i < 2; i++)
				print(v_rset.getString(i + 1) + "\t"); //$NON-NLS-1$

			println();
		}

		v_rset.close();
		v_stmt.close();

		return true;
	}


	// ***************************************************************************

	protected boolean listTables(Connection p_conn, int p_db_vendor, String p_sql) throws SQLException {
		String v_grep = grep(p_sql);
		String v_sql;

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				v_sql = "select owner||'.'||table_name from all_tables"; //$NON-NLS-1$
				if (v_grep != null)
					v_sql += " where lower(table_name) like lower('%" + v_grep + "%')";  //$NON-NLS-1$//$NON-NLS-2$
				break;
			case SqlMoreConnection.VENDOR_MSSQLSERVER:
				//        v_sql = "select name from sysobjects where object_type = 'U'";
				v_sql = "select name from sysobjects where type = 'U'"; //$NON-NLS-1$
				if (v_grep != null)
					v_sql += " and lower(name) like lower('%" + v_grep + "%')"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case SqlMoreConnection.VENDOR_MYSQL:
				v_sql = "show tables"; //$NON-NLS-1$
				if (v_grep != null)
					v_sql += " like '%" + v_grep + "%'"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case SqlMoreConnection.VENDOR_HSQLDB:
				v_sql = "select table_name from system_tables"; //$NON-NLS-1$
				break;
			default:
				printerr("Undefined operation for db vendor #" + p_db_vendor); //$NON-NLS-1$
				return false;
		}

		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		v_stmt = p_conn.prepareStatement(v_sql);
		v_rset = v_stmt.executeQuery();

		// print out a nice header
		println(" Name                                     "); //$NON-NLS-1$
		println(" -----------------------------------------"); //$NON-NLS-1$

		while(v_rset.next()) {
			print(" "); //$NON-NLS-1$
			//        for (int i=0; i<2; i++)
			//          Console.print(v_rset.getString(i+1)+"\t");
			print(v_rset.getString(1) + "\t"); //$NON-NLS-1$

			println();
		}

		v_rset.close();
		v_stmt.close();

		return true;
	}


	// ***************************************************************************

	protected boolean listViews(Connection p_conn, int p_db_vendor, String p_sql) throws SQLException {
		String v_grep = grep(p_sql);
		String v_sql;

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				v_sql = "select owner||'.'||view_name from all_views"; //$NON-NLS-1$
				if (v_grep != null)
					v_sql += " where lower(view_name) like lower('%" + v_grep + "%')"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case SqlMoreConnection.VENDOR_MSSQLSERVER:
				//        v_sql = "select name from sysobjects where object_type = 'U'";
				v_sql = "select name from sysobjects where type = 'V'"; //$NON-NLS-1$
				if (v_grep != null)
					v_sql += " and lower(name) like lower('%" + v_grep + "%')"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case SqlMoreConnection.VENDOR_HSQLDB:
				v_sql = "select view_name from system_views"; //$NON-NLS-1$
				break;
			default:
				printerr("Undefined operation for db vendor #" + p_db_vendor); //$NON-NLS-1$
				return false;
		}

		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		v_stmt = p_conn.prepareStatement(v_sql);
		v_rset = v_stmt.executeQuery();

		// print out a nice header
		println(" Name                                     "); //$NON-NLS-1$
		println(" -----------------------------------------"); //$NON-NLS-1$

		while(v_rset.next()) {
			print(" "); //$NON-NLS-1$
			//        for (int i=0; i<2; i++)
			//          Console.print(v_rset.getString(i+1)+"\t");
			print(v_rset.getString(1) + "\t"); //$NON-NLS-1$

			println();
		}

		v_rset.close();
		v_stmt.close();

		return true;
	}


	// ***************************************************************************

	protected boolean findValue(Connection p_conn, int p_db_vendor, String p_value) throws SQLException {
		String v_grep = grep(p_value);
		String v_value = p_value;
		String v_sql;
		String v_dyn_sql;

		if (v_grep != null) {
			int v_off = v_value.indexOf(' ');
			v_value = v_value.substring(0, v_off);
		}

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				v_sql = "select table_name, column_name from user_tab_columns where data_type in ('VARCHAR2','NUMBER')"; //$NON-NLS-1$
				break;
			default:
				println("Find not yet implemented for this database type"); //$NON-NLS-1$
				return false;
		}

		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		PreparedStatement v_dyn_stmt = null;
		ResultSet v_dyn_rset = null;

		v_stmt = p_conn.prepareStatement(v_sql);
		v_rset = v_stmt.executeQuery();

		// print out a nice header
		println(" Table                                     Hits  "); //$NON-NLS-1$
		println(" ----------------------------------------- ----------------------------"); //$NON-NLS-1$

		while(v_rset.next()) {
			v_dyn_sql = "select count(*) from " + v_rset.getString(1) + " where to_char(" + v_rset.getString(2) + ") = '" + v_value + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			//      Console.println("execute ["+v_dyn_sql+"]");

			v_dyn_stmt = p_conn.prepareStatement(v_dyn_sql);
			v_dyn_rset = v_dyn_stmt.executeQuery();

			if (v_dyn_rset.next() && v_dyn_rset.getInt(1) > 0)
				println(" " + Text.rpad(v_rset.getString(1) + ":" + v_rset.getString(2), 42) + v_dyn_rset.getString(1)); //$NON-NLS-1$ //$NON-NLS-2$

			v_dyn_rset.close();
			v_dyn_stmt.close();
		}

		println("\n Find completed"); //$NON-NLS-1$

		v_rset.close();
		v_stmt.close();

		return true;
	}


	// ***************************************************************************

	protected boolean descTable(Connection p_conn, String p_table) throws SQLException {
		String v_grep = grep(p_table);
		String v_table = p_table;
		String v_sql;

		if (v_grep != null) {
			int v_off = v_table.indexOf(' ');
			v_table = v_table.substring(0, v_off);
		}

		v_sql = "select * from " + v_table + " where 1=2"; //$NON-NLS-1$ //$NON-NLS-2$

		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		v_stmt = p_conn.prepareStatement(v_sql);
		v_rset = v_stmt.executeQuery();

		ResultSetMetaData v_meta = v_rset.getMetaData();
		int v_cols = v_meta.getColumnCount();

		// print out a nice header
		println(" Name                                      Type  "); //$NON-NLS-1$
		println(" ----------------------------------------- ----------------------------"); //$NON-NLS-1$

		// print out the column definitions
		for (int i = 0; i < v_cols; i++)
			if (v_grep == null || v_meta.getColumnName(i + 1).toLowerCase().indexOf(v_grep.toLowerCase()) > 0)
				println(" " + Text.rpad(v_meta.getColumnName(i + 1), 42) + v_meta.getColumnTypeName(i + 1) + "(" + v_meta.getColumnDisplaySize(i + 1) //$NON-NLS-1$ //$NON-NLS-2$
						+ ")"); //$NON-NLS-1$

		println("\n " + v_cols + " columns"); //$NON-NLS-1$ //$NON-NLS-2$

		v_rset.close();
		v_stmt.close();

		return true;
	}


	// ***************************************************************************

	protected boolean sourceObject(Connection p_conn, int p_db_vendor, String p_object) throws SQLException {
		PreparedStatement v_stmt = null;
		CallableStatement v_cstmt = null;
		ResultSet v_rset = null;

		String v_sql;

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				switch (objectType(p_conn, SqlMoreConnection.VENDOR_ORACLE, p_object)) {
					case UNKNOWN:
						printerr("Unable to determine object type for: " + p_object); //$NON-NLS-1$
						return false;
					case VIEW:
						v_sql = "select text from all_views where view_name = ?"; //$NON-NLS-1$
						break;
					default:
						v_sql = "select text from all_source where name = ?"; //$NON-NLS-1$
				}

				v_stmt = p_conn.prepareStatement(v_sql);
				v_stmt.setString(1, p_object.toUpperCase());
				//v_stmt.setString(2, p_object.toUpperCase());
				v_rset = v_stmt.executeQuery();
				break;
			case SqlMoreConnection.VENDOR_MSSQLSERVER:
				v_sql = "{call sp_helptext(?)}"; //$NON-NLS-1$
				v_cstmt = p_conn.prepareCall(v_sql);
				v_cstmt.setString(1, p_object.toUpperCase());
				v_rset = v_cstmt.executeQuery();
				break;
			case SqlMoreConnection.VENDOR_HSQLDB:
				v_sql = "select view_definition from system_views where table_name = ?"; //$NON-NLS-1$
				v_stmt = p_conn.prepareStatement(v_sql);
				v_stmt.setString(1, p_object.toUpperCase());
				v_rset = v_stmt.executeQuery();
				break;
			default:
				printerr("Undefined operation for db vendor #" + p_db_vendor); //$NON-NLS-1$
				return false;
		}

		// print out a nice header
		println(" " + p_object.toUpperCase()); //$NON-NLS-1$
		println(" ----------------------------------------------------------------------"); //$NON-NLS-1$

		// print out the object source
		while(v_rset.next()) {
			print(" " + v_rset.getString(1)); //$NON-NLS-1$
		}

		println("\n ----------------------------------------------------------------------"); //$NON-NLS-1$

		if (v_rset != null)
			v_rset.close();
		if (v_stmt != null)
			v_stmt.close();
		if (v_cstmt != null)
			v_cstmt.close();

		return true;
	}


	// ***************************************************************************

	protected boolean executeQuery(Connection p_conn, String p_sql) throws SQLException {
		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		v_stmt = p_conn.prepareStatement(p_sql);
		v_rset = v_stmt.executeQuery();

		listResultSet(v_rset);

		v_rset.close();
		v_stmt.close();

		v_rset = null;
		v_stmt = null;

		return true;
	}


	// ***************************************************************************

	private boolean listResultSet(ResultSet p_rset) throws SQLException {
		ResultSetMetaData v_meta = p_rset.getMetaData();
		int v_cols = v_meta.getColumnCount();
		int[] v_col_len = new int[v_cols];

		// calculate the column widths
		for (int i = 0; i < v_cols; i++) {
			v_col_len[i] = v_meta.getColumnDisplaySize(i + 1);
			if (v_col_len[i] > 80)
				v_col_len[i] = 80;
		}

		// print out the column headings
		for (int i = 0; i < v_cols; i++) {
			if (i < v_cols - 1) {
				print(Text.rpad(v_meta.getColumnName(i + 1), v_col_len[i] + 2));
			}
			else {
				print(v_meta.getColumnName(i + 1));
			}
		}

		println(""); //$NON-NLS-1$
		println("-------------------------------------------------------------------------------"); //$NON-NLS-1$

		// print out the query results
		int v_rcnt = 0;
		while(p_rset.next()) {
			v_rcnt++;
			for (int i = 0; i < v_cols; i++)
				if (i < v_cols - 1) {
					print(Text.rpad(p_rset.getString(i + 1), v_col_len[i] + 2));
				}
				else {
					print(p_rset.getString(i + 1));
				}

			println();
		}

		println(v_rcnt + " row(s) selected"); //$NON-NLS-1$ //$NON-NLS-2$
		v_col_len = null;
		return true;
	}


	// ***************************************************************************

	protected int objectType(Connection p_conn, int p_db_vendor, String p_object) throws SQLException {
		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		String v_sql;
		int v_rc = UNKNOWN;

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				v_sql = "select object_type from all_objects where object_name = ?"; //$NON-NLS-1$
				v_stmt = p_conn.prepareStatement(v_sql);
				v_stmt.setString(1, p_object.trim().toUpperCase());
				v_rset = v_stmt.executeQuery();
				if (v_rset.next()) {
					if (v_rset.getString(1).compareTo("VIEW") == 0) //$NON-NLS-1$
						v_rc = VIEW;
					else if (v_rset.getString(1).compareTo("TABLE") == 0) //$NON-NLS-1$
						v_rc = TABLE;
				}

				v_rset.close();
				v_stmt.close();

				break;
		}

		return v_rc;
	}


	// ***************************************************************************

	protected boolean executeDML(Connection p_conn, int p_op, String p_sql) throws SQLException {
		PreparedStatement v_stmt = null;
		int v_rowcnt = 0;

		v_stmt = p_conn.prepareStatement(p_sql);
		m_stmt = v_stmt;
		v_rowcnt = v_stmt.executeUpdate();

		switch (p_op) {
			case INSERT:
				println("\n " + v_rowcnt + " row(s) inserted"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case UPDATE:
				println("\n " + v_rowcnt + " row(s) updated"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case DELETE:
				println("\n" + v_rowcnt + " row(s) deleted"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			default:
				println("\nDML request completed with " + v_rowcnt + " row(s) affected"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		v_stmt.close();

		v_stmt = null;

		return true;
	}


	// ***************************************************************************

	protected boolean executeDDL(Connection p_conn, String p_sql) throws SQLException {
		Statement v_stmt = p_conn.createStatement();
		// there is a bug in the Oracle 9i driver that causes this to hang
		// on two calls, fixed in Oracle 10.1 driver
		v_stmt.execute(p_sql);

		if (v_stmt.getWarnings() != null) {
			println(v_stmt.getWarnings().getLocalizedMessage());
			p_conn.clearWarnings();
		}
		else
			println("\nDDL request completed"); //$NON-NLS-1$

		v_stmt.close();

		return true;
	}


	protected boolean executeStoredProc(Connection p_conn, int p_db_vendor, String p_sql) throws SQLException {
		CallableStatement v_cstmt = null;
		ResultSet v_rset = null;

		String v_sql = p_sql.trim();
		int v_len = v_sql.length();

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE: // "call "
				if (v_sql.toLowerCase().startsWith("call")) //$NON-NLS-1$
					v_sql = v_sql.substring(5);
				break;
			case SqlMoreConnection.VENDOR_MSSQLSERVER: // "exec "
				if (v_sql.toLowerCase().startsWith("exec")) //$NON-NLS-1$
					v_sql = v_sql.substring(5);
				break;
			default:
				printerr("Undefined operation for db vendor #" + p_db_vendor); //$NON-NLS-1$
				return false;
		}

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				/*        
				 v_sql = "begin :1 := "+v_sql+"; end;";
				 v_cstmt = p_conn.prepareCall(v_sql);
				 v_cstmt.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.CURSOR);
				 v_cstmt.execute();
				 v_rset = (ResultSet)v_cstmt.getObject(1);
				 */
				if (!v_sql.toLowerCase().startsWith("begin") && !v_sql.toLowerCase().startsWith("declare")) //$NON-NLS-1$ //$NON-NLS-2$
					v_sql = "begin " + v_sql + "; end;"; //$NON-NLS-1$ //$NON-NLS-2$

				v_cstmt = p_conn.prepareCall(v_sql);
				v_cstmt.execute();

				if (m_output)
					outputGetLines(p_conn);

				break;
			case SqlMoreConnection.VENDOR_MSSQLSERVER:
				// encase the call in brackets
				v_sql = v_sql.replaceFirst(" ", "('"); //$NON-NLS-1$ //$NON-NLS-2$
				if (v_len < v_sql.length()) {
					v_sql = v_sql.replaceAll(" ", "','"); //$NON-NLS-1$ //$NON-NLS-2$
					v_sql = v_sql + "')"; //$NON-NLS-1$
				}
				else
					v_sql = v_sql + "()"; //$NON-NLS-1$

				v_sql = "{call " + v_sql + "}"; //$NON-NLS-1$ //$NON-NLS-2$
				v_cstmt = p_conn.prepareCall(v_sql);
				v_rset = v_cstmt.executeQuery();
				break;
			default:
				printerr("Undefined operation for db vendor #" + p_db_vendor); //$NON-NLS-1$
				return false;
		}

		if (v_rset != null)
			listResultSet(v_rset);
		else
			println("\nCall completed."); //$NON-NLS-1$

		if (v_rset != null)
			v_rset.close();
		v_cstmt.close();

		v_rset = null;
		v_cstmt = null;

		return true;
	}


	// ***************************************************************************

	protected boolean showErrorsStoredProc(Connection p_conn, int p_db_vendor, String p_sql) throws SQLException {
		// this is insufficient for Oracle, we need to track the last segment type and
		// name to use USER_ERRORS selectively

		PreparedStatement v_stmt = null;
		ResultSet v_rset = null;

		if (p_conn.getWarnings() != null) {
			println("\nWARNINGS"); //$NON-NLS-1$
			println("--------------------------------------------------------------------------"); //$NON-NLS-1$
			// this may need to be itterated
			println(p_conn.getWarnings().getMessage());
		}

		String v_sql;

		println("\nLINE/COL ERROR"); //$NON-NLS-1$
		println("-------- -----------------------------------------------------------------"); //$NON-NLS-1$

		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				// ---------------------------------------------------------------------
				// Less than perfect hackery to obtain the error object type and name
				//
				v_sql = p_sql.trim().toLowerCase().replace('\n', ' ');

				String v_val = v_sql.replaceFirst("(?mi)^create\\s+or\\s+replace\\s+(\\w+)\\s+(\\w+)\\s+(\\w+)\\s+", "$1:$2:$3: "); //$NON-NLS-1$ //$NON-NLS-2$
				String[] v_arr = v_val.split(":"); //$NON-NLS-1$

				// ugly, this shouldn't be necessary
				if (v_arr.length < 2) {
					v_val = v_sql.replaceFirst("(?mi)^create\\s+(\\w+)\\s+(\\w+)\\s+(\\w+)\\s+", "$1:$2:$3: "); //$NON-NLS-1$ //$NON-NLS-2$
					v_arr = v_val.split(":"); //$NON-NLS-1$
				}

				//System.out.println(">>"+v_arr[0]);
				//System.out.println(">>>"+v_arr[1]);

				String v_name = v_arr[1];
				String v_type = v_arr[0];

				// check to see if we have a package body
				if (v_name.compareTo("body") == 0) { //$NON-NLS-1$
					v_name = v_arr[2];
					v_type += " " + v_arr[1]; //$NON-NLS-1$
				}
				// ---------------------------------------------------------------------

				v_sql = "select line, position, text from all_errors a " + "where upper(a.name) = upper(?) " + "and upper(a.type) = upper(?) " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ "and upper(a.owner) = upper(?) " + "order by line, position, attribute, message_number"; //$NON-NLS-1$ //$NON-NLS-2$

				v_stmt = p_conn.prepareStatement(v_sql);
				v_stmt.setString(1, v_name);
				v_stmt.setString(2, v_type);
				v_stmt.setString(3, p_conn.getMetaData().getUserName());
				v_rset = v_stmt.executeQuery();
				while(v_rset.next())
					println(Text.rpad(v_rset.getString(1) + "/" + v_rset.getString(2), 9) + v_rset.getString(3)); //$NON-NLS-1$

				v_rset.close();
				v_stmt.close();

				v_rset = null;
				v_stmt = null;

				break;
		}

		p_conn.clearWarnings();

		return true;
	}


	// ***************************************************************************

	protected boolean showSourceDDL(String p_sql) {
		println("\nLINE "); //$NON-NLS-1$
		println("---- -----------------------------------------------------------------"); //$NON-NLS-1$

		String[] v_arr = p_sql.split("\n"); //$NON-NLS-1$

		for (int i = 0; i < v_arr.length; i++)
			println(Text.rpad(i + 1 + "", 5) + v_arr[i]); //$NON-NLS-1$

		return true;
	}


	// ***************************************************************************

	protected boolean outputEnable(Connection p_conn, int p_db_vendor, int p_size) throws SQLException {
		if (p_db_vendor != SqlMoreConnection.VENDOR_ORACLE)
			return false;

		String v_sql = "begin dbms_output.enable(?); end;"; //$NON-NLS-1$

		CallableStatement v_cstmt = null;
		v_cstmt = p_conn.prepareCall(v_sql);
		v_cstmt.setInt(1, p_size);
		v_cstmt.execute();

		v_cstmt.close();

		m_output = true;

		return true;
	}


	// ***************************************************************************

	protected boolean outputGetLines(Connection p_conn) throws SQLException {
		int v_maxlines = 100;

		String v_sql = "begin dbms_output.get_lines(?, ?); end;"; //$NON-NLS-1$

		oracle.jdbc.OracleCallableStatement v_cstmt = null;
		v_cstmt = (oracle.jdbc.OracleCallableStatement)p_conn.prepareCall(v_sql);
		v_cstmt.registerIndexTableOutParameter(1, v_maxlines, oracle.jdbc.OracleTypes.VARCHAR, 200); // lines
		v_cstmt.setInt(2, v_maxlines); // numlines
		v_cstmt.registerOutParameter(2, oracle.jdbc.OracleTypes.INTEGER); // numlines
		v_cstmt.execute();

		oracle.sql.Datum[] v_arr = v_cstmt.getOraclePlsqlIndexTable(1);
		int v_cnt = v_cstmt.getInt(2);

		for (int i = 0; i < v_cnt; i++)
			if (v_arr[i] != null)
				println(v_arr[i].stringValue());

		v_cstmt.close();

		return true;
	}


	// ***************************************************************************

	protected boolean outputDisable(Connection p_conn, int p_db_vendor) throws SQLException {
		if (p_db_vendor != SqlMoreConnection.VENDOR_ORACLE)
			return false;

		String v_sql = "begin dbms_output.disable; end;"; //$NON-NLS-1$

		CallableStatement v_cstmt = null;
		v_cstmt = p_conn.prepareCall(v_sql);
		v_cstmt.execute();

		v_cstmt.close();

		m_output = false;

		return true;
	}


	// ***************************************************************************

	protected boolean showSessions(Connection p_conn, int p_db_vendor) throws SQLException {
		int v_cnt = 0;
		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				String v_sql = "select sid, serial#, nvl(username,'n/a'), to_char(logon_time,'yyyy-mm-dd hh24:mi') logon, machine||' via '||program from v$session order by username asc, program asc, logon asc"; //$NON-NLS-1$

				PreparedStatement v_stmt = p_conn.prepareStatement(v_sql);

				ResultSet v_rset = v_stmt.executeQuery();

				println("\nSID      SERIAL   USERNAME             LOGON           INFO"); //$NON-NLS-1$
				println("-------- -------- -------------------- ---------------- ------------------"); //$NON-NLS-1$
				while(v_rset.next()) {
					v_cnt++;
					println(Text.rpad(v_rset.getString(1), 9) + Text.rpad(v_rset.getString(2), 9) + Text.rpad(v_rset.getString(3), 20)
							+ Text.rpad(v_rset.getString(4), 16) + v_rset.getString(5));
					println("Kill cut-and-paste: alter system kill session '" + v_rset.getString(1) + "," + v_rset.getString(2) + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}

				println("\nNumber of Active Sessions: " + v_cnt); //$NON-NLS-1$

				v_sql = "alter system kill session 'sid,serial'"; //$NON-NLS-1$

				v_stmt.close();
				break;
		}

		return true;
	}


	// ***************************************************************************

	protected boolean showRowLocks(Connection p_conn, int p_db_vendor) throws SQLException {
		switch (p_db_vendor) {
			case SqlMoreConnection.VENDOR_ORACLE:
				String v_sql = "select l.sid, l.type, l.id1, l.id2 from v$lock l " + "where l.type = 'TM'"; //$NON-NLS-1$ //$NON-NLS-2$

				//        select o.object_name, l.sid, l.type, l.id1, l.id2 from v$lock l, all_objects o
				//        where l.type = 'TM'
				//        and o.object_id = l.id1

				PreparedStatement v_stmt = p_conn.prepareStatement(v_sql);

				ResultSet v_rset = v_stmt.executeQuery();

				println("\nSID      OBJECT ID"); //$NON-NLS-1$
				println("-------- -----------------------------------------------------------------"); //$NON-NLS-1$
				while(v_rset.next()) {
					println(Text.rpad(v_rset.getString(1), 9) + v_rset.getString(3));
				}

				v_stmt.close();
				break;
		}

		return true;
	}

}
