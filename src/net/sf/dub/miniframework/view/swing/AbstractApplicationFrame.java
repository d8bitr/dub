/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 10:36:42
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: AbstractApplicationFrame.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/10 09:54:46  dgm
 * terminate umgelegt
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.dub.miniframework.controller.Controller;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class AbstractApplicationFrame extends JFrame implements ApplicationFrame {
	
	private Controller applicationController;
	private JPanel mainPanel;
	private JPanel currentPanel;


	public AbstractApplicationFrame(Controller applicationController, String title) {
		super(title);
		getContentPane().setLayout(new BorderLayout());
		this.applicationController = applicationController;
		mainPanel = new JPanel(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				terminateApplication();
			}
		});
	}
	
	public Controller getApplicationController() {
		return applicationController;
	}
	
	public void setFrameSizeCentered(int width, int height) {
		setSize(width, height);
		Dimension dimensionScreen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dimensionScreen.width / 2) - (getSize().width / 2),  (dimensionScreen.height / 2) - (getSize().height / 2));
	}
	
	public void terminateApplication() {
		getApplicationController().stop();
		System.exit(0);
	}
	
	public JPanel getCurrentPanel() {
		return currentPanel;
	}
	
	public void setPanel(JPanel panel) {
		if (currentPanel != null) {
			mainPanel.remove(currentPanel);
		}
		mainPanel.add(panel, BorderLayout.CENTER);
		currentPanel = panel;
		mainPanel.validate();
		mainPanel.repaint();
	}
	
	public JFrame getFrame() {
		return this;
	}

}

