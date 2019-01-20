package com.ixion.client.resource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import util.MemoryTracker;

public class Texture extends Resource {
	private final int width, height;
	private final int[] pixels;
	private int textureID;

	private String path;

	public Texture(int width, int height, int[] pixels) {
		super(ResourceType.TEXTURE);
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}

	public Texture(BufferedImage image) {
		super(ResourceType.TEXTURE);
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}

	public Texture(String name) {
		super(ResourceType.TEXTURE);

		path = "/textures/" + name + ".png";
		BufferedImage image = null;
		try {
			image = ImageIO.read(Texture.class.getResourceAsStream(path));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load a texture file!" + System.lineSeparator() + e.getMessage());
		}
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}

	public String getLoadingString() {
		if (path != null) return "Texture -> " + path;
		return null;
	}

	public void create() {
		ByteBuffer buffer = MemoryTracker.createByteBuffer(width * height * 4);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int colour = pixels[x + y * width];

				byte r = (byte) ((colour >> 16) & 0xff);
				byte g = (byte) ((colour >> 8) & 0xff);
				byte b = (byte) (colour & 0xff);
				byte a = (byte) ((colour >> 24) & 0xff);
				buffer.put(r).put(g).put(b).put(a);
			}
		}
		buffer.flip();

		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		// TODO:Enable options to change from GL_NEAREST to GL_LINEAR.

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void delete() {
		glDeleteTextures(textureID);
	}

	public int getTextureID() {
		return textureID;
	}

	public void bind(int textureIndex, Shader shader) {
		glActiveTexture(GL_TEXTURE0 + textureIndex);
		glBindTexture(GL_TEXTURE_2D, textureID);
		shader.setUniform("tex", textureIndex);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}