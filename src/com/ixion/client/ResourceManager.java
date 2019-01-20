package com.ixion.client;

import java.util.ArrayList;
import java.util.List;

import com.ixion.client.resource.Resource;
import com.ixion.client.resource.Shader;
import com.ixion.client.resource.Texture;

public class ResourceManager {
	private static final List<Resource> resources = new ArrayList<Resource>();

	// public static Shader basicShader = new Shader("basicShader");
	public static Shader guiShader = new Shader("guiShader");
	public static Shader phongShader = new Shader("phongShader");

	// public static Texture testTexture = new Texture("icon32");
	public static Texture fontTexture = new Texture("font");

	// public static Model testModel = new Model("dragon");

	public static void add(Resource resource) {
		resources.add(resource);
	}

	public static void loadAll(boolean verbose) {
		for (Resource resource : resources) {
			resource.create();

			String loadingString = resource.getLoadingString();
			if (loadingString != null && verbose) {
				System.out.println("Loaded -> " + loadingString);
			}
		}
	}

	public static void deleteAll() {
		for (Resource resource : resources) {
			resource.delete();
		}
	}
}