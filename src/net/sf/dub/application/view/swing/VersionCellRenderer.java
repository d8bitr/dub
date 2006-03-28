/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 09.08.2005 - 14:01:34
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersionCellRenderer.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/09 14:24:48  dgm
 * Anzeige für Versionen, Aufräumen bevor neue Versionen aus der db geladen werden
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
public class VersionCellRenderer extends StandardTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (column >= 2) {
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return this;
	}

}

