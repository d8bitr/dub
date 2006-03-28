/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 14:35:19
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ConfigurationPanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.14  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.13  2005/09/09 15:19:11  dgm
 * folder icon
 *
 * Revision 1.12  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.11  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 * Revision 1.10  2005/08/17 14:52:33  dgm
 * passwort ist nicht mehr klartext
 *
 * Revision 1.9  2005/08/05 11:32:52  dgm
 * direktzugriff auf properties unterbunden
 *
 * Revision 1.8  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.7  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.6  2005/08/04 12:27:43  dgm
 * configurationsverhalten geändert, process refactored
 *
 * Revision 1.5  2005/08/04 11:59:01  dgm
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
package net.sf.dub.application.view.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.dub.application.controller.ConfigurationController;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;
import net.sf.dub.miniframework.view.swing.ImageFactory;
import net.sf.dub.miniframework.view.swing.dialogs.FileChooser;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ConfigurationPanel extends DubProcessablePanel {

	protected JTextField textFile = new JTextField(""); //$NON-NLS-1$
	protected JTextField textUsername = new JTextField(""); //$NON-NLS-1$
	protected JPasswordField textPassword = new JPasswordField(""); //$NON-NLS-1$
	protected JTextField textServer = new JTextField(""); //$NON-NLS-1$
	protected JTextField textPort = new JTextField(""); //$NON-NLS-1$
	protected JTextField textSid = new JTextField(""); //$NON-NLS-1$
	protected JTextArea textareaRemark = new JTextArea(""); //$NON-NLS-1$
	protected JButton buttonFile = new JButton(""); //$NON-NLS-1$

	public ConfigurationPanel(ProcessableController controller, ApplicationFrame application) {
		super(controller, application, Messages.get("ConfigurationPanel.title")); //$NON-NLS-1$
		setupComponents();
		updateView();
	}

	public void setupComponents() {
		FormLayout layout = new FormLayout("4dlu, right:max(72dlu;default), 2dlu, default:grow, 4dlu, default, 4dlu", // columns //$NON-NLS-1$
		                                   "4dlu, default, 8dlu, default, 4dlu, default, 4dlu, fill:max(64dlu;default), 8dlu, default, 4dlu, default, 4dlu, default, 4dlu, default, 4dlu, default, 4dlu, default, 4dlu"); // rows //$NON-NLS-1$
		CellConstraints cc = new CellConstraints();
		PanelBuilder builder = new PanelBuilder(layout);
		builder.add(new JLabel(Messages.get("ConfigurationPanel.welcome.label")), cc.xyw(2, 2, 3)); //$NON-NLS-1$

		builder.addSeparator(Messages.get("ConfigurationPanel.separator.file"), cc.xyw(2, 4, 5)); //$NON-NLS-1$
		builder.addLabel(Messages.get("ConfigurationPanel.file.file.label"), cc.xy(2, 6)); //$NON-NLS-1$
		builder.add(textFile, cc.xy(4, 6));
		buttonFile.setIcon(new ImageIcon(ImageFactory.getImage("folder.png"))); //$NON-NLS-1$
		builder.add(buttonFile, cc.xy(6, 6));
		builder.addLabel(Messages.get("ConfigurationPanel.file.comments.label"), cc.xy(2, 8)); //$NON-NLS-1$
		builder.add(new JScrollPane(textareaRemark), cc.xy(4, 8));

		builder.addSeparator(Messages.get("ConfigurationPanel.separator.db"), cc.xyw(2, 10, 5)); //$NON-NLS-1$
		builder.addLabel(Messages.get("ConfigurationPanel.db.username.label"), cc.xy(2, 12)); //$NON-NLS-1$
		builder.add(textUsername, cc.xy(4, 12));
		builder.addLabel(Messages.get("ConfigurationPanel.db.password.label"), cc.xy(2, 14)); //$NON-NLS-1$
		builder.add(textPassword, cc.xy(4, 14));
		builder.addLabel(Messages.get("ConfigurationPanel.db.server.label"), cc.xy(2, 16)); //$NON-NLS-1$
		builder.add(textServer, cc.xy(4, 16));
		builder.addLabel(Messages.get("ConfigurationPanel.db.port.label"), cc.xy(2, 18)); //$NON-NLS-1$
		builder.add(textPort, cc.xy(4, 18));
		builder.addLabel(Messages.get("ConfigurationPanel.db.sid.label"), cc.xy(2, 20)); //$NON-NLS-1$
		builder.add(textSid, cc.xy(4, 20));
		add(builder.getPanel(), BorderLayout.CENTER);

		textFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadConfiguration();
			}
		});

		buttonFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = showOpenFileDialog();
				if (result != null) {
					textFile.setText(result);
					loadConfiguration();
				}
			}
		});
	}

	protected void loadConfiguration() {
		ConfigurationController configurationController = ((ConfigurationController)getController());
		try {
			configurationController.loadConfiguration(textFile.getText(), getConfiguration());
			updateView();
		}
		catch (Exception ex) {
			showErrorDialog(Messages.get("ConfigurationPanel.error.file.loading"), ex); //$NON-NLS-1$
		}
	}

	public String showOpenFileDialog() {
		FileChooser fc = FileChooser.getSystemFileChooser();
		Vector filters = new Vector();
		filters.add("jar"); //$NON-NLS-1$
		if (fc.showFileOpenDialog(getApplicationFrame().getFrame(), filters)) {
			return fc.getSelectedFileName();
		}
		return null;
	}
	
	public void updateView() {
		textFile.setText(getConfiguration().getProperty("application.sourcefile")); //$NON-NLS-1$
		textareaRemark.setText(getConfiguration().getProperty("remark")); //$NON-NLS-1$
		textUsername.setText(getConfiguration().getProperty("database.name")); //$NON-NLS-1$
		textPassword.setText(getConfiguration().getProperty("database.password")); //$NON-NLS-1$
		textServer.setText(getConfiguration().getProperty("database.server")); //$NON-NLS-1$
		textPort.setText(getConfiguration().getProperty("database.port")); //$NON-NLS-1$
		textSid.setText(getConfiguration().getProperty("database.sid")); //$NON-NLS-1$
	}
	
	public void onNext() {
		saveData();
	}
	
	public void onBack() {
		saveData();
	}
	
	public void saveData() {
		getConfiguration().setProperty("application.sourcefile", textFile.getText()); //$NON-NLS-1$
		getConfiguration().setProperty("remark", textareaRemark.getText()); //$NON-NLS-1$
		getConfiguration().setProperty("database.name", textUsername.getText()); //$NON-NLS-1$
		getConfiguration().setProperty("database.password", new String(textPassword.getPassword())); //$NON-NLS-1$
		getConfiguration().setProperty("database.server", textServer.getText()); //$NON-NLS-1$
		getConfiguration().setProperty("database.port", textPort.getText()); //$NON-NLS-1$
		getConfiguration().setProperty("database.sid", textSid.getText()); //$NON-NLS-1$
	}

}
