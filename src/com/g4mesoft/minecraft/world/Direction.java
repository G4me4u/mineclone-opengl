package com.g4mesoft.minecraft.world;

import com.g4mesoft.math.Vec3f;
import com.g4mesoft.math.Vec3i;

public enum Direction {

	NORTH(0, "north", new Vec3i(0, 0, 1)),
	SOUTH(1, "south", new Vec3i(0, 0, -1)),
	UP(2, "up", new Vec3i(0, 1, 0)),
	DOWN(3, "down", new Vec3i(0, -1, 0)),
	WEST(4, "west", new Vec3i(-1, 0, 0)),
	EAST(5, "east", new Vec3i(1, 0, 0));
	
	private static final Direction[] DIRECTIONS;
	
	private final int index;
	private final String name;
	private final Vec3i normal;

	private Direction(int index, String name, Vec3i normal) {
		this.index = index;
		this.name = name;
		this.normal = normal;
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
	
	public Vec3i getNormal() {
		return normal;
	}
	
	public static Direction fromVector(Vec3f vec) {
		Direction result = Direction.NORTH;

		float bestValue = Float.NEGATIVE_INFINITY;
		for (Direction dir : DIRECTIONS) {
			Vec3i n = dir.normal;
			float v = vec.x * n.x + vec.y * n.y + vec.z * n.z;
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
