package sk.uniba.gravity.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.Body;
import sk.uniba.gravity.GameBody;
import sk.uniba.gravity.GameConstants;
import sk.uniba.gravity.Length;
import sk.uniba.gravity.PixelBody;
import sk.uniba.gravity.Vector2DUtils;
import sk.uniba.gravity.game.GameCanvas;
import sk.uniba.gravity.game.GameManager;
import sk.uniba.gravity.game.ui.SpeedSlider;
import sk.uniba.gravity.shape.Cross;

public class Gravity extends GameCanvas {

	private static final long serialVersionUID = -4662105822647187214L;

	private GameManager mng;

	private List<GameBody> bodies = new ArrayList<GameBody>();

	private Vector2D absRefPoint;
	private Vector2D relRefPoint;
	private double refScale = 1e-6;
	
	private JCheckBox trackCheck = new JCheckBox("Show track");
	private JCheckBox trackLimitCheck = new JCheckBox("Limit track");
	
	private SpeedSlider slider = new SpeedSlider();

	public Gravity() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);
		
		trackCheck.setOpaque(false);
		trackCheck.setForeground(Color.WHITE);
		
		trackLimitCheck.setOpaque(false);
		trackLimitCheck.setForeground(Color.WHITE);
	}

	@Override
	public void init(GameManager mng) {
		this.mng = mng;
		
		// UI
		slider.addChangeListener(event -> {
			mng.setSpeedMultiplier(Math.pow(10, slider.getValue()));
		});
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new FlowLayout());
		
		bottomPanel.add(trackCheck);
		bottomPanel.add(trackLimitCheck);
		bottomPanel.add(slider);

		add(bottomPanel, BorderLayout.SOUTH);
		
		

		// GAME STATE
		absRefPoint = new Vector2D(0, 0);
		
		GameBody sun = new GameBody(new Vector2D(0, 0), 696.342e6);
		sun.setDensity(1408);
		bodies.add(sun);

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
					double gForce = (GameConstants.G_CONSTANT * mass1 * mass2) / Math.pow(dist12, 2);

					// momentum p=F.t (force is applied for n seconds)
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
			if (trackCheck.isSelected()) {
				if (body.getTrack().isEmpty()) {
					body.addTrackPoint(newCenter);
				} else {
					Vector2D prevPos = body.getLastTrackPoint();
					if (prevPos.distance(newCenter) > body.getVelocity().getNorm()) {
						body.addTrackPoint(newCenter);
					} else {
						body.addTrackPoint(newCenter, true);
					}
				}	
			} else {
				body.clearTrack();
			}
			if (trackLimitCheck.isSelected()) {
				while (body.getTrack().size() > GameConstants.MAX_TRACK_SEGMENTS) {
					body.getTrack().remove(0);	
				}
			}
		}
	}
	
	public void bodySelected(Body selectedBody) {
		if (selectedBody == null) {
			relRefPoint = null;
		} else {
			relRefPoint = selectedBody.getCenter();
		}
	}

	@Override
	public void render() {
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render((Graphics2D) g.create());
	}

	private void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Graphics2D gg = (Graphics2D) g.create();

		for (GameBody body : bodies) {
			if (body.isSelected()) {
				Vector2D move = relRefPoint.subtract(body.getCenter());
				absRefPoint = absRefPoint.add(move.scalarMultiply(getRefScale()));
				relRefPoint = body.getCenter();
			}
		}

		// RELATIVE COORDINATES
		gg.translate(absRefPoint.getX(), absRefPoint.getY());

		for (GameBody body : bodies) {
			// the body
			PixelBody pBody = new PixelBody(body, refScale);
			pBody.draw(gg);
		}

		// simulation info
		g.setColor(Color.WHITE);
		g.drawString("FPS " + mng.getFps(), 10, 20);
		g.drawString("UPS " + mng.getUps(), 10, 40);

		Date date = new Date(mng.getGameTime());
		String dateValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
		g.drawString("TIME " + dateValue, 10, 60);

		Length length = new Length(1 / refScale);
		g.drawString("SCALE 1px = " + length, 10, 80);

		// reference cross
		g.setColor(Color.WHITE);
		Cross reference = new Cross(absRefPoint, 10);
		reference.draw(g);
	}

	protected Vector2D getAbsRefPoint() {
		return absRefPoint;
	}

	protected void setAbsRefPoint(Vector2D refPoint) {
		this.absRefPoint = refPoint;
	}
	
//	protected Vector2D getBodyRefPoint() {
//		return relRefPoint;
//	}
//
//	protected void setBodyRefPoint(Vector2D bodyRefPoint) {
//		this.relRefPoint = bodyRefPoint;
//	}

	protected double getRefScale() {
		return refScale;
	}

	protected void setRefScale(double refScale) {
		this.refScale = refScale;
	}
	
	protected List<GameBody> getBodyList() {
		return bodies;
	}
}
