package minecraft.common.world.block.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import minecraft.common.world.Blocks;
import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.RedstoneWireBlock;
import minecraft.common.world.block.WireConnection;
import minecraft.common.world.block.state.IBlockState;

public class RedstoneWireHandler {
	
	private final IServerWorld world;
	private final IBlockState sourceState;
	private final IBlockPosition sourcePos;
	private final int sourcePower;
	private final int powerReceived;
	private final int maxDepth;
	
	private List<IBlockPosition> wirePositions;
	private Map<IBlockPosition, IBlockState> wireMap;
	private TreeSet<Node> poweredNodes;
	
	private LinkedHashSet<IBlockPosition> blockUpdatePositions;
	private LinkedHashSet<IBlockPosition> stateUpdatePositions;
	
	public RedstoneWireHandler(IServerWorld world, IBlockState sourceState, IBlockPosition sourcePos, int sourcePower, int powerReceived) {
		this.world = world;
		this.sourcePos = sourcePos;
		this.sourceState = sourceState;
		this.sourcePower = sourcePower;
		this.powerReceived = powerReceived;
		this.maxDepth = getMaxDepth();
	}
	
	private int getMaxDepth() {
		int maxDepth = powerReceived - sourcePower;
		if (maxDepth < 0) {
			maxDepth *= -1;
		} else if (maxDepth > 15) {
			maxDepth = 15;
		}
		return maxDepth;
	}
	
	public void setPowerLevels() {
		fillWireMap();
		
		blockUpdatePositions = new LinkedHashSet<>();
		stateUpdatePositions = new LinkedHashSet<>();
		
		if (poweredNodes.isEmpty()) {
			
		} else {
			while (!poweredNodes.isEmpty()) {
				Node currentNode = poweredNodes.pollFirst();
				
				for (Direction dir : Direction.HORIZONTAL_DIRECTIONS) {
					WireConnection connection = currentNode.state.getValue(RedstoneWireBlock.CONNECTION_PROPERTIES.get(dir));
					
					if (connection == WireConnection.UP) {
						IBlockPosition diagonalPos = currentNode.pos.up().offset(dir);
						
					}
					
				}
			}
		}
	}
	
	private void getPowerSources() {
		List<IBlockPosition> wirePositionList = new ArrayList<>();
		Set<IBlockPosition> wirePositionSet = new HashSet<>();
		wirePositionList.add(sourcePos);
		wirePositionSet.add(sourcePos);
		
		int maxDepth = powerReceived - sourcePower;
		if (maxDepth < 0) {
			maxDepth *= -1;
		} else if (maxDepth > 15) {
			maxDepth = 15;
		}
		int depth = 1;
		int index = 0;
		IBlockPosition pos;
		IBlockState state;
		int size = wirePositions.size();
		while (depth < maxDepth && index < size) {
			
			while (index < size) {
				
			}
		}
	}
	
	private static class Node implements Comparable<Node> {
		private static int idCounter = 0;
		
		private final IBlockState state;
		public int power;
		private final int id;
		
		public Node(IBlockState state) {
			this.state = state;
			this.id = idCounter++;
		}
		
		public Node(IBlockState state, int power) {
			this(state);
			this.power = power;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof Node) {
				Node node = (Node)other;
				return node.id == id;
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return pos.hashCode() * 31 + power;
		}
		
		@Override
		public int compareTo(Node other) {
			int p = Integer.compare(other.power, power);
			return p == 0 ? Integer.compare(id, other.id) : p;
		}
	}
}
