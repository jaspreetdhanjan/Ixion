/*
 * Decompiled with CFR 0_102.
 */
package de.longor1996.util.objloader;

import de.longor1996.util.objloader.IOBJOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OBJLoader implements Runnable {
	private int stat_vcount;
	private int stat_vtcount;
	private int stat_vncount;
	private int stat_fcount;
	private Scanner input;
	private IOBJOutput output;
	private boolean normalize;
	private boolean tessellate;
	private boolean indexMinusOne;
	private boolean clampTexCoords;
	private int lineNumber;
	private OBJLoaderException exception;
	private List<int[]> tempVertices;

	public static void loadModel(Scanner input, IOBJOutput output) throws OBJLoaderException {
		OBJLoader ol = new OBJLoader();
		ol.input = input;
		ol.output = output;
		ol.normalize = false;
		ol.tessellate = true;
		ol.indexMinusOne = true;
		ol.clampTexCoords = false;
		ol.process();
	}

	public void setTessellate(boolean flag) {
		this.tessellate = flag;
	}

	public void setSubtractOneFromIndices(boolean flag) {
		this.indexMinusOne = flag;
	}

	public void setNormalizeNormals(boolean flag) {
		this.normalize = flag;
	}

	public void setClampTextureCoordinates(boolean flag) {
		this.clampTexCoords = flag;
	}

	public void setInput(Scanner input) {
		this.input = input;
	}

	public void setOutput(IOBJOutput output) {
		this.output = output;
	}

	public OBJLoaderException getException() {
		return exception;
	}

	public int getVertexCount() {
		return stat_vcount;
	}

	public int getVertexNormalCount() {
		return stat_vncount;
	}

	public int getVertexTextureCoordinateCount() {
		return stat_vtcount;
	}

	public int getFaceCount() {
		return stat_fcount;
	}

	@Override
	public void run() {
		try {
			process();
		} catch (OBJLoaderException e) {
			e.printStackTrace();
			exception = e;
		}
	}

	public void process() throws OBJLoaderException {
		output.onProcessingStart(this);
		lineNumber = 0;
		tempVertices = new ArrayList<int[]>();
		String line = null;
		while (input.hasNextLine()) {
			line = input.nextLine().trim();
			++lineNumber;
			if (line.isEmpty()) continue;
			if (line.charAt(0) == '#') continue;
			processLine(line);
		}
		output.onProcessingDone(this);
		input.close();
	}

	public void processLine(String line) throws OBJLoaderException {
		if (line.startsWith("v ")) {
			line = line.substring(2).trim();
			processVertex(line);
			return;
		}
		if (line.startsWith("vn ")) {
			line = line.substring(3).trim();
			processNormal(line);
			return;
		}
		if (line.startsWith("vt ")) {
			line = line.substring(3).trim();
			processTextureCoordinate(line);
			return;
		}
		if (line.startsWith("f ")) {
			line = line.substring(2).trim();
			processFace(line);
			return;
		}
		if (line.startsWith("o ")) {
			line = line.substring(2).trim();
			output.outputObjectGroup(line);
			return;
		}
		if (line.startsWith("g ")) {
			line = line.substring(2).trim();
			output.outputPolygonGroup(line);
			return;
		}
		if (line.startsWith("s ")) {
			if ((line = line.substring(2).trim()).equalsIgnoreCase("off")) {
				output.outputSmoothingGroup(-1);
			} else {
				output.outputSmoothingGroup(parseInteger(line));
			}
			return;
		}
		if (line.startsWith("mtllib ")) {
			line = line.substring(7).trim();
			output.outputMTLLibDefinition(line);
			return;
		}
		if (line.startsWith("usemtl ")) {
			line = line.substring(7).trim();
			output.outputMaterialBind(line);
			return;
		}
		if (line.startsWith("--l19loader ")) {
			if ((line = line.substring(12)).startsWith("tessellate ")) {
				tessellate = parseBoolean(line.substring(11));
				return;
			}
			if (line.startsWith("normalize ")) {
				normalize = parseBoolean(line.substring(10));
				return;
			}
			if (line.startsWith("subtractindices ")) {
				indexMinusOne = parseBoolean(line.substring(16));
				return;
			}
			throw new OBJLoaderException(this, "Unknown --l19loader directive: " + line);
		}
		if (output.processLine(line)) {
			return;
		}
		throw new OBJLoaderException(this, "Unknown directive: " + line);
	}

	private void processVertex(String line) throws OBJLoaderException {
		float z;
		float y;
		float x;
		String[] vectorStr = line.split(" ");
		if (vectorStr.length == 3) {
			x = parseFloat(vectorStr[0]);
			y = parseFloat(vectorStr[1]);
			z = parseFloat(vectorStr[2]);
		} else if (vectorStr.length == 2) {
			x = parseFloat(vectorStr[0]);
			y = parseFloat(vectorStr[1]);
			z = 0.0f;
		} else {
			throw new OBJLoaderException(this, "Incorrect number of vector components in vertex.");
		}
		output.outputVertex(x, y, z);
		++stat_vcount;
	}

	private void processNormal(String line) throws OBJLoaderException {
		String[] vectorStr = line.split(" ");
		if (vectorStr.length != 3) {
			throw new OBJLoaderException(this, "Incorrect number of vector components in normal.");
		}
		float x = parseFloat(vectorStr[0]);
		float y = parseFloat(vectorStr[1]);
		float z = parseFloat(vectorStr[2]);
		if (normalize) {
			float c = x * x + y * y + z * z;
			if (c <= 0.0f) {
				throw new OBJLoaderException(this, "Normal cannot be normalized. X*X+Y*Y+Z*Z <= 0.");
			}
			float sqrt = (float) Math.sqrt(c);
			if (c <= 0.0f) {
				throw new OBJLoaderException(this, "Normal cannot be normalized. sqrt(X*X+Y*Y+Z*Z) <= 0.");
			}
			x /= sqrt;
			y /= sqrt;
			z /= sqrt;
		}
		output.outputNormal(x, y, z);
		++stat_vncount;
	}

	private void processTextureCoordinate(String line) throws OBJLoaderException {
		float w;
		float v;
		float u;
		String[] vectorStr = line.split(" ");
		if (vectorStr.length == 2) {
			u = parseFloat(vectorStr[0]);
			v = parseFloat(vectorStr[1]);
			w = 0.0f;
		} else if (vectorStr.length == 3) {
			u = parseFloat(vectorStr[0]);
			v = parseFloat(vectorStr[1]);
			w = parseFloat(vectorStr[2]);
		} else {
			throw new OBJLoaderException(this, "Incorrect number of vector components in texture-coordinate.");
		}
		if (clampTexCoords) {
			u = OBJLoader.clamp(0.0f, 1.0f, u);
			v = OBJLoader.clamp(0.0f, 1.0f, u);
			w = OBJLoader.clamp(0.0f, 1.0f, u);
		}
		output.outputTextureCoodinate(u, v, w);
		++stat_vtcount;
	}

	private void processFace(String line) throws OBJLoaderException {
		String[] faceStr = line.split(" ");
		int nPoints = faceStr.length;
		tempVertices.clear();
		if (nPoints < 3) {
			throw new OBJLoaderException(this, "A face must have 3 or more vertices.");
		}
		if (nPoints == 3) {
			Object out = null;
			int[] arrn = new int[3];
			out = arrn;
			parseVertice(faceStr[0], arrn);
			tempVertices.add((int[]) out);
			int[] arrn2 = new int[3];
			out = arrn2;
			parseVertice(faceStr[1], arrn2);
			tempVertices.add((int[]) out);
			int[] arrn3 = new int[3];
			out = arrn3;
			parseVertice(faceStr[2], arrn3);
			tempVertices.add((int[]) out);
			output.outputFace(tempVertices.size(), tempVertices);
			++stat_fcount;
			return;
		}
		if (nPoints == 4 && tessellate) {
			int[] a = new int[3];
			int[] b = new int[3];
			int[] c = new int[3];
			int[] d = new int[3];
			parseVertice(faceStr[0], a);
			parseVertice(faceStr[1], b);
			parseVertice(faceStr[2], c);
			parseVertice(faceStr[3], d);
			tempVertices.add(a);
			tempVertices.add(b);
			tempVertices.add(c);
			tempVertices.add(a);
			tempVertices.add(c);
			tempVertices.add(d);
			output.outputFace(tempVertices.size(), tempVertices);
			++stat_fcount;
			return;
		}
		for (int i = 0; i < nPoints; ++i) {
			int[] out = new int[3];
			parseVertice(faceStr[i], out);
			tempVertices.add(out);
		}
		++stat_fcount;
		output.outputFace(tempVertices.size(), tempVertices);
	}

	private void parseVertice(String string, int[] is) throws OBJLoaderException {
		if (string.indexOf(47) != -1) {
			String[] parts = OBJLoader.split(string, '/', 3);
			for (int i = 0; i < 3; ++i) {
				String vStr = parts[i];
				is[i] = vStr != null ? (indexMinusOne ? parseInteger(vStr) - 1 : parseInteger(vStr)) : -1;
			}
		} else {
			is[0] = parseInteger(string);
			is[1] = -1;
			is[2] = -1;
		}
	}

	public static final float clamp(float min, float max, float value) {
		return value < min ? min : (value > max ? max : value);
	}

	public static final String[] split(String input, char splitter, int minOutSize) {
		int index;
		int splitCount = 0;
		int inLen = input.length();
		for (int i = 0; i < inLen; ++i) {
			if (input.charAt(i) != splitter) continue;
			++splitCount;
		}
		if (splitCount == 0) {
			return new String[] { input };
		}
		int outSize = splitCount + 1;
		if (outSize < minOutSize) {
			outSize = minOutSize;
		}
		String[] output = new String[outSize];
		int start = 0;
		int axind = 0;
		for (index = 0; index < inLen; ++index) {
			char cAt = input.charAt(index);
			if (cAt != splitter) continue;
			output[axind++] = start == index ? null : input.substring(start, index);
			start = index + 1;
		}
		if (inLen - index + 1 > 0) {
			output[axind++] = start == index ? null : input.substring(start, index);
		}
		return output;
	}

	public final float parseFloat(String string) throws OBJLoaderException {
		try {
			return Float.valueOf(string).floatValue();
		} catch (NumberFormatException ex) {
			throw new OBJLoaderException(this, "Failed to parse floating-point number: " + string, ex);
		}
	}

	public final int parseInteger(String string) throws OBJLoaderException {
		try {
			return Integer.valueOf(string);
		} catch (NumberFormatException ex) {
			throw new OBJLoaderException(this, "Failed to parse integer number: " + string, ex);
		}
	}

	public final boolean parseBoolean(String string) throws OBJLoaderException {
		try {
			return Boolean.valueOf(string);
		} catch (NumberFormatException ex) {
			throw new OBJLoaderException(this, "Failed to parse boolean: " + string, ex);
		}
	}

	public class OBJLoaderException extends Exception {
		private static final long serialVersionUID = -8819289391959558856L;

		public final OBJLoader this$0;

		public OBJLoaderException(OBJLoader oBJLoader, String msg) {
			super(String.valueOf(msg) + " @Line " + oBJLoader.lineNumber);
			this.this$0 = oBJLoader;
		}

		public OBJLoaderException(OBJLoader oBJLoader, String msg, Exception ex) {
			super(String.valueOf(msg) + " @Line " + oBJLoader.lineNumber, ex);
			this.this$0 = oBJLoader;
		}
	}
}
