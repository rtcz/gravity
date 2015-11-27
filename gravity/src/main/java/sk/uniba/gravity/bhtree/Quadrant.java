package sk.uniba.gravity.bhtree;

import java.awt.Graphics2D;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.commons.Scale;

public class Quadrant {

	private Vector2D center;
	private double size;

	/**
	 * Constructor: creates a new Quad with the given parameters (assume it is
	 * square).
	 *
	 * @param xmid
	 *            x-coordinate of center of quadrant
	 * @param ymid
	 *            y-coordinate of center of quadrant
	 * @param size
	 *            the side length of the quadrant
	 */
	public Quadrant(Vector2D center, double size) {
		this.center = center;
		this.size = size;
	}
	
	public Vector2D getCenter() {
		return center;
	}

	public double getSize() {
		return size;
	}

	public boolean isInside(Vector2D point) {
		double halfSize = this.size * 0.5;
		return point.getX() <= this.center.getX() + halfSize && point.getX() >= this.center.getX() - halfSize
				&& point.getY() <= this.center.getY() + halfSize && point.getY() >= this.center.getY() - halfSize;
	}

	/**
	 * Draws an unfilled rectangle that represents the quadrant.
	 */
	public void draw(Graphics2D g, Scale scale) {
		int x = (int) ((getCenter().getX() - getSize() / 2) * scale.down());
		int y = (int) ((getCenter().getY() - getSize() / 2) * scale.down());
		int size = (int) (getSize() * scale.down());
		g.drawRect(x, y, size, size);
	}
}
