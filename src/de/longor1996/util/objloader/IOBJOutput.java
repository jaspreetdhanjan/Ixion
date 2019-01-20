package de.longor1996.util.objloader;

import java.util.List;

public interface IOBJOutput {
	public void outputVertex(float x, float y, float z);

	public void outputNormal(float x, float y, float z);

	public void outputTextureCoodinate(float u, float v, float w);

	public void outputFace(int pointCount, List<int[]> points);

	public void outputObjectGroup(String objectGroupName);

	public void outputPolygonGroup(String polygonGroupName);

	public void outputMTLLibDefinition(String materialLibraryFileName);

	public void outputMaterialBind(String materialName);

	public void outputSmoothingGroup(int smoothingGroup);

	public void onProcessingStart(OBJLoader objLoader);

	public void onProcessingDone(OBJLoader objLoader);

	public boolean processLine(String line);
}