package sk.uniba.gravity.app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.Body;
import sk.uniba.gravity.GameBody;
import sk.uniba.gravity.Length;
import sk.uniba.gravity.Vector2DUtils;
import sk.uniba.gravity.game.GameCanvas;
import sk.uniba.gravity.game.GameLoop;
import sk.uniba.gravity.game.GameManager;
import sk.uniba.gravity.shape.Arrow;
import sk.uniba.gravity.shape.Cross;

public class Gravity extends GameCanvas implements GameLoop {

	private static final long serialVersionUID = -4662105822647187214L;

	private GameManager mng;

	private List<GameBody> bodies = new ArrayList<GameBody>();

	private Vector2D refPoint;
	private Vector2D bodyRefPoint;
	private double refSize = 1e-6;

	public Gravity() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g.create());
	}

	@Override
	public void init(GameManager mng) {
		this.mng = mng;
		refPoint = new Vector2D(0, 0);

		GameBody sun = new GameBody(new Vector2D(0, 0), 696.342e6);
		sun.setDensity(1408);
		bodies.add(sun);

		// TEMP CODE
		GameBody earth = new GameBody(new Vector2D(1.496e11, 0), 6.371e6);
		earth.setDensity(5515);
		earth.setVelocity(new Vector2D(0, -29800));
		bodies.add(earth);

		GameBody moon = new GameBody(new Vector2D(1.496e11 + 384.4e6, 0), 1.737e6);
		moon.setDensity(3346);
		moon.setVelocity(new Vector2D(0, -1023 + -29800));
		bodies.add(moon);
	}

	@Override
	public void update(double delta) {
		double dSeconds = delta / 1_000d;
		for (int i = 0; i < bodies.size(); i++) {
			for (int j = 0; j < bodies.size(); j++) {
				if (i > j) {

					Body b1 = bodies.get(i);
					Body b2 = bodies.get(j);
					double mass1 = b1.getMass();
					double mass2 = b2.getMass();

					// gravitational force by Newton's law of universal
					// gravitation F=G.m1.m2/r^2
					double dist12 = b1.getCenter().distance(b2.getCenter());
					double gForce = (Body.G_CONSTANT * mass1 * mass2) / Math.pow(dist12, 2);

					// momentum p=F.t (force is applied one second)
					double momentum = gForce * dSeconds;

					// body velocities v=p/m
					double sVelocity1 = momentum / mass1;
					double sVelocity2 = momentum / mass2;

					// direction vectors
					Vector2D uVect12 = Vector2DUtils.unit(b2.getCenter().subtract(b1.getCenter()));
					Vector2D uVect21 = uVect12.scalarMultiply(-1);

					Vector2D vVelocity1 = uVect12.scalarMultiply(sVelocity1);
					Vector2D vVelocity2 = uVect21.scalarMultiply(sVelocity2);

					b1.setVelocity(b1.getVelocity().add(vVelocity1));
					b2.setVelocity(b2.getVelocity().add(vVelocity2));
				}
			}
		}
		for (Body body : bodies) {
			Vector2D tVelocity = body.getVelocity().scalarMultiply(dSeconds);
			Vector2D newCenter = body.getCenter().add(tVelocity);
			body.setCenter(newCenter);

			// trajectory
			if (body.getTrajectory().isEmpty()) {
				body.addTrajectoryPoint(newCenter);
			} else {
				Vector2D prevPos = body.getLastTrajectoryPoint();
				if (prevPos.distance(newCenter) > body.getVelocity().getNorm()) {
					body.addTrajectoryPoint(newCenter);
				} else {
					body.addTrajectoryPoint(newCenter, true);
				}
			}
		}
	}

	@Override
	public void render() {
		repaint();
	}

	private void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Graphics2D gg = (Graphics2D) g.create();
		// RELATIVE COORDINATES
		gg.translate(refPoint.getX(), refPoint.getY());
		// RELATIVE SIZE
		// gg.scale(refSize, refSize);
		
		for (GameBody body : bodies) {
			if (body.isSelected()) {
				// TODO 
				Vector2D move = bodyRefPoint.subtract(body.getCenter()).scalarMultiply(getRefSize());
				gg.translate(move.getX(), move.getY());
				break;
			}
		}

		for (GameBody body : bodies) {
			// the body
			// TODO create PixelBody object
			Vector2D pixelPos = body.getCenter().scalarMultiply(refSize);
			int size = (int) (body.getSize() * refSize);
			int x = (int) (body.getTopLeftX() * refSize);
			int y = (int) (body.getTopLeftY() * refSize);
			gg.setColor(body.getColor());
			gg.fillOval(x, y, size, size);
			
			if (body.isSelected()) {
				gg.setColor(Color.CYAN);
				gg.setStroke(new BasicStroke(size / 10));
				gg.drawOval(x, y, size, size);
			}

			// trajectory
			gg.setStroke(new BasicStroke(1));
			gg.setColor(Color.ORANGE);

			int[] xPoints = new int[body.getTrajectory().size()];
			int[] yPoints = new int[body.getTrajectory().size()];
			for (int i = 0; i < body.getTrajectory().size(); i++) {
				xPoints[i] = (int) (body.getTrajectory().get(i).getX() * refSize);
				yPoints[i] = (int) (body.getTrajectory().get(i).getY() * refSize);
			}
			gg.drawPolyline(xPoints, yPoints, body.getTrajectory().size());

			// velocity indicators
			double pos = (body.getVelocity().getX() > 0) ? 1 : -1;
			double nVelX = Math.log10(Math.abs(body.getVelocity().getX()) + 1) * pos * 10;

			pos = (body.getVelocity().getY() > 0) ? 1 : -1;
			double nVelY = Math.log10(Math.abs(body.getVelocity().getY()) + 1) * pos * 10;

			Vector2D vSize = new Vector2D(nVelX, nVelY);
			Vector2D vSizeX = new Vector2D(vSize.getX(), 0);
			Vector2D vSizeY = new Vector2D(0, vSize.getY());

			Arrow velocity = new Arrow(pixelPos, pixelPos.add(vSize), 10);
			Arrow xVelocity = new Arrow(pixelPos, pixelPos.add(vSizeX), 10);
			Arrow yVelocity = new Arrow(pixelPos, pixelPos.add(vSizeY), 10);

			gg.setColor(Color.GREEN);
			velocity.draw(gg);
			gg.setColor(Color.BLUE);
			xVelocity.draw(gg);
			gg.setColor(Color.RED);
			yVelocity.draw(gg);
		}

		// simulation info
		g.setColor(Color.WHITE);
		int fps = mng.getFps();
		g.drawString("FPS " + fps, 10, 20);

		Date date = new Date(mng.getGameTime());
		String dateValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
		g.drawString("TIME " + dateValue, 10, 40);

		Length length = new Length(1 / refSize);
		g.drawString("SCALE 1px = " + length, 10, 60);

		// reference cross
		g.setColor(Color.WHITE);
		Cross reference = new Cross(refPoint, 10);
		reference.draw(g);
	}

	protected Vector2D getRefPoint() {
		return refPoint;
	}

	protected void setRefPoint(Vector2D refPoint) {
		this.refPoint = refPoint;
	}
	
	protected Vector2D getBodyRefPoint() {
		return bodyRefPoint;
	}

	protected void setBodyRefPoint(Vector2D bodyRefPoint) {
		this.bodyRefPoint = bodyRefPoint;
	}

	protected double getRefSize() {
		return refSize;
	}

	protected void setRefSize(double refSize) {
		this.refSize = refSize;
	}
	
	protected List<GameBody> getBodyList() {
		return bodies;
	}
}
