/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 04.08.2005 - 14:29:56
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ConfigurationLoaderTest.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.1  2005/08/04 15:34:29  dgm
 * Configuration laden
 *
 */
package net.sf.dub.application.test;

import java.io.File;

import net.sf.dub.application.data.Configuration;
import net.sf.dub.application.data.ConfigurationLoader;


import junit.framework.TestCase;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ConfigurationLoaderTest extends TestCase {

	public void testLoad() throws Exception {
		Configuration config = new Configuration();
		ConfigurationLoader loader = new ConfigurationLoader();
		File file = new File(this.getClass().getResource("testdata/dub.jar").getFile()); //$NON-NLS-1$
		loader.loadConfiguration(file, config);
	}
	
}

