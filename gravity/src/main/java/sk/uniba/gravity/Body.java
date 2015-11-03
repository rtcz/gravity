package sk.uniba.gravity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.shape.Circle;

public class Body extends Circle {

	/**
	 * Density of water 1000 kg/m3
	 */
	public static final double DEFAULT_DENSITY = 1000;

	/**
	 * m/s
	 */
	private Vector2D velocity = new Vector2D(0, 0);

	/**
	 * kg/m^3
	 */
	private double density = DEFAULT_DENSITY;

	private List<Vector2D> trajectory = new ArrayList<Vector2D>();

	private String name;

	/**
	 * true if last trajectory point is temporary
	 */
	private boolean tempTrajectoryPoint;

	public Body() {
		super(new Vector2D(0, 0), 0);
	}

	/**
	 * Volume that body represents
	 * 
	 * @return m^3
	 */
	public double getVolume() {
		return (float) 4 / 3 * Math.PI * Math.pow(getRadius(), 3);
	}

	/**
	 * Get mass of sphere that body represents
	 * 
	 * @return kg
	 */
	public double getMass() {
		return getVolume() * density;
	}

	public void setDensity(double density) {
		if (density == 0) {
			throw new IllegalArgumentException("Body can not have zero density");
		}
		this.density = density;
	}

	public double getDensity() {
		return density;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public void setVelocity(double speed, Vector2D unitVector) {
		this.velocity = unitVector.scalarMultiply(speed);
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public String getName() {
		if (name == null) {
			return "Untitled Body";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void merge(Body body) {
		double sumMass = getMass() + body.getMass();
		Vector2D velocity1 = getVelocity().scalarMultiply(getMass() / sumMass);
		Vector2D velocity2 = body.getVelocity().scalarMultiply(body.getMass() / sumMass);
		setVelocity(velocity1.add(velocity2));
		
		double sumVolume = getVolume() + body.getVolume();
		setDensity(sumMass / sumVolume);
		setRadius(Math.cbrt((3 * sumVolume) / (4 * Math.PI)));
	}

	@Override
	public String toString() {
		return getName();
	}

	public Vector2D getLastTrackPoint() {
		if (trajectory.isEmpty()) {
			return null;
		}
		return trajectory.get(trajectory.size() - 1);
	}

	public void addTrackPoint(Vector2D point) {
		addTrackPoint(point, false);
	}

	public void addTrackPoint(Vector2D point, boolean temp) {
		if (this.tempTrajectoryPoint) {
			// if true, trajectory is not empty
			trajectory.remove(trajectory.size() - 1);
		}
		trajectory.add(point);
		this.tempTrajectoryPoint = temp;
	}

	// public void limitTrack(int size) {
	// if (trajectory.size() > size) {
	// trajectory = trajectory.subList(trajectory.size() - size,
	// trajectory.size() - 1);
	// }
	// }

	public void clearTrack() {
		trajectory.clear();
	}

	public List<Vector2D> getTrack() {
		return trajectory;
	}
}
