package sk.uniba.gravity;

import sk.uniba.gravity.game.GameFrame;
import sk.uniba.gravity.game.GravityGame;

public class Main {

	public static void main(String[] args) {
		GameFrame frame = new GameFrame(new GravityGame());
		frame.setTitle("Gravity2D");
		frame.run();
	}

}
