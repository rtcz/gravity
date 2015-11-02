package sk.uniba.gravity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class CanvasBody {

	private GameBody body;
	private double scale;

	public CanvasBody(GameBody body, double scale) {
		this.body = body;
		this.scale = scale;
	}

	public int getX() {
		double tmp = body.getCenter().getX() - body.getRadius();
		return (int) Math.round(tmp * scale);
	}

	public int getY() {
		double tmp = body.getCenter().getY() - body.getRadius();
		return (int) Math.round(tmp * scale);
	}

	public int getSize() {
		return (int) Math.round(body.getSize() * scale);
	}

	public Color getColor() {
		// TODO involve density
		int colorValue = 255 - (int) Math.log10(body.getRadius()) * 10;
		if (colorValue < 128) {
			colorValue = 128;
		}
		return new Color(colorValue, colorValue, colorValue);
	}

	public int getCenterX() {
		double tmp = body.getCenter().getX();
		return (int) Math.round(tmp * scale);
	}

	public int getCenterY() {
		double tmp = body.getCenter().getY();
		return (int) Math.round(tmp * scale);
	}

	public void drawTrajectory(Graphics2D g) {
		g.setStroke(new BasicStroke(1));

		Color trackColor = Color.ORANGE;
		List<Vector2D> track = body.getTrack();
		for (int i = 0; i + 1 < track.size(); i++) {
			int x1 = (int) (track.get(i).getX() * scale);
			int y1 = (int) (track.get(i).getY() * scale);
			int x2 = (int) (track.get(i + 1).getX() * scale);
			int y2 = (int) (track.get(i + 1).getY() * scale);

			// number of segments is trackSize - 1, current segment is i + 1
			double fraction = (double) (i + 1) / (double) (track.size() - 1);

			int red = (int) (trackColor.getRed() * fraction);
			int green = (int) (trackColor.getGreen() * fraction);
			int blue = (int) (trackColor.getBlue() * fraction);
			int alpha = (int) (255 * fraction);

			g.setColor(new Color(red, green, blue, alpha));
			g.drawLine(x1, y1, x2, y2);
		}
	}

	public void drawSelection(Graphics2D g) {
		int size = getSize();
		g.setColor(Color.CYAN);
		int strokeWidth = size / 10;
		int strokeOffset = strokeWidth / 2;
		int strokeSize = size - strokeOffset * 2;
		g.setStroke(new BasicStroke(strokeWidth));
		g.drawOval(getX() + strokeOffset, getY() + strokeOffset, strokeSize, strokeSize);
	}

	public void drawName(Graphics2D g) {
		int alpha = 255;
		if (getSize() == 0) {
			// TODO refine, use log or something
			alpha *= body.getSize() * scale;
		}
		g.setColor(new Color(255, 255, 255, alpha));
		g.drawString(body.getName(), getCenterX(), getCenterY());
	}

	public void drawBody(Graphics2D g) {
		int size = getSize();
		g.setColor(getColor());
		g.fillOval(getX(), getY(), size, size);
	}
}
