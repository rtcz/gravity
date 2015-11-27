package sk.uniba.gravity.game;

public interface GameLoop {

	public void init(GameFrame mng);

	/**
	 * @param game
	 *            delta time since last update in milliseconds
	 */
	public void update(double deltaMs);

	public void render();

}
