/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.08.2005 - 16:14:49
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: UpdatePanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.20  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.19  2005/09/20 14:51:21  dgm
 * history, simpletextpanel, release 1.0.0
 *
 * Revision 1.18  2005/09/20 08:18:52  dgm
 * i18n, bugfix, aufgeräumt
 *
 * Revision 1.17  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.16  2005/09/08 15:08:04  dgm
 * ausgaben speichern, buttonbars im updatepanel
 *
 * Revision 1.15  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.14  2005/08/17 14:52:48  dgm
 * refactoring
 *
 * Revision 1.13  2005/08/15 14:47:40  dgm
 * process buttons werden auch disabled beim updaten
 *
 * Revision 1.12  2005/08/12 12:59:00  dgm
 * updaten in thread
 *
 * Revision 1.11  2005/08/11 14:06:26  dgm
 * SqlMore angepasst (Puh), komplett die Connection und die Ausgabe austauschbar gemacht, in gui integriert (wenn gleich noch nicht im thread)
 *
 * Revision 1.10  2005/08/10 09:55:37  dgm
 * letzte feinheiten vor integration von sqlmore
 *
 * Revision 1.9  2005/08/09 15:46:14  dgm
 * bugfix beim laden des configurationsfiles, anzeige update erweitert
 *
 * Revision 1.8  2005/08/09 14:24:48  dgm
 * Anzeige für Versionen, Aufräumen bevor neue Versionen aus der db geladen werden
 *
 * Revision 1.7  2005/08/09 12:58:20  dgm
 * Anzeige im letzten Schritt
 *
 * Revision 1.6  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import net.sf.dub.application.controller.SimpleTextController;
