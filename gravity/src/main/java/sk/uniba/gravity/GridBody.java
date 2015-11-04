package sk.uniba.gravity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class GridBody {

	private GameBody body;
	private Scale meterScale;
	private Scale pixelScale;
	private double scale;

	private int x;
	private int y;

	private int centerX;
	private int centerY;

	private int size;

	/**
	 * @param body
	 * @param meterScale
	 *            grid unit to meter
	 * @param pixelScale
	 *            grid unit to pixel
	 */
	public GridBody(GameBody body, Scale meterScale, Scale pixelScale) {
		this.body = body;
		this.meterScale = meterScale;
		this.pixelScale = pixelScale;

		// pixel to meter scale
		scale = meterScale.down() * pixelScale.up();

		x = (int) ((body.getCenter().getX() - body.getRadius()) * scale);
		y = (int) ((body.getCenter().getY() - body.getRadius()) * scale);
		centerX = (int) (body.getCenter().getX() * scale);
		centerY = (int) (body.getCenter().getY() * scale);
		size = (int) (body.getSize() * scale);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void drawTrajectory(Graphics2D g) {
		g.setStroke(new BasicStroke((float) pixelScale.up()));

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
		int strokeWidth = size / 20;
		int strokeOffset = strokeWidth / 2;
		int strokeSize = size - strokeOffset * 2;
		g.setStroke(new BasicStroke(strokeWidth));
		g.drawOval(getX() + strokeOffset, getY() + strokeOffset, strokeSize, strokeSize);
	}

	public void drawName(Graphics2D g) {
		int alpha = 255;
		if (getSize() <= pixelScale.up()) {
			double size = (body.getSize() * scale) / pixelScale.up();
			// TODO compensate by zoom factor
			// 1/2(sin(x*pi + 3/2*pi) + 1)
			alpha *= Math.pow(0.5 * (Math.sin(size * Math.PI + 1.5 * Math.PI) + 1), 0.25);
		}
		g.setColor(new Color(255, 255, 255, alpha));
		
		int x = (int) (body.getCenter().getX() * meterScale.down());
		int y = (int) (body.getCenter().getY() * meterScale.down());
		
		g.scale(pixelScale.up(), pixelScale.up());
		g.drawString(body.getName(), x, y);
		g.scale(pixelScale.down(), pixelScale.down());
	}

	public void drawBody(Graphics2D g) {
		drawBody(g, Color.LIGHT_GRAY);
	}

	public void drawBody(Graphics2D g, Color color) {
		if (getSize() > pixelScale.up()) {
			g.setColor(color);
			g.fillOval(getX(), getY(), getSize(), getSize());
		} else {
			// make body visible as a dot, if its too small
			g.setColor(Color.WHITE);
			g.fillOval(getX(), getY(), (int) pixelScale.up(), (int) pixelScale.up());
		}
	}
}
