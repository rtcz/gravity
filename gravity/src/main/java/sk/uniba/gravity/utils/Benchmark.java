package sk.uniba.gravity.utils;

public class Benchmark {

	private static long started;

	private static long recorded;
	private static long recordedMax = Long.MIN_VALUE;
	private static long recordedMin = Long.MAX_VALUE;

	private static long counted;
	private static long maxCounted;
	
	private Benchmark() {}
	
	public static void increment() {
		increment(1);
	}
	
	public static void increment(long value) {
		counted += value;
		if (counted > maxCounted) {
			maxCounted = counted;
		}
	}
	
	public static long getCount() {
		return counted;
	}
	
	public static long getMaxCount() {
		return maxCounted;
	}
	
	public static void resetCounter() {
		counted = 0;
	}

	public static void start() {
		started = System.nanoTime();
	}

	public static void stop() {
		recorded = System.nanoTime() - started;
		if (recorded > recordedMax) {
			recordedMax = recorded;
		}
		if (recorded < recordedMin) {
			recordedMin = recorded;
		}
	}

	public static double getResult() {
		return convertToMs(recorded);
	}

	public static double getMax() {
		return convertToMs(recordedMax);
	}

	public static double getMin() {
		return convertToMs(recordedMin);
	}

	private static double convertToMs(long nanoSeconds) {
		return nanoSeconds / 1e6;
	}

	public static void resetRecords() {
		recorded = 0;
		recordedMax = Long.MIN_VALUE;
		recordedMin = Long.MAX_VALUE;
		maxCounted = 0;
	}
}
