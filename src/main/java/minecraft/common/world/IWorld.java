package minecraft.common.world;

import java.util.List;

import minecraft.common.math.Vec3;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.state.IBlockState;
import minecraft.common.world.entity.PlayerEntity;

public interface IWorld {
	
	public BlockHitResult castBlockRay(float x, float y, float z, Vec3 dir);
	
	public IBlockState getBlockState(IBlockPosition pos);
	
	public Block getBlock(IBlockPosition pos);
	
	public int getHighestPoint(IBlockPosition pos);
	
	public boolean isLoadedBlock(IBlockPosition pos);
	
	public boolean isLoadedBlock(int chunkX, int chunkZ);
	
	public void update();
	
	public int getHeight();
	
	public PlayerEntity getPlayer();
	
	public List<EntityHitbox> getBlockHitboxes(EntityHitbox hitbox);
	
}
