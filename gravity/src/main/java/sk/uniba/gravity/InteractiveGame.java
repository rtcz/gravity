package sk.uniba.gravity;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public interface InteractiveGame extends MouseListener, KeyListener {

	public void init(GameManager mng);

	public void update();

	public void render();

}
