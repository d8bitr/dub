/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 20.09.2005 - 11:09:33
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: SimpleContentPanel.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/20 14:48:58  dgm
 * Einfaches Panel mit back-button, welches einen inhalt zwischen den processablePanels anzeigen kann ohne die reihenfolge durcheinander zu bringen
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.util.Messages;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 * Einfaches Panel mit Zurück Button
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class SimpleContentPanel extends AbstractPanel {

	protected JButton buttonBack = new JButton(Messages.get("SimpleContentPanel.back.button")); //$NON-NLS-1$

	public SimpleContentPanel(Controller controller, ApplicationFrame applicationFrame, String headerText, final JPanel currentPanel) {
		super(controller, applicationFrame, headerText);
		//TODO gleiche Problematik wie in AbstractPanel
		//setupComponents();
		final ApplicationFrame finalApplication = applicationFrame;
		buttonBack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				finalApplication.setPanel(currentPanel);
			}
		});
	}

	public void setupComponents() {
		add(createButtonPanel(), BorderLayout.SOUTH);
	}

	private JPanel createButtonPanel() {
		buttonBack.setIcon(ImageFactory.getIcon("back.png")); //$NON-NLS-1$
		ButtonBarBuilder bbbuilder = new ButtonBarBuilder();
		bbbuilder.addGlue();
		bbbuilder.addGriddedButtons(new JButton[] {buttonBack});
		FormLayout buttonLayout = new FormLayout("4dlu, default:grow, 4dlu", // columns //$NON-NLS-1$
                                                 "6dlu, default, 6dlu, fill:default:grow, 6dlu"); // rows //$NON-NLS-1$
		CellConstraints ccbb = new CellConstraints();
		JPanel buttonPanel = new JPanel(buttonLayout);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		buttonPanel.add(separator, ccbb.xy(2, 2));
		buttonPanel.add(bbbuilder.getPanel(), ccbb.xy(2, 4));
		return buttonPanel;
	}

}
