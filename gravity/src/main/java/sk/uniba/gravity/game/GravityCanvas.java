package sk.uniba.gravity.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.bhtree.BHTQuadrant;
import sk.uniba.gravity.body.Body;
import sk.uniba.gravity.body.GameBody;
import sk.uniba.gravity.body.GameBodyFactory;
import sk.uniba.gravity.body.GridBody;
import sk.uniba.gravity.commons.Scale;
import sk.uniba.gravity.commons.Size;
import sk.uniba.gravity.game.ui.JIntegerField;
import sk.uniba.gravity.game.ui.SpeedSlider;
import sk.uniba.gravity.shape.Arrow;
import sk.uniba.gravity.shape.Circle;
import sk.uniba.gravity.shape.Cross;
import sk.uniba.gravity.utils.Benchmark;
import sk.uniba.gravity.utils.Vector2DUtils;

public class GravityCanvas extends GameCanvas {

	private static final long serialVersionUID = -4662105822647187214L;

	public static final double RADIUS_FIELD_MULTIPLIER = 1e3;

	public enum DiskRotation {
		NONE, DIFFERENTIAL
	}

	public enum DiskDistro {
		UNIFORM, SQUARE, NORMAL
	}

	private GameFrame mng;

	private final List<GameBody> bodies = new ArrayList<GameBody>();

	private BHTQuadrant tree;

	private Vector2D absRefPoint = new Vector2D(0, 0);
	private Vector2D relRefPoint = new Vector2D(0, 0);

	private final Scale meterScale = new Scale(GameConstants.METER_SCALE);
	private final Scale pixelScale = new Scale(GameConstants.PIXEL_SCALE);

	private JButton solSystem = new JButton("Sol System");

	private JButton protoDisk = new JButton("Create");

	private JLabel rotationLabel = new JLabel("Rotation");
	private JComboBox<DiskRotation> diskRotation = new JComboBox<DiskRotation>(DiskRotation.values());

	private JLabel distroLabel = new JLabel("Distribution");
	private JComboBox<DiskDistro> diskDistro = new JComboBox<DiskDistro>(DiskDistro.values());

	private JPanel protoGroup = new JPanel();

	private JButton clear = new JButton("Clear");

	private JCheckBox showTreeCheck = new JCheckBox("Show BH Tree");
	private JCheckBox particleModeCheck = new JCheckBox("Particle Mode");
	private JCheckBox showTrackCheck = new JCheckBox("Show Tracks");
	private JCheckBox trackLimitCheck = new JCheckBox("Limit Tracks");
	private JCheckBox showNameCheck = new JCheckBox("Show Names");

	private JLabel newBodyLabel = new JLabel("Title");
	private JTextField newBodyName = new JTextField();

	private JLabel densityLabel = new JLabel("Density");
	private JIntegerField densityField = new JIntegerField();
	private JLabel densityUnitLabel = new JLabel("kg/m^3");

	private JLabel radiusLabel = new JLabel("Radius");
	private JIntegerField radiusField = new JIntegerField();
	private JLabel radiusUnitLabel = new JLabel("km");

	private JPanel newBodyGroup = new JPanel();

	private SpeedSlider slider = new SpeedSlider();

	protected boolean isNewBodyAction;
	protected Vector2D firstDragPos;
	protected Vector2D lastDragPos;

	private double deltaTime = 1;

	public GravityCanvas() {
		setDoubleBuffered(true);
		setFocusable(true);
		setBackground(Color.BLACK);

		// panel.setBorder(BorderFactory.createTitledBorder(name));

		showTreeCheck.setOpaque(false);
		showTreeCheck.setForeground(Color.WHITE);

		particleModeCheck.setOpaque(false);
		particleModeCheck.setForeground(Color.WHITE);

		showTrackCheck.setOpaque(false);
		showTrackCheck.setForeground(Color.WHITE);

		trackLimitCheck.setOpaque(false);
		trackLimitCheck.setForeground(Color.WHITE);

		showNameCheck.setOpaque(false);
		showNameCheck.setForeground(Color.WHITE);

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

		newBodyGroup.setBorder(getBorder("New Body"));
		newBodyGroup.setForeground(Color.WHITE);
		newBodyGroup.setOpaque(false);

		rotationLabel.setForeground(Color.WHITE);
		distroLabel.setForeground(Color.WHITE);

		protoGroup.setBorder(getBorder("Protodisk"));
		protoGroup.setForeground(Color.WHITE);
		protoGroup.setOpaque(false);

		// addContents();
	}

