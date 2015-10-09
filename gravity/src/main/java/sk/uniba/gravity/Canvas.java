package sk.uniba.gravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
		bodies.add(new Body(new Vector2D(200, 200), 50, new Vector2D(1, 1)));
		bodies.add(new Body(new Vector2D(400, 400), 25, new Vector2D(-1, -1)));
	}

	@Override
	public void update() {
		for (int i = 0; i < bodies.size(); i++) {
			for (int j = 0; j < bodies.size(); j++) {
				if (i > j) {
					
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
