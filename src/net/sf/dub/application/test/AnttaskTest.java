/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 17.08.2005 - 15:02:10
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: AnttaskTest.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.6  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.5  2005/10/31 10:37:43  dgm
 * assert test für build-server
 *
 * Revision 1.4  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.3  2005/08/24 13:13:00  dgm
 * debug
 *
 * Revision 1.2  2005/08/22 14:02:46  dgm
 * anttask und build.xml
 *
 * Revision 1.1  2005/08/17 14:52:09  dgm
 * ant task
 *
 */
package net.sf.dub.application.test;

import java.net.URL;

import net.sf.dub.application.anttask.DubTask;


import junit.framework.TestCase;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class AnttaskTest extends TestCase {

	public void testTask() throws Exception {
		DubTask tt = new DubTask();
		URL url = AnttaskTest.class.getResource("testdata/anttask/dub.properties"); //$NON-NLS-1$
		assert(url != null);
		tt.setConfigurationFile(url.getFile());
		url = AnttaskTest.class.getResource("testdata/anttask"); //$NON-NLS-1$
		tt.setSqlVersionDir(url.getFile());
		String jarFile = System.getProperty("java.io.tmpdir") + "/dub.jar"; //$NON-NLS-1$ //$NON-NLS-2$
		tt.setOutputFile(jarFile);
		tt.execute();
	}
	
}
