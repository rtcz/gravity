package sk.uniba.gravity;

import java.awt.Graphics2D;

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

	public void draw(Graphics2D g) {
		int size = (int) (radius * 2);
		int x = (int) (center.getX() - radius);
		int y = (int) (center.getY() - radius);
		g.fillOval(x, y, size, size);
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
}
