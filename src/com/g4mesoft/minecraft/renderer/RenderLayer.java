package com.g4mesoft.minecraft.renderer;

public enum RenderLayer {

	BLOCK_LAYER(0),
	SPRITE_LAYER(1);

	public static final RenderLayer[] LAYERS;
	
	static {
		LAYERS = new RenderLayer[values().length];
		
		for (RenderLayer layer : values())
			LAYERS[layer.getIndex()] = layer;
	}
	
	private final int index;
	private final int flag;
	
	private RenderLayer(int index) {
		this.index = index;
		
		flag = 0x01 << index;
		
		if (flag == 0)
			throw new IllegalArgumentException("Invalid flag for " + toString());
	}
	
	public int getIndex() {
		return index;
	}

	public int getFlag() {
		return flag;
	}
}
