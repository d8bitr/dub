/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 10:36:42
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: JNLPFileChooser.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 *
 */
package net.sf.dub.miniframework.view.swing.dialogs;

import java.awt.Component;
import java.io.*;
import java.util.*;
import javax.jnlp.*;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;



/**
 * [Summary].
 * <p>
 * [Description].
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class JNLPFileChooser extends FileChooser {

	private FileContents fc;
	private int mode;
	/**
	 * [Summary].
	 * <p>
	 * [Description].
	 * 
	 * 
	 */
	public JNLPFileChooser() {
		super();
		fc = null;
	}
	
	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#setFileChooserType(int)
	 */
	public void setFileChooserType(int mode) {
		this.mode = mode;
	}

	public boolean showFileOpenDialog(Component parent,Iterator filters,String Label){
		return showFileOpenDialog(parent);
	}
	
	public boolean showFileOpenDialog(Component parent,Vector filters) {
		return showFileOpenDialog(parent);
	}
	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#showFileOpenDialog(java.awt.Component)
	 */
	public boolean showFileOpenDialog(Component parent) {
		FileOpenService fos;
		try { 
			 fos = (FileOpenService)ServiceManager.lookup("javax.jnlp.FileOpenService");  //$NON-NLS-1$
		   } catch (Exception e) { 
			   e.printStackTrace();
			   fos = null;
		   } 
		try{
			if(fos != null){
				LookAndFeel oldLf = UIManager.getLookAndFeel();
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				fc = fos.openFileDialog(null, null);
				UIManager.setLookAndFeel(oldLf);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return (fc!=null);		
	}

	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#showFileSaveDialog(java.awt.Component)
	 */
	public boolean showFileSaveDialog(Component parent,ByteArrayInputStream is, String path, String filename) throws Exception {
		FileSaveService fss;
		try { 
			   fss = (FileSaveService)ServiceManager.lookup("javax.jnlp.FileSaveService");  //$NON-NLS-1$
		   } catch (Exception e) { 
			   e.printStackTrace();  
			   fss = null; 
		   } 
		try{
			if(fss != null){
				LookAndFeel oldLf = UIManager.getLookAndFeel();
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				fc = fss.saveFileDialog(path, null, is, filename);
				UIManager.setLookAndFeel(oldLf);
			}
			if(fc==null)
				return false;
			return true;	
		}catch(Exception ex){
			throw new Exception(ex);
		}
	}

	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#getSelectedFile()
	 */
	public String getSelectedFileName() {
		try{
		return fc==null?null:fc.getName();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#getSelectedFile()
	 */
	public InputStream getSelectedFile() {
		try{
		return fc==null?null:fc.getInputStream();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String getSelectedDir(){return null;}
	
	protected int getMode() {
		return this.mode;
	}
	
}