	private Border getBorder(String title) {
		return BorderFactory.createTitledBorder(null, title, TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null,
				Color.WHITE);
	}

	private void addContents() {
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(0, 100));
		topPanel.setOpaque(false);

		topPanel.add(showTreeCheck);
		topPanel.add(particleModeCheck);

		topPanel.add(showTrackCheck);
		// topPanel.add(trackLimitCheck);
		topPanel.add(showNameCheck);

		newBodyGroup.add(newBodyLabel);
		newBodyGroup.add(newBodyName);
		newBodyGroup.add(Box.createHorizontalStrut(10));
		newBodyGroup.add(densityLabel);
		newBodyGroup.add(densityField);
		newBodyGroup.add(densityUnitLabel);
		newBodyGroup.add(Box.createHorizontalStrut(10));
		newBodyGroup.add(radiusLabel);
		newBodyGroup.add(radiusField);
		newBodyGroup.add(radiusUnitLabel);
		topPanel.add(newBodyGroup);

		JPanel rightPanel = new JPanel();
		rightPanel.setOpaque(false);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);

		bottomPanel.add(solSystem);

		protoGroup.add(protoDisk);
		protoGroup.add(Box.createHorizontalStrut(10));
		protoGroup.add(rotationLabel);
		protoGroup.add(diskRotation);
		protoGroup.add(Box.createHorizontalStrut(10));
		protoGroup.add(distroLabel);
		protoGroup.add(diskDistro);

		bottomPanel.add(protoGroup);

		bottomPanel.add(clear);

		bottomPanel.add(slider);

		JPanel leftPanel = new JPanel();
		leftPanel.setOpaque(false);

		add(topPanel, BorderLayout.NORTH);
		add(rightPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);
		add(leftPanel, BorderLayout.WEST);
	}

	@Override
	public void init(GameFrame mng) {
		addContents();
		this.mng = mng;

		particleModeCheck.addChangeListener(event -> {
			boolean value = particleModeCheck.isSelected();
			if (value) {
				showTrackCheck.setSelected(false);
				trackLimitCheck.setSelected(false);
				showNameCheck.setSelected(false);
			}
			showTrackCheck.setEnabled(!value);
			trackLimitCheck.setEnabled(!value);
			showNameCheck.setEnabled(!value);
		});

		showTrackCheck.addChangeListener(event -> {
			boolean value = showTrackCheck.isSelected();
			if (!value) {
				synchronized (this) {
					for (Body body : bodies) {
						body.clearTrack();
					}
				}
			}
		});

		slider.addChangeListener(event -> {
			double value = Math.pow(10, slider.getValue());
			mng.setSpeedMultiplier((int) Math.round(value));
		});
		solSystem.setMargin(new Insets(0, 0, 0, 0));
		solSystem.addActionListener(e -> {
			synchronized (this) {
				bodies.addAll(GameBodyFactory.createSolSystem());
			}
		});

		protoDisk.setMargin(new Insets(0, 0, 0, 0));
		protoDisk.addActionListener(e -> {
			Vector2D scrCenter = new Vector2D(getWidth() / 2, getHeight() / 2);
			Vector2D center = scrCenter.subtract(absRefPoint).scalarMultiply(getMeterScale().up());
			double radius = Math.min(getWidth(), getHeight()) / 2 * getMeterScale().up();
			Circle disk = new Circle(center, radius);
			synchronized (this) {
				double bodyRadius = radiusField.getValue() * RADIUS_FIELD_MULTIPLIER;
				double bodyDensity = densityField.getValue();
				Body body = new Body();
				body.setDensity(bodyDensity);
				body.setRadius(bodyRadius);
				DiskRotation rotation = (DiskRotation) diskRotation.getSelectedItem();
				DiskDistro distro = (DiskDistro) diskDistro.getSelectedItem();
				bodies.addAll(GameBodyFactory.createProtoDisk(disk, body, rotation, distro));
			}
		});

		clear.setMargin(new Insets(0, 0, 0, 0));
		clear.addActionListener(e -> {
			synchronized (this) {
				bodies.clear();
			}
		});

		absRefPoint = new Vector2D(getWidth() / 2, getHeight() / 2);

		// TODO remove
		// TEST
		// GameBody major = new GameBody("Major");
		// major.setRadius(10e6);
		// bodies.add(major);
		//
		// GameBody minor1 = new GameBody("Minor1");
		// minor1.setRadius(1e6);
		// minor1.setPosition(new Vector2D(12e6, 0));
		// minor1.setVelocity(new Vector2D(0, -6e3));
		// bodies.add(minor1);

		// mng.setSpeedMultiplier(10000);
	}

	@Override
	public synchronized void update(double deltaMs) {
		double deltaSec = deltaMs / 1_000d;
		hasSameDeltaTime(deltaSec);

		// Sanity auto switch
		// TODO move to listener (observable size list needed)
		if (bodies.size() > GameConstants.PARTICLE_MODE_THRESHOLD) {
			particleModeCheck.setSelected(true);
			particleModeCheck.setEnabled(false);
		} else {
			particleModeCheck.setEnabled(true);
		}

		if (!particleModeCheck.isSelected()) {
			// TODO optimise with utilisation of BH Tree
			List<Body> collisionList = new ArrayList<Body>();
			for (int i = 0; i < bodies.size(); i++) {
				for (int j = 0; j < bodies.size(); j++) {
					if (i > j) {
						GameBody b1 = bodies.get(i);
						GameBody b2 = bodies.get(j);
						if (b1.isNear(b2) && b1.isColliding(b2)) {
							Body majorBody;
							Body minorBody;
							if (b1.getMass() > b2.getMass()) {
								majorBody = b1;
								minorBody = b2;
							} else {
								majorBody = b2;
								minorBody = b1;
							}
							majorBody.merge(minorBody);
							collisionList.add(minorBody);
						}
					}
				}
			}
			// removal of collided bodies
			for (Body body : collisionList) {
				bodies.remove(body);
			}
		}

		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		for (Body body : bodies) {
			double x = body.getPosition().getX();
			double y = body.getPosition().getY();
			if (x < minX) {
				minX = x--;
			}
			if (x > maxX) {
				maxX = x++;
			}
			if (y < minY) {
				minY = y--;
			}
			if (y > maxY) {
				maxY = y++;
			}
		}
		double size = Math.max(maxX - minX, maxY - minY);
		Vector2D rootPos = new Vector2D((maxX + minX) / 2, (maxY + minY) / 2);

		// Benchmark.resetCounter();
		tree = new BHTQuadrant(rootPos, size);
		for (Body body : bodies) {
			// construct Barnes-Hut tree
			if (tree.isInside(body)) {
				tree.insert(body);
			} else {
				throw new RuntimeException("Body is out of bounds");
			}

		}

		Benchmark.start();
		// TODO test cached pool
		ExecutorService executor = Executors.newWorkStealingPool();

		for (Body body : bodies) {
			executor.execute(() -> {
				// calculate net force
				if (particleModeCheck.isSelected()) {
					tree.assignForce(body, GameConstants.EPSILON);
				} else {
					tree.assignForce(body);
				}

				// update position
				body.move(deltaSec);

				// TODO trajectory
				// lower velocity = less points
				// higher velocity = more points
				// no time step dependence
				if (showTrackCheck.isSelected()) {
					if (body.getTrack().isEmpty()) {
						body.addTrackPoint(body.getPosition());
					} else {
						// Vector2D prevPos = body.getLastTrackPoint();
						// if (prevPos.distance(newCenter) >
						// body.getVelocity().getNorm()) {
						// body.addTrackPoint(newCenter);
						// } else {
						// body.addTrackPoint(newCenter, true);
						// }
						body.addTrackPoint(body.getPosition());
					}
					// if (trackLimitCheck.isSelected()) {
					while (body.getTrack().size() > GameConstants.MAX_TRACK_SEGMENTS) {
						// TODO use better way to slice array
						body.getTrack().remove(0);
					}
					// }
				}
			});
		}

		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {}
		Benchmark.stop();
	}

	public int counter;

	/**
	 * Detects change in deltaTime.
	 * 
	 * Returns false only when interval has changed significantly, since Δt can
	 * slightly vary between game loops.
	 * 
	 * @param deltaTime
	 */
	public boolean hasSameDeltaTime(double deltaTime) {
		if (this.deltaTime / deltaTime > GameConstants.DELTA_TIME_CHANGE_FACTOR) {
			this.deltaTime = deltaTime;
			return false;
		} else if (deltaTime / this.deltaTime > GameConstants.DELTA_TIME_CHANGE_FACTOR) {
			this.deltaTime = deltaTime;
			return false;
		} else {
			return true;
		}
	}

	public void bodySelected(Body selectedBody) {
		if (selectedBody == null) {
			relRefPoint = null;
		} else {
			relRefPoint = selectedBody.getPosition();
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

	private synchronized void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Graphics2D gg = (Graphics2D) g.create();

		for (GameBody body : bodies) {
			// TODO use reference to selected body
			// TODO absRefPoint to real coordinates
			if (body.isSelected()) {
				Vector2D move = relRefPoint.subtract(body.getPosition());
				absRefPoint = absRefPoint.add(move.scalarMultiply(getMeterScale().down()));
				relRefPoint = body.getPosition();
			}
		}

		// RELATIVE COORDINATES
		gg.translate(absRefPoint.getX(), absRefPoint.getY());

		if (showTreeCheck.isSelected()) {
			gg.setColor(Color.GREEN);
			tree.draw(gg, meterScale);
		}

		gg.scale(pixelScale.down(), pixelScale.down());

		// body trajectories
		if (showTrackCheck.isSelected()) {
			for (GameBody body : bodies) {
				GridBody gBody = new GridBody(body, meterScale, pixelScale);
				gBody.drawTrajectory(gg);
			}
		}

		// bodies
		for (GameBody body : bodies) {
			GridBody gBody = new GridBody(body, meterScale, pixelScale);
			if (particleModeCheck.isSelected()) {
				gBody.drawBody(gg, true);
			} else {
				gBody.drawBody(gg, false);
				if (body.isSelected()) {
					gBody.drawSelection(gg);
				}
				if (showNameCheck.isSelected()) {
					gBody.drawName(gg);
				}
			}
		}

		// new body vector
		if (isNewBodyAction && !lastDragPos.equals(firstDragPos)) {
			g.setColor(Color.RED);

			Arrow arrow = new Arrow(lastDragPos, firstDragPos, 10);
			arrow.draw(g);

			Vector2D arrowCenter = lastDragPos.add(firstDragPos).scalarMultiply(0.5);
			Vector2D velocity = arrow.getMainLine().scalarMultiply(arrow.getMainLine().getNorm());
			Size speed = new Size(velocity.getNorm());

			g.setColor(Color.WHITE);
			g.drawString(speed + "/s", (int) arrowCenter.getX(), (int) arrowCenter.getY());
		}

		// simulation info
		g.setColor(Color.WHITE);
		g.drawString("FPS " + mng.getFps(), 10, 20);
		g.drawString("UPS " + mng.getUps(), 10, 40);
		g.drawString("Δt = " + deltaTime + "s", 10, 60);

		Date date = new Date(mng.getGameTime());
		String dateValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
		g.drawString("TIME " + dateValue, 10, 80);

		Size size = new Size(meterScale.up());
		g.drawString("SCALE 1px = " + size, 10, 100);

		g.drawString("Body count " + bodies.size(), 10, 120);

		// benchmark info
		// g.drawString("Recorded " + Benchmark.getResult() + "ms", 10, 240);
		// g.drawString("Recorded Max. " + Benchmark.getMax() + "ms", 10, 260);
		// g.drawString("Counted " + Benchmark.getCount(), 10, 280);
		// g.drawString("Counted Max. " + Benchmark.getMaxCount(), 10, 300);

		// reference cross
		g.setColor(Color.WHITE);
		Cross reference = new Cross(absRefPoint, 10);
		reference.draw(g);

	}

	public synchronized void addBody() {
		if (densityField.getValue() <= 0 || radiusField.getValue() <= 0) {
			return;
		}
		GameBody newBody = new GameBody();

		Vector2D center = firstDragPos.subtract(absRefPoint).scalarMultiply(meterScale.up());
		newBody.setPosition(center);

		Vector2D vVector = firstDragPos.subtract(lastDragPos);
		double norm = vVector.getNorm();
		if (norm > 0) {
			double speed = Math.pow(norm, 2) * 0.5;
			Vector2D unitVector = Vector2DUtils.scalarDivide(vVector, norm);
			newBody.setVelocity(unitVector.scalarMultiply(speed));
		}
		newBody.setName(newBodyName.getText());
		newBody.setDensity(densityField.getValue());
		newBody.setRadius(radiusField.getValue() * RADIUS_FIELD_MULTIPLIER);

		bodies.add(newBody);
	}

	protected GameFrame getFrame() {
		return mng;
	}

	protected Vector2D getAbsRefPoint() {
		return absRefPoint;
	}

	protected void setAbsRefPoint(Vector2D refPoint) {
		this.absRefPoint = refPoint;
	}

	protected Scale getMeterScale() {
		return meterScale;
	}

	protected List<GameBody> getBodyList() {
		return bodies;
	}

}
