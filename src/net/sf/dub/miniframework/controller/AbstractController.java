/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 13.07.2005 - 09:55:00
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:50:11 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: AbstractController.java,v $
 * Revision 1.1  2006/03/28 15:50:11  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/04 11:59:02  dgm
 * miniframework ausgegliedert, diverse änderungen
 *
 * Revision 1.3  2005/08/03 16:15:06  dgm
 * weitere änderungen am handling der controller, neue panel, etc..
 *
 * Revision 1.2  2005/08/02 15:22:07  dgm
 * Panel Steuerung, neue Panels
 *
 * Revision 1.1  2005/07/14 14:42:52  dgm
 * initialer import für dub
 *
 */
package net.sf.dub.miniframework.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.dub.miniframework.data.DataContainer;


/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class AbstractController implements Controller, ControllerListener {
	
	protected Controller parent;
	
	public AbstractController(Controller parentController) {
		parent = parentController;
	}
	
	protected Collection controllerListener = new ArrayList();
	
	public void addControllerListener(ControllerListener listener) {
		controllerListener.add(listener);
	}
	
	public void removeControllerListener(ControllerListener listener) {
		controllerListener.remove(listener);
	}
	
	protected void notifyOnControllerStarted() {
		for (Iterator it = new ArrayList(controllerListener).iterator(); it.hasNext();) {
			ControllerListener listener = (ControllerListener)it.next();
			if (listener != this) {
				listener.onControllerStarted(new ControllerStartedEvent(this));
			}
		}
	}
	
	protected void notifyOnControllerStopped() {
		for (Iterator it = new ArrayList(controllerListener).iterator(); it.hasNext();) {
			ControllerListener listener = (ControllerListener)it.next();
			if (listener != this) {
				listener.onControllerStopped(new ControllerStoppedEvent(this));
			}
		}
	}
	

	public void onControllerStarted(ControllerStartedEvent event) {
	}
	
	public void onControllerStopped(ControllerStoppedEvent event) {
	}

	public void start() {
		notifyOnControllerStarted();
	}
	
	public void stop() {
		notifyOnControllerStopped();
	}
	
	public void startControllerThreaded(Controller controller) {
		final Controller controllerToStart = controller;
		Thread worker = new Thread() {
			public void run() {
				controllerToStart.start();
			}
		};
		worker.start();
	}
	
	public Controller getParent() {
		if (parent == null) {
			return this;
		}
		return parent;
	}

	public Controller getRoot() {
		if (parent == null) {
			return this;
		}
		return parent.getRoot();
	}

	public DataContainer getData() {
		return null;
	}
	
}
