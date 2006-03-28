/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 07:53:53
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DatabaseTest.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.4  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.3  2005/09/08 21:32:46  dgm
 * test angepasst
 *
 * Revision 1.2  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.1  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 */
package net.sf.dub.application.test;

import net.sf.dub.application.db.Database;

import junit.framework.TestCase;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class DatabaseTest extends TestCase {
	
	public void testConnection() throws Exception {
		Database db = new Database();
		//db.connect("your.sb.server.com", "1521", "sid", "name", "password");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$ //$NON-NLS-5$
	}

}

