package sk.uniba.gravity.shape;

import java.awt.Graphics2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.Vector2DUtils;

public class Arrow {

	private Vector2D start;
	private Vector2D end;
	private double headSize;

	public Arrow(Vector2D start, Vector2D end, double headSize) {
		this.start = start;
		this.end = end;
		this.headSize = headSize;
	}

	public void draw(Graphics2D g) {

		Vector2D arrowVect = end.subtract(start);
		Vector2D uVect = Vector2DUtils.unit(arrowVect);

		Vector2D vHeadSize = uVect.scalarMultiply(headSize);

		Vector2D headLeft = Vector2DUtils.rotate(vHeadSize, -135).add(end);
		Vector2D headRight = Vector2DUtils.rotate(vHeadSize, 135).add(end);

		g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
		g.drawLine((int) end.getX(), (int) end.getY(), (int) headLeft.getX(), (int) headLeft.getY());
		g.drawLine((int) end.getX(), (int) end.getY(), (int) headRight.getX(), (int) headRight.getY());
	}
}
