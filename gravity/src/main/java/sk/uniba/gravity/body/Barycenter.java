package sk.uniba.gravity.body;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.utils.Vector2DUtils;

public class Barycenter implements MassBody {

	private double mass = 0;
	private Vector2D position = Vector2D.ZERO;

	public Barycenter() {}
	
	public Barycenter(MassBody massPoint) {
		this.position = massPoint.getPosition();
		this.mass = massPoint.getMass();
	}
	
	public void add(MassBody body) {
		Vector2D centerMass1 = getPosition().scalarMultiply(getMass());
		Vector2D centerMass2 = body.getPosition().scalarMultiply(body.getMass());
		double sumMass = getMass() + body.getMass();
		this.position = Vector2DUtils.scalarDivide(centerMass1.add(centerMass2), sumMass);
		this.mass = sumMass;
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public Vector2D getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return mass + " " + position;
	}
	
}
