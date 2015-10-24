package sk.uniba.gravity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class PixelBody {

	private GameBody body;
	private double scale;

	public PixelBody(GameBody body, double scale) {
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
	
//	public int getCenterX() {
//		double tmp = body.getCenter().getX();
//		return (int) Math.round(tmp * scale);
//	}
//
//	public int getCenterY() {
//		double tmp = body.getCenter().getY();
//		return (int) Math.round(tmp * scale);
//	}

	public void draw(Graphics2D g) {
		int size = getSize();
		
		g.setColor(getColor());
		g.fillOval(getX(), getY(), size, size);

		if (body.isSelected()) {
			g.setColor(Color.CYAN);
			int strokeSize = size / 10;
			g.setStroke(new BasicStroke(strokeSize));
			int strokeOffset = strokeSize / 2;
			g.drawOval(getX() + strokeOffset, getY() + strokeOffset, size - strokeOffset * 2, size - strokeOffset * 2);
		}

		// trajectory
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
		
		
		
		// velocity indicators
//		double signX = (body.getVelocity().getX() > 0) ? 1 : -1;
//		double nVelX = Math.log10(Math.abs(body.getVelocity().getX()) + 1) * signX * 10;
//
//		double signY = (body.getVelocity().getY() > 0) ? 1 : -1;
//		double nVelY = Math.log10(Math.abs(body.getVelocity().getY()) + 1) * signY * 10;
//
//		Vector2D vSize = new Vector2D(nVelX, nVelY);
//		Vector2D vSizeX = new Vector2D(vSize.getX(), 0);
//		Vector2D vSizeY = new Vector2D(0, vSize.getY());
//
//		Arrow velocity = new Arrow(pixelPos, pixelPos.add(vSize), 10);
//		Arrow xVelocity = new Arrow(pixelPos, pixelPos.add(vSizeX), 10);
//		Arrow yVelocity = new Arrow(pixelPos, pixelPos.add(vSizeY), 10);
//
//		g.setColor(Color.GREEN);
//		velocity.draw(g);
//		g.setColor(Color.BLUE);
//		xVelocity.draw(g);
//		g.setColor(Color.RED);
//		yVelocity.draw(g);
	}
}
