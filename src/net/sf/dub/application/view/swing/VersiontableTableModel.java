/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 09:23:30
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersiontableTableModel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.5  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.4  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.3  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.2  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 * Revision 1.1  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 */
package net.sf.dub.application.view.swing;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import net.sf.dub.application.db.Database;
import net.sf.dub.application.db.RetrieveDataException;
import net.sf.dub.miniframework.util.Messages;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersiontableTableModel extends AbstractTableModel {
	
	protected Vector tables;
	
	public VersiontableTableModel(Database database) throws RetrieveDataException {
		tables = database.getPlainQueryData("select rownum, table_name, tablespace_name from user_tables order by table_name"); //$NON-NLS-1$
	}

	public int getColumnCount() {
		if (tables == null) {
			return 0;
		}
		Object firstObject = tables.get(0);
		if ((firstObject != null) && firstObject.getClass().isAssignableFrom(Vector.class)) {
			Vector firstRow = (Vector)firstObject;
			return firstRow.size();
		}
		return 0;
	}

	public int getRowCount() {
		if (tables == null) {
			return 0;
		}
		return tables.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (tables == null) {
			return ""; //$NON-NLS-1$
		}
		Object rowObject = tables.get(rowIndex);
		if ((rowObject != null) && rowObject.getClass().isAssignableFrom(Vector.class)) {
			Vector rowVector = (Vector)rowObject;
			return rowVector.get(columnIndex);
		}
		return null;
	}
	
	public String getColumnName(int column) {
		if (column == 0) {
			return "#"; //$NON-NLS-1$
		}
		else if (column == 1) {
			return Messages.get("VersiontableTableModel.column.tablename"); //$NON-NLS-1$
		}
		else if (column == 2) {
			return Messages.get("VersiontableTableModel.column.tablespace"); //$NON-NLS-1$
		}
		return "?"; //$NON-NLS-1$
	}

}

