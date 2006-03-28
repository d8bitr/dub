/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.08.2005 - 16:13:58
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: VersiontablePanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.17  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.16  2005/09/09 15:20:12  dgm
 * zuviele new lines
 *
 * Revision 1.15  2005/09/02 07:59:24  dgm
 * bugfix für ArrayIndexOutOfBoundsException
 *
 * Revision 1.14  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.13  2005/08/16 15:19:02  dgm
 * kleinere änderungen
 *
 * Revision 1.12  2005/08/15 14:47:55  dgm
 * trim hinzugefügt
 *
 * Revision 1.11  2005/08/09 15:46:14  dgm
 * bugfix beim laden des configurationsfiles, anzeige update erweitert
 *
 * Revision 1.10  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.9  2005/08/07 21:43:46  dgm
 * validierung version_tabelle
 *
 * Revision 1.8  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 * Revision 1.7  2005/08/05 11:32:52  dgm
 * direktzugriff auf properties unterbunden
 *
 * Revision 1.6  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.5  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.4  2005/08/04 11:59:01  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.3  2005/08/04 07:45:10  dgm
 * refactoring, about panel "back", fix der größe vom textarea
 *
 * Revision 1.2  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 * Revision 1.1  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 */
package net.sf.dub.application.view.swing;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import net.sf.dub.application.controller.VersiontableController;
import net.sf.dub.application.db.ExecutionException;
import net.sf.dub.application.db.RetrieveDataException;
import net.sf.dub.miniframework.controller.ProcessException;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersiontablePanel extends DubProcessablePanel {
	
	protected JTextField textVersiontable = new JTextField(""); //$NON-NLS-1$
	protected JTable tableTables = new JTable();

	public VersiontablePanel(ProcessableController controller, ApplicationFrame application) {
		super(controller, application, Messages.get("VersiontablePanel.title")); //$NON-NLS-1$
		setupComponents();
		updateView();
	}

	public void setupComponents() {
		FormLayout layout = new FormLayout("4dlu, right:max(72dlu;default), 2dlu, default:grow, 4dlu", // columns //$NON-NLS-1$
		                                   "4dlu, default, 8dlu, default, 4dlu, default, 4dlu, fill:default:grow, 4dlu"); // rows //$NON-NLS-1$
		CellConstraints cc = new CellConstraints();
		PanelBuilder builder = new PanelBuilder(layout);
		builder.add(new JLabel(Messages.get("VersiontablePanel.about")), cc.xyw(2, 2, 3)); //$NON-NLS-1$

		builder.addSeparator(Messages.get("VersiontablePanel.separator.table"), cc.xyw(2, 4, 3)); //$NON-NLS-1$
		builder.addLabel(Messages.get("VersiontablePanel.table.tablename.label"), cc.xy(2, 6)); //$NON-NLS-1$
		builder.add(textVersiontable, cc.xy(4, 6));
		builder.addLabel(Messages.get("VersiontablePanel.table.existingtables.label"), cc.xy(2, 8)); //$NON-NLS-1$
		builder.add(new JScrollPane(tableTables), cc.xy(4, 8));
		tableTables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableTables.setDefaultRenderer(Object.class, new VersiontableCellRenderer());
		tableTables.getTableHeader().setReorderingAllowed(false);
		tableTables.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				// Es muß eine Zeile ausgewählt sein -> Beim initialisieren noch false, dies führt zu einer ArrayIndexOutOfBoundsException
				// die verwirrenderweise als InvokeTargetException ausgewiesen wird (Und dies auch nur wenn man mehrere JVM installiert hat)
				if (!tableTables.getSelectionModel().isSelectionEmpty()) {
					textVersiontable.setText((String)tableTables.getModel().getValueAt(tableTables.getSelectedRow(), 1));
				}
			}
		});
		VersiontableController versionController = (VersiontableController)getController();
		try {
			VersiontableTableModel dataModel = new VersiontableTableModel(versionController.getDatabase());
			tableTables.setModel(dataModel);
			TableColumn columnRownum = tableTables.getColumnModel().getColumn(0);
			columnRownum.setPreferredWidth(64);
			columnRownum.setMaxWidth(64);
			TableColumn columnTablespace = tableTables.getColumnModel().getColumn(2);
			columnTablespace.setPreferredWidth(192);
			columnTablespace.setMaxWidth(192);
		}
		catch (RetrieveDataException ex) {
			showErrorDialog(Messages.get("VersiontablePanel.error.loadingtable"), ex); //$NON-NLS-1$
		}
		textVersiontable.getDocument().addDocumentListener(new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {
			}
			public void insertUpdate(DocumentEvent e) {
				setButtonNextEnabled(textVersiontable.getText().length() > 0);
			}
			public void removeUpdate(DocumentEvent e) {
				setButtonNextEnabled(textVersiontable.getText().length() > 0);
			}
		});
		add(builder.getPanel(), BorderLayout.CENTER);
	}

	public void updateView() {
		textVersiontable.setText(getConfiguration().getProperty("update.table")); //$NON-NLS-1$
	}
	
	public void onNext() throws ProcessException {
		saveData();
		VersiontableController versionController = (VersiontableController)getController();
		String versionTable = versionController.getConfiguration().getProperty("update.table"); //$NON-NLS-1$
		try {
			if (!versionController.existVersionTable()) {
				String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
				int resultCreateTable = JOptionPane.showConfirmDialog(getApplicationFrame().getFrame(), Messages.get("VersiontablePanel.create.message.1") + lineSeparator + lineSeparator + Messages.get("VersiontablePanel.create.message.2") + " '" + versionTable + "'?" + lineSeparator + lineSeparator + Messages.get("VersiontablePanel.create.message.3"), Messages.get("VersiontablePanel.create.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				if (resultCreateTable == JOptionPane.CANCEL_OPTION) {
					throw new ProcessException(null, null, true);
				}
				try {
					versionController.createVersiontable(versionTable);
				}
				catch (ExecutionException ee) {
					throw new ProcessException(Messages.get("VersiontablePanel.error.table.create"), ee); //$NON-NLS-1$
				}
			}
		}
		catch (RetrieveDataException rdex) {
			throw new ProcessException(Messages.get("VersiontablePanel.error.load.tables"), rdex); //$NON-NLS-1$
		}
	}
	
	public void onBack() {
		saveData();
	}
	
	public void saveData() {
		getConfiguration().setProperty("update.table", textVersiontable.getText().trim()); //$NON-NLS-1$
	}

}

