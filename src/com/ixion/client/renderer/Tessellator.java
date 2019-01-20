package com.ixion.client.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import util.MemoryTracker;
import vecmath.Vec3;

public class Tessellator {
	public static Tessellator instance;

	public static void create() {
		instance = new Tessellator(2097152);
	}

	private static final int INDEX_POS = 0;
	private static final int INDEX_COL = 1;
	private static final int INDEX_NOR = 2;
	private static final int INDEX_TEX = 3;

	private static final int BYTES_PER_FLOAT = Float.SIZE / Byte.SIZE;

	private static final int FLOATS_PER_POS = 3;
	private static final int FLOATS_PER_COL = 4;
	private static final int FLOATS_PER_NOR = 3;
	private static final int FLOATS_PER_TEX = 2;

	private static final int SIZEOF_POS = FLOATS_PER_POS * BYTES_PER_FLOAT;
	private static final int SIZEOF_COL = FLOATS_PER_COL * BYTES_PER_FLOAT;
	private static final int SIZEOF_NOR = FLOATS_PER_NOR * BYTES_PER_FLOAT;
	private static final int SIZEOF_TEX = FLOATS_PER_TEX * BYTES_PER_FLOAT;

	private static final long OFFS_POS = 0 * BYTES_PER_FLOAT;
	private static final long OFFS_COL = FLOATS_PER_POS * BYTES_PER_FLOAT;
	private static final long OFFS_NOR = (FLOATS_PER_POS + FLOATS_PER_COL) * BYTES_PER_FLOAT;
	private static final long OFFS_TEX = (FLOATS_PER_POS + FLOATS_PER_COL + FLOATS_PER_NOR) * BYTES_PER_FLOAT;

	private boolean tessellating = false;

	private final FloatBuffer data;
	private final int vaoID;
	private final int vboID;

	private int vertices;
	private int drawMode;
	private float r, g, b, a;
	private float xo, yo, zo;
	private float nx, ny, nz;
	private float u, v;
	private boolean hasNormal, hasTexture, hasColour;

	private Tessellator(int size) {
		data = MemoryTracker.createFloatBuffer(size);
		vaoID = glGenVertexArrays();
		vboID = glGenBuffers();

		reset();
	}

	public void offset(float xo, float yo, float zo) {
		this.xo = xo;
		this.yo = yo;
		this.zo = zo;
	}

	public void addOffset(float xa, float ya, float za) {
		xo += xa;
		yo += ya;
		zo += za;
	}

	public void texCoord(float u, float v) {
		hasTexture = true;
		this.u = u;
		this.v = v;
	}

	public void normal(float nx, float ny, float nz) {
		hasNormal = true;
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}

