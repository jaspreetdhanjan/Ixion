package com.ixion.client.renderer;

import static org.lwjgl.opengl.GL11.*;

import com.ixion.client.resource.*;

import vecmath.Mat4;

public class Renderer {
	private TextRenderer textRenderer = new TextRenderer();

	public void init() {
		textRenderer.init();
	}

	public int getHex(int a, int r, int g, int b) {
		if (a > 255) a = 255;
		if (r > 255) r = 255;
		if (g > 255) g = 255;
		if (b > 255) b = 255;

		if (a < 0) a = 0;
		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;

		return (a << 24 | r << 16 | g << 8 | b);
	}

	public int[] packToARGB(int color) {
		int a = (color & 0xff000000) >> 24;
		int r = (color & 0xff0000) >> 16;
		int g = (color & 0xff00) >> 8;
		int b = (color & 0xff);
		return new int[] { a, r, g, b };
	}

	public void drawString(Shader guiShader, String msg, float x, float y) {
		textRenderer.render(guiShader, msg, 0xffffff, x, y);
	}

	public void testDraw(Shader basicShader, Texture testTexture, Mat4 modelviewMatrix, Mat4 projectionMatrix) {
		basicShader.bind();
		basicShader.setUniform("modelViewMatrix", modelviewMatrix);
		basicShader.setUniform("projectionMatrix", projectionMatrix);

		testTexture.bind(0, basicShader);
		{
			Tessellator t = Tessellator.instance;
			t.begin(GL_TRIANGLES);
			t.colour(0xffffff);
			t.quad(0, 0, 0, 10, 0, 0, 10, 10, 0, 0, 10, 0);
			// t.quad(0, 0, 0, 0, 10, 0, 10, 10, 0, 0, 10, 0);
			t.end();
		}
		testTexture.unbind();
		basicShader.unbind();

		/*
		shader.bind();
		shader.setUniform("modelMatrix", modelMatrix);
		shader.setUniform("viewMatrix", viewMatrix);
		shader.setUniform("projectionMatrix", projectionMatrix);
		shader.setUniform("normalMatrix", normalMatrix);
		shader.setUniform(light);
		
		testTexture.bind(0, shader);
		{
			t.begin(GL_TRIANGLE_STRIP);
			t.colour(0xffffff);
			t.box(2, 2, 2, 3, 3, 3);
			t.end();
		}
		testTexture.unbind();
		shader.unbind();
		*/
	}
}