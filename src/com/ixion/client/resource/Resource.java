package com.ixion.client.resource;

import com.ixion.client.ResourceManager;

public abstract class Resource {
	public enum ResourceType {
		SHADER, TEXTURE, MODEL
	}
	
	public ResourceType type;

	public Resource(ResourceType type) {
		this.type = type;
		ResourceManager.add(this);
	}

	public final ResourceType getType() {
		return type;
	}

	public abstract void create();

	public abstract void delete();

	public String getLoadingString() {
		return null;
	}
}