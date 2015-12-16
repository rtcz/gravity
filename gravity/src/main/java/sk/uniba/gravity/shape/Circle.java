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

	public Vector2D getPosition() {
		return center;
	}

	public void setPosition(Vector2D center) {
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
		// TODO test this method
		double bothRadi = getRadius() + circle.getRadius();
		return circle.getPosition().getX() < getPosition().getX() + bothRadi
				&& getPosition().getX() < circle.getPosition().getX() + bothRadi
				&& circle.getPosition().getY() < getPosition().getY() + bothRadi
				&& getPosition().getY() < circle.getPosition().getY() + bothRadi;
	}

	/**
	 * @param circle
	 * @return true if circles intersect
	 */
	public boolean isColliding(Circle circle) {
		double deltaX = getPosition().getX() - circle.getPosition().getX();
		double deltaY = getPosition().getY() - circle.getPosition().getY();
		double bothRadi = getRadius() + circle.getRadius();
		return Math.pow(deltaX, 2) + Math.pow(deltaY, 2) <= Math.pow(bothRadi, 2);
	}

	public boolean isInside(Vector2D point) {
		double deltaX = getPosition().getX() - point.getX();
		double deltaY = getPosition().getY() - point.getY();
		return Math.pow(deltaX, 2) + Math.pow(deltaY, 2) <= Math.pow(getRadius(), 2);
	}

	public Vector2D collisionPoint(Circle circle) {
		double bothRadi = getRadius() + circle.getRadius();
		double x = (getPosition().getX() * circle.getRadius() + circle.getPosition().getX() * getRadius()) / bothRadi;
		double y = (getPosition().getY() * circle.getRadius() + circle.getPosition().getY() * getRadius()) / bothRadi;
		return new Vector2D(x, y);
	}
}
