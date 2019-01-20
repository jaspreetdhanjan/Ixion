package com.ixion.client.screen;

import vecmath.*;

import com.ixion.client.Display;
import com.ixion.client.InputHandler;
import com.ixion.client.ResourceManager;
import com.ixion.client.renderer.*;
import com.ixion.client.resource.*;

public class Screen {
	private long timeOpened, timeClosed, diff;

	protected Display display;
	protected InputHandler inputHandler;

	protected float zoomFactor = 1f;
	protected float nearClip = 0.1f;
	protected float farClip = 100f;

	protected Mat4 projectionMatrix = new Mat4();
	protected Mat4 modelMatrix = new Mat4();
	protected Mat4 viewMatrix = new Mat4();

	public String performanceString = "";

	public void init(Display display) {
		this.display = display;
		inputHandler = display.getInputHandler();
		timeOpened = System.currentTimeMillis();
	}

	public void onClose() {
		timeClosed = System.currentTimeMillis();
		diff = timeClosed - timeOpened;
		diff = diff / 1000;
		System.out.println("The screen ran for: " + diff + " seconds");
	}

	public boolean shouldGrabScreen() {
		return false;
	}

	public void keyPressed(int eventKey) {
		// System.out.println("Key pressed -> " + eventKey);
	}

	public void keyReleased(int eventKey) {
		// System.out.println("Key released -> " + eventKey);
	}

	public void onClick(int mb, int action) {
		// System.out.println("Mouse pressed -> " + mb);
	}

	public void tick(double delta) {
	}

	public final void render(Renderer renderer) {
		projectionMatrix.gluPerspective(60f / zoomFactor, getAspectRatio(), nearClip, farClip);
		viewMatrix.identity();
		modelMatrix.identity();

		renderScene(renderer);

		projectionMatrix.gluOrthographic(0, display.getFramebufferWidth(), 0, display.getFramebufferHeight(), -1000f, 1000f);
		viewMatrix.identity();
		modelMatrix.identity();

		renderHud(renderer);
	}

	/** Render 3d stuff */
	protected void renderScene(Renderer renderer) {
	}

	/** Render 2d stuff overlayed on depth buffer */
	protected void renderHud(Renderer renderer) {
		Shader guiShader = ResourceManager.guiShader;
		guiShader.bind();

		guiShader.setUniform("modelMatrix", modelMatrix);
		guiShader.setUniform("viewMatrix", viewMatrix);
		guiShader.setUniform("projectionMatrix", projectionMatrix);

		renderer.drawString(guiShader, display.getTitle(), 10, display.getFramebufferHeight() - 50);
		renderer.drawString(guiShader, performanceString, 10, display.getFramebufferHeight() - 100);

		guiShader.unbind();
	}

	protected final float getAspectRatio() {
		float w = display.getFramebufferWidth();
		float h = display.getFramebufferHeight();
		if (h < 1) h = 1;
		return w / h;
	}
}