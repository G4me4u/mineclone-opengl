package minecraft.common.world;

import minecraft.common.math.Vec3;

public enum Direction {

	NORTH(0, "north", 0, 0, 1, 1, 3, 2),
	SOUTH(1, "south", 0, 0, -1, 0, 2, 3),
	WEST(2, "west", -1, 0, 0, 3, 0, 1),
	EAST(3, "east", 1, 0, 0, 2, 1, 0),
	UP(4, "up", 0, 1, 0, 5, 4, 4),
	DOWN(5, "down", 0, -1, 0, 4, 5, 5);
	
	public static final Direction[] DIRECTIONS;
	
	private final int index;
	private final String name;
	private final int offsetX;
	private final int offsetY;
	private final int offsetZ;
	private final int oppositeIndex;
	private final int CWIndex;
	private final int CCWIndex;

	private Direction(int index, String name, int offsetX, int offsetY, int offsetZ, int oppositeIndex, int CWIndex, int CCWIndex) {
		this.index = index;
		this.name = name;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.oppositeIndex = oppositeIndex;
		this.CWIndex = CWIndex;
		this.CCWIndex = CCWIndex;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static Direction fromIndex(int index) {
		if (index < 0 || index >= DIRECTIONS.length)
			return null;
		return DIRECTIONS[index];
	}

	public String getName() {
		return name;
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
	
	public Direction getOpposite() {
		return fromIndex(oppositeIndex);
	}
	
	public Direction rotateCW() {
		return fromIndex(CWIndex);
	}
	
	public Direction rotateCCW() {
		return fromIndex(CCWIndex);
	}
	
	public static Direction fromVector(Vec3 vec) {
		Direction result = Direction.NORTH;

		float bestValue = Float.NEGATIVE_INFINITY;
		for (Direction dir : DIRECTIONS) {
			float v = vec.x * dir.offsetX + vec.y * dir.offsetY + vec.z * dir.offsetZ;
			
			if (v > bestValue) {
				bestValue = v;
				result = dir;
			}
		}
		
		return result;
	}
	
	static {
		DIRECTIONS = new Direction[values().length];
		for (Direction dir : values())
			DIRECTIONS[dir.index] = dir;
	}
}
