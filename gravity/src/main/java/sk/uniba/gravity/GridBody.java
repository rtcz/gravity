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

	private Color bodyColor = Color.LIGHT_GRAY;
	private Color trackColor = Color.ORANGE;

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

		x = (int) ((body.getPosition().getX() - body.getRadius()) * scale);
		y = (int) ((body.getPosition().getY() - body.getRadius()) * scale);
		centerX = (int) (body.getPosition().getX() * scale);
		centerY = (int) (body.getPosition().getY() * scale);
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

		Color trackColor = getTrackColor();
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
			// 1/2(sin(x*pi + 3/2*pi) + 1)
			alpha *= Math.pow(0.5 * (Math.sin(size * Math.PI + 1.5 * Math.PI) + 1), 0.25);
			if (alpha == 0) {
				return;
			}
		}
		g.setColor(new Color(255, 255, 255, alpha));

		int x = (int) (body.getPosition().getX() * meterScale.down());
		int y = (int) (body.getPosition().getY() * meterScale.down());

		g.scale(pixelScale.up(), pixelScale.up());
		g.drawString(body.getName(), x, y);
		g.scale(pixelScale.down(), pixelScale.down());
	}

	public void drawBody(Graphics2D g) {
		drawBody(g, getBodyColor(), false);
	}
	
	public void drawBody(Graphics2D g, boolean particleOnly) {
		drawBody(g, getBodyColor(), particleOnly);
	}

	public void drawBody(Graphics2D g, Color color, boolean particleOnly) {
		if (particleOnly || getSize() < pixelScale.up()) {
			// make body visible as a pixel, if using particle mode or its smaller then pixel
			g.setColor(Color.WHITE);
			g.fillRect(getCenterX(), getCenterY(), (int) pixelScale.up(), (int) pixelScale.up());
		} else {
			g.setColor(color);
			g.fillOval(getX(), getY(), getSize(), getSize());
		}
	}

	public Color getBodyColor() {
		return bodyColor;
	}

	public void setBodyColor(Color bodyColor) {
		this.bodyColor = bodyColor;
	}

	public Color getTrackColor() {
		return trackColor;
	}

	public void setTrackColor(Color trackColor) {
		this.trackColor = trackColor;
	}

}
