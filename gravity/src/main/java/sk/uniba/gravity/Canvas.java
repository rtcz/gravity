package sk.uniba.gravity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Canvas extends JPanel implements InteractiveGame {

	private static final long serialVersionUID = -4662105822647187214L;

	public static final double ZOOM_FACTOR = 1.1;
	public static final double MIN_ZOOM = 1e-12;
	public static final double MAX_ZOOM = 1;

	private GameManager mng;

	private List<Body> bodies = new ArrayList<Body>();

	private Vector2D refPoint;
	private double refSize = 1e-6;

	public Canvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g.create());
	}

	@Override
	public void init(GameManager mng) {
		this.mng = mng;
		refPoint = new Vector2D(0, 0);

		// TEMP CODE
		Body earth = new Body(new Vector2D(100e6, 100e6), 6.371e6);
		earth.setDensity(5515);
		// earth.setCenter(refPoint.subtract(earth.getCenter()));
		// earth.setCenter(earth.getCenter().subtract(refPoint));
		bodies.add(earth);

		Body moon = new Body(new Vector2D(372e6, 372e6), 1.737e6);
		moon.setDensity(3346);
		moon.setVelocity(1023, new Vector2D(0.70711, -0.70711));
		// moon.setCenter(moon.getCenter().subtract(refPoint));
		// moon.setCenter(refPoint.subtract(moon.getCenter()));
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
					double momentum = gForce * 1;

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
			body.addTrajectoryPoint(newCenter);
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

		for (Body body : bodies) {
			gg.setColor(body.getColor());

			// the body
			// TODO create PixelBody object
			Vector2D pixelPos = body.getCenter().scalarMultiply(refSize);
			int size = (int) (body.getSize() * refSize);
			int x = (int) (body.getX() * refSize);
			int y = (int) (body.getY() * refSize);
			gg.fillOval(x, y, size, size);
			
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
			double nVelX = body.getVelocity().getX() * 0.1;
			double nVelY = body.getVelocity().getY() * 0.1;
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		lastDragPos = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	Vector2D lastDragPos;

	@Override
	public void mouseDragged(MouseEvent e) {
		if (lastDragPos == null) {
			lastDragPos = new Vector2D(e.getX(), e.getY());
		}
		Vector2D newDragPos = new Vector2D(e.getX(), e.getY());
		Vector2D move = lastDragPos.subtract(newDragPos);

		refPoint = refPoint.subtract(move);
		lastDragPos = newDragPos;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		double zoom = 1;
		if (e.getWheelRotation() > 0) {
			zoom = ZOOM_FACTOR;
		} else {
			zoom = 1 / ZOOM_FACTOR;
		}

		// max zoom limit
		if (refSize * zoom > MAX_ZOOM) {
			zoom = MAX_ZOOM / refSize;
		}
		// min zoom limit
		if (refSize * zoom < MIN_ZOOM) {
			zoom = MIN_ZOOM / refSize;
		}

		Vector2D mousePos = new Vector2D(e.getX(), e.getY());
		Vector2D mouseRefPos = refPoint.subtract(mousePos);
		Vector2D zoomedRefPos = mouseRefPos.scalarMultiply(zoom);
		Vector2D move = mouseRefPos.subtract(zoomedRefPos);

		refSize *= zoom;
		refPoint = refPoint.subtract(move);
	}

}
