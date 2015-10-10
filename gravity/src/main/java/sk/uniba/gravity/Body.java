package sk.uniba.gravity;

import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Body extends Circle {

	/**
	 * Gravitational constant
	 * 6.674×10−11 N⋅m2/kg2 
	 */
	public static final double G_CONSTANT = 6.674e-11;
	
	/**
	 * Average density of Earth is 5515 kg/m3
	 */
	public static final double DEFAULT_DENSITY = 5_515;
	
	/**
	 * Pixel size in meters (1000km)
	 */
	public static final double PIXEL_SIZE = 1_000_000;
	//public static final double PIXEL_SIZE = 1;
	
	private Vector2D velocity;
	
	private double density = DEFAULT_DENSITY;
	
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
		double volume = (float) 4/3 * Math.PI * Math.pow(getRadius() * PIXEL_SIZE, 3);
		return volume * density;
	}

	public Color getColor() {
		int colorValue = 255 - (int) getRadius();
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
	
	public Vector2D getVelocity() {
		return velocity;
	}
	
	@Override
	public String toString() {
		return "R" + getRadius() * PIXEL_SIZE;
	}
}