import net.sf.dub.application.controller.UpdateController;
import net.sf.dub.application.data.VersionBean;
import net.sf.dub.application.db.ExecutionException;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.util.EventStringListener;
import net.sf.dub.miniframework.util.EventStringWriter;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.util.ObservableThread;
import net.sf.dub.miniframework.util.ThreadListener;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;
import net.sf.dub.miniframework.view.swing.ImageFactory;
import net.sf.dub.miniframework.view.swing.ImageTableCellRenderer;
import net.sf.dub.miniframework.view.swing.dialogs.FileChooser;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Dieses Panel zeigt die Updates an, und steuert den Ablauf dieser in die Datenbank. Weiter zeigt es den Fortschritt an
 * und läßt fehlerhafte mittels Kontroll Elemente überspringen.
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class UpdatePanel extends DubProcessablePanel {

	protected JTable tableVersions = new JTable();
	protected JTextArea textareaOutput = new JTextArea(""); //$NON-NLS-1$
	protected JButton buttonVersion = new JButton(Messages.get("UpdatePanel.versions.execute.button")); //$NON-NLS-1$
	protected JButton buttonOutputClean = new JButton(Messages.get("UpdatePanel.output.delete")); //$NON-NLS-1$
	protected JButton buttonOutputSave = new JButton(Messages.get("UpdatePanel.output.save")); //$NON-NLS-1$
	protected JLabel labelCurrentVersion = new JLabel(""); //$NON-NLS-1$
	protected JLabel labelCurrentFile = new JLabel(""); //$NON-NLS-1$
	protected JLabel labelCurrentSql = new JLabel(""); //$NON-NLS-1$
	protected JProgressBar progressVersion = new JProgressBar();
	protected JProgressBar progressFile = new JProgressBar();
	protected JProgressBar progressSql = new JProgressBar();
	protected JButton buttonSkip = new JButton(Messages.get("UpdatePanel.progress.skip")); //$NON-NLS-1$
	protected JButton buttonViewFile = new JButton(Messages.get("UpdatePanel.progress.view.file")); //$NON-NLS-1$
	protected JButton buttonViewSql = new JButton(Messages.get("UpdatePanel.progress.view.sql")); //$NON-NLS-1$
	protected final EventStringWriter eventWriter = new EventStringWriter();
	
	protected static final int EXECUTION_FINISHED = 0;
	protected static final int EXECUTION_FAILED = 1;
	protected static final int EXECUTION_CONTINUED = 2;
	
	protected int executionStatus = EXECUTION_FINISHED;
	protected Vector versions;
	protected int currentVersion;
	protected Vector files = new Vector();
	protected Vector filenames = new Vector();
	protected int currentFileIndex;
	protected int currentSqlIndex;


	class ComponentConsistentListener implements ThreadListener {
		public void onThreadStartet() {
			buttonVersion.setEnabled(false);
			buttonSkip.setEnabled(false);
			buttonViewFile.setEnabled(false);
			buttonViewSql.setEnabled(false);
			tableVersions.setEnabled(false);
			setButtonBackEnabled(false);
		}
		public void onThreadFinished() {
			if (executionStatus == EXECUTION_FINISHED) {
				buttonVersion.setEnabled(true);
				buttonSkip.setEnabled(false);
				buttonViewFile.setEnabled(false);
				buttonViewSql.setEnabled(false);
				tableVersions.setEnabled(true);
				setButtonBackEnabled(true);
			}
			else if (executionStatus == EXECUTION_FAILED) {
				buttonVersion.setEnabled(false);
				buttonSkip.setEnabled(true);
				buttonViewFile.setEnabled(true);
				buttonViewSql.setEnabled(true);
				tableVersions.setEnabled(false);
				setButtonBackEnabled(false);
			}
		}
	}

	public UpdatePanel(ProcessableController controller, ApplicationFrame application) {
		super(controller, application, Messages.get("UpdatePanel.title")); //$NON-NLS-1$
		setupComponents();
		eventWriter.addListener(new EventStringListener() {
			public void onFlush() {
				textareaOutput.append(eventWriter.getBuffer().toString());
				textareaOutput.setCaretPosition(textareaOutput.getText().length());
				eventWriter.getBuffer().delete(0, eventWriter.getBuffer().length()-1);
			}
		});
		updateView();
	}

	public void updateView() {
		//
	}

	public void setupComponents() {
		FormLayout layout = new FormLayout("4dlu, right:max(72dlu;default), 2dlu, max(128dlu;default), 32dlu, right:default, 2dlu, default:grow, 4dlu", // columns //$NON-NLS-1$
		                                   "4dlu, default, 8dlu, default, 4dlu, fill:default:grow, 4dlu, default, 4dlu, default, 4dlu, default, 4dlu, default, 4dlu, default, 8dlu, default, 4dlu, default, 4dlu, fill:default:grow, 4dlu, default, 4dlu"); // rows //$NON-NLS-1$
		CellConstraints cc = new CellConstraints();
		PanelBuilder builder = new PanelBuilder(layout);
		builder.add(new JLabel(Messages.get("UpdatePanel.about")), cc.xyw(2, 2, 5)); //$NON-NLS-1$

		builder.addSeparator(Messages.get("UpdatePanel.versions.separator"), cc.xyw(2, 4, 7)); //$NON-NLS-1$
		builder.addLabel(Messages.get("UpdatePanel.versions.label"), cc.xy(2, 6)); //$NON-NLS-1$
		builder.add(new JScrollPane(tableVersions), cc.xyw(4, 6, 5));
		buttonVersion.setIcon(new ImageIcon(ImageFactory.getImage("noatun.png"))); //$NON-NLS-1$
		builder.add(createButtonPanel(new JButton[]{buttonVersion}), cc.xyw(4, 8, 5));

		builder.addSeparator(Messages.get("UpdatePanel.progress.separator"), cc.xyw(2, 10, 7)); //$NON-NLS-1$
		builder.addLabel(Messages.get("UpdatePanel.progress.version.label"), cc.xy(2, 12)); //$NON-NLS-1$
		initProgressBar(progressVersion);
		builder.add(progressVersion, cc.xy(4, 12));
		builder.addLabel(Messages.get("UpdatePanel.progress.version.current.label") + ":", cc.xy(6, 12)); //$NON-NLS-1$ //$NON-NLS-2$
		builder.add(labelCurrentVersion, cc.xy(8, 12));
		
		builder.addLabel(Messages.get("UpdatePanel.progress.file.label"), cc.xy(2, 14)); //$NON-NLS-1$
		initProgressBar(progressFile);
		builder.add(progressFile, cc.xy(4, 14));
		builder.addLabel(Messages.get("UpdatePanel.progress.file.current.label") + ":", cc.xy(6, 14)); //$NON-NLS-1$ //$NON-NLS-2$
		builder.add(labelCurrentFile, cc.xy(8, 14));

		builder.addLabel(Messages.get("UpdatePanel.progress.sql.label"), cc.xy(2, 16)); //$NON-NLS-1$
		initProgressBar(progressSql);
		builder.add(progressSql, cc.xy(4, 16));
		builder.addLabel(Messages.get("UpdatePanel.progress.sql.current.label") + ":", cc.xy(6, 16)); //$NON-NLS-1$ //$NON-NLS-2$
		builder.add(labelCurrentSql, cc.xy(8, 16));
		
		buttonSkip.setIcon(new ImageIcon(ImageFactory.getImage("redo.png"))); //$NON-NLS-1$
		buttonViewFile.setIcon(new ImageIcon(ImageFactory.getImage("file.gif"))); //$NON-NLS-1$
		buttonViewSql.setIcon(new ImageIcon(ImageFactory.getImage("file.gif"))); //$NON-NLS-1$
		builder.add(createButtonPanel(new JButton[]{buttonSkip, buttonViewFile, buttonViewSql}), cc.xyw(2, 18, 7));
		
		builder.addSeparator(Messages.get("UpdatePanel.output.separator"), cc.xyw(2, 20, 7)); //$NON-NLS-1$
		builder.addLabel(Messages.get("UpdatePanel.output.commandline.label"), cc.xy(2, 22)); //$NON-NLS-1$
		textareaOutput.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textareaOutput);
		scrollPane.setPreferredSize(new Dimension(10, 512));
		builder.add(scrollPane, cc.xyw(4, 22, 5));
		buttonOutputSave.setIcon(new ImageIcon(ImageFactory.getImage("filesave.png"))); //$NON-NLS-1$
		buttonOutputClean.setIcon(new ImageIcon(ImageFactory.getImage("edit.png"))); //$NON-NLS-1$
		builder.add(createButtonPanel(new JButton[]{buttonOutputClean, buttonOutputSave}), cc.xyw(4, 24, 5));
		
		textareaOutput.setFont(new Font("Courier", Font.PLAIN, 11)); //$NON-NLS-1$
		
		buttonVersion.setEnabled(false);
		
		final UpdateController updateController = (UpdateController)getController();
		VersionTableModel versionDataModel = new VersionTableModel(updateController.getConfiguration());
		tableVersions.setModel(versionDataModel);
		tableVersions.setDefaultRenderer(Image.class, new ImageTableCellRenderer());
		tableVersions.setDefaultRenderer(Object.class, new VersionCellRenderer());
		tableVersions.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tableVersions.getTableHeader().setReorderingAllowed(false);
		tableVersions.getTableHeader().setResizingAllowed(false);
		TableColumn columnFile = tableVersions.getColumnModel().getColumn(0);
		columnFile.setMaxWidth(64);
		columnFile.setMinWidth(64);
		TableColumn columnDatabase = tableVersions.getColumnModel().getColumn(1);
		columnDatabase.setMaxWidth(64);
		columnDatabase.setMinWidth(64);
		tableVersions.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				int[] selectedVersions = tableVersions.getSelectedRows();
				boolean validSelection = selectedVersions.length > 0;
				for (int i = 0; i < selectedVersions.length; i++) {
					VersionBean currentSelectedVersion = updateController.getConfiguration().getVersions().getVersion(selectedVersions[i]);
					validSelection &= currentSelectedVersion.isInFile();
				}
				buttonVersion.setEnabled(validSelection);
			}
		});
		
		buttonVersion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = tableVersions.getSelectedRows();
				Vector selectedVersions = new Vector();
				for (int i = 0; i < selectedRows.length; i++) {
					VersionBean selectedVersion = getConfiguration().getVersions().getVersion(selectedRows[i]);
					selectedVersions.add(selectedVersion);
				}
				if (confirmExecuteVersionsInDatabase(selectedVersions)) {
					executeSelectedVersions(selectedVersions);
				}
			}
		});
		buttonOutputClean.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				textareaOutput.setText(""); //$NON-NLS-1$
			}
		});
		buttonOutputSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showSaveFileDialog();
			}
		});
		buttonSkip.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				processVersions();
			}
		});
		final ApplicationFrame finalApplication = getApplicationFrame();
		buttonViewFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				finalApplication.setPanel(new SimpleTextPanel(new SimpleTextController(getController()), finalApplication, Messages.get("UpdatePanel.progress.file.current.label"), UpdatePanel.this, (String)files.get(currentFileIndex), true, false)); //$NON-NLS-1$
			}
		});
		buttonViewSql.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String sql = updateController.getParsedSql(currentSqlIndex);
				finalApplication.setPanel(new SimpleTextPanel(new SimpleTextController(getController()), finalApplication, Messages.get("UpdatePanel.progress.sql.current.label"), UpdatePanel.this, sql, true, false)); //$NON-NLS-1$
			}
		});

		add(builder.getPanel(), BorderLayout.CENTER);
	}
	
	protected void initProgressBar(JProgressBar progressBar) {
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setString(""); //$NON-NLS-1$
	}

	protected void updateDatabase(VersionBean version) {
		try {
			UpdateController updateController = (UpdateController)getController();
			updateController.updateDatabaseVersion(version);
		}
		catch (ExecutionException e) {
			showErrorDialog(Messages.get("UpdatePanel.database.execute"), e); //$NON-NLS-1$
		}
	}
	
	protected void showSaveFileDialog() {
		FileChooser fc = FileChooser.getSystemFileChooser();
		byte[] data = textareaOutput.getText().getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try {
			fc.showFileSaveDialog(getApplicationFrame().getFrame(), bais, System.getProperty("user.home"), "dub-output.txt");  //$NON-NLS-1$//$NON-NLS-2$
		}
		catch (Exception e) {
			showErrorDialog(Messages.get("UpdatePanel.output.save.error"), e); //$NON-NLS-1$
		}
	}

	private JPanel createButtonPanel(JButton[] buttons) {
		ButtonBarBuilder bbbuilder = new ButtonBarBuilder();
		bbbuilder.addGlue();
		bbbuilder.addGriddedButtons(buttons);
		return bbbuilder.getPanel();
	}

	/** Check ob Versionen bereits in der Datenbank sind, der Benutzer muss im zweifelsfall bestätigen */
	protected boolean confirmExecuteVersionsInDatabase(final Vector versionsToExecute) {
		final JFrame parent = getApplicationFrame().getFrame();
		for (int currentVersionToExecute = 0; currentVersionToExecute < versionsToExecute.size(); currentVersionToExecute++) {
			VersionBean version = (VersionBean)versionsToExecute.get(currentVersionToExecute);
			if (version.isInDatabase()) {
				int optionResult = JOptionPane.showConfirmDialog(parent, Messages.get("UpdatePanel.versions.execute.applied.message.1") + " " + version.getVersionName() + " " + Messages.get("UpdatePanel.versions.execute.applied.message.2"), Messages.get("UpdatePanel.versions.execute.applied.title"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				if (optionResult == JOptionPane.CANCEL_OPTION) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	protected void executeSelectedVersions(final Vector versionsToExecute) {
		if (versionsToExecute.size() <= 0) {
			showErrorDialog(Messages.get("UpdatePanel.execute.error.version.selection")); //$NON-NLS-1$
		}
		progressVersion.setMaximum(versionsToExecute.size());
		this.versions = versionsToExecute;
		currentVersion = 0;
		currentFileIndex = 0;
		currentSqlIndex = 0;
		processVersions();
	}

	protected void processVersions() {
		ObservableThread versionsThread = new ObservableThread() {
			public void runObserved() {
				try {
					while (currentVersion < versions.size()) {
						VersionBean currentVersionBean = (VersionBean)versions.get(currentVersion);
						updateVersionFeedback(currentVersion, versions.size(), currentVersionBean.getVersionName());

						// Files bereinigen
						if (currentFileIndex == 0) {
							files.clear();
							Iterator iterFiles = currentVersionBean.getFileValueIterator();
							while (iterFiles.hasNext()) {
								files.add(iterFiles.next());
							}
							filenames.clear();
							Iterator iterFilenames = currentVersionBean.getFileKeyIterator();
							while (iterFilenames.hasNext()) {
								filenames.add(iterFilenames.next());
							}
							progressFile.setMaximum(files.size());
						}
						processFiles();

						updateDatabase(currentVersionBean);
						currentFileIndex = 0;
						currentVersion++;
					}
					// Hier erst, wenn alle Versionen durch gearbeitet wurden
					updateVersionFeedback(currentVersion, versions.size(), labelCurrentVersion.getText());
					executionStatus = EXECUTION_FINISHED;
				}
				catch (SQLException sex) {
					executionStatus = EXECUTION_FAILED;
				}
			}
		};
		versionsThread.addThreadListener(new ComponentConsistentListener());
		versionsThread.start();
	}
	
	
	protected void processFiles() throws SQLException {
		while (currentFileIndex < files.size()) {
			String currentFile = (String)files.get(currentFileIndex);
			updateFileFeedback(currentFileIndex, files.size(), (String)filenames.get(currentFileIndex));
			
			if (currentSqlIndex == 0) {
				UpdateController updateController = (UpdateController)getController();
				updateController.initializeSqlParser(currentFile, eventWriter);
				progressSql.setMaximum(updateController.getParsedSqlCount());
			}
			processSqls();
			
			currentSqlIndex = 0;
			currentFileIndex++;
		}
		updateFileFeedback(currentFileIndex, files.size(), labelCurrentFile.getText());
	}
	
	protected void processSqls() throws SQLException {
		UpdateController updateController = (UpdateController)getController();
		if (executionStatus == EXECUTION_FAILED) {
			currentSqlIndex++;
			executionStatus = EXECUTION_CONTINUED;
		}
		while (currentSqlIndex < updateController.getParsedSqlCount()) {
			updateSqlFeedback(currentSqlIndex, updateController.getParsedSqlCount(), updateController.getParsedSql(currentSqlIndex));
			updateController.executeParsedSql(currentSqlIndex);
			currentSqlIndex++;
		}
		updateSqlFeedback(currentSqlIndex, updateController.getParsedSqlCount(), labelCurrentSql.getText());
	}

	protected void updateVersionFeedback(int current, int count, String text) {
		progressVersion.setValue(current);
		progressVersion.setString(current + " / " + count); //$NON-NLS-1$
		labelCurrentVersion.setText(text);
	}

	protected void updateFileFeedback(int current, int count, String text) {
		progressFile.setValue(current);
		progressFile.setString(current + " / " + count); //$NON-NLS-1$
		labelCurrentFile.setText(text);
	}

	protected void updateSqlFeedback(int current, int count, String text) {
		progressSql.setValue(current);
		progressSql.setString(current + " / " + count); //$NON-NLS-1$
		labelCurrentSql.setText(text);
	}

}
