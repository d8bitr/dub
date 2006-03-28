/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 09.09.2005 - 14:40:59
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ImagePanel.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/09 15:17:32  dgm
 * das imagepanel (wurde auch zeit)
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ImagePanel extends JPanel {
	
	private Image image;
	private boolean borderline = false;
	private int imageWidth;
	private int imageHeight;
	
	public ImagePanel(Image image, boolean borderline) {
		this.image = image;
		this.borderline = borderline;
		imageWidth = this.image.getWidth(this);
		imageHeight = this.image.getHeight(this);
		setSize(imageWidth, imageWidth);
		setPreferredSize(new Dimension(imageWidth, imageWidth));
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(image, 0, 0, imageWidth, imageHeight, this);
		if (borderline) {
			setForeground(Color.BLACK);
			g.drawLine(0, 0, imageWidth-1, 0);
			g.drawLine(imageWidth-1, 0, imageWidth-1, imageHeight-1);
			g.drawLine(imageWidth-1, imageHeight-1, 0, imageHeight-1);
			g.drawLine(0, imageHeight-1, 0, 0);
		}
	}

}

