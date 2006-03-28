/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 05.08.2005 - 14:00:48
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ImageTableCellRenderer.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.1  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ImageTableCellRenderer extends StandardTableCellRenderer {

	  private final ImageIcon icon = new ImageIcon();

	public ImageTableCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		setIcon(icon);
	}

	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		setFont(null);
		setText(null);
		icon.setImage((Image)value);
		if (isSelected) {
			setBackground(table.getSelectionBackground());
		}
		else {
			super.setBackground(row);
		}
		return this;
	}

}

