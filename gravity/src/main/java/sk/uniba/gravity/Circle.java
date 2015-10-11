package sk.uniba.gravity;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Circle {

	private Vector2D center;
	private double radius;

	public Circle(Vector2D center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public Circle(double centerX, double centerY, double radius) {
		this(new Vector2D(centerX, centerY), radius);
	}

	public Vector2D getCenter() {
		return center;
	}

	public void setCenter(Vector2D center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}
	
	public double getSize() {
		return radius * 2;
	}
	
	public double getX() {
		return center.getX() - radius;
	}
	
	public double getY() {
		return center.getY() - radius;
	}
}
