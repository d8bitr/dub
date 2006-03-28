/**
 * Project dub
 * 
 * Creation date: 24.08.2005 - 10:08:37
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ResourceBundleTest.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.3  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/24 15:21:44  dgm
 * i18n fortgeführt
 *
 * Revision 1.1  2005/08/24 12:35:42  dgm
 * i18n eingeführt
 *
 */
package net.sf.dub.application.test;

import java.util.ResourceBundle;

import net.sf.dub.miniframework.util.Messages;


import junit.framework.TestCase;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class ResourceBundleTest extends TestCase {

	public void testMultipleResources() {
		ResourceBundle rb1 = ResourceBundle.getBundle("net.sf.dub.application.test.testdata.resourcebundles.data1"); //$NON-NLS-1$
		ResourceBundle rb2 = ResourceBundle.getBundle("net.sf.dub.application.test.testdata.resourcebundles.data2"); //$NON-NLS-1$
		assertEquals(rb1.getString("aaa"), "111");  //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(rb2.getString("bbb"), "222"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testMessageResource() {
		Messages.addResourceBundle("net.sf.dub.application.test.testdata.resourcebundles.data1"); //$NON-NLS-1$
		Messages.addResourceBundle("net.sf.dub.application.test.testdata.resourcebundles.data2"); //$NON-NLS-1$
		assertEquals(Messages.get("aaa"), "111");  //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Messages.get("bbb"), "222"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void testMessageResourceWithBaseClass() {
		Messages.addResourceBundle(ResourceBundleTest.class, "testdata.resourcebundles.data1"); //$NON-NLS-1$
		Messages.addResourceBundle(ResourceBundleTest.class, "testdata.resourcebundles.data2"); //$NON-NLS-1$
		assertEquals(Messages.get("aaa"), "111"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Messages.get("bbb"), "222"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}

