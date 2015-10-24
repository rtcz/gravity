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
import sk.uniba.gravity.Size;
import sk.uniba.gravity.CanvasBody;
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
	
	private JCheckBox trackCheck = new JCheckBox("Show tracks");
	private JCheckBox trackLimitCheck = new JCheckBox("Limit tracks");
	private JCheckBox showNameCheck = new JCheckBox("Show names");
	
	private SpeedSlider slider = new SpeedSlider();

	public Gravity() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);
		
		trackCheck.setOpaque(false);
		trackCheck.setForeground(Color.WHITE);
		
		trackLimitCheck.setOpaque(false);
		trackLimitCheck.setForeground(Color.WHITE);
		
		showNameCheck.setOpaque(false);
		showNameCheck.setForeground(Color.WHITE);
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
		bottomPanel.add(showNameCheck);
		bottomPanel.add(slider);

		add(bottomPanel, BorderLayout.SOUTH);

		// GAME STATE
		absRefPoint = new Vector2D(0, 0);
		
		GameBody sun = new GameBody("Sun");
		sun.setRadius(696.342e6);
		sun.setDensity(1408);
		sun.setCenter(new Vector2D(+6.442860875172776e8, +2.748913433991132e8));
		sun.setVelocity(new Vector2D(+3.151184859677259e0, -9.185194654328578e0));
		bodies.add(sun);
		
		GameBody mercury = new GameBody("Mercury");
		mercury.setRadius(2.440e6);
		mercury.setDensity(5427);
		mercury.setCenter(new Vector2D(+3.901558897485410e10, +2.904514484583830e10));
		mercury.setVelocity(new Vector2D(+3.879081706909912e4, -4.110223749127960e4));
		bodies.add(mercury);
		
		GameBody venus = new GameBody("Venus");
		venus.setRadius(6.052e6);
		venus.setDensity(5204);
		venus.setCenter(new Vector2D(-4.733027208737938e9, -1.083207490477231e11));
		venus.setVelocity(new Vector2D(-3.473833166185593e4, +1.856561942705289e3));
		bodies.add(venus);

		GameBody earth = new GameBody("Earth");
		earth.setRadius(6.371e6);
		earth.setDensity(5515);
		earth.setCenter(new Vector2D(-2.636314250687937e10, +1.448755934863529e11));
		earth.setVelocity(new Vector2D(+2.977359332571185e4, +5.558856867535258e3));
		bodies.add(earth);

		GameBody moon = new GameBody("Moon");
		moon.setRadius(1.737e6);
		moon.setDensity(3344);
		moon.setCenter(new Vector2D(-2.674751557311480e10, +1.448001055998542e11));
		moon.setVelocity(new Vector2D(+2.951864258933297e4, +6.520608815172885e3));
		moon.setName("Moon");
		bodies.add(moon);
		
		GameBody mars = new GameBody("Mars");
		mars.setRadius(3.389e6);
		mars.setDensity(3933);
		mars.setCenter(new Vector2D(+1.990267404151246e11, +7.450413194711113e10));
		mars.setVelocity(new Vector2D(+7.560777278212286e3, -2.477045044227772e4));
		bodies.add(mars);
		
		GameBody jupiter = new GameBody("Jupiter");
		jupiter.setRadius(69.911e6);
		jupiter.setDensity(1326);
		jupiter.setCenter(new Vector2D(-7.490058923334916e11, -3.198963469183056e11));
		jupiter.setVelocity(new Vector2D(-4.979372438187077e3, +1.140864406048834e4));
		bodies.add(jupiter);
		
		GameBody saturn = new GameBody("Saturn");
		saturn.setRadius(58.232e6);
		saturn.setDensity(687);
		saturn.setCenter(new Vector2D(+1.0834503736344e12, +8.513589639615979e11));
		saturn.setVelocity(new Vector2D(+6.490272474127367e3, -7.575137301499483e3));
		bodies.add(saturn);
		
		GameBody uranus = new GameBody("Uranus");
		uranus.setRadius(25.362e6);
		uranus.setDensity(1318);
		uranus.setCenter(new Vector2D(-2.723971684699241e12, -2.891270404270630e11));
		uranus.setVelocity(new Vector2D(-6.680837052483795e2, +7.089916083324248e3));
		bodies.add(uranus);
		
		GameBody neptune = new GameBody("Neptune");
		neptune.setRadius(24.624);
		neptune.setDensity(1638);
		neptune.setCenter(new Vector2D(-2.327426565170483e12, -3.890812806779972e12));
		neptune.setVelocity(new Vector2D(-4.630808049976979e3, +2.758234623717156e3));
		bodies.add(neptune);
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
			}
			if (!trackCheck.isSelected() || trackLimitCheck.isSelected()) {
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
			CanvasBody cBody = new CanvasBody(body, refScale);
			if (trackCheck.isSelected()) {
				cBody.drawTrajectory(gg);
			}
			cBody.drawBody(gg);
			if (body.isSelected()) {
				cBody.drawSelection(gg);	
			}
			if (showNameCheck.isSelected()) {
				cBody.drawName(gg);	
			}
		}

		// simulation info
		g.setColor(Color.WHITE);
		g.drawString("FPS " + mng.getFps(), 10, 20);
		g.drawString("UPS " + mng.getUps(), 10, 40);

		Date date = new Date(mng.getGameTime());
		String dateValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
		g.drawString("TIME " + dateValue, 10, 60);

		Size size = new Size(1 / refScale);
		g.drawString("SCALE 1px = " + size, 10, 80);

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
