package sk.uniba.gravity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

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
		int trackSize = body.getTrack().size();
		for (int i = 0; i + 1 < trackSize; i++) {
			int x1 = (int) (body.getTrack().get(i).getX() * scale);
			int y1 = (int) (body.getTrack().get(i).getY() * scale);
			int x2 = (int) (body.getTrack().get(i + 1).getX() * scale);
			int y2 = (int) (body.getTrack().get(i + 1).getY() * scale);
			
			// number of segments is trackSize - 1, current segment is i + 1
			double fraction = (double) (i + 1) / (double) (trackSize - 1);
			
			int red = (int) (trackColor.getRed() * fraction);
			int green = (int) (trackColor.getGreen() * fraction);
			int blue = (int) (trackColor.getBlue() * fraction);
			
			g.setColor(new Color(red, green, blue));
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
		g.setColor(Color.WHITE);
		g.drawString(body.getName(), getCenterX(), getCenterY());
	}
	
	public void drawBody(Graphics2D g) {
		int size = getSize();
		g.setColor(getColor());
		g.fillOval(getX(), getY(), size, size);
	}
}
