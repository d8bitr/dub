/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 10:35:28
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ApplicationFrame.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 */
package net.sf.dub.miniframework.view.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.dub.miniframework.controller.Controller;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public interface ApplicationFrame {

	public Controller getApplicationController();
	public void setFrameSizeCentered(int width, int height);
	public void terminateApplication();
	public void setPanel(JPanel panel);
	public JPanel getCurrentPanel();
	public JFrame getFrame();

}

