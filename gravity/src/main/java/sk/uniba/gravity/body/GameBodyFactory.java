package sk.uniba.gravity.body;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import sk.uniba.gravity.game.GameConstants;
import sk.uniba.gravity.shape.Circle;

public class GameBodyFactory {

	private GameBodyFactory() {}
	
	public static List<GameBody> createProtoDisk(Circle circle, double density, double radius) {
		List<GameBody> bodies = new ArrayList<GameBody>();
		
		for (int i = 0; i < GameConstants.PROTODISK_SIZE; i++) {
			double distance = circle.getRadius() * Math.sqrt(Math.random());
			double angle = 2 * Math.PI * Math.random();
			double x = distance * Math.cos(angle);
			double y = distance * Math.sin(angle);
			Vector2D bodyCenter = circle.getPosition().add(new Vector2D(x, y));

			GameBody body = new GameBody("Planet " + (i + 1));
			body.setRadius(radius);
			body.setDensity(density);
			body.setPosition(bodyCenter);
			// TODO add velocity
			// body.setVelocity(new Vector2D(+3.879081706909912e4,
			// -4.110223749127960e4));
			bodies.add(body);
		}
		return bodies;
	}
	
	public static List<GameBody> createSolSystem() {
		List<GameBody> bodies = new ArrayList<GameBody>();
		
		GameBody sun = new GameBody("Sun");
		sun.setRadius(696.342e6);
		sun.setDensity(1408);
		sun.setPosition(new Vector2D(+6.442860875172776e8, +2.748913433991132e8));
		sun.setVelocity(new Vector2D(+3.151184859677259e0, -9.185194654328578e0));
		bodies.add(sun);

		GameBody mercury = new GameBody("Mercury");
		mercury.setRadius(2.440e6);
		mercury.setDensity(5427);
		mercury.setPosition(new Vector2D(+3.901558897485410e10, +2.904514484583830e10));
		mercury.setVelocity(new Vector2D(+3.879081706909912e4, -4.110223749127960e4));
		bodies.add(mercury);

		GameBody venus = new GameBody("Venus");
		venus.setRadius(6.052e6);
		venus.setDensity(5204);
		venus.setPosition(new Vector2D(-4.733027208737938e9, -1.083207490477231e11));
		venus.setVelocity(new Vector2D(-3.473833166185593e4, +1.856561942705289e3));
		bodies.add(venus);

		GameBody earth = new GameBody("Earth");
		earth.setRadius(6.371e6);
		earth.setDensity(5515);
		earth.setPosition(new Vector2D(-2.636314250687937e10, +1.448755934863529e11));
		earth.setVelocity(new Vector2D(+2.977359332571185e4, +5.558856867535258e3));
		bodies.add(earth);

		GameBody moon = new GameBody("Moon");
		moon.setRadius(1.737e6);
		moon.setDensity(3344);
		moon.setPosition(new Vector2D(-2.674751557311480e10, +1.448001055998542e11));
		moon.setVelocity(new Vector2D(+2.951864258933297e4, +6.520608815172885e3));
		moon.setName("Moon");
		bodies.add(moon);

		GameBody mars = new GameBody("Mars");
		mars.setRadius(3.389e6);
		mars.setDensity(3933);
		mars.setPosition(new Vector2D(+1.990267404151246e11, +7.450413194711113e10));
		mars.setVelocity(new Vector2D(+7.560777278212286e3, -2.477045044227772e4));
		bodies.add(mars);

		GameBody jupiter = new GameBody("Jupiter");
		jupiter.setRadius(69.911e6);
		jupiter.setDensity(1326);
		jupiter.setPosition(new Vector2D(-7.490058923334916e11, -3.198963469183056e11));
		jupiter.setVelocity(new Vector2D(-4.979372438187077e3, +1.140864406048834e4));
		bodies.add(jupiter);

		GameBody io = new GameBody("Io");
		io.setRadius(1.821e6);
		io.setDensity(3530);
		io.setPosition(new Vector2D(-7.492627120071737e11, -3.195614069659672e11));
		io.setVelocity(new Vector2D(+8.722245915140697e3, +2.198947504781624e4));
		bodies.add(io);

		GameBody europa = new GameBody("Europa");
		europa.setRadius(1.565e6);
		europa.setDensity(2900);
		europa.setPosition(new Vector2D(-7.483389941256535e11, -3.199159550614262e11));
		europa.setVelocity(new Vector2D(-5.275136182232703e3, -2.403014405114805e3));
		bodies.add(europa);

		GameBody ganymede = new GameBody("Ganymede");
		ganymede.setRadius(2.634e6);
		ganymede.setDensity(1940);
		ganymede.setPosition(new Vector2D(-7.480334529773365e11, -3.194466077682545e11));
		ganymede.setVelocity(new Vector2D(-4.126443694580075e2, +1.551054916289784e3));
		bodies.add(ganymede);

		GameBody callisto = new GameBody("Callisto");
		callisto.setRadius(2.403e6);
		callisto.setDensity(1851);
		callisto.setPosition(new Vector2D(-7.501310477531239e11, -3.214068799222987e11));
		callisto.setVelocity(new Vector2D(-1.158806579721592e4, +1.625231382195907e4));
		bodies.add(callisto);

		GameBody saturn = new GameBody("Saturn");
		saturn.setRadius(58.232e6);
		saturn.setDensity(687);
		saturn.setPosition(new Vector2D(+1.0834503736344e12, +8.513589639615979e11));
		saturn.setVelocity(new Vector2D(+6.490272474127367e3, -7.575137301499483e3));
		bodies.add(saturn);

		GameBody titan = new GameBody("Titan");
		titan.setRadius(2.575e6);
		titan.setDensity(1880);
		titan.setPosition(new Vector2D(+1.083787721229425e12, +8.524014419156948e11));
		titan.setVelocity(new Vector2D(+1.172823613448644e4, -9.246307961514402e3));
		bodies.add(titan);

		GameBody uranus = new GameBody("Uranus");
		uranus.setRadius(25.362e6);
		uranus.setDensity(1318);
		uranus.setPosition(new Vector2D(-2.723971684699241e12, -2.891270404270630e11));
		uranus.setVelocity(new Vector2D(-6.680837052483795e2, +7.089916083324248e3));
		bodies.add(uranus);

		GameBody neptune = new GameBody("Neptune");
		neptune.setRadius(24.624e6);
		neptune.setDensity(1638);
		neptune.setPosition(new Vector2D(-2.327426565170483e12, -3.890812806779972e12));
		neptune.setVelocity(new Vector2D(-4.630808049976979e3, +2.758234623717156e3));
		bodies.add(neptune);

		GameBody triton = new GameBody("Triton");
		triton.setRadius(1.352e6);
		triton.setDensity(2054);
		triton.setPosition(new Vector2D(-2.327179260794316e12, -3.890919849469012e12));
		triton.setVelocity(new Vector2D(-1.879934244840464e3, +5.818844541445264e3));
		bodies.add(triton);
		
		return bodies;
	}
}
