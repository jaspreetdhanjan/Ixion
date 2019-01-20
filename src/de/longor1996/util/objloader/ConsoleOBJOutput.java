package de.longor1996.util.objloader;

import java.util.*;

public class ConsoleOBJOutput implements IOBJOutput {
	public void outputVertex(float x, float y, float z) {
		System.out.println("vertex " + x + " " + y + " " + z);
	}

	public void outputNormal(float x, float y, float z) {
		System.out.println("normal " + x + " " + y + " " + z);
	}

	public void outputTextureCoodinate(float u, float v, float w) {
		System.out.println("texcoord " + u + " " + v + " " + w);
	}

	public void outputFace(int pointCount, List<int[]> tempPoints) {
		System.out.print("face [");
		for (int i = 0; i < pointCount; ++i) {
			System.out.print(Arrays.toString(tempPoints.get(i)));
		}
		System.out.println("]");
	}

	public void outputObjectGroup(String objectGroupName) {
		System.out.println("polygroupgroup " + objectGroupName);
	}

	public void outputPolygonGroup(String polygonGroupName) {
		System.out.println("polygroup " + polygonGroupName);
	}

	public void outputMTLLibDefinition(String materialLibraryFileName) {
		System.out.println("define material library " + materialLibraryFileName);
	}

	public void outputMaterialBind(String materialName) {
		System.out.println("material " + materialName);
	}

	public void outputSmoothingGroup(int smoothingGroup) {
		System.out.println("smoothingroup " + smoothingGroup);
	}

	public void onProcessingStart(OBJLoader objLoader) {
	}

	public void onProcessingDone(OBJLoader objLoader) {
	}

	public boolean processLine(String line) {
		return false;
	}
}