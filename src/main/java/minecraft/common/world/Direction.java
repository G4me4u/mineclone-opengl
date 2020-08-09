package minecraft.common.world;

import java.util.Arrays;

import minecraft.common.math.Vec3;

public enum Direction {

	NORTH(0, "north", Axis.Z, -1, 1, 3, 2),
	SOUTH(1, "south", Axis.Z, 1, 0, 2, 3),
	WEST(2, "west", Axis.X, -1, 3, 0, 1),
	EAST(3, "east", Axis.X, 1, 2, 1, 0),
	DOWN(4, "down", Axis.Y, -1, 5, 4, 4),
	UP(5, "up", Axis.Y, 1, 4, 5, 5);
	
	public static final Direction[] DIRECTIONS;
	public static final Direction[] HORIZONTAL_DIRECTIONS;
	
	private final int index;
	private final String name;
	private final Axis axis;
	private final int offsetX;
	private final int offsetY;
	private final int offsetZ;
	private final int oppositeIndex;
	private final int CWIndex;
	private final int CCWIndex;

	private Direction(int index, String name, Axis axis, int offset, int oppositeIndex, int CWIndex, int CCWIndex) {
		this.index = index;
		this.name = name;
		this.axis = axis;
		this.offsetX = axis == Axis.X ? offset : 0;
		this.offsetY = axis == Axis.Y ? offset : 0;
		this.offsetZ = axis == Axis.Z ? offset : 0;
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
		
		HORIZONTAL_DIRECTIONS = Arrays.stream(DIRECTIONS).filter(direction -> direction.getAxis().isHorizontal()).toArray(Direction[]::new);
	}
	
	public enum Axis {
		X(0, "X"),
		Y(1, "Y"),
		Z(2, "Z");
		
		public static final Axis[] AXES;
		
		private final int index;
		private final String name;
		
		Axis(int index, String name) {
			this.index = index;
			this.name = name;
		}
		
		public int getIndex() {
			return index;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isHorizontal() {
			return this == X || this == Z;
		}
		
		static {
			AXES = new Axis[values().length];
			
			for (Axis axis : values()) {
				AXES[axis.index] = axis;
			}
		}
	}
}
