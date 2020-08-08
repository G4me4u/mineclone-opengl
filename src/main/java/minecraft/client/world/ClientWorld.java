package minecraft.client.world;

import minecraft.client.MinecraftClient;
import minecraft.common.world.IClientWorld;
import minecraft.server.world.ServerWorld;

public class ClientWorld extends ServerWorld implements IClientWorld {

	public ClientWorld(MinecraftClient app) {
		super(app);
	}
}
