package mineclone.client.world;

import mineclone.client.MinecloneClient;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.block.Block;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.ImmutableBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.entity.PlayerEntity;
import mineclone.server.world.ServerWorld;

public class ClientWorld extends ServerWorld implements IClientWorld {

	private final MinecloneClient app;
	
	protected final PlayerEntity player;
	
	public ClientWorld(MinecloneClient app) {
		this.app = app;

		player = new PlayerEntity(this, app.getController());
	}
	
	@Override
	public IWorldChunk getChunk(IChunkPosition chunkPos) {
		return chunkManager.getChunk(chunkPos);
	}
	
	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState state, boolean updateNeighbors) {
		int oldHighestPoint = getHighestPoint(pos);
		IBlockState oldState = getBlockState(pos);
			
		if (super.setBlockState(pos, state, updateNeighbors)) {
			int highestPoint = getHighestPoint(pos);
			
			if (oldHighestPoint != highestPoint) {
				int x = pos.getX();
				int z = pos.getZ();
				
				markRangeDirty(new ImmutableBlockPosition(x, oldHighestPoint, z), 
				               new ImmutableBlockPosition(x,    highestPoint, z), true);
			} else {
				Block oldBlock = oldState.getBlock();
				Block newBlock = state.getBlock();
				
				if (oldBlock.isSolid() || newBlock.isSolid()) {
					markDirty(pos, (oldBlock != newBlock));
				} else {
					markDirty(pos, false);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	private void markRangeDirty(IBlockPosition p0, IBlockPosition p1, boolean includeBorders) {
		app.getWorldRenderer().markRangeDirty(p0, p1, includeBorders);
	}

	private void markDirty(IBlockPosition pos, boolean includeBorders) {
		app.getWorldRenderer().markDirty(pos, includeBorders);
	}
	
	@Override
	public void update() {
		super.update();

		player.update();
	}

	@Override
	public PlayerEntity getPlayer() {
		return player;
	}
}
