package sk.uniba.gravity.bhtree;

import java.awt.Graphics2D;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.body.Barycenter;
import sk.uniba.gravity.body.Body;
import sk.uniba.gravity.body.MassBody;
import sk.uniba.gravity.commons.Scale;
import sk.uniba.gravity.game.GameConstants;

/**
 * Barnes-Hut Tree Quadrant
 * 
 * @see http://arborjs.org/docs/barnes-hut
 */
public class BHTQuadrant extends Quadrant {

	private BHTQuadrant[] quadrants;

	/**
	 * Represents single body in external node or aggregate body in internal
	 * node.
	 */
	private Barycenter barycenter;

	public BHTQuadrant(Vector2D center, double size) {
		super(center, size);
	}

	/**
	 * Adds new body to the tree recursively.
	 * 
	 * @param body
	 */
	public void insert(MassBody body) {
		if (isEmpty()) {
			// empty node becomes external node
			barycenter = new Barycenter(body);
		} else if (isInternal()) {
			// update barycenter
			barycenter.add(body);
			// move body to some sub-quadrant
			insertDeeper(body);
		} else {
			// external node becomes internal node
			subdivide();
			// copy barycenter (single body) deeper
			insertDeeper(new Barycenter(barycenter));
			// move body to some sub-quadrant
			insertDeeper(body);
			// update barycenter
			barycenter.add(body);
		}
	}

	/**
	 * Changes quadrant from external to internal by dividing it into new
	 * quadrants.
	 */
	private void subdivide() {
		quadrants = new BHTQuadrant[4];
		double halfSize = getSize() * 0.5;
		double quarterSize = getSize() * 0.25;

		Vector2D nwCenter = getCenter().add(new Vector2D(-quarterSize, quarterSize));
		Vector2D neCenter = getCenter().add(new Vector2D(quarterSize, quarterSize));
		Vector2D swCenter = getCenter().add(new Vector2D(-quarterSize, -quarterSize));
		Vector2D seCenter = getCenter().add(new Vector2D(quarterSize, -quarterSize));

		quadrants[0] = new BHTQuadrant(nwCenter, halfSize);
		quadrants[1] = new BHTQuadrant(neCenter, halfSize);
		quadrants[2] = new BHTQuadrant(swCenter, halfSize);
		quadrants[3] = new BHTQuadrant(seCenter, halfSize);
	}

	/**
	 * Inserts a body into the appropriate quadrant.
	 */
	private void insertDeeper(MassBody body) {
		for (int i = 0; i < quadrants.length; i++) {
			if (quadrants[i].isInside(body)) {
				quadrants[i].insert(body);
				break;
			}
		}
	}

	private boolean isEmpty() {
		return barycenter == null;
	}

	/**
	 * External node represents a single body
	 */
	private boolean isExternal() {
		return quadrants == null;
	}

	/**
	 * Internal node represents the group of aggregated bodies
	 */
	private boolean isInternal() {
		return !isExternal();
	}
	
	public void assignForce(Body body) {
		assignForce(body, 0);
	}

	public void assignForce(Body body, double epsilon) {
		if (isEmpty() || barycenter.getPosition().equals(body.getPosition())) {
			// empty quadrant or the same body
			return;
		}

		// if the current node is external, update net force acting on body
		if (isExternal()) {
			body.addForce(barycenter, epsilon);
		} else {
			double distance = getCenter().distance(body.getPosition());
			if ((getSize() / distance) < GameConstants.BH_TREE_THETA) {
				// use approximation
				body.addForce(barycenter, epsilon); // b is far away
			} else {
				// recurse on each of current node's children
				for (int i = 0; i < quadrants.length; i++) {
					quadrants[i].assignForce(body, epsilon);
				}
			}
		}
	}
	
	public boolean isInside(MassBody body) {
		return super.isInside(body.getPosition());
	}
	
	public void draw(Graphics2D g, Scale scale) {
		super.draw(g, scale);
		if (isInternal()) {
			for (int i = 0; i < quadrants.length; i++) {
				quadrants[i].draw(g, scale);
			}	
		}
	}

	@Override
	public String toString() {
		if (isExternal()) {
			return "EXT " + barycenter + "\n";
		} else {
			return "INT " + barycenter + "\n" + quadrants;
		}
	}

}
