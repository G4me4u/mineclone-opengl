package minecraft.common.world;

import java.util.List;

import minecraft.common.math.Vec3;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.BlockState;
import minecraft.common.world.entity.PlayerEntity;

public interface IWorld {
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3 dir);
	
	public BlockState getBlockState(IBlockPosition blockPos);
	
	public Block getBlock(IBlockPosition blockPos);
	
	public int getHighestPoint(IBlockPosition blockPos);
	
	public boolean isLoadedBlock(IBlockPosition blockPos);
	
	public boolean isLoadedBlock(int chunkX, int chunkZ);
	
	public void update();
	
	public int getHeight();
	
	public PlayerEntity getPlayer();
	
	public List<EntityHitbox> getBlockHitboxes(EntityHitbox hitbox);
}
