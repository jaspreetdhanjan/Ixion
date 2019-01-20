package com.ixion.client;

import com.ixion.client.resource.Shader;

import vecmath.Vec3;

public class Light {
	public Vec3 pos;
	public float r, g, b;

	public Light(Vec3 pos, float r, float g, float b) {
		this.pos = pos;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public void setUniforms(Shader shader) {
		shader.setUniform("lightPos", pos);
		shader.setUniform("lightColour", r, g, b);
	}
}