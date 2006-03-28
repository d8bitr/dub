/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.09.2005 - 14:29:11
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:46 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: PlainSqlParser.java,v $
 * Revision 1.1  2006/03/28 15:49:46  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 */
package net.sf.dub.application.sqlmore;

import java.util.Vector;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class PlainSqlParser {

	protected Vector parsedSql = new Vector();  // Beinhaltet ParseResult Objekte
	protected SqlMore more;
	
	protected class ParseResult {
		public int operator;
		public String sql;
	}
	
	
	public PlainSqlParser(String sqlContentToParse) {
		if (sqlContentToParse != null)  {
			more = new SqlMore(null, sqlContentToParse, null);
			parseSql();
		}
	}
	
	public void parseSql() {
		parsedSql.clear();
		int operator = Parser.NADA;
		StringBuffer sqlBuffer = new StringBuffer();
		while((operator = more.parseSql(sqlBuffer)) != Parser.EOF) {
			ParseResult result = new ParseResult();
			result.operator = operator;
			result.sql = sqlBuffer.toString();
			parsedSql.add(result);
		}
	}
	
	public int getParsedSqlCount() {
		return parsedSql.size();
	}
	
	public String getParsedSql(int index) {
		return ((ParseResult)parsedSql.get(index)).sql;
	}

}
