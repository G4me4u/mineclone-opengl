package mineclone.common.world.chunk;

import mineclone.common.world.Direction;

public interface IChunkPosition {

	public int getChunkX();
	
	public int getChunkY();
	
	public int getChunkZ();
	
	default public IChunkPosition north() {
		return offset(Direction.NORTH);
	}
	
	default public IChunkPosition south() {
		return offset(Direction.SOUTH);
	}
	
	default public IChunkPosition west() {
		return offset(Direction.WEST);
	}
	
	default public IChunkPosition east() {
		return offset(Direction.EAST);
	}
	
	default public IChunkPosition down() {
		return offset(Direction.DOWN);
	}
	
	default public IChunkPosition up() {
		return offset(Direction.UP);
	}
	
	default public IChunkPosition offset(Direction dir) {
		return offset(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());
	}
	
	default public IChunkPosition offset(Direction dir, int amount) {
		return offset(dir.getOffsetX() * amount, dir.getOffsetY() * amount, dir.getOffsetZ() * amount);
	}

	public IChunkPosition offset(int xo, int yo, int zo);
	
	public static int hashCode(IChunkPosition chunkPos) {
		int hash = 0;
		hash = 31 * (hash + chunkPos.getChunkZ());
		hash = 31 * (hash + chunkPos.getChunkY());
		hash = 31 * (hash + chunkPos.getChunkX());
		return hash;
	}
	
	default public boolean equals(int x, int y, int z) {
		return (getChunkX() == x && getChunkY() == y && getChunkZ() == z);
	}

	default public boolean equals(IChunkPosition chunkPos) {
		return equals(chunkPos.getChunkX(), chunkPos.getChunkY(), chunkPos.getChunkZ());
	}
	
}
