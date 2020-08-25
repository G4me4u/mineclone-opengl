package minecraft.client.world;

import minecraft.client.MinecraftClient;
import minecraft.common.world.IClientWorld;
import minecraft.common.world.WorldChunk;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.ImmutableBlockPosition;
import minecraft.common.world.block.state.IBlockState;
import minecraft.common.world.entity.PlayerEntity;
import minecraft.server.world.ServerWorld;

public class ClientWorld extends ServerWorld implements IClientWorld {

	private final MinecraftClient app;
	
	protected final PlayerEntity player;
	
	public ClientWorld(MinecraftClient app) {
		this.app = app;

		player = new PlayerEntity(this, app.getController());
	}
	
	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState state, boolean updateNeighbors) {
		WorldChunk chunk = getChunk(pos);
		
		if (chunk != null) {
			int oldHighestPoint = chunk.getHighestPoint(pos);
			IBlockState oldState = chunk.getBlockState(pos);
				
			if (super.setBlockState(pos, state, updateNeighbors)) {
				int highestPoint = chunk.getHighestPoint(pos);
				
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
