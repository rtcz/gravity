package sk.uniba.gravity;

public class Main {

	public static void main(String[] args) {
		GameManager manager = new GameManager(new Canvas());
		manager.setTitle("Hra hier");
		//manager.setDisplayMode(1920, 1080, true);
		manager.run();
	}

}
