/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 03.08.2005 - 11:36:25
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ProcessablePanel.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.8  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.7  2005/09/19 15:02:57  dgm
 * viel schöner so alles
 *
 * Revision 1.6  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 * Revision 1.5  2005/08/15 14:46:17  dgm
 * bugfix process buttons
 *
 * Revision 1.4  2005/08/08 14:56:58  dgm
 * Versiontabelle laden, erstellen, migrieren
 *
 * Revision 1.3  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 * Revision 1.2  2005/08/05 11:18:42  dgm
 * Datenbankzugriff, tabellen auslesen, ..
 *
 * Revision 1.1  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.2  2005/08/04 07:45:10  dgm
 * refactoring, about panel "back", fix der größe vom textarea
 *
 * Revision 1.1  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.controller.ProcessException;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.util.Messages;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class ProcessablePanel extends AbstractPanel {

	private JButton buttonBack = new JButton(Messages.get("ProcessablePanel.back")); //$NON-NLS-1$
	private JButton buttonNext = new JButton(Messages.get("ProcessablePanel.next")); //$NON-NLS-1$
	private Package controllerPackage;
	private Package panelPackage;

	public ProcessablePanel(ProcessableController controller, ApplicationFrame application, String headerText) {
		super(controller, application, headerText);
		add(createButtonPanel(), BorderLayout.SOUTH);
		buttonBack.setIcon(ImageFactory.getIcon("back.png", true)); //$NON-NLS-1$
		buttonBack.setEnabled(getProcessableController().getProcessStepBack() != null);
		buttonNext.setIcon(ImageFactory.getIcon("forward.png", true)); //$NON-NLS-1$
		buttonNext.setEnabled(getProcessableController().getProcessStepNext() != null);
		// TODO Problem ist das die Komponenten noch nicht initialisiert wurden vom erst später aufgerufenen Konstruktor der Child Klasse
		//updateView(); 
	}

	public ProcessableController getProcessableController() {
		return (ProcessableController)super.getController();
	}
	
	private JPanel createButtonPanel() {
		buttonBack.setMnemonic('Z');
		//TODO buttonBack.setIcon(new ImageIcon(ImageFactory.getImage()));
		buttonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonBack();
			}
		});
		buttonNext.setMnemonic('W');
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonNext();
			}
		});
		ButtonBarBuilder builder = new ButtonBarBuilder();
		builder.addGlue();
		builder.addGriddedButtons(new JButton[] {buttonBack, buttonNext});

		
		FormLayout buttonLayout = new FormLayout("4dlu, default:grow, 4dlu", // columns //$NON-NLS-1$
                                                 "6dlu, default, 6dlu, fill:default:grow, 6dlu"); // rows //$NON-NLS-1$
		CellConstraints cc = new CellConstraints();
		JPanel buttonPanel = new JPanel(buttonLayout);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		buttonPanel.add(separator, cc.xy(2, 2));
		buttonPanel.add(builder.getPanel(), cc.xy(2, 4));
		return buttonPanel;
	}
		
	public void setButtonBackEnabled(boolean enable) {
		buttonBack.setEnabled((getProcessableController().getProcessStepBack() != null) && enable);
	}
	
	public void setButtonNextEnabled(boolean enable) {
		buttonNext.setEnabled((getProcessableController().getProcessStepNext() != null) && enable);
	}
	
	protected void onButtonNext() {
		try {
			onNext();
			getProcessableController().onNext();
		}
		catch (ProcessException pe) {
			if (!pe.isConsumend() || (pe.getMessage() != null)) {
				JOptionPane.showMessageDialog(getApplicationFrame().getFrame(), pe.getMessage(), Messages.get("ProcessablePanel.error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
			return;
		}
		try {
			String stepNext = getProcessableController().getProcessStepNext();
			if (stepNext != null) {
				getApplicationFrame().setPanel(createPanel(stepNext));
			}
		}
		catch (Exception ex) {
			showErrorDialog(Messages.get("ProcessablePanel.error.head"), ex); //$NON-NLS-1$
		}
	}

	protected void onButtonBack() {
		try {
			onBack();
			getProcessableController().onBack();
		}
		catch (ProcessException pe) {
			if (!pe.isConsumend()) {
				JOptionPane.showMessageDialog(getApplicationFrame().getFrame(), pe.getMessage(), Messages.get("ProcessablePanel.error.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			}
			return;
		}
		try {
			String stepBack = getProcessableController().getProcessStepBack();
			if (stepBack != null) {
				getApplicationFrame().setPanel(createPanel(stepBack));
			}
		}
		catch (Exception ex) {
			showErrorDialog(Messages.get("ProcessablePanel.error.head"), ex); //$NON-NLS-1$
		}
	}

	public void setControllerBase(Package controllerPackage) {
		this.controllerPackage = controllerPackage;
	}
	
	public void setPanelBase(Package panelPackage) {
		this.panelPackage = panelPackage;
	}
	
	protected JPanel createPanel(String step) throws Exception {
		if ((controllerPackage == null) || (panelPackage == null)) {
			throw new Exception(Messages.get("ProcessablePanel.error.packages")); //$NON-NLS-1$
		}
		String controllerClassName = controllerPackage.getName() + "." + step + "Controller"; //$NON-NLS-1$ //$NON-NLS-2$
		Class controllerClass = Class.forName(controllerClassName);
		Constructor controllerConstructor = controllerClass.getConstructor(new Class[] {Controller.class});
		ProcessableController newController = (ProcessableController)controllerConstructor.newInstance(new Object[] {getController()});
		
		String panelClassName = panelPackage.getName() + "." + step + "Panel"; //$NON-NLS-1$ //$NON-NLS-2$
		Class panelClass = Class.forName(panelClassName);
		Constructor panelConstructor = panelClass.getConstructor(new Class[] {ProcessableController.class, ApplicationFrame.class});
		JPanel newPanel = (JPanel)panelConstructor.newInstance(new Object[] {newController, getApplicationFrame()});
		return newPanel;
	}
	
	public abstract void onNext() throws ProcessException;

	public abstract void onBack() throws ProcessException;
	
	public abstract void updateView();

}

