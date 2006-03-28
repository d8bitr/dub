/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 20.09.2005 - 12:40:39
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: SimpleTextPanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/20 14:51:21  dgm
 * history, simpletextpanel, release 1.0.0
 *
 */
package net.sf.dub.application.view.swing;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;
import net.sf.dub.miniframework.view.swing.SimpleContentPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 * Zeigt einen einfachen Text an
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class SimpleTextPanel extends SimpleContentPanel {
	
	protected JScrollPane scrollPane = new JScrollPane();
	protected JTextArea textArea;
	protected JEditorPane editor;
	protected boolean isHtml = false;

	public SimpleTextPanel(Controller controller, ApplicationFrame applicationFrame, String headerText, JPanel currentPanel, String text, boolean editable, boolean isHtml) {
		super(controller, applicationFrame, headerText, currentPanel);
		this.isHtml = isHtml;
		if (this.isHtml) {
			editor = new JEditorPane("text/html", text); //$NON-NLS-1$
		}
		else {
			textArea = new JTextArea();
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setText(text);
			textArea.setEditable(editable);
			textArea.setCaretPosition(0);
		}
		setupComponents();
	}

	public void setupComponents() {
		super.setupComponents();
		FormLayout layout = new FormLayout("4dlu, fill:default:grow, 4dlu", // columns //$NON-NLS-1$
                                           "4dlu, fill:default:grow, 4dlu"); // rows //$NON-NLS-1$
		CellConstraints cc = new CellConstraints();
		PanelBuilder builder = new PanelBuilder(layout);
		scrollPane.setViewportView(isHtml ? (JComponent)editor : textArea);
		builder.add(scrollPane, cc.xy(2, 2));
		add(builder.getPanel(), BorderLayout.CENTER);
	}

}
