/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 09:17:53
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: Dub.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.21  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.20  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.19  2005/09/12 12:45:36  dgm
 * start panel und about verbessert
 *
 * Revision 1.18  2005/09/09 15:19:39  dgm
 * todos angepasst
 *
 * Revision 1.17  2005/09/08 14:24:52  dgm
 * übersetzungen gemacht
 *
 * Revision 1.16  2005/09/08 12:43:39  dgm
 * about animation
 *
 * Revision 1.15  2005/09/07 12:40:07  dgm
 * Lizenzen eingebunden
 *
 * Revision 1.14  2005/09/02 14:07:20  dgm
 * todos ergänzt
 *
 * Revision 1.13  2005/09/02 09:12:11  dgm
 * deployment verbessern
 *
 * Revision 1.12  2005/08/29 14:38:19  dgm
 * todos upgedated
 *
 * Revision 1.11  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.10  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.9  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 * Revision 1.8  2005/08/16 15:19:02  dgm
 * kleinere änderungen
 *
 * Revision 1.7  2005/08/10 09:55:20  dgm
 * zukünftige ideen als todos in main class
 *
 * Revision 1.6  2005/08/04 15:34:29  dgm
 * Configuration laden
 *
 * Revision 1.5  2005/08/04 11:59:01  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.4  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
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

/**
 * Main Class für dub
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class Dub {
	
	//TODO history in about
	//TODO Dokumentation
	//TODO Deployment Webstart
	//TODO Cli
	
	private Dub() {
	}

	/** Haupteinsprungspunkt für Dub */
	public static void main(String[] args) {
		//java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		new DubLauncher().initialise();
	}

}
