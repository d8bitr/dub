/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 10.08.2005 - 14:35:27
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:46 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: SqlParser.java,v $
 * Revision 1.1  2006/03/28 15:49:46  danielgalan
 * inital import
 *
 * Revision 1.4  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.3  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.2  2005/08/11 14:06:26  dgm
 * SqlMore angepasst (Puh), komplett die Connection und die Ausgabe austauschbar gemacht, in gui integriert (wenn gleich noch nicht im thread)
 *
 * Revision 1.1  2005/08/10 13:59:36  dgm
 * aufräumarbeiten
 *
 */
package net.sf.dub.application.sqlmore;

import java.io.Writer;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Wrapper-Klasse für sqlmore, soll folgende Funktionalität haben:<br/>
 * <ul>
 *   <li>Parse ein sql-file</li>
 *   <li>halte die einzelnen geparsten, einzeln ausführbaren (atomaren) sql operationen vor</li>
 *   <li>erzeuge und verwalte eine autarke Datenbank-Verbindung</li>
 *   <li>führe die atomaren sql operation aus</li>
 * </ul>
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class SqlParser extends PlainSqlParser {
	
	public SqlParser(SqlMoreConnection sqlConnection, String sqlContentToParse, Writer writer) {
		super(null);
		more = new SqlMore(sqlConnection, sqlContentToParse + System.getProperty("line.separator"), writer); //$NON-NLS-1$
		parseSql();
		clearNadaOperations();
		/*
		parsedSql.clear();
		int operator = Parser.NADA;
		StringBuffer sqlBuffer = new StringBuffer();
		while((operator = more.parseSql(sqlBuffer)) != Parser.EOF) {
			ParseResult result = new ParseResult();
			result.operator = operator;
			result.sql = sqlBuffer.toString();
			parsedSql.add(result);
		}
		*/
	}
	
	private void executeSql(ParseResult parseResult) throws SQLException {
		more.execSql(parseResult.operator, parseResult.sql);
	}
	
	public void executeSql(int index) throws SQLException {
		executeSql((ParseResult)parsedSql.get(index));
	}
	
	public void close() throws SQLException{
		more.getConnection().close();
	}
	
	private void clearNadaOperations() {
		//Vector deletable = new Vector();
		Iterator iterParsedSql = parsedSql.iterator();
		while (iterParsedSql.hasNext()) {
			ParseResult result = (ParseResult)iterParsedSql.next();
			if (result.operator == Parser.NADA) {
				iterParsedSql.remove();
			}
		}
	}

}
