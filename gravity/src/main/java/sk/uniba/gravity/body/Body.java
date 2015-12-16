package sk.uniba.gravity.body;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.game.GameConstants;
import sk.uniba.gravity.shape.Circle;
import sk.uniba.gravity.utils.Vector2DUtils;

public class Body extends Circle implements MassBody {

	/**
	 * Density of water 1000 kg/m3
	 */
	public static final double DEFAULT_DENSITY = 1000;

	/**
	 * m/s
	 */
	private Vector2D velocity = Vector2D.ZERO;

	/**
	 * Force exerted on the body kg*m/s^2
	 */
	private Vector2D force = Vector2D.ZERO;

	/**
	 * kg/m^3
	 */
	private double density = DEFAULT_DENSITY;

	private List<Vector2D> trajectory = new ArrayList<Vector2D>();

	private String name;

	/**
	 * true if last trajectory point is temporary
	 */
	private boolean tempTrajectoryPoint;

	public Body() {
		super(Vector2D.ZERO, 0);
	}

	/**
	 * Volume that body represents
	 * 
	 * @return m^3
	 */
	public double getVolume() {
		return (float) 4 / 3 * Math.PI * Math.pow(getRadius(), 3);
	}

	/**
	 * Get mass of sphere that body represents
	 * 
	 * @return kg
	 */
	@Override
	public double getMass() {
		return getVolume() * density;
	}

	public void setDensity(double density) {
		if (density == 0) {
			throw new IllegalArgumentException("Body can not have zero density");
		}
		this.density = density;
	}

	public double getDensity() {
		return density;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public void setVelocity(double speed, Vector2D unitVector) {
		this.velocity = unitVector.scalarMultiply(speed);
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public Vector2D getForce() {
		return force;
	}

	// TODO compute acceleration only (bypass pairwise force)
	public void addForce(MassBody body, double epsilon) {
		Vector2D vDist12 = body.getPosition().subtract(getPosition());
		double dist12 = vDist12.getNorm();

		// gravitational force by Newton's law of universal
		// gravitation F=G*m1*m2/r^2
		double nominator = GameConstants.G * getMass() * body.getMass();
		// TODO add epsilon (gravitation softening parameter)
		double denominator = Math.pow(dist12, 2) + Math.pow(epsilon, 2);
		double sForce = nominator / denominator;

		Vector2D unitVector = Vector2DUtils.scalarDivide(vDist12, dist12);
		force = force.add(unitVector.scalarMultiply(sForce));
	}

	public void resetForce() {
		this.force = Vector2D.ZERO;
	}

	/**
	 * Leapfrog integration scheme. It is assumed that the actual velocity has
	 * been calculated at t-Δt/2. Uses new velocity at t+Δt/2 to update position
	 * to t+Δt.
	 * 
	 * @see http://www.wikiwand.com/en/Leapfrog_integration
	 * 
	 * @param deltaTime
	 *            in seconds
	 */
	public void move(double deltaTime) {
		// a=F/m | a0 = F0 / m
		Vector2D acceleration = Vector2DUtils.scalarDivide(getForce(), getMass());
		// v=a*t | v1 = v0 + a0*Δt
		setVelocity(getVelocity().add(acceleration.scalarMultiply(deltaTime)));
		// s=v*t | s1 = s0 + v1*Δt
		setPosition(getPosition().add(getVelocity().scalarMultiply(deltaTime)));
		// supplied force has been used
		resetForce();
	}

	public String getName() {
		if (name == null) {
			return "Untitled Body";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Merge preserves volume of bodies.
	 * 
	 * @param body
	 *            to merge with
	 */
	public void merge(Body body) {
		double sumMass = getMass() + body.getMass();
		double sumVolume = getVolume() + body.getVolume();

		// move to barycenter
		Barycenter barycenter = new Barycenter();
		barycenter.add(this);
		barycenter.add(body);
		setPosition(barycenter.getPosition());

		// calculate net velocity
		Vector2D velocity1 = getVelocity().scalarMultiply(getMass() / sumMass);
		Vector2D velocity2 = body.getVelocity().scalarMultiply(body.getMass() / sumMass);
		setVelocity(velocity1.add(velocity2));

		// recalculate density and radius
		setDensity(sumMass / sumVolume);
		setRadius(Math.cbrt((3 * sumVolume) / (4 * Math.PI)));
	}

	@Override
	public String toString() {
		return getName();
	}

	public Vector2D getLastTrackPoint() {
		if (trajectory.isEmpty()) {
			return null;
		}
		return trajectory.get(trajectory.size() - 1);
	}

	public void addTrackPoint(Vector2D point) {
		addTrackPoint(point, false);
	}

	public void addTrackPoint(Vector2D point, boolean temp) {
		if (this.tempTrajectoryPoint) {
			// if true, trajectory is not empty
			trajectory.remove(trajectory.size() - 1);
		}
		trajectory.add(point);
		this.tempTrajectoryPoint = temp;
	}

	public void clearTrack() {
		trajectory.clear();
	}

	public List<Vector2D> getTrack() {
		return trajectory;
	}
}
