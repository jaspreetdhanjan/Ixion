package com.ixion.client.renderer;

import vecmath.Mat4;
import vecmath.Vec3;

public class FrustumCuller {
	private float[][] frustum = new float[6][4];
	private Mat4 clipMatrix = new Mat4();

	public void updatePlanes(Mat4 projectionMatrix, Mat4 modelViewMatrix) {
		clipMatrix.m00 = (modelViewMatrix.m00 * projectionMatrix.m00) + (modelViewMatrix.m01 * projectionMatrix.m10) + (modelViewMatrix.m02 * projectionMatrix.m20) + (modelViewMatrix.m03 * projectionMatrix.m30);
		clipMatrix.m01 = (modelViewMatrix.m00 * projectionMatrix.m01) + (modelViewMatrix.m01 * projectionMatrix.m11) + (modelViewMatrix.m02 * projectionMatrix.m21) + (modelViewMatrix.m03 * projectionMatrix.m31);
		clipMatrix.m02 = (modelViewMatrix.m00 * projectionMatrix.m02) + (modelViewMatrix.m01 * projectionMatrix.m12) + (modelViewMatrix.m02 * projectionMatrix.m22) + (modelViewMatrix.m03 * projectionMatrix.m32);
		clipMatrix.m03 = (modelViewMatrix.m00 * projectionMatrix.m03) + (modelViewMatrix.m01 * projectionMatrix.m13) + (modelViewMatrix.m02 * projectionMatrix.m23) + (modelViewMatrix.m03 * projectionMatrix.m33);
		clipMatrix.m10 = (modelViewMatrix.m10 * projectionMatrix.m00) + (modelViewMatrix.m11 * projectionMatrix.m10) + (modelViewMatrix.m12 * projectionMatrix.m20) + (modelViewMatrix.m13 * projectionMatrix.m30);
		clipMatrix.m11 = (modelViewMatrix.m10 * projectionMatrix.m01) + (modelViewMatrix.m11 * projectionMatrix.m11) + (modelViewMatrix.m12 * projectionMatrix.m21) + (modelViewMatrix.m13 * projectionMatrix.m31);
		clipMatrix.m12 = (modelViewMatrix.m10 * projectionMatrix.m02) + (modelViewMatrix.m11 * projectionMatrix.m12) + (modelViewMatrix.m12 * projectionMatrix.m22) + (modelViewMatrix.m13 * projectionMatrix.m32);
		clipMatrix.m13 = (modelViewMatrix.m10 * projectionMatrix.m03) + (modelViewMatrix.m11 * projectionMatrix.m13) + (modelViewMatrix.m12 * projectionMatrix.m23) + (modelViewMatrix.m13 * projectionMatrix.m33);
		clipMatrix.m20 = (modelViewMatrix.m20 * projectionMatrix.m00) + (modelViewMatrix.m21 * projectionMatrix.m10) + (modelViewMatrix.m22 * projectionMatrix.m20) + (modelViewMatrix.m23 * projectionMatrix.m30);
		clipMatrix.m21 = (modelViewMatrix.m20 * projectionMatrix.m01) + (modelViewMatrix.m21 * projectionMatrix.m11) + (modelViewMatrix.m22 * projectionMatrix.m21) + (modelViewMatrix.m23 * projectionMatrix.m31);
		clipMatrix.m22 = (modelViewMatrix.m20 * projectionMatrix.m02) + (modelViewMatrix.m21 * projectionMatrix.m12) + (modelViewMatrix.m22 * projectionMatrix.m22) + (modelViewMatrix.m23 * projectionMatrix.m32);
		clipMatrix.m23 = (modelViewMatrix.m20 * projectionMatrix.m03) + (modelViewMatrix.m21 * projectionMatrix.m13) + (modelViewMatrix.m22 * projectionMatrix.m23) + (modelViewMatrix.m23 * projectionMatrix.m33);
		clipMatrix.m30 = (modelViewMatrix.m30 * projectionMatrix.m00) + (modelViewMatrix.m31 * projectionMatrix.m10) + (modelViewMatrix.m32 * projectionMatrix.m20) + (modelViewMatrix.m33 * projectionMatrix.m30);
		clipMatrix.m31 = (modelViewMatrix.m30 * projectionMatrix.m01) + (modelViewMatrix.m31 * projectionMatrix.m11) + (modelViewMatrix.m32 * projectionMatrix.m21) + (modelViewMatrix.m33 * projectionMatrix.m31);
		clipMatrix.m32 = (modelViewMatrix.m30 * projectionMatrix.m02) + (modelViewMatrix.m31 * projectionMatrix.m12) + (modelViewMatrix.m32 * projectionMatrix.m22) + (modelViewMatrix.m33 * projectionMatrix.m32);
		clipMatrix.m33 = (modelViewMatrix.m30 * projectionMatrix.m03) + (modelViewMatrix.m31 * projectionMatrix.m13) + (modelViewMatrix.m32 * projectionMatrix.m23) + (modelViewMatrix.m33 * projectionMatrix.m33);

		frustum[0][0] = clipMatrix.m03 - clipMatrix.m00;
		frustum[0][1] = clipMatrix.m13 - clipMatrix.m10;
		frustum[0][2] = clipMatrix.m23 - clipMatrix.m20;
		frustum[0][3] = clipMatrix.m33 - clipMatrix.m30;
		normalise(frustum, 0);

		frustum[1][0] = clipMatrix.m03 + clipMatrix.m00;
		frustum[1][1] = clipMatrix.m13 + clipMatrix.m10;
		frustum[1][2] = clipMatrix.m23 + clipMatrix.m20;
		frustum[1][3] = clipMatrix.m33 + clipMatrix.m30;
		normalise(frustum, 1);

		frustum[2][0] = clipMatrix.m03 + clipMatrix.m01;
		frustum[2][1] = clipMatrix.m13 + clipMatrix.m11;
		frustum[2][2] = clipMatrix.m23 + clipMatrix.m21;
		frustum[2][3] = clipMatrix.m33 + clipMatrix.m31;
		normalise(frustum, 2);

		frustum[3][0] = clipMatrix.m03 - clipMatrix.m01;
		frustum[3][1] = clipMatrix.m13 - clipMatrix.m11;
		frustum[3][2] = clipMatrix.m23 - clipMatrix.m21;
		frustum[3][3] = clipMatrix.m33 - clipMatrix.m31;
		normalise(frustum, 3);

		frustum[4][0] = clipMatrix.m03 - clipMatrix.m02;
		frustum[4][1] = clipMatrix.m13 - clipMatrix.m12;
		frustum[4][2] = clipMatrix.m23 - clipMatrix.m22;
		frustum[4][3] = clipMatrix.m33 - clipMatrix.m32;
		normalise(frustum, 4);

		frustum[5][0] = clipMatrix.m03 + clipMatrix.m02;
		frustum[5][1] = clipMatrix.m13 + clipMatrix.m12;
		frustum[5][2] = clipMatrix.m23 + clipMatrix.m22;
		frustum[5][3] = clipMatrix.m33 + clipMatrix.m32;
		normalise(frustum, 5);
	}

