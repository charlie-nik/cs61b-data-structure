public class Body {
	/** Declare all the six attributes of a Body instance. */
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	/** The gravitational constant G.*/
	static final double G = 6.67e-11;

	public Body(double xP, double yP, double xV,
				double yV, double m, String img) {
		/** Construct a Body instance. */
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Body(Body b) {
		/** Construct a copy of Body B.*/
		xxPos = b.xxPos;
		yyPos = b.yyPos;
		xxVel = b.xxVel;
		yyVel = b.yyVel;
		mass = b.mass;
		imgFileName = b.imgFileName;
	}


	public double calcDistance(Body b) {
		/** Return the distance between B and the current Body.*/
		double dx = b.xxPos - this.xxPos;
		double dy = b.yyPos - this.yyPos;
		return Math.pow((dx * dx + dy * dy), 0.5);
	}

	public double calcForceExertedBy(Body b) {
		/** Return the force exerted by B upon the current Body.*/
		double r = calcDistance(b);
		return G * mass * b.mass / (r * r);
	}

	public double calcForceExertedByX(Body b) {
		/** Return the force exerted by B upon the current Body
			in the X direction.*/
		double dx = b.xxPos - xxPos;
		double f = calcForceExertedBy(b);
		double r = calcDistance(b);
		return f * dx / r;
	}

	public double calcForceExertedByY(Body b) {
		/** Return the force exerted by B upon the current Body
			in the Y direction.*/
		double dy = b.yyPos - yyPos;
		double f = calcForceExertedBy(b);
		double r = calcDistance(b);
		return f * dy / r;
	}

	public double calcNetForceExertedByX(Body[] bodies) {
		/** Return the net force exerted upon the current Body
			in the X direction.*/
		double res = 0;
		for (Body b: bodies) {
			if (this.equals(b)) {
				continue;
			}
			res += calcForceExertedByX(b);
		}
		return res;
	}

	public double calcNetForceExertedByY(Body[] bodies) {
		/** Return the net force exerted upon the current Body
			in the Y direction.*/
		double res = 0;
		for (Body b: bodies) {
			if (this.equals(b)) {
				continue;
			}
			res += calcForceExertedByY(b);
		}
		return res;
	}

	public void update(double dt, double fX, double fY) {
		/** Update the position and velocity of the current Body
			given the new accelerations and time passed.
			Return nothing.*/
		double ax = fX / mass;
		double ay = fY / mass;
		
		xxVel = xxVel + dt * ax;
		yyVel = yyVel + dt * ay;
		xxPos = xxPos + dt * xxVel;
		yyPos = yyPos + dt * yyVel;
	}

	public void draw() {
		/** Let the current Body draw itself in its position.*/
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}