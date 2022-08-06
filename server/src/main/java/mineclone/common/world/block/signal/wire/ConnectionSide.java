package mineclone.common.world.block.signal.wire;

import mineclone.common.world.Axis;
import mineclone.common.world.Direction;
import mineclone.common.world.block.IBlockPosition;

public enum ConnectionSide {
//	                         i   o   ax  ay  az  wn  ws  ww  we  wd  wu  dx  dy  dz
	NORTH     ("north"     ,  0,  1, -1, -1,  0,  0, -1,  6,  8, 10, 12,  0,  0, -1),
	SOUTH     ("south"     ,  1,  0, -1, -1,  1, -1,  1,  9,  7, 13, 11,  0,  0,  1),
	WEST      ("west"      ,  2,  3,  2, -1, -1,  6,  9,  2, -1, 14, 16, -1,  0,  0),
	EAST      ("east"      ,  3,  2,  3, -1, -1,  8,  7, -1,  3, 17, 15,  1,  0,  0),
	DOWN      ("down"      ,  4,  5, -1,  4, -1, 10, 13, 14, 17,  4, -1,  0, -1,  0),
	UP        ("up"        ,  5,  4, -1,  5, -1, 12, 11, 16, 15, -1,  5,  0,  1,  0),
	NORTH_WEST("north_west",  6,  7,  2, -1,  0,  6,  2,  6,  0, -1, -1, -1,  0, -1),
	SOUTH_EAST("south_east",  7,  6,  3, -1,  1,  3,  7,  1,  7, -1, -1,  1,  0,  1),
	NORTH_EAST("north_east",  8,  9,  3, -1,  0,  8,  3,  0,  8, -1, -1,  1,  0, -1),
	SOUTH_WEST("south_west",  9,  8,  2, -1,  1,  2,  9,  9,  1, -1, -1, -1,  0,  1),
	NORTH_DOWN("north_down", 10, 11, -1,  4,  0, 10,  4, -1, -1, 10,  0,  0, -1, -1),
	SOUTH_UP  ("south_up"  , 11, 10, -1,  5,  1,  5, 11, -1, -1,  1, 11,  0,  1,  1),
	NORTH_UP  ("north_up"  , 12, 13, -1,  5,  0, 12,  5, -1, -1,  0, 12,  0,  1, -1),
	SOUTH_DOWN("south_down", 13, 12, -1,  4,  1,  4, 13, -1, -1, 13,  1,  0, -1,  1),
	WEST_DOWN ("west_down" , 14, 15,  2,  4, -1, -1, -1, 14,  4, 14,  2, -1, -1,  0),
	EAST_UP   ("east_up"   , 15, 14,  3,  5, -1, -1, -1,  5, 15,  3, 15,  1,  1,  0),
	WEST_UP   ("west_up"   , 16, 17,  2,  5, -1, -1, -1, 16,  5,  2, 16, -1,  1,  0),
	EAST_DOWN ("east_down" , 17, 16,  3,  4, -1, -1, -1,  4, 17, 17,  3,  1, -1,  0);

	public static final ConnectionSide[] ALL;

	static {

		ConnectionSide[] values = values();
		ALL = new ConnectionSide[values.length];

		for (ConnectionSide side : values) {
			ALL[side.index] = side;
		}
	}

	private final String name;
	private final int index;

	private final int oppositeIndex;
	private final int xProjectedIndex;
	private final int yProjectedIndex;
	private final int zProjectedIndex;
	private final boolean isAligned;
	private final int withNorthIndex;
	private final int withSouthIndex;
	private final int withWestIndex;
	private final int withEastIndex;
	private final int withDownIndex;
	private final int withUpIndex;

	private final int dx;
	private final int dy;
	private final int dz;

	private ConnectionSide(String name, int index, int oppositeIndex, int xProjectedIndex, int yProjectedIndex, int zProjectedIndex, int withNorthIndex, int withSouthIndex, int withWestIndex, int withEastIndex, int withDownIndex, int withUpIndex, int dx, int dy, int dz) {
		this.name = name;
		this.index = index;

		this.oppositeIndex = oppositeIndex;
		this.xProjectedIndex = xProjectedIndex;
		this.yProjectedIndex = yProjectedIndex;
		this.zProjectedIndex = zProjectedIndex;
		this.isAligned = isAlignedX() || isAlignedY() || isAlignedZ();
		this.withNorthIndex = withNorthIndex;
		this.withSouthIndex = withSouthIndex;
		this.withWestIndex = withWestIndex;
		this.withEastIndex = withEastIndex;
		this.withDownIndex = withDownIndex;
		this.withUpIndex = withUpIndex;

		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public static ConnectionSide fromIndex(int index) {
		return (index >= 0 && index < ALL.length) ? ALL[index] : null;
	}

	public ConnectionSide getOpposite() {
		return ALL[oppositeIndex];
	}

	public ConnectionSide projectX() {
		return fromIndex(xProjectedIndex);
	}

	public ConnectionSide projectY() {
		return fromIndex(yProjectedIndex);
	}

	public ConnectionSide projectZ() {
		return fromIndex(zProjectedIndex);
	}

	public ConnectionSide project(Axis axis) {
		return fromIndex(axis.choose(xProjectedIndex, yProjectedIndex, zProjectedIndex));
	}

	public ConnectionSide projectHorizontal() {
		ConnectionSide px = projectX();
		ConnectionSide pz = projectZ();

		return px == null ? pz : (pz == null ? px : this);
	}

	public ConnectionSide projectVertical() {
		return projectY();
	}

	public boolean isAlignedX() {
		return xProjectedIndex == index;
	}

	public boolean isAlignedY() {
		return yProjectedIndex == index;
	}

	public boolean isAlignedZ() {
		return zProjectedIndex == index;
	}

	public boolean isAligned(Axis axis) {
		return axis.choose(xProjectedIndex, yProjectedIndex, zProjectedIndex) == index;
	}

	public boolean isAlignedHorizontal() {
		return isAlignedX() || isAlignedZ();
	}

	public boolean isAlignedVertical() {
		return isAlignedY();
	}

	public boolean isAligned() {
		return isAligned;
	}

	public ConnectionSide withNorth() {
		return fromIndex(withNorthIndex);
	}

	public ConnectionSide withSouth() {
		return fromIndex(withSouthIndex);
	}

	public ConnectionSide withWest() {
		return fromIndex(withWestIndex);
	}

	public ConnectionSide withEast() {
		return fromIndex(withEastIndex);
	}

	public ConnectionSide withDown() {
		return fromIndex(withDownIndex);
	}

	public ConnectionSide withUp() {
		return fromIndex(withUpIndex);
	}

	public int getOffsetX() {
		return dx;
	}

	public int getOffsetY() {
		return dy;
	}

	public int getOffsetZ() {
		return dz;
	}

	public Direction getDirection() {
		return isAligned() ? Direction.fromIndex(index) : null;
	}

	public static ConnectionSide fromDirection(Direction dir) {
		return ALL[dir.getIndex()];
	}

	public boolean is(Direction dir) {
		return index == dir.getIndex();
	}

	public IBlockPosition offset(IBlockPosition pos) {
		return pos.offset(dx, dy, dz);
	}
}
