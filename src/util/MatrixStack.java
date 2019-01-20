package util;

import java.nio.FloatBuffer;

import vecmath.*;

public class MatrixStack {
	private Mat4[] mats;
	private int pp;

	public MatrixStack(int stackSize) {
		mats = new Mat4[stackSize];
		for (int i = 0; i < stackSize; i++) {
			mats[i] = new Mat4();
		}
	}

	public MatrixStack clear() {
		pp = 0;
		mats[0].identity();
		return this;
	}

	public MatrixStack loadMatrix(Mat4 mat) {
		if (mat == null) {
			throw new IllegalArgumentException("mat must not be null");
		}
		mats[pp].set(mat);
		return this;
	}

	public MatrixStack loadMatrix(FloatBuffer columnMajorArray) {
		if (columnMajorArray == null) {
			throw new IllegalArgumentException("columnMajorArray must not be null");
		}
		mats[pp].set(columnMajorArray);
		return this;
	}

	public MatrixStack pushMatrix() {
		if (pp == mats.length - 1) {
			throw new IllegalStateException("max stack size of " + (pp + 1) + " reached");
		}
		mats[pp + 1].set(mats[pp]);
		pp++;
		return this;
	}

	public MatrixStack popMatrix() {
		if (pp == 0) {
			throw new IllegalStateException("already at the buttom of the stack");
		}
		pp--;
		return this;
	}

	public Mat4 get(Mat4 dest) {
		if (dest == null) {
			throw new IllegalArgumentException("dest must not be null");
		}
		dest.set(mats[pp]);
		return dest;
	}

	public Mat4 getDirect() {
		return mats[pp];
	}

	public MatrixStack translate(float x, float y, float z) {
		Mat4 c = mats[pp];
		c.translate(x, y, z);
		return this;
	}

	public MatrixStack translate(Vec3 xyz) {
		if (xyz == null) {
			throw new IllegalArgumentException("xyz must not be null");
		}
		translate(xyz.x, xyz.y, xyz.z);
		return this;
	}

	public MatrixStack scale(float x, float y, float z) {
		Mat4 c = mats[pp];
		c.scale(x, y, z);
		return this;
	}

	public MatrixStack scale(Vec3 xyz) {
		if (xyz == null) {
			throw new IllegalArgumentException("xyz must not be null");
		}
		this.scale(xyz.x, xyz.y, xyz.z);
		return this;
	}

	public MatrixStack scale(float xyz) {
		return scale(xyz, xyz, xyz);
	}

	public MatrixStack rotX(float ang) {
		mats[pp].rotX(ang);
		return this;
	}

	public MatrixStack rotY(float ang) {
		mats[pp].rotY(ang);
		return this;
	}

	public MatrixStack rotZ(float ang) {
		mats[pp].rotZ(ang);
		return this;
	}

	public MatrixStack loadIdentity() {
		mats[pp].identity();
		return this;
	}

	public MatrixStack multMatrix(Mat4 mat) {
		if (mat == null) {
			throw new IllegalArgumentException("mat must not be null"); //$NON-NLS-1$
		}
		mats[pp].mul(mat);
		return this;
	}

	public MatrixStack lookAt(Vec3 position, Vec3 centre, Vec3 up) {
		mats[pp].gluLookAt(position, centre, up);
		return this;
	}

	public MatrixStack perspective(float fovy, float aspect, float zNear, float zFar) {
		mats[pp].gluPerspective(fovy, aspect, zNear, zFar);
		return this;
	}

	public MatrixStack ortho(float left, float right, float bottom, float top, float near, float far) {
		mats[pp].gluOrthographic(left, right, bottom, top, near, far);
		return this;
	}
}