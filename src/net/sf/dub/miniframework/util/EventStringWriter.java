/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 11.08.2005 - 14:35:06
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: EventStringWriter.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/17 14:51:38  dgm
 * refactoring
 *
 * Revision 1.1  2005/08/11 14:06:26  dgm
 * SqlMore angepasst (Puh), komplett die Connection und die Ausgabe austauschbar gemacht, in gui integriert (wenn gleich noch nicht im thread)
 *
 */
package net.sf.dub.miniframework.util;

import java.io.StringWriter;
import java.util.Vector;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class EventStringWriter extends StringWriter {
	
	private Vector listeners = new Vector();
	
	public EventStringWriter() {
		super();
	}
	
	public void addListener(EventStringListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(EventStringListener listener) {
		listeners.remove(listener);
	}

	public void flush() {
		super.flush();
		for (int i = 0; i < listeners.size(); i++) {
			((EventStringListener)listeners.get(i)).onFlush();
		}
	}

}
