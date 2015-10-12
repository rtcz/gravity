package sk.uniba.gravity.shape;

import java.awt.Graphics2D;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Cross {

	private Vector2D center;
	private int size;

	public Cross(Vector2D center, int size) {
		this.center = center;
		this.size = size;
	}

	public void draw(Graphics2D g) {
		Vector2D north = center.add(new Vector2D(0, -size));
		Vector2D east = center.add(new Vector2D(size, 0));
		Vector2D south = center.add(new Vector2D(0, size));
		Vector2D west = center.add(new Vector2D(-size, 0));

		g.drawLine((int) north.getX(), (int) north.getY(), (int) south.getX(), (int) south.getY());
		g.drawLine((int) west.getX(), (int) west.getY(), (int) east.getX(), (int) east.getY());
	}
}
