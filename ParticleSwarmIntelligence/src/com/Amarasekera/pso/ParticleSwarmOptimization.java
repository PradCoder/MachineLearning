package com.Amarasekera.pso;

public class ParticleSwarmOptimization {

	private double[] globalBestSolutions;
	private Particle[] particleSwarm;
	private int epochs;

	public ParticleSwarmOptimization() {
		this.globalBestSolutions = new double[Constants.NUM_DIMENSIONS];
		this.particleSwarm = new Particle[Constants.NUM_PARTICLES];
		generateRandomSolution();
	}

	private void generateRandomSolution() {
		for (int i = 0; i < Constants.NUM_DIMENSIONS; i++) {
			double randCoordinate = random(Constants.MIN, Constants.MAX);
			this.globalBestSolutions[i] = randCoordinate;
		}
	}

	public void solve() {

		// initialize the particles
		for (int i = 0; i < Constants.NUM_PARTICLES; i++) {

			double[] locations = initializeLocation();
			double[] velocities = initializeVelocity();

			this.particleSwarm[i] = new Particle(locations, velocities);
		}

		while (epochs < Constants.MAX_ITERATIONS) {

			for (Particle actualParticle : this.particleSwarm) {

				// updating the velocities
				for (int i = 0; i < actualParticle.getVelocity().length; i++) {
					double rp = Math.random();
					double rg = Math.random();

					actualParticle.getVelocity()[i] = Constants.w * actualParticle.getVelocity()[i]
							+ Constants.c1 * rp
									* (actualParticle.getBestPosition()[i] - actualParticle.getPosition()[i])
							+ Constants.c2 * rg * (this.globalBestSolutions[i] - actualParticle.getPosition()[i]);
				}

				// update the positions
				for (int i = 0; i < actualParticle.getPosition().length; i++) {

					actualParticle.getPosition()[i] += actualParticle.getVelocity()[i];

					if (actualParticle.getPosition()[i] < Constants.MIN) {
						actualParticle.getPosition()[i] += Constants.MIN;
					} else if (actualParticle.getPosition()[i] > Constants.MAX) {
						actualParticle.getPosition()[i] = Constants.MAX;
					}
				}

				if (Constants.f(actualParticle.getPosition()) > Constants.f(actualParticle.getBestPosition())) {
					actualParticle.setBestPosition(actualParticle.getPosition());
				}

				if (Constants.f(actualParticle.getPosition()) > Constants.f(globalBestSolutions)) {
					System.arraycopy(actualParticle.getBestPosition(), 0, globalBestSolutions, 0,
							actualParticle.getBestPosition().length);
				}
			}
			this.epochs++;
		}
	}

	private double[] initializeLocation() {

		double x = random(Constants.MIN, Constants.MAX);
		double y = random(Constants.MIN, Constants.MAX);

		double[] newLocation = new double[] { x, y };
		return newLocation;
	}

	private double[] initializeVelocity() {

		double vx = random(-(Constants.MAX - Constants.MIN), Constants.MAX - Constants.MIN);
		double vy = random(-(Constants.MAX - Constants.MIN), Constants.MAX - Constants.MIN);

		double[] newVelocity = new double[] { vx, vy };
		return newVelocity;
	}

	private double random(double min, double max) {
		return min + (max - min) * Math.random();
	}

	public void showSolution() {
		System.out.println("Solution for PSO problem!");
		System.out.println("Best solution x: " + this.globalBestSolutions[0] + " - y:" + this.globalBestSolutions[1]);
		System.out.println("Value f(x,y) = " + Constants.f(globalBestSolutions));
	}
}
