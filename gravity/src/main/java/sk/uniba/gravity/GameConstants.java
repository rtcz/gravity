package sk.uniba.gravity;

public final class GameConstants {

	private GameConstants() {}

	/**
	 * grid unit to pixel 1:10
	 */
	public static final double PIXEL_SCALE = 1e-1;

	/**
	 * grid unit to meter 1:1000000
	 */
	public static final double METER_SCALE = 1e-6;

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	public static final int MAX_SKIPPED_FRAMES = 5;

	public static final int DESIRED_FPS = 60;
	public static final int MIN_FPS = 12;
	public static final double MAX_LAG_MS = 250;

	public static final double ZOOM_FACTOR = 1.1;
	public static final double MIN_ZOOM = 1e-12;
	public static final double MAX_ZOOM = 1;

	public static final int MAX_TRACK_SEGMENTS = 1_000;

	/**
	 * Gravitational constant 6.674×10−11 N⋅m2/kg2
	 */
	public static final double G_CONSTANT = 6.674e-11;

	public static final int PROTODISK_SIZE = 100;

	public static final int MAX_UPDATE_SPEED = 10_000;
	// public static final int MAX_UPDATE_SPEED = Integer.MAX_VALUE;

	public static final double DELTA_TIME_CHANGE_FACTOR = 1.33;

	/**
	 * theta = 0 is for bruteforce
	 */
	public static final double BH_TREE_THETA = 0.5;

	/**
	 * Gravitational softening parameter. Distance in meters.
	 */
	public static final double EPSILON = 1e7;

	/**
	 * Maximal number of particles in "normal mode". Simulation is switched to
	 * particle mode if more particles are simulated.
	 */
	public static final int PARTICLE_MODE_THRESHOLD = 100;
}
