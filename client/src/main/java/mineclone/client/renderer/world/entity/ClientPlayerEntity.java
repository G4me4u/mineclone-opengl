package mineclone.client.renderer.world.entity;

import mineclone.client.MinecloneClient;
import mineclone.client.controller.PlayerController;
import mineclone.client.controller.PlayerHotbar;
import mineclone.client.input.Keyboard;
import mineclone.client.input.Mouse;
import mineclone.common.math.Mat3;
import mineclone.common.math.Vec3;
import mineclone.common.net.packet.universal.StateChangeUPacket;
import mineclone.common.world.BlockHitResult;
import mineclone.common.world.IWorld;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.entity.PlayerEntity;

public class ClientPlayerEntity extends PlayerEntity {

	private static final int INTERACT_INTERVAL = 4;
	
	private final MinecloneClient client;
	
	private final PlayerController controller;
	private final PlayerHotbar hotbar;
	
	private int interactTimer;
	
	public ClientPlayerEntity(IWorld world, MinecloneClient client) {
		super(world);
		
		this.client = client;

		controller = client.getController();
		hotbar = new PlayerHotbar();

		controller.setPlayer(this);
	}
	
	@Override
	public void update() {
		hotbar.update();
		
		boolean remove = Mouse.isHeld(Mouse.BUTTON_LEFT);
		boolean place = Mouse.isHeld(Mouse.BUTTON_RIGHT);
		
		if (remove || place) {
			interactTimer--;

			if (interactTimer <= 0) {
				Mat3 rot = new Mat3().rotateX(-yaw).rotateY(-pitch);
				Vec3 dir = new Vec3(-rot.m02, -rot.m12, -rot.m22);
				BlockHitResult hitResult = world.castBlockRay(eyeX, eyeY, eyeZ, dir);
				
				if (hitResult != null) {
					boolean success = false;
					
					IBlockPosition placePos;
					IBlockState placeState;
					
					if (remove) {
						placePos = hitResult.pos;
						placeState = Blocks.AIR_BLOCK.getDefaultState();
						success = true;
					} else {
						placePos = hitResult.pos.offset(hitResult.face);
						placeState = hotbar.getHotbarBlock().getPlacementState(world, placePos);
						success = !isBlockInsidePlayer(placeState, placePos);
					}
					
					if (success) {
						world.setBlockState(placePos, placeState);
						client.send(new StateChangeUPacket(placePos, placeState));
						interactTimer = INTERACT_INTERVAL;
					}
				}
			}
		} else {
			interactTimer = 0;
		}
		
		if (Keyboard.isHeld(Keyboard.KEY_P)) {
			Mat3 rot = new Mat3().rotateX(-yaw).rotateY(-pitch);
			Vec3 dir = new Vec3(-rot.m02, -rot.m12, -rot.m22);
			BlockHitResult hitResult = world.castBlockRay(eyeX, eyeY, eyeZ, dir);

			if (hitResult != null) {
				System.out.println(world.getBlockState(hitResult.pos));
			} else {
				System.out.println("NONE");
			}
		}
		
		controller.update();
		
		super.update();
	}
}
