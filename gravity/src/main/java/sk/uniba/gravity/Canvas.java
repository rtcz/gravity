package sk.uniba.gravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Canvas extends JPanel implements InteractiveGame {

	private static final long serialVersionUID = -4662105822647187214L;

	private GameManager mng;

	private List<Body> bodies = new ArrayList<Body>();
	
	private Vector2D refPoint;
	private double refSize = 1;

	public Canvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g);
	}

	@Override
	public void init(GameManager mng) {
		this.mng = mng;
		refPoint = new Vector2D(0, 0);

		// TEMP CODE
		Body earth = new Body(new Vector2D(100, 100), 6_371_000);
		earth.setDensity(5515);
		//earth.setCenter(refPoint.subtract(earth.getCenter()));
		//earth.setCenter(earth.getCenter().subtract(refPoint));
		bodies.add(earth);
		
		Body moon = new Body(new Vector2D(372, 372), 1_737_000);
		moon.setDensity(3346);
		//moon.setCenter(moon.getCenter().subtract(refPoint));
		//moon.setCenter(refPoint.subtract(moon.getCenter()));
		bodies.add(moon);
	}

	@Override
	public void update(int delta) {
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
					
					// momentum p
					double momentum = gForce * delta / 1_000d;
					
					// body velocities v=p/m
					double v1 = momentum / mass1;
					double v2 = momentum / mass2;
					
					// direction vector
					Vector2D vect12 = b2.getCenter().subtract(b1.getCenter());
					Vector2D uVect12 = new Vector2D(vect12.getX() / dist12, vect12.getY() / dist12);
					Vector2D uVect21 = uVect12.scalarMultiply(-1);
					
					b1.setVelocity(uVect12.scalarMultiply(v1));
					b2.setVelocity(uVect21.scalarMultiply(v2));
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
			g.setColor(body.getColor());
			int size = (int) (body.getRadius() * 2);
			int x = (int) (body.getCenter().getX() + refPoint.getX() - body.getRadius());
			int y = (int) (body.getCenter().getY() + refPoint.getY() - body.getRadius());
			g.fillOval(x, y, size, size);
		}
		
		g.setColor(Color.GREEN);
		Cross reference = new Cross(refPoint, 10);
		reference.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lastDragPos = null;
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

	Vector2D lastDragPos;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (lastDragPos == null) {
			lastDragPos = new Vector2D(e.getX(), e.getY());
		}
		Vector2D newDragPos = new Vector2D(e.getX(), e.getY());
		Vector2D move = lastDragPos.subtract(newDragPos);
		
		refPoint = refPoint.subtract(move);
		lastDragPos = newDragPos;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		refSize *= e.getWheelRotation();
	}

}
