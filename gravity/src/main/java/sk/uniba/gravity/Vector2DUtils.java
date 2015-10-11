package sk.uniba.gravity;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Vector2DUtils {

	public static Vector2D rotate(Vector2D vector, double degrees) {
		double rad = Math.toRadians(degrees);
		double x = vector.getX() * Math.cos(rad) - vector.getY() * Math.sin(rad);
		double y = vector.getX() * Math.sin(rad) + vector.getY() * Math.cos(rad);
		return new Vector2D(x, y);
	}
}
