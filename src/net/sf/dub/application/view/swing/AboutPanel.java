/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 13:47:23
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: AboutPanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.11  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.10  2005/09/20 14:51:21  dgm
 * history, simpletextpanel, release 1.0.0
 *
 * Revision 1.9  2005/09/12 12:45:36  dgm
 * start panel und about verbessert
 *
 * Revision 1.8  2005/09/08 12:43:39  dgm
 * about animation
 *
 * Revision 1.7  2005/09/07 12:40:07  dgm
 * Lizenzen eingebunden
 *
 * Revision 1.6  2005/08/15 14:48:04  dgm
 * text
 *
 * Revision 1.5  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.4  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.3  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.2  2005/08/04 07:45:11  dgm
 * refactoring, about panel "back", fix der größe vom textarea
 *
 * Revision 1.1  2005/07/14 14:42:52  dgm
 * initialer import für dub
 *
 */
package net.sf.dub.application.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.*;

import net.sf.dub.application.resources.DubResourcesAnchor;
import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;
import net.sf.dub.miniframework.view.swing.SimpleContentPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Zeigt Infos über die Anwendung an
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class AboutPanel extends SimpleContentPanel {
	
	protected JTextArea textLicence = new JTextArea();
	protected Hashtable licences = new Hashtable();
	protected JComboBox comboboxLicence;
	
	
	public AboutPanel(Controller controller, ApplicationFrame application, final JPanel currentPanel) {
		super(controller, application, Messages.get("AboutPanel.title"), currentPanel); //$NON-NLS-1$
		setupComponents();
	}

	public void setupComponents() {
		super.setupComponents();
		FormLayout layout = new FormLayout("4dlu, right:max(72dlu;default), 2dlu, default, 4dlu, default:grow, 4dlu", // columns //$NON-NLS-1$
                                           "4dlu, fill:128px, 8dlu, default, 4dlu, default, 4dlu, fill:default:grow, 4dlu"); // rows //$NON-NLS-1$
		CellConstraints cc = new CellConstraints();
		PanelBuilder builder = new PanelBuilder(layout);
		
		AnimationPanel animation = new AnimationPanel();
		animation.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		builder.add(animation, cc.xyw(2, 2, 5));
		builder.addSeparator(Messages.get("AboutPanel.separator.licences"), cc.xyw(2, 4, 5)); //$NON-NLS-1$

		comboboxLicence = new JComboBox(loadLicences());
		comboboxLicence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSelectedLicence();
			}
		});
		comboboxLicence.setSelectedIndex(-1);
		
		builder.add(new JLabel(Messages.get("AboutPanel.licences.component")), cc.xy(2, 6)); //$NON-NLS-1$
		builder.add(comboboxLicence, cc.xy(4, 6));
		
		textLicence.setEditable(false);
		textLicence.setLineWrap(true);
		textLicence.setWrapStyleWord(true);
		builder.add(new JLabel(Messages.get("AboutPanel.licences.licence")), cc.xy(2, 8)); //$NON-NLS-1$
		builder.add(new JScrollPane(textLicence), cc.xyw(4, 8, 3));

		add(builder.getPanel(), BorderLayout.CENTER);
	}

	protected void addLicence(String name, String fileName) {
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(DubResourcesAnchor.class.getResourceAsStream("licences/" + fileName))); //$NON-NLS-1$
			String lastLine = br.readLine();
			while ((lastLine != null)) {
				buffer.append(lastLine + System.getProperty("line.separator")); //$NON-NLS-1$
				lastLine = br.readLine();
			}
			br.close();
		}
		catch (IOException ioe) {
			buffer.setLength(0);
			buffer.append(Messages.get("AboutPanel.error.licence.load")); //$NON-NLS-1$
		}
		licences.put(name, buffer.toString());
	}
	
	protected Vector loadLicences() {
		addLicence(Messages.get("AboutPanel.licence.dub"), "dub.licence"); //$NON-NLS-1$ //$NON-NLS-2$
		addLicence(Messages.get("AboutPanel.licence.oracle"), "oracle-jdbc.licence"); //$NON-NLS-1$ //$NON-NLS-2$
		addLicence(Messages.get("AboutPanel.licence.looks"), "jgoodies-looks.licence"); //$NON-NLS-1$ //$NON-NLS-2$
		addLicence(Messages.get("AboutPanel.licence.forms"), "jgoodies-forms.licence"); //$NON-NLS-1$ //$NON-NLS-2$
		addLicence(Messages.get("AboutPanel.licence.launch4j"), "launch4j.licence"); //$NON-NLS-1$ //$NON-NLS-2$
		Vector items = new Vector(licences.keySet());
		Collections.sort(items);
		return items;
	}

	protected void showSelectedLicence() {
		Object selectedItem = comboboxLicence.getSelectedItem();
		if (selectedItem == null) {
			StringBuffer defaultText = new StringBuffer();
			defaultText.append(Messages.get("AboutPanel.licence.default.1") + System.getProperty("line.separator"));  //$NON-NLS-1$//$NON-NLS-2$
			defaultText.append(Messages.get("AboutPanel.licence.default.2") + System.getProperty("line.separator"));  //$NON-NLS-1$//$NON-NLS-2$
			defaultText.append(Messages.get("AboutPanel.licence.default.3") + System.getProperty("line.separator"));  //$NON-NLS-1$//$NON-NLS-2$
			textLicence.setText(defaultText.toString());
			
		}
		else {
			textLicence.setText((String)licences.get(selectedItem));
		}
		textLicence.setCaretPosition(0);
	}

}
