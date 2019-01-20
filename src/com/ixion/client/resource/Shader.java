package com.ixion.client.resource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;

import vecmath.*;

public class Shader extends Resource {
	private final String path;

	private int program;
	private int vertexShader, fragmentShader;

	public Shader(String name) {
		super(ResourceType.SHADER);
		path = "/shaders/" + name;
	}

	public void create() {
		program = glCreateProgram();

		vertexShader = loadShader(GL_VERTEX_SHADER);
		glAttachShader(program, vertexShader);

		fragmentShader = loadShader(GL_FRAGMENT_SHADER);
		glAttachShader(program, fragmentShader);

		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			System.out.println(glGetProgramInfoLog(program));
			System.out.println("Could not compile shader program!");
			System.exit(1);
		}

		glValidateProgram(program);
	}

	public String getLoadingString() {
		return "Shader -> " + path;
	}

	private int loadShader(int type) {
		String ext = "";
		if (type == GL_FRAGMENT_SHADER) ext = ".fs";
		if (type == GL_VERTEX_SHADER) ext = ".vs";

		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path + ext)));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(glGetShaderInfoLog(shaderID, 500));
			System.out.println("Could not compile shader!");
			System.exit(1);
		}
		return shaderID;
	}

	public void delete() {
		glDetachShader(program, vertexShader);
		glDetachShader(program, fragmentShader);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteProgram(program);
	}

	public int getShaderID() {
		return program;
	}

	public void bind() {
		glUseProgram(program);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void setUniform(String name, float... values) {
		int location = glGetUniformLocation(program, name);

		if (values.length == 1) {
			glUniform1f(location, values[0]);
		} else if (values.length == 2) {
			glUniform2f(location, values[0], values[1]);
		} else if (values.length == 3) {
			glUniform3f(location, values[0], values[1], values[2]);
		} else if (values.length == 4) {
			glUniform4f(location, values[0], values[1], values[2], values[3]);
		} else {
			System.err.println("Enter between 1 and 4 values!");
		}
	}

	public void setUniform(String name, int... values) {
		int location = glGetUniformLocation(program, name);

		if (values.length == 1) {
			glUniform1i(location, values[0]);
		} else if (values.length == 2) {
			glUniform2i(location, values[0], values[1]);
		} else if (values.length == 3) {
			glUniform3i(location, values[0], values[1], values[2]);
		} else if (values.length == 4) {
			glUniform4i(location, values[0], values[1], values[2], values[3]);
		} else {
			System.err.println("Enter between 1 and 4 values!");
		}
	}

	public void setUniform(String name, Vec3 value) {
		int location = glGetUniformLocation(program, name);
		glUniform3f(location, value.x, value.y, value.z);
	}

	public void setUniform(String name, Mat4 matrix) {
		int location = glGetUniformLocation(program, name);
		glUniformMatrix4fv(location, false, matrix.export());
	}
}