package net.sf.dub.application.sqlmore;

import java.util.regex.Pattern;


/**
 * Command parser
 *
 * Copyright (c) 2001-2004 Marc Lavergne. All rights reserved.
 *
 * The following products are free software; Licensee can redistribute and/or
 * modify them under the terms of	the GNU Lesser General Public License
 * (http://www.gnu.org/copyleft/lesser.html) as published by the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-	1307  USA;
 * either version 2.1 of the	license, or any later version:
 */
public class Parser extends Console {

	// used to identify procedural language blocks
	private Pattern m_pat_cr = Pattern.compile("(?i)^\\s*create\\s+"); //$NON-NLS-1$
	private Pattern m_pat_pk = Pattern.compile("(?i)^\\s*create\\s+.*\\s+package\\s+"); //$NON-NLS-1$
	private Pattern m_pat_pr = Pattern.compile("(?i)^\\s*create\\s+.*\\s+procedure\\s+"); //$NON-NLS-1$
	private Pattern m_pat_tr = Pattern.compile("(?i)^\\s*create\\s+.*\\s+trigger\\s+"); //$NON-NLS-1$
	private Pattern m_pat_fn = Pattern.compile("(?i)^\\s*create\\s+.*\\s+function\\s+"); //$NON-NLS-1$
	private Pattern m_pat_dc = Pattern.compile("(?i)^\\s*declare\\s*"); //$NON-NLS-1$
	private Pattern m_pat_bg = Pattern.compile("(?i)^\\s*begin\\s*"); //$NON-NLS-1$


	public int parseSql(StringBuffer sql) {
		int v_op;
		int v_line;
		boolean v_sp = false;
		String v_cmd = ""; //$NON-NLS-1$

		while(true) {
			// read in an initial command line
			v_cmd = readLine();
			v_op = parseOperator(v_cmd);
			v_line = 2;

			// identify any procedural language blocks and raise flag to avoid interpreting
			// semicolons as command terminators
			if ((v_op == STOREDPROC && (m_pat_dc.matcher(v_cmd).lookingAt() || m_pat_bg.matcher(v_cmd).lookingAt()))
					|| (v_op == DDL && m_pat_cr.matcher(v_cmd).lookingAt() && (m_pat_pk.matcher(v_cmd).lookingAt() || m_pat_pr.matcher(v_cmd).lookingAt()
							|| m_pat_tr.matcher(v_cmd).lookingAt() || m_pat_fn.matcher(v_cmd).lookingAt())))
				v_sp = true;

			while(v_op > 4000 && (!Text.trailingSemicolon(v_cmd) || v_sp == true) && !Text.trailingSlash(v_cmd)) {
				print("  " + v_line + "  "); //$NON-NLS-1$ //$NON-NLS-2$
				v_cmd += "\n" + readLine(); //$NON-NLS-1$
				v_line++;
			}

			// trim the command line
			v_cmd = v_cmd.trim();

			// remove any ending slash to prevent parser confusion
			if (v_sp && Text.trailingSlash(v_cmd))
				v_cmd = v_cmd.substring(0, v_cmd.length() - 1);
			// remove any ending semi-colon to prevent parser confusion
			else if (Text.trailingSemicolon(v_cmd))
				v_cmd = v_cmd.substring(0, v_cmd.length() - 1);

			//Console.printerr(v_op+":["+v_cmd+"]");

			sql.delete(0, sql.length() + 1);
			sql.append(v_cmd);

			return v_op;
		}
	}

	protected final static int EOF = -3;
	protected final static int COMMENT = -2;
	protected final static int NADA = -1;
	protected final static int QUIT = 2;
	protected final static int HELP = 3;
	protected final static int SOURCE_LIST = 6;
	protected final static int SESSION_LIST = 7;
	protected final static int LOCK_LIST = 8;

	protected final static int SCRIPT = 50;

	protected final static int ALIAS_GET = 101;
	protected final static int ALIAS_LIST = 102;
	protected final static int ALIAS_ADD = 103;
	protected final static int ALIAS_DEL = 104;
	protected final static int CONNECT = 105;
	protected final static int DISCONNECT = 106;

	protected final static int COMMIT = 1002;
	protected final static int ROLLBACK = 1003;
	protected final static int DESC = 1004;
	protected final static int SOURCE = 1005;
	protected final static int TABLES = 1006;
	protected final static int OBJECTS = 1007;
	protected final static int VIEWS = 1008;
	protected final static int FIND = 1009;
	protected final static int SHOWERRORS = 1010;
	protected final static int REPEAT = 1011;

	protected final static int USER_ADD = 2000;
	protected final static int USER_DEL = 2001;

