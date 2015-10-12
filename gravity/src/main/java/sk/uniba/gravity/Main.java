package sk.uniba.gravity;

import sk.uniba.gravity.app.GravityControl;
import sk.uniba.gravity.game.GameManager;

public class Main {

	public static void main(String[] args) {
		GameManager manager = new GameManager(new GravityControl());
		manager.setTitle("Hra hier");
		manager.setGameSpeed(1e0);
		manager.setDisplayMode(1280, 1024, false);
		manager.run();
	}

}
