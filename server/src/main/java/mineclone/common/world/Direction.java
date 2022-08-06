package mineclone.common.world;

import java.util.Arrays;

import mineclone.common.math.Vec3;
import mineclone.common.world.block.state.IIndexedValue;

public enum Direction implements IIndexedValue {

	NORTH("north", 0, 1, 3, 2, Axis.Z, -1),
	SOUTH("south", 1, 0, 2, 3, Axis.Z,  1),
	WEST ("west" , 2, 3, 0, 1, Axis.X, -1),
	EAST ("east" , 3, 2, 1, 0, Axis.X,  1),
	DOWN ("down" , 4, 5, 4, 4, Axis.Y, -1),
	UP   ("up"   , 5, 4, 5, 5, Axis.Y,  1);
	
	public static final Direction[] ALL;
	public static final Direction[] HORIZONTAL;
	public static final Direction[] VERTICAL;
	
	public static final int DIRECTIONS_FLAGS;
	public static final int HORIZONTAL_FLAGS;
	public static final int VERTICAL_FLAGS;
	
	static {
		ALL = new Direction[values().length];
		for (Direction dir : values())
			ALL[dir.index] = dir;
		
		HORIZONTAL = Arrays.stream(ALL)
				.filter(Direction::isHorizontal)
				.toArray(Direction[]::new);
		
		VERTICAL = Arrays.stream(ALL)
				.filter(Direction::isVertical)
				.toArray(Direction[]::new);

		DIRECTIONS_FLAGS = getFlags(ALL);
		HORIZONTAL_FLAGS = getFlags(HORIZONTAL);
		VERTICAL_FLAGS   = getFlags(VERTICAL);
	}
	
	private static int getFlags(Direction[] directions) {
		int flags = 0;
		for (Direction dir : directions)
			flags |= dir.getFlag();
		return flags;
	}
	
	private final String name;
	private final int index;
	
	private final int oppositeIndex;
	private final int cwIndex;
	private final int ccwIndex;

	private final Axis axis;
	private final int offsetX;
	private final int offsetY;
	private final int offsetZ;
	
	private final int flag;
	
	private Direction(String name, int index, int oppositeIndex, int cwIndex, int ccwIndex, Axis axis, int offset) {
		this.name = name;
		this.index = index;
		
		this.oppositeIndex = oppositeIndex;
		this.cwIndex = cwIndex;
		this.ccwIndex = ccwIndex;
		
		this.axis = axis;
		this.offsetX = (axis == Axis.X) ? offset : 0;
		this.offsetY = (axis == Axis.Y) ? offset : 0;
		this.offsetZ = (axis == Axis.Z) ? offset : 0;
	
		this.flag = 1 << index;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int getIndex() {
		return index;
	}
	
	public Direction getOpposite() {
		return fromIndex(oppositeIndex);
	}
	
	public Direction rotateCW() {
		return fromIndex(cwIndex);
	}
	
	public Direction rotateCCW() {
		return fromIndex(ccwIndex);
	}
	
	public Axis getAxis() {
		return axis;
	}
	
	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}
	
	public int getOffsetZ() {
		return offsetZ;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public boolean isHorizontal() {
		return axis.isHorizontal();
	}

	public boolean isVertical() {
		return !axis.isHorizontal();
	}
	
	public static Direction fromIndex(int index) {
		if (index < 0 || index >= ALL.length)
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		
		return ALL[index];
	}
	
	public static Direction fromVector(Vec3 vec) {
		Direction result = Direction.NORTH;
		float bestValue = Float.NEGATIVE_INFINITY;

		for (Direction dir : ALL) {
			float v = vec.dot(dir.offsetX, dir.offsetY, dir.offsetZ);
			
			if (v > bestValue) {
				bestValue = v;
				result = dir;
			}
		}
		
		return result;
	}
}
