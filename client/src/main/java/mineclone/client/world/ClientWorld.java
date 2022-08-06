package mineclone.client.world;

import mineclone.client.MinecloneClient;
import mineclone.client.renderer.world.WorldRenderer;
import mineclone.client.renderer.world.entity.ClientPlayerEntity;
import mineclone.common.world.IClientWorld;
import mineclone.common.world.World;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.ImmutableBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;

public class ClientWorld extends World implements IClientWorld {

	private final MinecloneClient client;
	
	protected final ClientPlayerEntity player;
	
	public ClientWorld(MinecloneClient client) {
		super(new ClientWorldChunkManager());
		
		this.client = client;

		// Add the player outside of the constructor...
		player = new ClientPlayerEntity(this, client);
		addEntity(player);
	}
	
	@Override
	public IWorldChunk getChunk(IChunkPosition chunkPos) {
		return chunkManager.getChunk(chunkPos);
	}
	
	@Override
	public boolean setChunk(IChunkPosition chunkPos, IWorldChunk chunk) {
		if (chunkManager.setChunk(chunkPos, chunk)) {
			int chunkX = chunkPos.getChunkX();
			int chunkY = chunkPos.getChunkY();
			int chunkZ = chunkPos.getChunkZ();

			WorldRenderer renderer = client.getWorldRenderer();
			renderer.markChunksDirty(chunkX - 1, 0     , chunkZ - 1,
			                         chunkX + 1, chunkY, chunkZ + 1);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState state) {
		int oldHighestPoint = getHighestPoint(pos);
		IBlockState oldState = getBlockState(pos);
			
		if (chunkManager.setBlockState(pos, state)) {
			int highestPoint = getHighestPoint(pos);
			
			if (oldHighestPoint != highestPoint) {
				int x = pos.getX();
				int z = pos.getZ();
				
				markRangeDirty(new ImmutableBlockPosition(x, oldHighestPoint, z), 
				               new ImmutableBlockPosition(x,    highestPoint, z), true);
			} else {
				IBlock oldBlock = oldState.getBlock();
				IBlock newBlock = state.getBlock();
				
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
		client.getWorldRenderer().markRangeDirty(p0, p1, includeBorders);
	}

	private void markDirty(IBlockPosition pos, boolean includeBorders) {
		client.getWorldRenderer().markDirty(pos, includeBorders);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public ClientWorldChunkManager getChunkManager() {
		return (ClientWorldChunkManager)super.getChunkManager();
	}
	
	@Override
	public ClientPlayerEntity getPlayer() {
		return player;
	}
}
