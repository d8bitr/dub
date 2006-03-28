/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 10:36:42
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DesktopFileChooser.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 *
 */
package net.sf.dub.miniframework.view.swing.dialogs;

import java.awt.Component;
import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;



/**
 * [Summary].
 * <p>
 * [Description].
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class DesktopFileChooser extends FileChooser {

	private int mode;
	private JFileChooser fc;
	private String file = null;
	//private javax.swing.filechooser.FileFilter fileFilter;


	public static final int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;
		
	/**
	 * [Summary].
	 * <p>
	 * [Description].
	 * 
	 * 
	 */
	public DesktopFileChooser() {
		super();
		mode = JFileChooser.FILES_ONLY;		
	}

	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#showFileOpenDialog(int)
	 */
	public void setFileChooserType(int mode) {
		this.mode = mode;
	}

	public boolean showFileOpenDialog(Component parent){
		return showFileOpenDialog(parent,null,null);
	}
	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#showFileOpenDialog(int)
	 */
	public boolean showFileOpenDialog(Component parent,Iterator filters,String label) {
		try{
			fc = new JFileChooser();

			if((filters!=null)&&(label!=null)){
				fc.setAcceptAllFileFilterUsed(false);
				/*
				for (Iterator it=filters; it.hasNext(); ) {
			  		ModelBase current=(ModelBase)it.next();
			  		fc.addChoosableFileFilter(getFileFilter((String)current.getValue(label)));
				}
				*/
			} 
			fc.setFileSelectionMode(mode);
			int returnVal = fc.showOpenDialog(parent);
			if(returnVal == JFileChooser.APPROVE_OPTION){
			file = fc.getSelectedFile().toString();
		}
		else
			file = null;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return (file!=null);
	}


	public boolean showFileOpenDialog(Component parent,Vector filters) {
			try{
			fc = new JFileChooser();
			if((filters!=null)&&(filters.size()>0)){
				fc.setAcceptAllFileFilterUsed(false);
				for(int i=0;i<filters.size();i++){
				  fc.addChoosableFileFilter(getFileFilter(filters.elementAt(i).toString()));
				}
			} 
			fc.setFileSelectionMode(mode);
			int returnVal = fc.showOpenDialog(parent);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				 file = fc.getSelectedFile().toString();
			}
			else
				file = null;
			}catch(Exception e){
				e.printStackTrace();
			}
			return (file!=null);
		}

	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#showFileSaveDialog()
	 */
	public boolean showFileSaveDialog(Component parent, ByteArrayInputStream is, String path, String filename) throws Exception{
		try{
			fc = new JFileChooser();
			int i = filename.lastIndexOf('.');
			if(i>0 && i<filename.length()-1) {
				String ext = filename.substring(i+1).toLowerCase();
				fc.setFileFilter(getFileFilter(ext));
				// bug in apple jvm -> setAcceptAllFileFilterUsed verursacht ArrayIndexOutOfBoundsException
				fc.setAcceptAllFileFilterUsed(System.getProperty("os.name").equals("Mac OS X")); //$NON-NLS-1$ //$NON-NLS-2$
			}				
			if(path!=null){
				fc.setCurrentDirectory(new File(path));
				fc.setSelectedFile(new File(path + filename));
			}else{
				fc.setSelectedFile(new File(filename));
			}
			int returnVal = fc.showSaveDialog(parent);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File selectedFile = fc.getSelectedFile();
				FileOutputStream fos = new FileOutputStream(selectedFile);
				byte[] b = new byte[is.available()];
				is.read(b);
				fos.write(b);
				fos.close();
			}else
				return false;
			return true;
		}catch(Exception e){
			throw new Exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#getSelectedFile()
	 */
	public String getSelectedFileName() {
		if(file == null)
			return null;
		return file + (mode==DIRECTORIES_ONLY?File.separator:""); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see de.etvice.util.FileChooser#getSelectedFile()
	 */
	public InputStream getSelectedFile() {
		try{
		if(file == null)
			return null;
		if(mode == DIRECTORIES_ONLY)
			return null;
		return new FileInputStream(file);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return null; 
		}	
	}
	
	public String getSelectedDir(){
		if((mode == DIRECTORIES_ONLY) && (file != null)) {
			return file + File.separator;
		}
		return null;
	}

	//gibt einen Datei-Filter zurück, der Dateien mit der übergebenen Endung herausfiltert
	protected javax.swing.filechooser.FileFilter getFileFilter(String extension){
		final String ext = extension;
		javax.swing.filechooser.FileFilter filter;
		filter = new javax.swing.filechooser.FileFilter() {
		  public boolean accept(File f) {
			return (f.getName().toLowerCase().endsWith("." + ext))||f.isDirectory(); //$NON-NLS-1$
		  }
		  public String getDescription() {
			return ("*." + ext); //$NON-NLS-1$
		  }
		};
		return filter;
	}
}
