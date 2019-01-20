package com.ixion.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.lang.management.ManagementFactory;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import com.ixion.client.renderer.*;
import com.ixion.client.screen.*;

public class Trillek {
	private static final String TITLE = "Ixion";
	private static final String VERSION = "Pre-Alpha";
	private static final int OPENGL_VERSION_MAJOR = 3;
	private static final int OPENGL_VERSION_MINOR = 3;

	private boolean stop = false;

	private Screen screen;
	private Renderer renderer = new Renderer();
	private InputHandler inputHandler = new InputHandler();
	private Display display = new Display(inputHandler, OPENGL_VERSION_MAJOR, OPENGL_VERSION_MINOR);

	public void run() {
		long lastTime = System.currentTimeMillis();
		double delta = 0;
		double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();

		init();

		while (!stop) {
			long nowTime = System.nanoTime();
			double diff = (nowTime - lastTime) / ns;
			delta += diff;
			lastTime = nowTime;

			if (delta >= 1) {
				tick(delta);
				ticks++;
				delta--;
			}

			frames++;
			render();

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				screen.performanceString = ticks + " ticks, " + frames + " fps";
				frames = 0;
				ticks = 0;
			}
		}

		onDestroy();

		glfwTerminate();
	}

	private void init() {
		if (glfwInit() != true) {
			crash("Initialisation", new RuntimeException("GLFW Initialisation Failed!"));
			return;
		}

		GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int monitorWidth = mode.width();
		int monitorHeight = mode.height();
		int x = monitorWidth * 1 / 10;
		int y = monitorHeight * 1 / 10;
		int screenW = monitorWidth * 8 / 10;
		int screenH = monitorHeight * 8 / 10;
		display.createWindow(x, y, screenW, screenH, TITLE);

		GL.createCapabilities();

		System.out.println("Operating system: " + System.getProperty("os.name"));
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
		System.out.println("Shading language version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));

		ResourceManager.loadAll(true);
		Tessellator.create();
		renderer.init();

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);

		setScreen(new GameScreen());
	}

	private void tick(double delta) {
		if (display.shouldClose()) {
			stop();
			return;
		}
		screen.tick(delta);
	}

	private void render() {
		glfwPollEvents();
		glViewport(0, 0, display.getFramebufferWidth(), display.getFramebufferHeight());

		System.out.println("Rendering?");

		float r = 0.0f;
		float g = 0.0f;
		float b = 0.0f;
		float a = 1.0f;

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(r, g, b, a);

		screen.render(renderer);

		glfwSwapBuffers(display.getGLFW());
	}

	private void onDestroy() {
		ResourceManager.deleteAll();
		display.destroy();
	}

	public void setScreen(Screen nextScreen) {
		if (screen != null) {
			screen.onClose();
		}
		screen = nextScreen;
		screen.init(display);
	}

	public boolean isDebugMode() {
		return ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}

	private void crash(String where, Exception e) {
		System.err.println("Crash at: " + where + "!");
		e.printStackTrace();
	}

	public void stop() {
		stop = true;
	}

	public String getWindowTitle() {
		return TITLE + " " + VERSION;
	}

	public static void main(String[] args) {
		new Trillek().run();
	}
}