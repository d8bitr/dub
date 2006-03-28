/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 13:46:28
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersionTableModel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.7  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.6  2005/09/09 15:20:02  dgm
 * update zeit und benutzer anzeigen
 *
 * Revision 1.5  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.4  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.3  2005/08/09 14:24:48  dgm
 * Anzeige für Versionen, Aufräumen bevor neue Versionen aus der db geladen werden
 *
 * Revision 1.2  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.1  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 */
package net.sf.dub.application.view.swing;

import java.awt.Image;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

import net.sf.dub.application.data.Configuration;
import net.sf.dub.application.data.VersionBean;
import net.sf.dub.application.data.VersionSet;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.ImageFactory;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersionTableModel extends AbstractTableModel {
	
	private VersionSet versions;
	private DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
	
	public VersionTableModel(Configuration configuration) {
		versions = configuration.getVersions();
	}

	public int getColumnCount() {
		return 7;
	}

	public int getRowCount() {
		return versions.getVersionCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		VersionBean version = versions.getVersion(rowIndex);
		if (columnIndex == 0) {
			if (version.isInFile()) {
				return ImageFactory.getImage("file.gif"); //$NON-NLS-1$
			}
			return ImageFactory.getImage("file_not.gif"); //$NON-NLS-1$
		}
		else if (columnIndex == 1) {
			if (version.isInDatabase()) {
				return ImageFactory.getImage("database.gif"); //$NON-NLS-1$
			}
			return ImageFactory.getImage("database_not.gif"); //$NON-NLS-1$
		}
		else if (columnIndex == 2) {
			return "" + version.getVersionMajor(); //$NON-NLS-1$
		}
		else if (columnIndex == 3) {
			return "" + version.getVersionMinor(); //$NON-NLS-1$
		}
		else if (columnIndex == 4) {
			return "" + version.getVersionMicro(); //$NON-NLS-1$
		}
		else if (columnIndex == 5) {
			return version.getUpdateBy();
		}
		else if (columnIndex == 6) {
			Date updateDate = version.getDatabaseUpdateDate();
			if (updateDate == null) {
				return null;
			}
			return df.format(updateDate);
		}
		return "?"; //$NON-NLS-1$
	}

	public Class getColumnClass(int columnIndex) {
		if (columnIndex < 2) {
			return Image.class;
		}
		return Object.class;
	}

	public String getColumnName(int column) {
		if (column == 0) {
			return Messages.get("VersionTableModel.column.file"); //$NON-NLS-1$
		}
		else if (column == 1) {
			return Messages.get("VersionTableModel.column.database"); //$NON-NLS-1$
		}
		else if (column == 2) {
			return Messages.get("VersionTableModel.column.major"); //$NON-NLS-1$
		}
		else if (column == 3) {
			return Messages.get("VersionTableModel.column.minor"); //$NON-NLS-1$
		}
		else if (column == 4) {
			return Messages.get("VersionTableModel.column.micro"); //$NON-NLS-1$
		}
		else if (column == 5) {
			return Messages.get("VersionTableModel.column.updateby"); //$NON-NLS-1$
		}
		else if (column == 6) {
			return Messages.get("VersionTableModel.column.updateDate"); //$NON-NLS-1$
		}
		return "?"; //$NON-NLS-1$
	}

}