	public void colour(float r, float g, float b, float a) {
		hasColour = true;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public void colour(int colour) {
		int a = (colour >> 24) & 0xff;
		int r = (colour >> 16) & 0xff;
		int g = (colour >> 8) & 0xff;
		int b = colour & 0xff;
		colour(r, g, b, a);
	}

	public void colour(int colour, int a) {
		int r = (colour >> 16) & 0xff;
		int g = (colour >> 8) & 0xff;
		int b = colour & 0xff;
		colour(r, g, b, a);
	}

	public void vertex(float x, float y, float z) {
		float xx = x + xo;
		float yy = y + yo;
		float zz = z + zo;
		data.put(xx).put(yy).put(zz);
		vertices++;

		if (hasColour) data.put(r).put(g).put(b).put(a);
		if (hasNormal) data.put(nx).put(ny).put(nz);
		if (hasTexture) data.put(u).put(v);
	}

	public void vertex(Vec3 p) {
		vertex(p.x, p.y, p.z);
	}

	public void line(float x0, float y0, float z0, float x1, float y1, float z1) {
		float Nx = y0 * z1 - z0 * y1;
		float Ny = z0 * x1 - x0 * z1;
		float Nz = x0 * y1 - y0 * x1;

		float dd = (float) Math.sqrt(Nx * Nx + Ny * Ny + Nz * Nz);

		Nx /= dd;
		Ny /= dd;
		Nz /= dd;

		normal(Nx, Ny, Nz);

		vertex(x0, y0, z0);
		vertex(x1, y1, z1);
	}

	public void line(Vec3 p0, Vec3 p1) {
		line(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);
	}

	public void triangle(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2) {
		float Ux = x1 - x0;
		float Uy = y1 - y0;
		float Uz = z1 - z0;

		float Vx = x2 - x0;
		float Vy = y2 - y0;
		float Vz = z2 - z0;

		float Nx = Uy * Vz - Uz * Vy;
		float Ny = Uz * Vx - Ux * Vz;
		float Nz = Ux * Vy - Uy * Vx;

		float dd = (float) Math.sqrt(Nx * Nx + Ny * Ny + Nz * Nz);

		Nx /= dd;
		Ny /= dd;
		Nz /= dd;

		normal(Nx, Ny, Nz);

		vertex(x0, y0, z0);
		vertex(x1, y1, z1);
		vertex(x2, y2, z2);
	}

	public void triangle(Vec3 p0, Vec3 p1, Vec3 p2) {
		triangle(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
	}

	public void quad(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		float Ux = x1 - x0;
		float Uy = y1 - y0;
		float Uz = z1 - z0;

		float Vx = x3 - x0;
		float Vy = y3 - y0;
		float Vz = z3 - z0;

		float Nx = Uy * Vz - Uz * Vy;
		float Ny = Uz * Vx - Ux * Vz;
		float Nz = Ux * Vy - Uy * Vx;

		float dd = (float) Math.sqrt(Nx * Nx + Ny * Ny + Nz * Nz);

		Nx /= dd;
		Ny /= dd;
		Nz /= dd;

		normal(Nx, Ny, Nz);

		vertex(x0, y0, z0);
		vertex(x1, y1, z1);
		vertex(x3, y3, z3);
		vertex(x2, y2, z2);
	}

	public void quad(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3) {
		quad(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z);
	}

	public void box(float x0, float y0, float z0, float x1, float y1, float z1) {
		quad(x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1);
		quad(x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0);
		quad(x1, y1, z0, x1, y1, z1, x1, y0, z1, x1, y0, z0);
		quad(x0, y1, z1, x0, y1, z0, x0, y0, z0, x0, y0, z1);
		quad(x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
		quad(x0, y1, z0, x1, y1, z0, x1, y0, z0, x0, y0, z0);
	}

	public void box(Vec3 m0, Vec3 m1) {
		box(m0.x, m0.y, m0.z, m1.x, m1.y, m1.z);
	}

	public void begin(int mode) {
		if (tessellating) {
			throw new RuntimeException("Tessellator is already tessellating!");
		}

		drawMode = mode;
		tessellating = true;
	}

	public void end() {
		data.rewind();
		if (vertices == 0) {
			reset();
			return;
		}

		glBindVertexArray(vaoID);

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);

		int stride = SIZEOF_POS;
		if (hasColour) stride += SIZEOF_COL;
		if (hasNormal) stride += SIZEOF_NOR;
		if (hasTexture) stride += SIZEOF_TEX;

		glVertexAttribPointer(INDEX_POS, FLOATS_PER_POS, GL_FLOAT, false, stride, OFFS_POS);
		if (hasColour) glVertexAttribPointer(INDEX_COL, FLOATS_PER_COL, GL_FLOAT, false, stride, OFFS_COL);
		if (hasNormal) glVertexAttribPointer(INDEX_NOR, FLOATS_PER_NOR, GL_FLOAT, false, stride, OFFS_NOR);
		if (hasTexture) glVertexAttribPointer(INDEX_TEX, FLOATS_PER_TEX, GL_FLOAT, false, stride, OFFS_TEX);

		glEnableVertexAttribArray(INDEX_POS);
		if (hasColour) glEnableVertexAttribArray(INDEX_COL);
		if (hasNormal) glEnableVertexAttribArray(INDEX_NOR);
		if (hasTexture) glEnableVertexAttribArray(INDEX_TEX);

		glDrawArrays(drawMode, 0, vertices);

		glBindVertexArray(0);
		reset();
	}

	private void reset() {
		vertices = 0;
		tessellating = false;
		r = g = b = a = 1f;
		nx = ny = nz = 0;
		xo = yo = zo = 0;
		u = v = 0;
		drawMode = 0;
		hasNormal = hasTexture = hasColour = false;
	}
}