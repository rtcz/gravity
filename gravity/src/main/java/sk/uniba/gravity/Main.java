package sk.uniba.gravity;

public class Main {

	public static void main(String[] args) {
		GameManager manager = new GameManager(new Canvas());
		manager.setTitle("Hra hier");
		manager.setGameSpeed(1e5);
		manager.setDisplayMode(1280, 1024, false);
		manager.run();
	}

}
