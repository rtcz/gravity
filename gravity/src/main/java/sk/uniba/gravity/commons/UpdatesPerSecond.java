package sk.uniba.gravity.commons;


public class UpdatesPerSecond {
	
	private static final int SECOND = 1_000_000_000;

	private long recorded;
	private int count;
	private int counter;
	
	public void update() {
		if (secondHasPassed()) {
			recorded = System.nanoTime();
			count = counter;
			counter = 0;
		}
		counter++;
	}
	
	public int getCount() {
		return count;
	}
	
	private boolean secondHasPassed() {
		return System.nanoTime() - recorded > SECOND;
	}
}
