package sk.uniba.gravity;

import java.awt.Graphics2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Arrow {

	private Vector2D start;
	private Vector2D end;

	public Arrow(Vector2D start, Vector2D end) {
		this.start = start;
		this.end = end;
	}

	public void draw(Graphics2D g) {

		double length = start.distance(end);
		double headSize = Math.sqrt(2) * length / 5;

		Vector2D arrowVect = end.subtract(start);
		Vector2D uVect = new Vector2D(arrowVect.getX() / length, arrowVect.getY() / length);

		Vector2D vHeadSize = uVect.scalarMultiply(headSize);

		Vector2D headLeft = Vector2DUtils.rotate(vHeadSize, -135).add(end);
		Vector2D headRight = Vector2DUtils.rotate(vHeadSize, 135).add(end);

		g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
		g.drawLine((int) end.getX(), (int) end.getY(), (int) headLeft.getX(), (int) headLeft.getY());
		g.drawLine((int) end.getX(), (int) end.getY(), (int) headRight.getX(), (int) headRight.getY());
	}
}
