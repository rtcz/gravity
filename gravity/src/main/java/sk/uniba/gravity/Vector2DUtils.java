package sk.uniba.gravity;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import sk.uniba.gravity.shape.Circle;

public class Vector2DUtils {

	public static Vector2D rotate(Vector2D vector, double degrees) {
		double rad = Math.toRadians(degrees);
		double x = vector.getX() * Math.cos(rad) - vector.getY() * Math.sin(rad);
		double y = vector.getX() * Math.sin(rad) + vector.getY() * Math.cos(rad);
		return new Vector2D(x, y);
	}

	public static Vector2D unit(Vector2D vector) {
		return scalarDivide(vector, vector.getNorm());
	}
	
	public static Vector2D scalarDivide(Vector2D vector, double scalar) {
		return new Vector2D(vector.getX() / scalar, vector.getY() / scalar);
	}
	
	public static Vector2D scalarDivide(double scalar, Vector2D vector) {
		return new Vector2D(scalar / vector.getX(), scalar / vector.getY());
	}

	public static boolean isPosInside(Circle circle, Vector2D pos) {
		double dX = circle.getPosition().getX() - pos.getX();
		double dY = circle.getPosition().getY() - pos.getY();
		return Math.pow(dX, 2) + Math.pow(dY, 2) < Math.pow(circle.getRadius(), 2);
	}
}
