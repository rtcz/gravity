package sk.uniba.gravity;

import java.awt.Color;
import java.awt.Graphics2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Body extends Circle {

	public static final float DENSITY = 1;
	
	private Vector2D velocity;

	public Body(Vector2D center, double radius, Vector2D velocity) {
		super(center, radius);
		this.velocity = velocity;
	}
	
	public Body(Vector2D center, double radius) {
		this(center, radius, new Vector2D(0, 0));
	}

	public double getMass() {
		double area = Math.PI * Math.pow(getRadius(), 2);
		return area * DENSITY;
	}

	public Color getColor() {
		int colorValue = 255 - (int) getRadius();
		if (colorValue < 128) {
			colorValue = 128;
		}
		return new Color(colorValue, colorValue, colorValue);
	}

	public void draw(Graphics2D g) {
		g.setColor(getColor());
		super.draw(g);
	}
	
	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}
	
	public Vector2D getVelocity() {
		return velocity;
	}
}
