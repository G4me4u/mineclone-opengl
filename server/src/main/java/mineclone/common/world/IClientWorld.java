package mineclone.common.world;

import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.entity.PlayerEntity;

public interface IClientWorld extends IWorld {

	public IWorldChunk getChunk(IChunkPosition chunkPos);
	
	public boolean setChunk(IChunkPosition chunkPos, IWorldChunk chunk);
	
	public PlayerEntity getPlayer();

}
