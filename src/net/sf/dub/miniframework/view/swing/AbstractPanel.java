/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 14:40:45
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: AbstractPanel.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.5  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.4  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 * Revision 1.3  2005/08/11 14:06:26  dgm
 * SqlMore angepasst (Puh), komplett die Connection und die Ausgabe austauschbar gemacht, in gui integriert (wenn gleich noch nicht im thread)
 *
 * Revision 1.2  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.4  2005/08/04 07:45:10  dgm
 * refactoring, about panel "back", fix der größe vom textarea
 *
 * Revision 1.3  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 * Revision 1.2  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 * Revision 1.1  2005/07/14 14:42:52  dgm
 * initialer import für dub
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.util.Messages;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 * Abstraktes JPanel das in das ApplicationFrame eingebettet wird.
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class AbstractPanel extends JPanel {
	
	protected Controller controller;
	protected ApplicationFrame applicationFrame;
	private JLabel headerLabel = new JLabel(""); //$NON-NLS-1$

	public AbstractPanel(Controller controller, ApplicationFrame applicationFrame, String headerText) {
		super(new BorderLayout());
		this.controller = controller;
		this.applicationFrame = applicationFrame;
		add(createHeaderPanel(headerText), BorderLayout.NORTH);
		// TODO Problem ist das die Komponenten noch nicht initialisiert wurden vom erst später aufgerufenen Konstruktor der Child Klasse
		//setupComponents();
	}
	
	private JPanel createHeaderPanel(String headerText) {
		FormLayout headerLayout = new FormLayout("4dlu, default:grow, 4dlu", // columns //$NON-NLS-1$
                                                 "4dlu, fill:max(16dlu;default):grow, 4dlu"); // rows //$NON-NLS-1$
		JPanel headerPanel = new JPanel(headerLayout);
		headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 12f));
		headerLabel.setForeground(Color.WHITE);
		CellConstraints cc = new CellConstraints();
		JPanel headerBackgroundPanel = new JPanel(new BorderLayout());
		headerBackgroundPanel.setBackground(Color.GRAY);
		headerBackgroundPanel.add(headerLabel, BorderLayout.CENTER);
		headerPanel.add(headerBackgroundPanel, cc.xy(2, 2));
		setHeaderText(headerText);
		return headerPanel;
	}
	
	protected void setHeaderText(String headerText) {
		headerLabel.setText(" " + headerText); //$NON-NLS-1$
	}

	public Controller getController() {
		return controller;
	}
	
	public void showErrorDialog(String errorText, Exception ex) {
		String lineSpeperator = System.getProperty("line.separator"); //$NON-NLS-1$
		JOptionPane.showMessageDialog(getApplicationFrame().getFrame(), errorText + lineSpeperator + lineSpeperator + "[" + ex + "]" + lineSpeperator + ex.getLocalizedMessage(), Messages.get("AbstractPanel.ErrorDialog.Title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ex.printStackTrace();
	}
	
	public void showErrorDialog(String errorText) {
		JOptionPane.showMessageDialog(getApplicationFrame().getFrame(), errorText, Messages.get("AbstractPanel.ErrorDialog.Title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}
	
	protected ApplicationFrame getApplicationFrame() {
		return applicationFrame;
	}

	public abstract void setupComponents();

}
