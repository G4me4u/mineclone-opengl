package mineclone.client.renderer.model;

import mineclone.common.util.ColorUtil;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.signal.wire.IWire;
import mineclone.common.world.block.signal.wire.WireType;
import mineclone.common.world.block.state.IBlockState;

public abstract class WireBlockModel extends AbstractBlockModel {

	protected final IWire wire;
	protected final WireType type;

	protected final int[] colorTable;

	public WireBlockModel(IBlock block, int color) {
		if (!block.isWire()) {
			throw new IllegalArgumentException(block + " is not a Wire!");
		}

		this.wire = (IWire)block;
		this.type = this.wire.getWireType();

		this.colorTable = createColorTable(color);
	}

	private int[] createColorTable(int baseColor) {
		int br = ColorUtil.unpackR(baseColor);
		int bg = ColorUtil.unpackG(baseColor);
		int bb = ColorUtil.unpackB(baseColor);

		ColorUtil.checkARGB(0, br, bg, bb);

		int min = type.min();
		int max = type.max();
		int count = (max - min) + 1;

		int[] table = new int[count];
		int index = 0;

		for (int signal = min; signal <= max; signal++) {
			float f = (float)(signal - min) / (max - min);

			float r = ColorUtil.normalize(br) * ((signal == min) ? 0.3F : (0.6F * f + 0.4F));
			float g = ColorUtil.normalize(bg) * ((signal == min) ? 0.3F : (0.6F * f + 0.4F));
			float b = ColorUtil.normalize(bb) * ((signal == min) ? 0.3F : (0.6F * f + 0.4F));

			table[index++] = ColorUtil.pack(r, g, b);
		}

		return table;
	}

	protected int getWireColor(IBlockState state) {
		return colorTable[wire.getSignal(state) - type.min()];
	}
}
