package com.ixion.client.resource;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ixion.client.renderer.FrustumCuller;
import com.ixion.client.renderer.Tessellator;

import de.longor1996.util.objloader.IOBJOutput;
import de.longor1996.util.objloader.OBJLoader;
import de.longor1996.util.objloader.OBJLoader.OBJLoaderException;
import vecmath.Vec3;

public class Model extends Resource implements IOBJOutput {
	private final String path;

	public List<int[]> faces = new ArrayList<int[]>();
	public List<Vec3> normals = new ArrayList<Vec3>();
	public List<Vec3> vertices = new ArrayList<Vec3>();
	public List<Vec3> texCoords = new ArrayList<Vec3>();

	public Model(String name) {
		super(ResourceType.MODEL);
		path = "/models/" + name + ".obj";
	}

	public void create() {
		try {
			OBJLoader ol = new OBJLoader();
			ol.setInput(new Scanner(getClass().getResourceAsStream(path)));
			ol.setOutput(this);
			ol.setTessellate(true);
			ol.setNormalizeNormals(true);
			ol.setSubtractOneFromIndices(true);
			ol.setClampTextureCoordinates(false);
			ol.process();
		} catch (OBJLoaderException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		faces.clear();
		normals.clear();
		vertices.clear();
		texCoords.clear();
	}

	public String getLoadingString() {
		return "Model -> " + path;
	}

	public void outputVertex(float x, float y, float z) {
		vertices.add(new Vec3(x, y, z));
	}

	public void outputNormal(float x, float y, float z) {
		normals.add(new Vec3(x, y, z));
	}

	public void outputTextureCoodinate(float u, float v, float w) {
		texCoords.add(new Vec3(u, v, w));
	}

	public void outputFace(int pointCount, List<int[]> points) {
		faces.addAll(points);
	}

	public void outputObjectGroup(String objectGroupName) {
	}

	public void outputPolygonGroup(String polygonGroupName) {
	}

	public void outputMTLLibDefinition(String materialLibraryFileName) {
	}

	public void outputMaterialBind(String materialName) {
	}

	public void outputSmoothingGroup(int smoothingGroup) {
	}

	public void onProcessingStart(OBJLoader objLoader) {
	}

	public void onProcessingDone(OBJLoader objLoader) {
	}

	public boolean processLine(String line) {
		return false;
	}

	public void render(FrustumCuller culler, int colour) {
		Tessellator t = Tessellator.instance;
		t.begin(GL_TRIANGLES);

		boolean[] noRender = new boolean[faces.size()];
		for (int i = 0; i < noRender.length - 2; i += 3) {
			if (culler == null) break;

			int p0 = faces.get(i + 0)[0];
			int p1 = faces.get(i + 1)[0];
			int p2 = faces.get(i + 2)[0];

			Vec3 n0 = vertices.get(p0);
			Vec3 n1 = vertices.get(p1);
			Vec3 n2 = vertices.get(p2);

			boolean b0 = !culler.pointInFrustum(n0);
			boolean b1 = !culler.pointInFrustum(n1);
			boolean b2 = !culler.pointInFrustum(n2);

			noRender[i + 0] = b0 && b1 && b2;
			noRender[i + 1] = b0 && b1 && b2;
			noRender[i + 2] = b0 && b1 && b2;
		}

		for (int i = 0; i < faces.size(); i++) {
			if (noRender[i]) continue;

			int pp = 0;
			int vp = faces.get(i)[pp++];
			int tp = faces.get(i)[pp++];
			int np = faces.get(i)[pp++];

			Vec3 vertex = vertices.get(vp);
			Vec3 texCoord = texCoords.get(tp);
			Vec3 normal = normals.get(np);

			t.colour(colour);
			t.normal(normal.x, normal.y, normal.z);
			t.texCoord(texCoord.x, texCoord.y);
			t.vertex(vertex.x, vertex.y, vertex.z);
		}
		t.end();
	}
}