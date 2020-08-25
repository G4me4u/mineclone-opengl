package minecraft.common.world;

import minecraft.common.world.entity.PlayerEntity;

public interface IClientWorld extends IWorld {

	public PlayerEntity getPlayer();

}
