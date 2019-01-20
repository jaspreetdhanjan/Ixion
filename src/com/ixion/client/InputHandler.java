package com.ixion.client;

public class InputHandler {
	private double xMouse, yMouse;
	private double dxMouse, dyMouse;
	private boolean focused, mouseInScreen;

	private int currentKeyCode, currentKeyStatus;

	public void keyInvoked(int keyCode, int action) {
		currentKeyCode = keyCode;
		currentKeyStatus = action;
	}

	public int getKeyStatus(int keyCode) {
		if (keyCode == currentKeyCode) return currentKeyStatus;
		return -1;
	}

	public void onMouseClick(int mouseButton, int action) {
	}

	public void setMouseX(double xMouse) {
		this.xMouse = xMouse;
	}

	public double getMouseX() {
		return xMouse;
	}

	public void setMouseY(double yMouse) {
		this.yMouse = yMouse;
	}

	public double getMouseY() {
		return yMouse;
	}

	public void setMouseDX(double dxMouse) {
		this.dxMouse = dxMouse;
	}

	public double getMouseDX() {
		return dxMouse;
	}

	public void setMouseDY(double dyMouse) {
		this.dyMouse = dyMouse;
	}

	public double getMouseDY() {
		return dyMouse;
	}

	public void setFocus(boolean focused) {
		this.focused = focused;
	}

	public boolean getFocus() {
		return focused;
	}

	public void setMouseInScreen(boolean mouseInScreen) {
		this.mouseInScreen = mouseInScreen;
	}

	public boolean getMouseInScreen() {
		return mouseInScreen;
	}
}