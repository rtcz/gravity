package sk.uniba.gravity.app;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

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

	public GravityGame() {
		super();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (SwingUtilities.isRightMouseButton(e)) {
			bodySelected(null);
			for (GameBody body : getBodyList()) {
				body.setSelected(false);
				Vector2D mousePos = new Vector2D(e.getX(), e.getY());
				mousePos = mousePos.subtract(getAbsRefPoint());
				mousePos = mousePos.scalarMultiply(getMeterScale().up());
				// TODO translate body coordinates
				if (Vector2DUtils.isPosInside(body, mousePos)) {
					body.setSelected(true);
					bodySelected(body);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		requestFocus();
		firstDragPos = lastDragPos = new Vector2D(e.getX(), e.getY());

		isNewBodyAction = SwingUtilities.isLeftMouseButton(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			addBody();
		}
		firstDragPos = null;
		lastDragPos = null;
		isNewBodyAction = false;
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

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Vector2D currentDragPos = new Vector2D(e.getX(), e.getY());

		if (SwingUtilities.isMiddleMouseButton(e)) {
			Vector2D move = lastDragPos.subtract(currentDragPos);
			setAbsRefPoint(getAbsRefPoint().subtract(move));
		}
		lastDragPos = currentDragPos;
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
		if (getMeterScale().down() * zoom > MAX_ZOOM) {
			zoom = MAX_ZOOM / getMeterScale().down();
		}
		// min zoom limit
		if (getMeterScale().down() * zoom < MIN_ZOOM) {
			zoom = MIN_ZOOM / getMeterScale().down();
		}

		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		Vector2D mouseRefPos = getAbsRefPoint().subtract(mousePos);
		Vector2D zoomedRefPos = mouseRefPos.scalarMultiply(zoom);
		Vector2D move = mouseRefPos.subtract(zoomedRefPos);

		getMeterScale().zoom(zoom);
		setAbsRefPoint(getAbsRefPoint().subtract(move));
	}
}
