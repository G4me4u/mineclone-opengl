package com.g4mesoft.minecraft.world.block;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {

	private final Map<String, Block> blocks;

	public BlockRegistry() {
		blocks = new HashMap<String, Block>();
	}
	
	public Block addBlock(String name, Block block) {
		if (name == null)
			throw new NullPointerException("Name is null");
		if (block == null)
			throw new NullPointerException("Block is null");
		
		if (blocks.containsKey(name))
			throw new DuplicateBlockNameException(name, block);
		
		blocks.put(name, block);
		
		return block;
	}
	
	public boolean hasBlockEntry(String name) {
		return getBlock(name) != null;
	}
	
	public Block getBlock(String name) {
		return blocks.get(name);
	}

	public Block getBlock(String name, Block defaultBlock) {
		Block block = getBlock(name);
		return block != null ? block : defaultBlock;
	}
}
