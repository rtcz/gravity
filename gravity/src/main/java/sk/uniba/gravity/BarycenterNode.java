package sk.uniba.gravity;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class BarycenterNode implements MassNode {

	private double mass = 0;
	private Vector2D center = Vector2D.ZERO;

	public BarycenterNode() {}

	public BarycenterNode(MassNode body) {
		this.mass = body.getMass();
		this.center = body.getCenter();
	}

	public void addBody(MassNode body) {
		Vector2D vDist = body.getCenter().subtract(getCenter());
		double sumMass = getMass() + body.getMass();
		Vector2D vBarycenter = vDist.scalarMultiply(body.getMass() / sumMass);
		this.center = getCenter().add(vBarycenter);
		this.mass = sumMass;	
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public Vector2D getCenter() {
		return center;
	}

}
