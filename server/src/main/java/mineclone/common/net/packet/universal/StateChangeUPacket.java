package mineclone.common.net.packet.universal;

import mineclone.common.net.packet.IPacket;
import mineclone.common.net.packet.PacketDecodeBuffer;
import mineclone.common.net.packet.PacketEncodeBuffer;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.ImmutableBlockPosition;
import mineclone.common.world.block.state.IBlockState;

public class StateChangeUPacket implements IPacket {

	private IBlockPosition pos;
	private IBlockState state;
	
	public StateChangeUPacket() {
	}

	public StateChangeUPacket(IBlockPosition pos, IBlockState state) {
		this.pos = pos;
		this.state = state;
	}
	
	@Override
	public void encode(PacketEncodeBuffer buffer) {
		buffer.writeInt(pos.getX());
		buffer.writeInt(pos.getY());
		buffer.writeInt(pos.getZ());
		buffer.writeInt(Blocks.getIdentifier(state));
	}

	@Override
	public void decode(PacketDecodeBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		pos = new ImmutableBlockPosition(x, y, z);
		state = Blocks.getState(buffer.readInt());
	}
	
	public IBlockPosition getPos() {
		return pos;
	}
	
	public IBlockState getState() {
		return state;
	}
}
