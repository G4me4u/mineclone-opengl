package mineclone.common.world.chunk;

import mineclone.common.world.block.IBlockPosition;

public final class ChunkPosition implements IChunkPosition {

	public int chunkX;
	public int chunkY;
	public int chunkZ;

	public ChunkPosition() {
		this(0, 0, 0);
	}

	public ChunkPosition(IBlockPosition pos) {
		chunkX = pos.getX() >>> IChunk.CHUNK_SHIFT;
		chunkY = pos.getY() >>> IChunk.CHUNK_SHIFT;
		chunkZ = pos.getZ() >>> IChunk.CHUNK_SHIFT;
	}

	public ChunkPosition(IChunkPosition chunkPos) {
		this(chunkPos.getChunkX(), chunkPos.getChunkY(), chunkPos.getChunkZ());
	}
	
	public ChunkPosition(int chunkX, int chunkY, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
	}
	
	@Override
	public int getChunkX() {
		return chunkX;
	}

	@Override
	public int getChunkY() {
		return chunkY;
	}
	
	@Override
	public int getChunkZ() {
		return chunkZ;
	}

	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}

	public void setChunkY(int chunkY) {
		this.chunkY = chunkY;
	}
	
	public void setChunkZ(int chunkZ) {
		this.chunkZ = chunkZ;
	}
	
	public void set(IChunkPosition chunkPos) {
		this.chunkX = chunkPos.getChunkX();
		this.chunkY = chunkPos.getChunkY();
		this.chunkZ = chunkPos.getChunkZ();
	}

	@Override
	public ChunkPosition offset(int xo, int yo, int zo) {
		return new ChunkPosition(chunkX + xo, chunkY + yo, chunkZ + zo);
	}

	@Override
	public int hashCode() {
		return IChunkPosition.hashCode(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IChunkPosition)
			return equals((IChunkPosition)obj);
		return false;
	}
}
