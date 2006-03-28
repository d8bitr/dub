/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 10:24:33
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ConfigurationLoader.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.11  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.10  2005/09/19 15:08:54  dgm
 * Reichlich Fixes, und Veränderungen
 *
 * Revision 1.9  2005/09/09 15:18:17  dgm
 * update zeit und benutzer anzeigen
 *
 * Revision 1.8  2005/08/25 08:15:53  dgm
 * i18n fortgeführt
 *
 * Revision 1.7  2005/08/09 15:46:14  dgm
 * bugfix beim laden des configurationsfiles, anzeige update erweitert
 *
 * Revision 1.6  2005/08/05 14:59:15  dgm
 * Gui elemente, Laden verändert
 *
 * Revision 1.5  2005/08/05 11:32:52  dgm
 * direktzugriff auf properties unterbunden
 *
 * Revision 1.4  2005/08/04 21:44:03  dgm
 * wieder ein schwung änderungen
 *
 * Revision 1.3  2005/08/04 15:34:29  dgm
 * Configuration laden
 *
 * Revision 1.2  2005/08/04 12:27:43  dgm
 * configurationsverhalten geändert, process refactored
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 */
package net.sf.dub.application.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.sf.dub.miniframework.util.Messages;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ConfigurationLoader {

	public void loadConfiguration(String filename, Configuration configuration) throws FileNotFoundException, InvalidConfigurationFile, IOException {
		if ((filename == null) || filename.equals("")) { //$NON-NLS-1$
			throw new FileNotFoundException(Messages.get("ConfigurationLoader.error.file.invalid")); //$NON-NLS-1$
		}
		loadConfiguration(new File(filename), configuration);
	}
	
	public void loadConfiguration(File file, Configuration configuration) throws FileNotFoundException, InvalidConfigurationFile, IOException {
 		if ((file == null) || !file.exists()) {
			throw new FileNotFoundException(Messages.get("ConfigurationLoader.error.file.notfound")); //$NON-NLS-1$
		}
 		
 		// clean Configuraion
 		configuration.clearVersionsInFile();
 		
		JarFile jar;
		try {
			jar = new JarFile(file);
			Enumeration enumEntries = jar.entries();
			while(enumEntries.hasMoreElements()) {
				JarEntry entry = (JarEntry)enumEntries.nextElement();
				if (!entry.isDirectory() && entry.getName().equalsIgnoreCase("dub.properties")) { //$NON-NLS-1$
					configuration.clearProperties();
					configuration.loadProperties(jar.getInputStream(entry));
					configuration.setProperty("application.sourcefile", file.getAbsolutePath()); //$NON-NLS-1$
				}
				else if (!entry.isDirectory() && entry.getName().matches("^[0-9]+[\\.][0-9]+[\\.][0-9]+/.+[\\.]sql$")) {  // Zahl.Zahl.Zahl/*.sql (Zahl kann beliebig lang sein) //$NON-NLS-1$
					String entryName = entry.getName();
					int positionFirstPoint = entryName.indexOf('.', 0);
					int positionSecondPoint = entryName.indexOf('.', positionFirstPoint + 1);
					int positionSlash = entryName.indexOf('/', positionSecondPoint + 1);
					String versionMajor = entryName.substring(0, positionFirstPoint);
					String versionMinor = entryName.substring(positionFirstPoint + 1, entryName.indexOf('.', positionFirstPoint + 1));
					String versionMicro = entryName.substring(positionSecondPoint + 1, entryName.indexOf('/', positionSlash));
					String filename = entryName.substring(positionSlash + 1, entryName.length());
					String currentVersion = versionMajor + "." + versionMinor + "." + versionMicro; //$NON-NLS-1$ //$NON-NLS-2$
					VersionBean version;
					if (!configuration.getVersions().existsVersion(currentVersion)) {
						version = new VersionBean();
						version.setVersionMajor(Integer.valueOf(versionMajor).intValue());
						version.setVersionMinor(Integer.valueOf(versionMinor).intValue());
						version.setVersionMicro(Integer.valueOf(versionMicro).intValue());
						version.setInFile(true);
						configuration.getVersions().addVersion(version);
					}
					else {
						version = configuration.getVersions().getVersion(currentVersion);
					}
					BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(jar.getInputStream(entry))));
					StringBuffer buffer = new StringBuffer();
					String line = br.readLine();
					while (line != null) {
						buffer.append(line + System.getProperty("line.separator")); //$NON-NLS-1$
						line = br.readLine();
					}
					version.addFile(filename, buffer.toString());
				}
			}
		}
		catch (IOException ioex) {
			throw new InvalidConfigurationFile(Messages.get("ConfigurationLoader.error.file.invalidformat"), ioex); //$NON-NLS-1$
		}
		try {
			jar.close();
		}
		catch (IOException ioex) {
			throw new IOException(Messages.get("ConfigurationLoader.error.file.close")); //$NON-NLS-1$
		}
	}

}

