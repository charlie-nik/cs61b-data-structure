public class NBody {

	public static double readRadius(String filename) {
		/** Read and return the radius of the universe from file.*/
		In in = new In(filename);
		int numOfBodies = in.readInt();
		double radius = in.readDouble();
		return radius;
	}

	public static Body[] readBodies(String filename) {
		/** Read and return an array of Bodies from file.*/
		In in = new In(filename);
		int numOfBodies = in.readInt();
		double radius = in.readDouble();

		Body[] bodies = new Body[numOfBodies];

		for (int i = 0; i < numOfBodies; i++) {
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String img = in.readString();
			bodies[i] = new Body(xxPos, yyPos, xxVel, yyVel, mass, img);
		}
		return bodies;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Body[] bodies = readBodies(filename);

		StdDraw.enableDoubleBuffering();

		String background = "images/starfield.jpg";
		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0, 0, background);
		for (Body b: bodies) {
			b.draw();
		}

		int time = 0;
		while (time <= T) {
			double[] xForces = new double[bodies.length];
			double[] yForces = new double[bodies.length];
			for (int i = 0; i < bodies.length; i++) {
				xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
				yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
			}
			for (int i = 0; i < bodies.length; i++) {
				bodies[i].update(time, xForces[i], yForces[i]);
			}

			StdDraw.picture(0, 0, background);
			for (Body b: bodies) {
				b.draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
			time += dt;
		}

		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < bodies.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
						  bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
						  bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);
		}
	}
}