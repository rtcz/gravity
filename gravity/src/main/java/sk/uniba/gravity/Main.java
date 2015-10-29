package sk.uniba.gravity;

import sk.uniba.gravity.app.GravityGame;
import sk.uniba.gravity.game.GameManager;

public class Main {

	public static void main(String[] args) {
		GameManager manager = new GameManager(new GravityGame());
		manager.setTitle("Hra hier");
		manager.setDisplayMode(1280, 1024, false);
		manager.run();
	}

}
