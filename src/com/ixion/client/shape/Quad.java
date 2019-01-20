package com.ixion.client.shape;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;

import com.ixion.client.renderer.FrustumCuller;
import com.ixion.client.renderer.Tessellator;

import vecmath.Vec3;

public class Quad extends Shape {
	public Vec3 p0, p1, p2, p3;
	public Vec3 normal;

	public Quad(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;

		Vec3 u = p1.clone().sub(p0);
		Vec3 v = p3.clone().sub(p0);
		normal = u.cross(v).normalise();
	}

	public void render(FrustumCuller culler) {
		Tessellator t = Tessellator.instance;
		t.begin(GL_TRIANGLE_STRIP);
		t.colour(colour);
		t.quad(p0, p1, p2, p3);
		t.end();
	}
}