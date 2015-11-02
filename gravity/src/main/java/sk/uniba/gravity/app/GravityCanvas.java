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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
import sk.uniba.gravity.shape.Arrow;
import sk.uniba.gravity.shape.Cross;

public class GravityCanvas extends GameCanvas {

	private static final long serialVersionUID = -4662105822647187214L;

	private GameManager mng;

	private List<GameBody> bodies = new ArrayList<GameBody>();

	private Vector2D absRefPoint = new Vector2D(0, 0);
	private Vector2D relRefPoint = new Vector2D(0, 0);
	private double refScale = 1e-6;

	private JCheckBox trackCheck = new JCheckBox("Show tracks");
	private JCheckBox trackLimitCheck = new JCheckBox("Limit tracks");
	private JCheckBox showNameCheck = new JCheckBox("Show names");

	private JLabel newBodyLabel = new JLabel("New Body");
	private JTextField newBodyName = new JTextField();

	private JLabel densityLabel = new JLabel("Density");
	private JIntegerField densityField = new JIntegerField();
	private JLabel densityUnitLabel = new JLabel("kg/m^3");

	private JLabel radiusLabel = new JLabel("Radius");
	private JIntegerField radiusField = new JIntegerField();
	private JLabel radiusUnitLabel = new JLabel("km");

	private SpeedSlider slider = new SpeedSlider();

	protected boolean isNewBodyAction;
	protected Vector2D firstDragPos;
	protected Vector2D lastDragPos;

	public GravityCanvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);

		trackCheck.setOpaque(false);
		trackCheck.setFocusable(false);
		trackCheck.setForeground(Color.WHITE);

		trackLimitCheck.setOpaque(false);
		trackLimitCheck.setFocusable(false);
		trackLimitCheck.setForeground(Color.WHITE);

		showNameCheck.setOpaque(false);
		showNameCheck.setFocusable(false);
		showNameCheck.setForeground(Color.WHITE);

		slider.setFocusable(false);

		newBodyLabel.setForeground(Color.WHITE);
		newBodyName.setColumns(10);
		newBodyName.setText("Planet X");

		densityLabel.setForeground(Color.WHITE);
		densityField.setColumns(10);
		densityField.setText("1000");
		densityUnitLabel.setForeground(Color.WHITE);

		radiusLabel.setForeground(Color.WHITE);
		radiusField.setColumns(10);
		radiusField.setText("1000");
		radiusUnitLabel.setForeground(Color.WHITE);
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

		bottomPanel.add(newBodyLabel);
		bottomPanel.add(newBodyName);

		bottomPanel.add(densityLabel);
		bottomPanel.add(densityField);
		bottomPanel.add(densityUnitLabel);

		bottomPanel.add(radiusLabel);
		bottomPanel.add(radiusField);
		bottomPanel.add(radiusUnitLabel);

		add(bottomPanel, BorderLayout.SOUTH);
		bodies = GameBodyFactory.createSolSystem();
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
					// gravitation F=G*m1*m2/r^2
					double dist12 = b1.getCenter().distance(b2.getCenter());
					double gForce = (GameConstants.G_CONSTANT * mass1 * mass2) / Math.pow(dist12, 2);

					// momentum p=F*t (force is applied for n seconds)
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
			// d=v*t
			Vector2D distance = body.getVelocity().scalarMultiply(dSeconds);
			Vector2D newCenter = body.getCenter().add(distance);
			body.setCenter(newCenter);

			// trajectory
			if (trackCheck.isSelected()) {
				if (body.getTrack().isEmpty()) {
					body.addTrackPoint(newCenter);
				} else {
					// Vector2D prevPos = body.getLastTrackPoint();
					// if (prevPos.distance(newCenter) >
					// body.getVelocity().getNorm()) {
					// body.addTrackPoint(newCenter);
					// } else {
					// body.addTrackPoint(newCenter, true);
					// }
					body.addTrackPoint(newCenter);
				}
				if (trackLimitCheck.isSelected()) {
					while (body.getTrack().size() > GameConstants.MAX_TRACK_SEGMENTS) {
						body.getTrack().remove(0);
					}
				}
			} else {
				body.clearTrack();
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

		// body trajectories
		for (GameBody body : bodies) {
			CanvasBody cBody = new CanvasBody(body, refScale);
			if (trackCheck.isSelected()) {
				cBody.drawTrajectory(gg);
			}
		}

		// bodies
		for (GameBody body : bodies) {
			CanvasBody cBody = new CanvasBody(body, refScale);
			cBody.drawBody(gg);
			if (body.isSelected()) {
				cBody.drawSelection(gg);
			}
			if (showNameCheck.isSelected()) {
				cBody.drawName(gg);
			}
			if (cBody.getSize() <= 1) {
				// make body visible as a dot, if its too small
				gg.setColor(Color.WHITE);
				gg.drawRect(cBody.getX(), cBody.getY(), 1, 1);
			}
		}

		// new body vector
		if (isNewBodyAction && !lastDragPos.equals(firstDragPos)) {
			g.setColor(Color.RED);

			Arrow bodyArrow = new Arrow(lastDragPos, firstDragPos, 10);
			bodyArrow.draw(g);

			// g.drawString(str, lastDragPos.getX(), lastDragPos.getY());
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

		g.drawString("Body count " + bodies.size(), 10, 100);

		// reference cross
		g.setColor(Color.WHITE);
		Cross reference = new Cross(absRefPoint, 10);
		reference.draw(g);
	}

	public void addBody() {
		if (densityField.getValue() <= 0 || radiusField.getValue() <= 0) {
			return;
		}
		GameBody newBody = new GameBody();

		Vector2D center = firstDragPos.subtract(absRefPoint).scalarMultiply(1 / refScale);
		newBody.setCenter(center);

		Vector2D velocity = firstDragPos.subtract(lastDragPos).scalarMultiply(1e-6 / refScale);
		newBody.setVelocity(velocity);

		newBody.setName(newBodyName.getText());
		newBody.setDensity(densityField.getValue());
		newBody.setRadius(radiusField.getValue() * 1e3);

		bodies.add(newBody);
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
