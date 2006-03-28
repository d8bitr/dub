/**
 * Project dub - (c) see bsd.licence file
 * 
 * Creation date: 07.09.2005 - 14:39:07
 * Last author:   $Author: danielgalan $
 * Last modified: $Date: 2006/03/28 15:49:47 $
 * Revision:      $Revision: 1.1 $
 * 
 * $Log: AnimationPanel.java,v $
 * Revision 1.1  2006/03/28 15:49:47  danielgalan
 * inital import
 *
 * Revision 1.2  2006/03/22 12:19:26  dgm
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/08 12:43:39  dgm
 * about animation
 *
 */
package net.sf.dub.application.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.dub.miniframework.util.Messages;

import com.jgoodies.animation.Animation;
import com.jgoodies.animation.Animations;
import com.jgoodies.animation.Animator;
import com.jgoodies.animation.animations.BasicTextAnimation;
import com.jgoodies.animation.animations.FanAnimation;
import com.jgoodies.animation.components.BasicTextLabel;
import com.jgoodies.animation.components.FanComponent;


/**
 * Small about animation
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class AnimationPanel extends JPanel {
	
	private Font fontHeader = new Font("Tahoma", Font.BOLD, 24); //$NON-NLS-1$
	private Font fontNormal = new Font("Tahoma", Font.PLAIN, 16); //$NON-NLS-1$
	
	public AnimationPanel() {
		super(new BorderLayout());
		setBackground(Color.WHITE);
		
		// Fan animation
		FanComponent fan =  new FanComponent(9, Color.BLACK);
		long fanSpeed = 120000;
		Animation animFan = new FanAnimation(fan, fanSpeed, FanAnimation.defaultRotationFunction(fanSpeed));
		animFan = Animations.repeat(Float.MAX_VALUE, animFan);

		// Credits
		List creditAnimations = new ArrayList();
		String versionNumber = getClass().getPackage().getImplementationVersion();
		versionNumber = (versionNumber == null) ? Messages.get("AnimationPanel.version.unknown") : versionNumber; //$NON-NLS-1$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.1") + " " + versionNumber, fan)); //$NON-NLS-1$ //$NON-NLS-2$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.2"), fan)); //$NON-NLS-1$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.3"), fan)); //$NON-NLS-1$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.4"), fan)); //$NON-NLS-1$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.5"), fan)); //$NON-NLS-1$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.6"), fan)); //$NON-NLS-1$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.7"), fan)); //$NON-NLS-1$
		creditAnimations.add(createCredit(Messages.get("AnimationPanel.credit.8"), fan)); //$NON-NLS-1$
		Animation animCredits = Animations.repeat(Float.MAX_VALUE, Animations.sequential(creditAnimations));
		
		// dub fix
		BasicTextLabel labelDub = createLabel(Messages.get("AnimationPanel.application.name"), -200, 0, fontHeader); //$NON-NLS-1$
		labelDub.setColor(Color.BLUE);
		fan.add(labelDub);
		
		// dub Background
		BasicTextLabel labelDubBack = createLabel(Messages.get("AnimationPanel.application.name"), -200,0, fontHeader); //$NON-NLS-1$
		labelDubBack.setColor(Color.BLUE);
		Animation animDub = BasicTextAnimation.defaultScale(labelDubBack, creditAnimations.size() * 10000, Messages.get("AnimationPanel.application.name"), Color.BLUE); //$NON-NLS-1$
		animDub = Animations.repeat(Float.MAX_VALUE, animDub);
		fan.add(labelDubBack);
		
		// Putting the animations together
		List listAnimations = new ArrayList();
		listAnimations.add(animFan);
		listAnimations.add(animDub);
		listAnimations.add(animCredits);
		Animation all = Animations.parallel(listAnimations);
		all.animate(Long.MAX_VALUE);
		
		// start them with 30 fps
		Animator animator = new Animator(all, 30);
		animator.start();
		
		add(fan, BorderLayout.CENTER);
	}
	
	protected BasicTextLabel createLabel(String text, int x, int y, Font font) {
		BasicTextLabel label = new BasicTextLabel(text);
		label.setFont(font);
		label.setBounds(x, y, 600, 100);
		return label;
	}

	protected BasicTextLabel createLabel(int x, int y, Font font) {
		return createLabel("", x, y, font); //$NON-NLS-1$
	}
	
	protected Animation createCredit(String creditText, JComponent container) {
		BasicTextLabel labelCredits = createLabel(50, 50, fontNormal);
		Animation animCredits = BasicTextAnimation.defaultFade(labelCredits, 10000, creditText, Color.GRAY);
		container.add(labelCredits);
		return animCredits;
	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Animation Test"); //$NON-NLS-1$
		frame.setLocation(200, 200);
		frame.setSize(new Dimension(800, 200));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new AnimationPanel(), BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

