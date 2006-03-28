/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 17.08.2005 - 14:50:27
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:35:06 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: DubTask.java,v $
 * Revision 1.1  2006/03/28 15:35:06  danielgalan
 * initial import
 *
 * Revision 1.6  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.5  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.4  2005/08/24 13:06:57  dgm
 * debug
 *
 * Revision 1.3  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 * Revision 1.2  2005/08/22 14:02:46  dgm
 * anttask und build.xml
 *
 * Revision 1.1  2005/08/17 14:51:47  dgm
 * ant task
 *
 */
package net.sf.dub.application.anttask;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * Ant-Task for dub
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class DubTask extends Task {
	
	private File sqlVersionDir;
	private File configurationFile;
	private File outputFile;
	
	private class SqlVersionDirFilter implements FileFilter {
		public boolean accept(File pathname) {
			return pathname.isDirectory() && pathname.getName().matches("^[0-9]+[\\.][0-9]+[\\.][0-9]+$");  //$NON-NLS-1$
		}
	}

	private class SqlFileFilter implements FileFilter {
		public boolean accept(File pathname) {
			return pathname.isFile() && pathname.getName().endsWith(".sql");  //$NON-NLS-1$
		}
	}

	public DubTask() {
		super();
	}
	
	public void setSqlVersionDir(String sqlVersionDir) {
		this.sqlVersionDir = new File(sqlVersionDir);
		if (!this.sqlVersionDir.isDirectory()) {
			throw new BuildException("No valid SQL Version directory specified!"); //$NON-NLS-1$
		}
	}
	
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = new File(configurationFile);
		if (!this.configurationFile.isFile()) {
			throw new BuildException("No valid configuration file specified!"); //$NON-NLS-1$
		}
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = new File(outputFile);
		if (this.outputFile.isDirectory()) {
			throw new BuildException("No valid Outputfile specified!"); //$NON-NLS-1$
		}
	}
	
	public void execute() throws BuildException {
		if (sqlVersionDir == null) {
			throw new BuildException("No valid SQL Version directory specified!"); //$NON-NLS-1$
		}
		if (configurationFile == null) {
			throw new BuildException("No valid configuration file specified!"); //$NON-NLS-1$
		}
		if (outputFile == null) {
			throw new BuildException("No valid Outputfile specified!"); //$NON-NLS-1$
		}

		JarOutputStream jos = null;
		try {
			jos = new JarOutputStream(new FileOutputStream(outputFile));
			addJarEntry(jos, configurationFile.getName(), new FileInputStream(configurationFile));
			
			File[] dirs = sqlVersionDir.listFiles(new SqlVersionDirFilter());
			for (int i = 0; i < dirs.length; i++) {
				File[] files = dirs[i].listFiles(new SqlFileFilter());
				for (int j = 0; j < files.length; j++) {
					addJarEntry(jos, dirs[i].getName() + "/" + files[j].getName(), new FileInputStream(files[j])); //$NON-NLS-1$
				}
			}

			jos.close();
		}
		catch (FileNotFoundException fnfe) {
			throw new BuildException("File not found", fnfe); //$NON-NLS-1$
		}
		catch (IOException ioe) {
			throw new BuildException(ioe);
		}
		finally {
			if (jos != null) {
				try {
					jos.close();
				}
				catch (IOException e) {
				}
			}
		}
	}


	/** Add a jar entry to the deployment archive */
	public void addJarEntry(JarOutputStream outputStream, String entryName, InputStream inputStream) throws IOException {
		outputStream.putNextEntry(new JarEntry(entryName));
		copyStream(outputStream, inputStream);
	}


	/** Copies the input stream to the output stream */
	private void copyStream(OutputStream outputStream, InputStream inputStream) throws IOException {
		byte[] bytes = new byte[4096];
		int read = inputStream.read(bytes, 0, 4096);
		while(read > 0) {
			outputStream.write(bytes, 0, read);
			read = inputStream.read(bytes, 0, 4096);
		}
	}

}
