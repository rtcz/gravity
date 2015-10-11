package sk.uniba.gravity;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

public interface InteractiveGame extends MouseListener, KeyListener, MouseMotionListener, MouseWheelListener {

	public void init(GameManager mng);

	/**
	 * @param game delta time since last update in milliseconds
	 */
	public void update(double delta);

	public void render();

}
