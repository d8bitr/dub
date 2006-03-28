/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 09.08.2005 - 15:28:35
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: FileTableModel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.5  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.4  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.3  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.2  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.1  2005/08/09 15:46:14  dgm
 * bugfix beim laden des configurationsfiles, anzeige update erweitert
 *
 */
package net.sf.dub.application.view.swing;

import javax.swing.table.AbstractTableModel;

import net.sf.dub.application.data.VersionBean;
import net.sf.dub.miniframework.util.Messages;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class FileTableModel extends AbstractTableModel {
	
	private VersionBean version;
	
	public FileTableModel(VersionBean version) {
		this.version = version;
	}

	public int getColumnCount() {
		return 1;
	}

	public int getRowCount() {
		if (version == null) {
			return 0;
		}
		return version.getFilesCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (version == null) {
			return ""; //$NON-NLS-1$
		}
		String result = version.getVersionFileName(rowIndex);
		return result;
	}

	public String getColumnName(int column) {
		if (column == 0) {
			return Messages.get("FileTableModel.column.filename"); //$NON-NLS-1$
		}
		return "?"; //$NON-NLS-1$
	}

}
