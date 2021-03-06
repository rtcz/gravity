package sk.uniba.gravity.game;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import sk.uniba.gravity.commons.UpdatesPerSecond;

public class GameFrame extends JFrame implements KeyListener {

	private static final long serialVersionUID = 8104184387340267764L;

	public static final int WIDTH = GameConstants.WINDOW_WIDTH;
	public static final int HEIGHT = GameConstants.WINDOW_HEIGHT;

	public static final int MAX_SKIPPED_FRAMES = GameConstants.MAX_SKIPPED_FRAMES;

	public static final int DESIRED_FPS = GameConstants.DESIRED_FPS;
	public static final int MIN_FPS = GameConstants.MIN_FPS;
	public static final double MAX_LAG_MS = GameConstants.MAX_LAG_MS;

	public static final int MAX_UPDATE_SPEED = GameConstants.MAX_UPDATE_SPEED;

	private static boolean syncUpdates = true;
	private static boolean syncRenders = true;

	private UpdatesPerSecond fps = new UpdatesPerSecond();
	private UpdatesPerSecond ups = new UpdatesPerSecond();

	private long gameTime;

	private boolean isFullscreen = false;

	private int speedMultiplier = 1;

	private GraphicsDevice device;

	/**
	 * desired duration of one game loop in miliseconds
	 */
	private double deltaTime = 1_000d / (double) DESIRED_FPS;

	private GameCanvas canvas;

	public GameFrame(GameCanvas canvas) {
		this.canvas = canvas;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();

		setWindowedMode();
		setContentPane(canvas);
		setVisible(true);
		canvas.init(this);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				keyPressed(e);
				return false;
			} else if (e.getID() == KeyEvent.KEY_TYPED) {
				keyTyped(e);
				return false;
			}
			return true;
		});
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

	public void run() {
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
				updateTime += deltaTime;
				gameTime += delta * speedMultiplier;

				// TODO limit by actual delta game time
				if (speedMultiplier > MAX_UPDATE_SPEED) {
					for (int i = 0; i < speedMultiplier / MAX_UPDATE_SPEED; i++) {
						ups.update();
						canvas.update(delta * MAX_UPDATE_SPEED);
					}
				} else {
					ups.update();
					canvas.update(delta * speedMultiplier);
				}

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
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {}
			}
		}

	}

	/**
	 * @param multiplier
	 *            1 is for realtime
	 */
	public void setSpeedMultiplier(int multiplier) {
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

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		} else if (e.getKeyCode() == KeyEvent.VK_F5) {
			if (device.isFullScreenSupported()) {
				if (isFullscreen) {
					setWindowedMode();
				} else {
					setFullScreenMode();
				}
				repaint();
			}
		}
	}

	private void setFullScreenMode() {
		// Switch to fullscreen mode
		dispose();
		// setVisible(false);
		setResizable(false);
		setUndecorated(true);
		device.setFullScreenWindow(this);
		setVisible(true);
		isFullscreen = true;
	}

	private void setWindowedMode() {
		// Switch to windowed mode
		dispose();
		// setVisible(false);
		setUndecorated(false);
		setResizable(true);
		device.setFullScreenWindow(null);
		setVisible(true);
		isFullscreen = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
