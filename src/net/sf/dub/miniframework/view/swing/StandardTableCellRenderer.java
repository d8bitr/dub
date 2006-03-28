/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 14:19:38
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: StandardTableCellRenderer.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.1  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class StandardTableCellRenderer extends DefaultTableCellRenderer {

	private static Color COLOR_ODD = new Color(238, 242, 255);

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (!isSelected) {
			setBackground(row);
		}
		return this;
	}
	
	public void setBackground(int row) {
		if ((row % 2) != 0) {
			setBackground(COLOR_ODD);
		}
		else {
			setBackground(Color.WHITE);
		}
	}

}

