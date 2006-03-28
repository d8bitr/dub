/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 09:59:02
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DubApplicationFrame.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.7  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.6  2005/09/20 14:51:21  dgm
 * history, simpletextpanel, release 1.0.0
 *
 * Revision 1.5  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.4  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.3  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.2  2005/08/10 09:54:59  dgm
 * terminate umgelegt
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.4  2005/08/04 07:45:11  dgm
 * refactoring, about panel "back", fix der größe vom textarea
 *
 * Revision 1.3  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 * Revision 1.2  2005/08/01 21:29:36  dgm
 * gui
 *
 * Revision 1.1  2005/07/14 14:42:52  dgm
 * initialer import für dub
 *
 */
package net.sf.dub.application.view.swing;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import net.sf.dub.application.controller.AboutController;
import net.sf.dub.application.controller.SimpleTextController;
import net.sf.dub.application.controller.StartController;
import net.sf.dub.application.resources.DubResourcesAnchor;
import net.sf.dub.miniframework.controller.Controller;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.AbstractApplicationFrame;
import net.sf.dub.miniframework.view.swing.BrowserControl;
import net.sf.dub.miniframework.view.swing.ImageFactory;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class DubApplicationFrame extends AbstractApplicationFrame {

	private JMenuBar menubarApplication = new JMenuBar();
	private JMenu menuDub = new JMenu(Messages.get("DubApplicationFrame.menu.dub")); //$NON-NLS-1$
	private JMenu menuHelp = new JMenu(Messages.get("DubApplicationFrame.menu.help")); //$NON-NLS-1$
	private JMenuItem menuAbout = new JMenuItem(Messages.get("DubApplicationFrame.menu.help.about")); //$NON-NLS-1$
	private JMenuItem menuExit = new JMenuItem(Messages.get("DubApplicationFrame.menu.dub.exit")); //$NON-NLS-1$
	private JMenuItem menuGoWebsite = new JMenuItem(Messages.get("DubApplicationFrame.menu.help.gowebsite")); //$NON-NLS-1$
	private JMenuItem menuHistory= new JMenuItem(Messages.get("DubApplicationFrame.menu.help.history")); //$NON-NLS-1$

	public DubApplicationFrame(Controller controller) {
		super(controller, Messages.get("DubApplicationFrame.title")); //$NON-NLS-1$
		setIconImage(ImageFactory.getImage("dub_icon.gif")); //$NON-NLS-1$
		setFrameSizeCentered(800, 600);

		setJMenuBar(menubarApplication);
		//menubarApplication.putClientProperty(PlasticLookAndFeel.BORDER_STYLE_KEY, BorderStyle.EMPTY);
		menubarApplication.putClientProperty(PlasticLookAndFeel.IS_3D_KEY, Boolean.TRUE);
		menubarApplication.add(menuDub);
		menuDub.setMnemonic('T');
		menuHelp.setMnemonic('H');
		menuDub.add(menuExit);
		menuExit.setIcon(ImageFactory.getIcon("exit.png")); //$NON-NLS-1$
		menuExit.setMnemonic('B');
		menuExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateApplication();
			}
		});
		menuGoWebsite.setIcon(ImageFactory.getIcon("gohome.png")); //$NON-NLS-1$
		menuGoWebsite.setMnemonic('W');
		menuGoWebsite.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visitWebsite();
			}
		});
		menuHelp.add(menuGoWebsite);
		menuHistory.setIcon(ImageFactory.getIcon("history.png")); //$NON-NLS-1$
		menuHistory.setMnemonic('H');
		menuHistory.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHistory();
			}
		});
		menuHelp.add(menuHistory);
		menuHelp.addSeparator();
		menuAbout.setIcon(ImageFactory.getIcon("messagebox_info.png")); //$NON-NLS-1$
		menuAbout.setMnemonic('Ü');
		menuAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}
		});
		menuHelp.add(menuAbout);
		menubarApplication.add(menuHelp);

		StartController startController = new StartController(getApplicationController()); 
		setPanel(new StartPanel(startController, this));
	}

	public void showAbout() {
		if (!getCurrentPanel().getClass().isAssignableFrom(AboutPanel.class)) {
			AboutController aboutController = new AboutController(getApplicationController());
			setPanel(new AboutPanel(aboutController, this, getCurrentPanel()));
		}
	}
	
	public void showHistory() {
		if (!getCurrentPanel().getClass().isAssignableFrom(SimpleTextPanel.class)) {
			SimpleTextController textController = new SimpleTextController(getApplicationController());
			
			StringBuffer buffer = new StringBuffer();
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(DubResourcesAnchor.class.getResourceAsStream("history/history.txt"))); //$NON-NLS-1$
				String lastLine = br.readLine();
				while ((lastLine != null)) {
					buffer.append(lastLine + System.getProperty("line.separator")); //$NON-NLS-1$
					lastLine = br.readLine();
				}
				br.close();
			}
			catch (IOException ioe) {
				buffer.setLength(0);
				buffer.append(Messages.get("History.error.load")); //$NON-NLS-1$
			}
			setPanel(new SimpleTextPanel(textController, this, Messages.get("History.title"), getCurrentPanel(), buffer.toString(), false, false)); //$NON-NLS-1$
		}
	}
	
	public void visitWebsite() {
		new BrowserControl().displayURL("http://sf.net/projects/dub"); //$NON-NLS-1$
	}
	
}