	protected final static int OUTPUT_ON = 3000;
	protected final static int OUTPUT_OFF = 3001;

	protected final static int EXPORT = 4001;

	protected final static int SELECT = 5001;
	protected final static int INSERT = 5002;
	protected final static int UPDATE = 5003;
	protected final static int DELETE = 5004;
	protected final static int DDL = 5005;
	protected final static int STOREDPROC = 5006;


	protected int parseOperator(String sql) {
		int operator = NADA;

		String v_sql = sql.trim();
		String v_sql_lc = v_sql.toLowerCase();

		if (v_sql_lc.length() == 0)
			operator = NADA;
		else if (v_sql_lc.compareTo("commit") == 0) //$NON-NLS-1$
			operator = COMMIT;
		else if (v_sql_lc.compareTo("rollback") == 0) //$NON-NLS-1$
			operator = ROLLBACK;
		else if (v_sql_lc.startsWith("desc")) //$NON-NLS-1$
			operator = DESC;
		else if (v_sql_lc.startsWith("source")) //$NON-NLS-1$
			operator = SOURCE;
		else if (v_sql_lc.startsWith("tables")) //$NON-NLS-1$
			operator = TABLES;
		else if (v_sql_lc.startsWith("objects")) //$NON-NLS-1$
			operator = OBJECTS;
		else if (v_sql_lc.startsWith("views")) //$NON-NLS-1$
			operator = VIEWS;
		else if (v_sql_lc.startsWith("find")) //$NON-NLS-1$
			operator = FIND;
		else if (v_sql_lc.startsWith("help")) //$NON-NLS-1$
			operator = HELP;
		else if (v_sql_lc.compareTo("s") == 0) //$NON-NLS-1$
			operator = SOURCE_LIST;
		else if (v_sql_lc.compareTo("u") == 0) //$NON-NLS-1$
			operator = SESSION_LIST;
		else if (v_sql_lc.compareTo("l") == 0) //$NON-NLS-1$
			operator = LOCK_LIST;
		else if (v_sql_lc.startsWith("@"))  // TODO hier wohl noch start und @@ hinzufügen //$NON-NLS-1$
			operator = SCRIPT;
		else if (v_sql_lc.startsWith("--")) //$NON-NLS-1$
			operator = COMMENT;
		// this is tricky since chars in java can't be signed, we check for
		// the return 65535 as a workaround
		else if (v_sql_lc.charAt(0) == 65535)
			operator = EOF;
		else if (v_sql_lc.startsWith("set serveroutput on")) //$NON-NLS-1$
			operator = OUTPUT_ON;
		else if (v_sql_lc.startsWith("set serveroutput off")) //$NON-NLS-1$
			operator = OUTPUT_OFF;
		else if (v_sql_lc.startsWith("alias")) //$NON-NLS-1$
			if (v_sql_lc.trim().length() == 5)
				operator = ALIAS_LIST;
			else
				operator = ALIAS_GET;
		else if (v_sql_lc.startsWith("+alias ")) //$NON-NLS-1$
			operator = ALIAS_ADD;
		else if (v_sql_lc.startsWith("-alias ")) //$NON-NLS-1$
			operator = ALIAS_DEL;
		else if (v_sql_lc.startsWith("connect")) //$NON-NLS-1$
			operator = CONNECT;
		else if (v_sql_lc.startsWith("disconnect")) //$NON-NLS-1$
			operator = DISCONNECT;
		else if (v_sql_lc.startsWith("quit") || v_sql_lc.startsWith("exit"))  //$NON-NLS-1$//$NON-NLS-2$
			operator = QUIT;
		else if (v_sql_lc.startsWith("show error")) //$NON-NLS-1$
			operator = SHOWERRORS;
		else if (v_sql_lc.startsWith("/")) //$NON-NLS-1$
			operator = REPEAT;
		else if (v_sql_lc.startsWith("select") || v_sql_lc.startsWith("show"))  //$NON-NLS-1$//$NON-NLS-2$
			// special case with MySQL where "show" is used as a select
			operator = SELECT;
		else if (v_sql_lc.startsWith("insert")) //$NON-NLS-1$
			operator = INSERT;
		else if (v_sql_lc.startsWith("update")) //$NON-NLS-1$
			operator = UPDATE;
		else if (v_sql_lc.startsWith("delete")) //$NON-NLS-1$
			operator = DELETE;
		else if (v_sql_lc.startsWith("exec") || v_sql_lc.startsWith("call") || v_sql_lc.startsWith("begin") || v_sql_lc.startsWith("declare"))   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
			operator = STOREDPROC;
		else if (v_sql_lc.startsWith("export")) //$NON-NLS-1$
			operator = EXPORT;
		else
			operator = DDL;

		return operator;
	}

}
