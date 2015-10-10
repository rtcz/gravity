package sk.uniba.gravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Canvas extends JPanel implements InteractiveGame {

	private static final long serialVersionUID = -4662105822647187214L;

	private GameManager mng;

	private List<Body> bodies = new ArrayList<Body>();

	public Canvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);

		addKeyListener(this);
		addMouseListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	@Override
	public void init(GameManager mng) {
		this.mng = mng;

		// TEMP CODE
		Body earth = new Body(new Vector2D(100, 100), 6.371);
		earth.setDensity(5515);
		bodies.add(earth);
		Body moon = new Body(new Vector2D(372, 372), 1.737);
		moon.setDensity(3346);
		bodies.add(moon);
	}

	@Override
	public void update() {
		for (int i = 0; i < bodies.size(); i++) {
			for (int j = 0; j < bodies.size(); j++) {
				if (i > j) {
					
					Body b1 = bodies.get(i);
					Body b2 = bodies.get(j);
					double mass1 = b1.getMass();
					double mass2 = b2.getMass();
					
					// gravitational force by Newton's law of universal gravitation F=G.m1.m2/r^2
					double dist12 = b1.getCenter().distance(b2.getCenter());
					if (dist12 < 0) {
						dist12 = 0;
					}
					double gForce = (Body.G_CONSTANT * mass1 * mass2) / Math.pow(dist12 * Body.PIXEL_SIZE, 2);
					
					// direction vector
					Vector2D vect12 = b2.getCenter().subtract(b1.getCenter());
					Vector2D uVect12 = new Vector2D(vect12.getX() / dist12, vect12.getY() / dist12);
					
					// gravitational force vector
					Vector2D vForce = uVect12.scalarMultiply(gForce);
					
					// momentum p=F.t
					Vector2D momentum = vForce.scalarMultiply((float) 100);
					
					// body velocities v=p/m, opposite momentum is applied to second body
					Vector2D velocity1 = new Vector2D(momentum.getX() / mass1, momentum.getY() / mass1);
					Vector2D velocity2 = new Vector2D(-momentum.getX() / mass2, -momentum.getY() / mass2);
					
					b1.setVelocity(velocity1);
					b2.setVelocity(velocity2);
				}
			}
		}
		for (Body body : bodies) {
			Vector2D newCenter = body.getCenter().add(body.getVelocity());
			body.setCenter(newCenter);
		}
	}

	@Override
	public void render() {
		repaint();
	}

	private void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.WHITE);
		int fps = mng.getFps();
		g.drawString("FPS " + fps, 10, 20);

		for (Body body : bodies) {
			body.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
