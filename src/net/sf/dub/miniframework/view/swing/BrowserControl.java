/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 16.08.2005 - 11:48:10
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: BrowserControl.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 * Revision 1.1  2005/08/16 15:19:02  dgm
 * kleinere änderungen
 *
 */
package net.sf.dub.miniframework.view.swing;

import java.io.IOException;

/**
 * A simple, static class to display a URL in the system browser.
 *
 * Under Unix, the system browser is hard-coded to be 'netscape'.
 * Netscape must be in your PATH for this to work.  This has been
 * tested with the following platforms: AIX, HP-UX and Solaris.
 *
 * Under Windows, this will bring up the default browser under windows,
 * usually either Netscape or Microsoft IE.  The default browser is
 * determined by the OS.  This has been tested under Windows 95/98/NT.
 *
 * Examples:
 * BrowserControl.displayURL("http://www.javaworld.com")
 *
 * BrowserControl.displayURL("file://c:\\docs\\index.html")
 *
 * BrowserContorl.displayURL("file:///user/joe/index.html");
 * 
 * Note - you must include the url type -- either "http://" or
 * "file://".
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
*/
public class BrowserControl {

	// Used to identify the windows platform.
	private static final String WIN_ID = "Windows"; //$NON-NLS-1$

	// The default system browser under windows.
	private static final String WIN_PATH = "rundll32"; //$NON-NLS-1$

	// The flag to display a url.
	private static final String WIN_FLAG = "url.dll,FileProtocolHandler"; //$NON-NLS-1$

	// The default browser under unix.
	private static final String UNIX_PATH = "netscape"; //$NON-NLS-1$

	// The flag to display a url.
	private static final String UNIX_FLAG = "-remote openURL"; //$NON-NLS-1$

	/**
	 * Display a file in the system browser.  If you want to display a
	 * file, you must include the absolute path name.
	 *
	 * @param url the file's url (the url must start with either "http://" or "file://").
	 */
	public void displayURL(String url) {
		boolean windows = isWindowsPlatform();
		String cmd = null;
		try {
			if (windows) {
				// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
				cmd = WIN_PATH + " " + WIN_FLAG + " " + url; //$NON-NLS-1$ //$NON-NLS-2$
				Runtime.getRuntime().exec(cmd);
			}
			else {
				// Under Unix, Netscape has to be running for the "-remote"
				// command to work.  So, we try sending the command and
				// check for an exit value.  If the exit command is 0,
				// it worked, otherwise we need to start the browser.
				// cmd = 'netscape -remote openURL(http://www.javaworld.com)'
				cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Process p = Runtime.getRuntime().exec(cmd);
				try {
					// wait for exit code -- if it's 0, command worked,
					// otherwise we need to start the browser up.
					int exitCode = p.waitFor();
					if (exitCode != 0) {
						// Command failed, start up the browser
						// cmd = 'netscape http://www.javaworld.com'
						cmd = UNIX_PATH + " " + url; //$NON-NLS-1$
						p = Runtime.getRuntime().exec(cmd);
					}
				}
				catch (InterruptedException x) {
					System.err.println("Error bringing up browser" + ": '" + cmd + "'");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				}
			}
		}
		catch (IOException x) {
			// couldn't exec browser
			System.err.println("Could not invoke browser" + ": '" + cmd + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	/**
	 * Try to determine whether this application is running under Windows
	 * or some other platform by examing the "os.name" property.
	 *
	 * @return true if this application is running under a Windows OS
	 */
	public boolean isWindowsPlatform() {
		String os = System.getProperty("os.name"); //$NON-NLS-1$
		if (os != null && os.startsWith(WIN_ID)) {
			return true;
		}
		return false;
	}

}
