/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 09:53:57
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersiontableCellRenderer.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.4  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.3  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.2  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 * Revision 1.1  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 */
package net.sf.dub.application.view.swing;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import net.sf.dub.miniframework.view.swing.StandardTableCellRenderer;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersiontableCellRenderer extends StandardTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (column == 0) {
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else {
			setHorizontalAlignment(SwingConstants.LEFT);
		}
		return this;
	}

}
