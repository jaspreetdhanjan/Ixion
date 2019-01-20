package com.ixion.client.renderer;

import com.ixion.client.ResourceManager;
import com.ixion.client.resource.*;

import static org.lwjgl.opengl.GL11.*;

public class TextRenderer {
	private Texture fontTexture;
	private float gridSize = 16f;
	private float characterWidth = 0.3f;
	private float characterHeight = 0.225f;

	public void init() {
		fontTexture = ResourceManager.fontTexture;
	}

	public void render(Shader guiShader, String msg, int colour, float x, float y) {
		Tessellator t = Tessellator.instance;

		glEnable(GL_CULL_FACE);

		fontTexture.bind(0, guiShader);
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);

		t.begin(GL_TRIANGLE_STRIP);
		t.normal(0, 0, -1f);
		t.colour(1.0f, 1.0f, 1.0f, 1.0f);
		t.offset(x, y, 0);
		for (int i = 0; i < msg.length(); i++) {
			final float cellSize = 1.0f / gridSize;
			int asciiCode = (int) msg.charAt(i);
			float cellX = ((int) asciiCode % gridSize) * cellSize;
			float cellY = ((int) asciiCode / gridSize) * cellSize;

			drawCharacter(t, i, cellSize, cellX, cellY);
		}
		t.end();

		glDisable(GL_BLEND);
		glDisable(GL_CULL_FACE);
	}

	private void drawCharacter(Tessellator t, int i, float cellSize, float cellX, float cellY) {
		t.texCoord(cellX, cellY + cellSize);
		t.vertex(i * characterWidth / 3, 0, 0);
		t.texCoord(cellX + cellSize, cellY + cellSize);
		t.vertex(i * characterWidth / 3 + characterWidth / 2, 0, 0);
		t.texCoord(cellX + cellSize, cellY);
		t.vertex(i * characterWidth / 3 + characterWidth / 2, 0 + characterHeight, 0);
		t.texCoord(cellX, cellY);
		t.vertex(i * characterWidth / 3, 0 + characterHeight, 0);
	}
}