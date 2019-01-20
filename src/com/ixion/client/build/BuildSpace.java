package com.ixion.client.build;

public class BuildSpace {
	public BuildBB bb = new BuildBB();

	public BuildSpace(float x0, float y0, float z0, float x1, float y1, float z1) {
		bb.set(x0, y0, z0, x1, y1, z1);
	}
}