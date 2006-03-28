/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 02.08.2005 - 12:41:41
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: StartPanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.12  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.11  2005/09/20 14:51:21  dgm
 * history, simpletextpanel, release 1.0.0
 *
 * Revision 1.10  2005/09/12 12:45:36  dgm
 * start panel und about verbessert
 *
 * Revision 1.9  2005/09/09 15:19:26  dgm
 * image und text (vorläufig)
 *
 * Revision 1.8  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.7  2005/08/16 15:19:02  dgm
 * kleinere änderungen
 *
 * Revision 1.6  2005/08/15 14:47:16  dgm
 * rt logo auf start seite
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
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;
import net.sf.dub.miniframework.view.swing.BrowserControl;
import net.sf.dub.miniframework.view.swing.ImageFactory;
import net.sf.dub.miniframework.view.swing.ImagePanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class StartPanel extends DubProcessablePanel {
	
	protected Image dubLogo;
	protected JPanel dubPanel;

	public StartPanel(ProcessableController controller, ApplicationFrame application) {
		super(controller, application, Messages.get("StartPanel.title")); //$NON-NLS-1$

		
		dubLogo = ImageFactory.getImage("logo.jpg"); //$NON-NLS-1$
		dubPanel = new ImagePanel(dubLogo, true);
		dubPanel.addMouseListener(new MouseAdapter() {
			Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
			Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			public void mouseEntered(MouseEvent e) {
				dubPanel.setCursor(handCursor);
			}

			public void mouseExited(MouseEvent e) {
				dubPanel.setCursor(defaultCursor);
			}

			public void mouseClicked(MouseEvent e) {
				new BrowserControl().displayURL("http://sf.net/projects/dub"); //$NON-NLS-1$
			}
		});
		setupComponents();
		updateView();
	}

	public void setupComponents() {
		// 52px wird ignoriert, wieso?
		FormLayout layout = new FormLayout("4dlu, default:grow, 4dlu, 256px, 4dlu", // columns //$NON-NLS-1$
		                                   "4dlu, fill:default:grow, 4dlu, top:300px:grow, 4dlu, 52px, 4dlu"); // rows //$NON-NLS-1$
		JPanel contentPanel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		
		JTextArea textWelcome = new JTextArea(Messages.get("StartPanel.welcome") + System.getProperty("line.separator") + Messages.get("StartPanel.picture") + ":");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		textWelcome.setEditable(false);
		textWelcome.setLineWrap(true);
		textWelcome.setOpaque(false);
		textWelcome.setWrapStyleWord(true);
		contentPanel.add(textWelcome, cc.xyw(2, 2, 3));

		JPanel explainPanel = new JPanel();
		explainPanel.add(new ImagePanel(ImageFactory.getImage("ProcessFlowSmall.png"), false)); //$NON-NLS-1$
		contentPanel.add(explainPanel, cc.xyw(2, 4, 3));

		contentPanel.add(dubPanel, cc.xy(4, 6));

		add(contentPanel, BorderLayout.CENTER);
	}

	public void updateView() {
		// Nada
	}
	
}

