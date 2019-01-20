package com.ixion.client.shape;

import static org.lwjgl.opengl.GL11.GL_LINES;

import com.ixion.client.renderer.FrustumCuller;
import com.ixion.client.renderer.Tessellator;

import vecmath.Vec3;

public class Line extends Shape {
	public Vec3 p0, p1;
	public Vec3 normal;
	public float len;

	public Line(Vec3 p0, Vec3 p1) {
		this.p0 = p0;
		this.p1 = p1;
		normal = p1.clone().cross(p0).normalise();
		len = p0.distanceTo(p1);
	}

	public void render(FrustumCuller culler) {
		Tessellator t = Tessellator.instance;
		t.begin(GL_LINES);
		t.colour(colour);
		t.line(p0, p1);
		t.end();
	}
}