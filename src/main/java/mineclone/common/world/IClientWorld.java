package mineclone.common.world;

import mineclone.common.world.entity.PlayerEntity;

public interface IClientWorld extends IWorld {

	public PlayerEntity getPlayer();

}
