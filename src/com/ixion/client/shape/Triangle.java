package com.ixion.client.shape;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import com.ixion.client.renderer.FrustumCuller;
import com.ixion.client.renderer.Tessellator;

import vecmath.Vec3;

public class Triangle extends Shape {
	public Vec3 p0, p1, p2;
	public Vec3 normal;

	public Triangle(Vec3 p0, Vec3 p1, Vec3 p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;

		Vec3 u = p1.clone().sub(p0);
		Vec3 v = p2.clone().sub(p0);
		normal = u.cross(v).normalise();
	}

	public void render(FrustumCuller culler) {
		Tessellator t = Tessellator.instance;
		t.begin(GL_TRIANGLES);
		t.colour(colour);
		t.triangle(p0, p1, p2);
		t.end();
	}
}
