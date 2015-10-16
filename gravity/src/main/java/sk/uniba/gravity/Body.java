package sk.uniba.gravity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.shape.Circle;

public class Body extends Circle {

	/**
	 * Gravitational constant
	 * 6.674×10−11 N⋅m2/kg2 
	 */
	public static final double G_CONSTANT = 6.674e-11;
	
	/**
	 * Density of water 1000 kg/m3
	 */
	public static final double DEFAULT_DENSITY = 1000;
	
	/**
	 * m/s
	 */
	private Vector2D velocity;
	
	private double density = DEFAULT_DENSITY;
	
	private List<Vector2D> trajectory = new ArrayList<Vector2D>();
	
	/**
	 * true if last trajectory point is temporary
	 */
	private boolean tempTrajectoryPoint;
	
	public Body(Vector2D center, double radius) {
		super(center, radius);
		velocity = new Vector2D(0, 0);
		density = DEFAULT_DENSITY;
	}

	/**
	 * Get mass of sphere that body represents
	 * @return
	 */
	public double getMass() {
		double volume = (float) 4/3 * Math.PI * Math.pow(getRadius(), 3);
		return volume * density;
	}

	public Color getColor() {
		// TODO involve density
		int colorValue = 255 - (int) Math.log10(getRadius()) * 10;
		if (colorValue < 128) {
			colorValue = 128;
		}
		return new Color(colorValue, colorValue, colorValue);
	}

	public void setDensity(double density) {
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
	
	@Override
	public String toString() {
		return "R " + getRadius() / 1000f + " KM";
	}
	
	public Vector2D getLastTrajectoryPoint() {
		if (trajectory.isEmpty()) {
			return null;
		}
		return trajectory.get(trajectory.size() - 1);
	}
	
	public void addTrajectoryPoint(Vector2D point) {
		addTrajectoryPoint(point, false);
	}
	
	public void addTrajectoryPoint(Vector2D point, boolean temp) {
		if (this.tempTrajectoryPoint) {
			// if true, trajectory is not empty
			trajectory.remove(trajectory.size() - 1);
		}
		trajectory.add(point);
		this.tempTrajectoryPoint = temp;
	}
	
	public List<Vector2D> getTrajectory() {
		return trajectory;
	}
}
