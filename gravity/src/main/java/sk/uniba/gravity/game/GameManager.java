package sk.uniba.gravity.game;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import sk.uniba.gravity.GameConstants;

public class GameManager {

	public static final int WIDTH = GameConstants.WINDOW_WIDTH;
	public static final int HEIGHT = GameConstants.WINDOW_HEIGHT;

	public static final int MAX_SKIPPED_FRAMES = GameConstants.MAX_SKIPPED_FRAMES;

	public static final int DESIRED_FPS = GameConstants.DESIRED_FPS;
	public static final int MIN_FPS = GameConstants.MIN_FPS;
	public static final double MAX_LAG_MS = GameConstants.MAX_LAG_MS;

	private static boolean syncUpdates = true;
	private static boolean syncRenders = true;

	private UpdatesPerSecond fps = new UpdatesPerSecond();
	private UpdatesPerSecond ups = new UpdatesPerSecond();

	private long gameTime;

	private int width;

	private int height;

	private boolean fullscreen;

	private double speedMultiplier = 1;

	private JFrame window = new JFrame();

	/**
	 * desired duration of one game loop in miliseconds
	 */
	private double deltaTime = 1_000d / (double) DESIRED_FPS;

	private GameCanvas canvas;

	public GameManager(GameCanvas canvas) {
		this.canvas = canvas;
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDisplayMode(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT, false);
	}

	/**
	 * if @param flag is true: - game rate is not constant - ommit updates when
	 * logic is too far ahead of graphics - more fluent gameplay
	 */
	public void syncUpdates(boolean flag) {
		syncUpdates = flag;
	}

	/**
	 * if @param flag is true: - game rate is not constant - do extra renders
	 * when fps is low - more fluent framerate
	 */
	public void syncRenders(boolean flag) {
		syncRenders = flag;
	}

	public void setDisplayMode(int width, int height, boolean fullscreen) {
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
	}

	private void init() {
		window.setSize(width, height);
		if (fullscreen) {
			window.setUndecorated(true);
			window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			// set window to center of the screen
			window.setLocationRelativeTo(null);
			window.setResizable(false);
		}
		window.setContentPane(canvas);
		window.setLayout(new BorderLayout());
		canvas.addContents();
		window.setVisible(true);
		canvas.init(this);
	}

	public void setTitle(String title) {
		window.setTitle(title);
	}

	public void run() {
		init();
		double updateTime = getTime();
		// double renderTime = getTime();
		double lastTime = getTime();
		int skippedFrames = 0;
		int maxSkippedFrames = DESIRED_FPS / MIN_FPS;
		gameTime = 0;
		while (true) {
			long currTime = getTime();
			if (syncUpdates) {
				// remove gap between logic and graphics
				if ((currTime - updateTime) > MAX_LAG_MS) {
					updateTime = currTime;
				}
			}
			if (currTime > updateTime) {
				double delta = (currTime - lastTime);
				lastTime = currTime;
				// updateTime += deltaTime * (1 / speedMultiplier);
				updateTime += deltaTime;
				gameTime += delta * speedMultiplier;

				ups.update();
				canvas.update(delta * speedMultiplier);

				// if (currTime > renderTime) {
				// renderTime += deltaTime;
				if (syncRenders) {
					// render extra frames if fps if too low
					if ((currTime < updateTime) || skippedFrames > maxSkippedFrames) {
						fps.update();
						canvas.render();
						skippedFrames = 0;
					} else {
						skippedFrames++;
					}
				} else if (currTime < updateTime) {
					fps.update();
					canvas.render();
				}
				// }
			} else {
				// game is up to date, it has time to sleep now
				int sleepTime = (int) (updateTime - currTime);
				// safety check
				if (sleepTime > 0) {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {}
				}
			}
		}

	}

	/**
	 * @param multiplier
	 *            1 is for realtime
	 */
	public void setSpeedMultiplier(double multiplier) {
		this.speedMultiplier = multiplier;
	}

	public double getGameSpeed() {
		return speedMultiplier;
	}

	public int getFps() {
		return fps.getCount();
	}

	public int getUps() {
		return ups.getCount();
	}

	public long getGameTime() {
		return gameTime;
	}

	/**
	 * @return current time in miliseconds
	 */
	public long getTime() {
		return System.nanoTime() / 1_000_000;
	}
}
