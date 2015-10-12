package sk.uniba.gravity.game;

public interface GameLoop {

	public void init(GameManager mng);

	/**
	 * @param game delta time since last update in milliseconds
	 */
	public void update(double delta);

	public void render();

}
