package sk.uniba.gravity.app;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.GameBody;
import sk.uniba.gravity.GameConstants;
import sk.uniba.gravity.Vector2DUtils;
import sk.uniba.gravity.game.InteractiveGame;

public class GravityGame extends GravityCanvas implements InteractiveGame {

	private static final long serialVersionUID = -5192501583994098342L;

	public static final double ZOOM_FACTOR = GameConstants.ZOOM_FACTOR;
	public static final double MIN_ZOOM = GameConstants.MIN_ZOOM;
	public static final double MAX_ZOOM = GameConstants.MAX_ZOOM;

	private Vector2D startDragPos;
	
	private boolean isDragAction;

	public GravityGame() {
		super();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		bodySelected(null);
		for (GameBody body : getBodyList()) {
			body.setSelected(false);
			Vector2D mousePos = new Vector2D(e.getX(), e.getY());
			mousePos = mousePos.subtract(getAbsRefPoint());
			mousePos = mousePos.scalarMultiply(1 / getRefScale());
			// TODO translate body coordinates
			if (Vector2DUtils.isPosInside(body, mousePos)) {
				body.setSelected(true);
				bodySelected(body);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		requestFocus();
		startDragPos = new Vector2D(e.getX(), e.getY());
		
		if (!isDragAction) {
			newBodyStart = startDragPos;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!isDragAction) {
			addBody();
		}
		newBodyStart = null;
		newBodyEnd = null;
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
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			isDragAction = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			isDragAction = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Vector2D endDragPos = new Vector2D(e.getX(), e.getY());
		if (isDragAction) {
			Vector2D move = startDragPos.subtract(endDragPos);
			setAbsRefPoint(getAbsRefPoint().subtract(move));	
		} else {
			newBodyEnd = endDragPos;
		}
		startDragPos = endDragPos;
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
		if (getRefScale() * zoom > MAX_ZOOM) {
			zoom = MAX_ZOOM / getRefScale();
		}
		// min zoom limit
		if (getRefScale() * zoom < MIN_ZOOM) {
			zoom = MIN_ZOOM / getRefScale();
		}

		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		Vector2D mouseRefPos = getAbsRefPoint().subtract(mousePos);
		Vector2D zoomedRefPos = mouseRefPos.scalarMultiply(zoom);
		Vector2D move = mouseRefPos.subtract(zoomedRefPos);

		setRefScale(getRefScale() * zoom);
		setAbsRefPoint(getAbsRefPoint().subtract(move));
	}
}
