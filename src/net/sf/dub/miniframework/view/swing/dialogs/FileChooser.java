/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 10:36:42
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: FileChooser.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 *
 */
package net.sf.dub.miniframework.view.swing.dialogs;

import java.awt.Component;

import java.io.*;
import java.util.*;



/**
 * Interface fuer die FileOpen und FileSave Dialoge.
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class FileChooser {

	public abstract void setFileChooserType(int mode);

	public abstract boolean showFileOpenDialog(Component parent);
	
	public abstract boolean showFileOpenDialog(Component parent,Iterator filters,String Label);

	public abstract boolean showFileOpenDialog(Component parent,Vector filters);
	
	public abstract boolean showFileSaveDialog(
		Component parent,
		ByteArrayInputStream is,
		String Path,
		String Filename) throws Exception;

	public abstract String getSelectedFileName();
	
	public abstract InputStream getSelectedFile();
	
	public abstract String getSelectedDir();
	
	public static FileChooser getSystemFileChooser(){
		/*
		System.out.println("javawebstart.version = " + System.getProperty("javawebstart.version"));
		System.out.println("jnlp.webstart = " + System.getProperty("jnlp.webstart", "false"));
		System.out.println("eval = " + new Boolean(System.getProperty("jnlp.webstart", "false")).booleanValue());
		*/
		if (System.getProperty("javawebstart.version") != null) { //$NON-NLS-1$
			return new JNLPFileChooser();
		}
		return new DesktopFileChooser();
	}
}
