package sk.uniba.gravity.shape;

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

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}

	public double getSize() {
		return radius * 2;
	}

	/**
	 * @param circle
	 * @return true if bounding rectangles overlap
	 */
	public boolean isNear(Circle circle) {
		double bothRadi = getRadius() + circle.getRadius();
		return circle.getCenter().getX() < getCenter().getX() + bothRadi
				&& getCenter().getX() < circle.getCenter().getX() + bothRadi
				&& circle.getCenter().getX() < getCenter().getX() + bothRadi
				&& getCenter().getX() < circle.getCenter().getX() + bothRadi;
	}

	/**
	 * @param circle
	 * @return true if circles intersect
	 */
	public boolean collides(Circle circle) {
		double deltaX = getCenter().getX() - circle.getCenter().getX();
		double deltaY = getCenter().getY() - circle.getCenter().getY();
		double dist = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		return dist <= getRadius() + circle.getRadius();
	}

	public Vector2D collisionPoint(Circle circle) {
		double bothRadi = getRadius() + circle.getRadius();
		double x = (getCenter().getX() * circle.getRadius() + circle.getCenter().getX() * getRadius()) / bothRadi;
		double y = (getCenter().getY() * circle.getRadius() + circle.getCenter().getY() * getRadius()) / bothRadi;
		return new Vector2D(x, y);
	}
}
