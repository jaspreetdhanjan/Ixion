package com.ixion.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.*;

public class Display {
	private InputHandler inputHandler;

	private final int openglVersionMajor;
	private final int openglVersionMinor;

	private long window;
	private int screenW, screenH;
	private int framebufferW, framebufferH;
	private String title;

	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWCursorEnterCallback cursorEnterCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWWindowFocusCallback focusCallback;
	private GLFWWindowSizeCallback resizeCallback;
	private GLFWFramebufferSizeCallback frameBufferCallback;

	public Display(InputHandler inputHandler, int openglVersionMajor, int openglVersionMinor) {
		this.inputHandler = inputHandler;
		this.openglVersionMajor = openglVersionMajor;
		this.openglVersionMinor = openglVersionMinor;
	}

	public void createWindow(int x, int y, int screenW, int screenH, String title) {
		this.screenW = screenW;
		this.screenH = screenH;
		this.title = title;

		glfwDefaultWindowHints();

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, openglVersionMajor);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, openglVersionMinor);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		window = glfwCreateWindow(screenW, screenH, title, NULL, NULL);
		if (window == NULL) {
			throw new IllegalStateException("Window creation failed!");
		}

		GLFWErrorCallback.createPrint(System.err).set();

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				inputHandler.keyInvoked(key, action);
			}
		});

		glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
			public void invoke(long window, double xMouse, double yMouse) {
				double oldMouseX = xMouse;
				double oldMouseY = yMouse;
				inputHandler.setMouseX(xMouse);
				inputHandler.setMouseY(yMouse);

				if (inputHandler.getMouseInScreen()) {
					float ar = Display.this.screenW / Display.this.screenH;
					inputHandler.setMouseDX((xMouse - oldMouseX) / ar);
					inputHandler.setMouseDY((yMouse - oldMouseY) / ar);
				}
			}
		});

		glfwSetCursorEnterCallback(window, cursorEnterCallback = new GLFWCursorEnterCallback() {
			public void invoke(long window, boolean entered) {
				inputHandler.setMouseInScreen(entered);
			}

			public void callback(long window) {
			}

			public void close() {
			}
		});

		glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				inputHandler.onMouseClick(button, action);
			}
		});

		glfwSetWindowFocusCallback(window, focusCallback = new GLFWWindowFocusCallback() {
			public void invoke(long window, boolean focused) {
				inputHandler.setFocus(focused);
			}

			public void callback(long window) {
			}

			public void close() {
			}
		});

		glfwSetWindowSizeCallback(window, resizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int w, int h) {
				boolean tryResize = false;
				if (w < 512 || h < 512) {
					if (w < 512) w = 512;
					if (h < 512) h = 512;
					tryResize = true;
				}

				if (tryResize) {
					glfwSetWindowSize(window, w, h);
				}
				setScreenSize(w, h);
			}
		});

		glfwSetFramebufferSizeCallback(window, frameBufferCallback = new GLFWFramebufferSizeCallback() {
			public void invoke(long window, int framebufferW, int framebufferH) {
				setFramebufferSize(framebufferW, framebufferH);
			}
		});

		glfwSetWindowPos(window, x, y);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		int[] tmpWidth = new int[1];
		int[] tmpHeight = new int[1];
		glfwGetFramebufferSize(window, tmpWidth, tmpHeight);
		setFramebufferSize(tmpWidth[0], tmpHeight[0]);
	}

	private void setScreenSize(int screenW, int screenH) {
		this.screenW = screenW;
		this.screenH = screenH;
	}

	private void setFramebufferSize(int framebufferW, int framebufferH) {
		this.framebufferW = framebufferW;
		this.framebufferH = framebufferH;
	}

	public int getFramebufferWidth() {
		return framebufferW;
	}

	public int getFramebufferHeight() {
		return framebufferH;
	}

	public int getScreenWidth() {
		return screenW;
	}

	public int getScreenHeight() {
		return screenH;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}

	public String getTitle() {
		return title;
	}

	public void destroy() {
		glfwDestroyWindow(window);

		keyCallback.free();
		cursorPosCallback.free();
		cursorEnterCallback.free();
		mouseButtonCallback.free();
		focusCallback.free();
		resizeCallback.free();
		frameBufferCallback.free();
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	public long getGLFW() {
		return window;
	}
	

	/*		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
				glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
				blockInput = true;
			}
	
			if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS && screen.shouldGrabScreen()) {
				glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
				blockInput = false;
			}
	*/

}