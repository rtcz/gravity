package sk.uniba.gravity.app;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.game.InteractiveGame;

public class GravityControl extends Gravity implements InteractiveGame {

	private static final long serialVersionUID = -5192501583994098342L;

	public static final double ZOOM_FACTOR = 1.1;
	public static final double MIN_ZOOM = 1e-12;
	public static final double MAX_ZOOM = 1;
	
	public GravityControl() {
		super();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
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

		setRefPoint(getRefPoint().subtract(move));
		lastDragPos = newDragPos;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		double zoom = 1;
		if (e.getWheelRotation() > 0) {
			zoom = ZOOM_FACTOR;
		} else {
			zoom = 1 / ZOOM_FACTOR;
		}

		// max zoom limit
		if (getRefSize() * zoom > MAX_ZOOM) {
			zoom = MAX_ZOOM / getRefSize();
		}
		// min zoom limit
		if (getRefSize() * zoom < MIN_ZOOM) {
			zoom = MIN_ZOOM / getRefSize();
		}

		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		Vector2D mouseRefPos = getRefPoint().subtract(mousePos);
		Vector2D zoomedRefPos = mouseRefPos.scalarMultiply(zoom);
		Vector2D move = mouseRefPos.subtract(zoomedRefPos);

		setRefSize(getRefSize() * zoom);
		setRefPoint(getRefPoint().subtract(move));
	}
}