	private void normalise(float frustum[][], int wall) {
		float xd = frustum[wall][0];
		float yd = frustum[wall][1];
		float zd = frustum[wall][2];
		float dd = (float) Math.sqrt(xd * xd + yd * yd + zd * zd);

		int pp = 0;
		frustum[wall][pp++] /= dd;
		frustum[wall][pp++] /= dd;
		frustum[wall][pp++] /= dd;
		frustum[wall][pp++] /= dd;
	}

	public boolean pointInFrustum(Vec3 pos) {
		for (int i = 0; i < 6; i++) {
			if (frustum[i][0] * pos.x + frustum[i][1] * pos.y + frustum[i][2] * pos.z + frustum[i][3] <= 0) return false;
		}
		return true;
	}

	public boolean sphereInFrustum(Vec3 pos, float radius) {
		for (int i = 0; i < 6; i++) {
			if (frustum[i][0] * pos.x + frustum[i][1] * pos.y + frustum[i][2] * pos.z + frustum[i][3] <= -radius) return false;
		}
		return true;
	}

	public boolean cubeInFrustum(float x, float y, float z, float xs, float ys, float zs) {
		for (int i = 0; i < 6; i++) {
			int c = 0;
			if (frustum[i][0] * (x - xs) + frustum[i][1] * (y - ys) + frustum[i][2] * (z - zs) + frustum[i][3] > 0) c++;
			if (frustum[i][0] * (x + xs) + frustum[i][1] * (y - ys) + frustum[i][2] * (z - zs) + frustum[i][3] > 0) c++;
			if (frustum[i][0] * (x - xs) + frustum[i][1] * (y + ys) + frustum[i][2] * (z - zs) + frustum[i][3] > 0) c++;
			if (frustum[i][0] * (x + xs) + frustum[i][1] * (y + ys) + frustum[i][2] * (z - zs) + frustum[i][3] > 0) c++;
			if (frustum[i][0] * (x - xs) + frustum[i][1] * (y - ys) + frustum[i][2] * (z + zs) + frustum[i][3] > 0) c++;
			if (frustum[i][0] * (x + xs) + frustum[i][1] * (y - ys) + frustum[i][2] * (z + zs) + frustum[i][3] > 0) c++;
			if (frustum[i][0] * (x - xs) + frustum[i][1] * (y + ys) + frustum[i][2] * (z + zs) + frustum[i][3] > 0) c++;
			if (frustum[i][0] * (x + xs) + frustum[i][1] * (y + ys) + frustum[i][2] * (z + zs) + frustum[i][3] > 0) c++;
			if (c == 0) return false;
		}
		return true;
	}
}