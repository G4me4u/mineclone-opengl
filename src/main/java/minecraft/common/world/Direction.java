package minecraft.common.world;

import minecraft.common.math.Vec3;

public enum Direction {

	NORTH(0, "north", 0, 0, 1),
	SOUTH(1, "south", 0, 0, -1),
	UP(2, "up", 0, 1, 0),
	DOWN(3, "down", 0, -1, 0),
	WEST(4, "west", -1, 0, 0),
	EAST(5, "east", 1, 0, 0);
	
	private static final Direction[] DIRECTIONS;
	
	private final int index;
	private final String name;
	private final int offsetX;
	private final int offsetY;
	private final int offsetZ;

	private Direction(int index, String name, int offsetX, int offsetY, int offsetZ) {
		this.index = index;
		this.name = name;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
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
