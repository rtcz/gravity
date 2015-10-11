package sk.uniba.gravity;

import javax.swing.JFrame;

public class GameManager {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public static final int MAX_SKIPPED_FRAMES = 5;

	public static final int DESIRED_FPS = 60;
	public static final int MIN_FPS = 12;
	public static final double MAX_LAG_MS = 250;

	private static boolean syncUpdates = true;
	private static boolean syncRenders = true;

	/** last time FPS was recorded */
	private long fpsRecorded;

	/**
	 * last recorded FPS
	 */
	private int fps;

	private int fpsCounter;

	private long gameTime;

	private int width;

	private int height;

	private boolean fullscreen;

	private double gameSpeed = 1;

	private JFrame window = new JFrame();

	/**
	 * desired duration of one game loop in miliseconds
	 */
	private double deltaTime = 1_000d / DESIRED_FPS;

	private Canvas canvas;

	public GameManager(Canvas canvas) {
		this.canvas = canvas;
		// set window to center of the screen
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDisplayMode(WIDTH, HEIGHT, false);
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

	private void setup() {
		window.setSize(width, height);
		if (fullscreen) {
			window.setUndecorated(true);
			window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			window.setLocationRelativeTo(null);
			window.setResizable(false);
		}
		window.setContentPane(canvas);
		window.setVisible(true);
	}

	public void setTitle(String title) {
		window.setTitle(title);
	}

	public void run() {
		setup();
		canvas.init(this);
		long nextTime = getTime();
		long lastTime = getTime();
		int skippedFrames = 0;
		int maxSkippedFrames = DESIRED_FPS / MIN_FPS;
		gameTime = 0;
		while (true) {
			long currTime = getTime();
			if (syncUpdates) {
				// remove gap between logic and graphics
				if ((currTime - nextTime) > MAX_LAG_MS) {
					nextTime = currTime;
				}
			}
			if (currTime > nextTime) {
				double delta = (currTime - lastTime);
				lastTime = currTime;
				nextTime += deltaTime;
				gameTime += delta * gameSpeed;
				canvas.update(delta * gameSpeed);

				if (syncRenders) {
					// render extra frames if fps if too low
					if ((currTime < nextTime) || skippedFrames > maxSkippedFrames) {
						updateFps();
						canvas.render();
						skippedFrames = 0;
					} else {
						skippedFrames++;
					}
				} else if (currTime < nextTime) {
					updateFps();
					canvas.render();
				}

			} else {
				// game is up to date, it has time to sleep now
				int sleepTime = (int) (nextTime - currTime);
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
	 * @param speed
	 *            multiplier, 1 is for realtime
	 */
	public void setGameSpeed(double speed) {
		this.gameSpeed = speed;
	}

	public double getGameSpeed() {
		return gameSpeed;
	}

	public void updateFps() {
		if (getTime() - fpsRecorded > 1_000) {
			fpsRecorded = getTime();
			fps = fpsCounter;
			fpsCounter = 0;
		}
		fpsCounter++;
	}

	public int getFps() {
		return fps;
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
