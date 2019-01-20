package com.ixion.client;

import vecmath.Vec3;

public class Player {
	private static final float DIST_FROM_EYE_TO_TOP_OF_HEAD = 0.1f;

	private float height = 1.8f;

	public Vec3 pos;
	public boolean tryJump;
	public boolean onGround;

	public Player(Vec3 pos) {
		this.pos = pos;
	}

	public void move(Vec3 m) {
		pos.add(m);
	}

	public void tick(double delta, Vec3 gravity) {
		float jumpHeight = 120f / 60f / 60f;

		if (tryJump) {
			Vec3 gravityDir = gravity.clone().normalise();
			move(gravityDir.mul(-jumpHeight));
			tryJump = false;
		}
	}

	public Vec3 getEyePos() {
		return pos.clone().add(0, height, 0).sub(0, DIST_FROM_EYE_TO_TOP_OF_HEAD, 0);
	}
}