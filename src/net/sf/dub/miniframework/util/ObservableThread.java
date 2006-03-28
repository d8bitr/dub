/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 12.08.2005 - 12:46:56
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: ObservableThread.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/17 14:51:38  dgm
 * refactoring
 *
 * Revision 1.1  2005/08/12 12:59:00  dgm
 * updaten in thread
 *
 */
package net.sf.dub.miniframework.util;

import java.util.Vector;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class ObservableThread extends Thread {
	
	private Vector listeners = new Vector();
	
	public ObservableThread() {
		super();
	}
	
	public ObservableThread(Runnable runnbale) {
		super(runnbale);
	}
	
	public void addThreadListener(ThreadListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ThreadListener listener) {
		listeners.remove(listener);
	}
	
	public void run() {
		for (int i = 0; i < listeners.size(); i++) {
			((ThreadListener)listeners.get(i)).onThreadStartet();
		}
		runObserved();
		for (int i = 0; i < listeners.size(); i++) {
			((ThreadListener)listeners.get(i)).onThreadFinished();
		}
	}
	
	public abstract void runObserved();

}

