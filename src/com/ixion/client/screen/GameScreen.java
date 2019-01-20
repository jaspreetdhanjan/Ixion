package com.ixion.client.screen;

import vecmath.*;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;

import com.ixion.client.*;
import com.ixion.client.build.*;
import com.ixion.client.renderer.*;
import com.ixion.client.resource.*;

public class GameScreen extends Screen {
	private float xRot, yRot;
	private FrustumCuller frustumCuller = new FrustumCuller();

	private Vec3 right = new Vec3(1, 0, 0);
	private Vec3 forward = new Vec3(0, 0, 1);
	private Vec3 gravityDir = new Vec3(0, -1, 0);
	private float gravityPower = 9.81f / 60f / 60f;

	private Vec3 right1 = new Vec3(1, 0, 0);
	private Vec3 forward1 = new Vec3(0, 0, 1);

	private Player player = new Player(new Vec3(1, 0, 11));
	private Light light = new Light(new Vec3(7.5f, 10, 7.5f), 0.4f, 0.3f, 0.1f);

	private BuildSpace buildSpace = new BuildSpace(0, 0, 0, 15f, 10f, 15f);

	public void tick(double delta) {
		if (inputHandler.getMouseDX() != 0 || inputHandler.getMouseDY() != 0) {
			xRot += inputHandler.getMouseDX() * 0.001;
			yRot += inputHandler.getMouseDY() * 0.001;
//			inputHandler.dxMouse = 0;
//			inputHandler.dyMouse = 0;
		}

		float xa = 0, za = 0;
		if (inputHandler.getKeyStatus(GLFW_KEY_W) == GLFW_PRESS || inputHandler.getKeyStatus(GLFW_KEY_UP) == GLFW_PRESS) za--;
		if (inputHandler.getKeyStatus(GLFW_KEY_S) == GLFW_PRESS || inputHandler.getKeyStatus(GLFW_KEY_DOWN) == GLFW_PRESS) za++;
		if (inputHandler.getKeyStatus(GLFW_KEY_A) == GLFW_PRESS || inputHandler.getKeyStatus(GLFW_KEY_LEFT) == GLFW_PRESS) xa--;
		if (inputHandler.getKeyStatus(GLFW_KEY_D) == GLFW_PRESS || inputHandler.getKeyStatus(GLFW_KEY_RIGHT) == GLFW_PRESS) xa++;
		if (inputHandler.getKeyStatus(GLFW_KEY_SPACE) == GLFW_PRESS) player.tryJump = true;

		float speed = 0.05f;
		if (xa != 0 || za != 0) {
			Vec3 m = forward1.clone().mul(za).add(right1.clone().mul(xa)).normalise().mul(speed);
			player.move(m);
		}

		player.tick(delta, gravityDir.clone().mul(gravityPower));
	}

	protected void renderScene(Renderer renderer) {
		Vec3 ep = player.getEyePos().mul(-1f);
		viewMatrix.rotX(xRot);
		viewMatrix.rotY(yRot);
		viewMatrix.translate(ep);
		
		forward1 = viewMatrix.mul(forward);
		right1 = viewMatrix.mul(right);
		
		Mat4 modelViewMatrix = viewMatrix.clone().mul(modelMatrix);
		Mat4 normalMatrix = Mat4.createNormalMatrix(modelViewMatrix);
		frustumCuller.updatePlanes(projectionMatrix, modelViewMatrix);

		Shader shader = ResourceManager.phongShader;
		shader.bind();
		shader.setUniform("modelMatrix", modelMatrix);
		shader.setUniform("viewMatrix", viewMatrix);
		shader.setUniform("projectionMatrix", projectionMatrix);
		shader.setUniform("normalMatrix", normalMatrix);
		shader.setUniform("viewPos", ep);
		light.setUniforms(shader);

		BuildBB bb = buildSpace.bb;
		renderRoom(bb.x0, bb.y0, bb.z0, bb.x1, bb.y1, bb.z1, 0xffcccccc);
		shader.unbind();
	}

	private void renderRoom(float x0, float y0, float z0, float x1, float y1, float z1, int roomColour) {
		Tessellator t = Tessellator.instance;
		t.begin(GL_TRIANGLE_STRIP);
		t.colour(roomColour);
		t.box(x0, y0, z0, x1, y1, z1);
		t.end();
	}

	public boolean shouldGrabScreen() {
		return true;
	}
}