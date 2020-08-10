package minecraft.common.world.block.handler;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.WireConnection;
import minecraft.common.world.block.state.IBlockState;

import static minecraft.common.world.block.RedstoneWireBlock.*;

public class WireHandler {
	
	private static final int MAX_WIRE_CONNECTIONS = 8;
	
	private static final int INITIAL_CAPACITY = 64;
	
	private WireNode[] nodes;
	private int nodeCount;
	
	private Set<IBlockPosition> nodePositions;
	private PriorityQueue<WireNode> powerNodes;
	
	private IServerWorld world;
	private Block wireBlock;
	
	public WireHandler() {
		nodes = new WireNode[INITIAL_CAPACITY];
		nodeCount = 0;
	
		nodePositions = new HashSet<>();
		
		allocNodes(nodes, 0, INITIAL_CAPACITY);
	}
	
	private static void allocNodes(WireNode[] nodes, int offset, int endOffset) {
		while (offset < endOffset)
			nodes[offset++] = new WireNode();
	}
	
	private void reallocNodes(int newCapacity) {
		WireNode[] newNodes = new WireNode[newCapacity];
		
		System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
		allocNodes(newNodes, nodes.length, newCapacity);
		
		nodes = newNodes;
	}

	public void updateWireNetwork(IServerWorld world, IBlockPosition sourcePos, IBlockState sourceState) {
		this.world = world;
		wireBlock = sourceState.getBlock();
		
		int sourcePower = world.getPower(sourcePos, IServerWorld.DIRECT_POWER_FLAGS);
		int depth = Math.abs(sourceState.get(POWER) - sourcePower);
		
		if (depth != 0) {
			buildGraph(sourcePos, sourceState, depth);
			findExternalPower(sourcePower);
			
			// The cached node positions are no longer required.
			nodePositions.clear();
			
			relaxGraphPower();
			replaceGraphStates();
			
			// TODO: implement updates.
		}
	}
	
	private void buildGraph(IBlockPosition sourcePos, IBlockState sourceState, int depth) {
		int nodeIndex = 0;

		WireNode sourceNode = nodes[nodeIndex];
		sourceNode.pos = sourcePos;
		sourceNode.state = sourceState;
		
		nodeCount = 1;
		
		for (int d = 0; d < depth && nodeIndex < nodeCount; d++) {
			int breathEnd = nodeCount;
			
			while (nodeIndex < breathEnd)
				addConnections(nodes[nodeIndex++]);
		}
	}
	
	private void addConnections(WireNode node) {
		node.connectionCount = 0;
		
		for (Direction dir : Direction.HORIZONTAL_DIRECTIONS)
			addConnectionsTo(node, dir);
	}

	private void addConnectionsTo(WireNode node, Direction dir) {
		WireConnection connection = node.state.get(CONNECTION_PROPERTIES.get(dir));
	
		if (connection != WireConnection.NONE) {
			IBlockPosition sidePos = node.pos.offset(dir);
		
			if (connection != WireConnection.UP) {
				IBlockState sideState = world.getBlockState(sidePos);

				if (sideState.isOf(wireBlock)) {
					addNode(node, sidePos, sideState);
					return;
				}
				
				if (!sideState.isAligned(Direction.DOWN))
					addNode(node, sidePos.down());
			}
			
			IBlockState aboveState = world.getBlockState(node.pos.up());
			
			if (!aboveState.isAligned(Direction.DOWN) && !aboveState.isAligned(dir))
				addNode(node, sidePos.up());
		}
	}

	private void addNode(WireNode source, IBlockPosition pos) {
		IBlockState state = world.getBlockState(pos);
		
		if (state.isOf(wireBlock))
			addNode(source, pos, state);
	}
	
	private void addNode(WireNode source, IBlockPosition pos, IBlockState state) {
		if (nodePositions.add(pos)) {
			if (nodeCount >= nodes.length)
				reallocNodes(nodes.length << 1);
			
			WireNode node = nodes[nodeCount++];
			
			node.pos = pos;
			node.state = state;
			
			source.connections[source.connectionCount++] = node;
		}
	}
	
	private void findExternalPower(int sourcePower) {
		// TODO: update graph power.
		//nodes[0].setExternalPower(sourcePower);
		// powerNodes.add(node)
	}
	
	private void relaxGraphPower() {
		WireNode node;
		
		while ((node = powerNodes.poll()) != null) {
			int connectionPower = node.power - 1;
			
			if (connectionPower > 0) {
				for (int i = 0; i < node.connectionCount; i++) {
					WireNode connection = node.connections[i];
					
					if (connection.power < connectionPower) {
						if (connection.power != 0)
							powerNodes.remove(connection);
						
						connection.power = connectionPower;
						
						powerNodes.add(connection);
					}
				}
			}
		}
	}
	
	private void replaceGraphStates() {
		for (int i = 0; i < nodeCount; i++) {
			WireNode node = nodes[i];
			
			IBlockState state = node.state.with(POWER, node.power);
			
			if (state != node.state) {
				node.state = state;
				
				world.setBlockState(node.pos, state, false);
			}
		}
	}
	
	private static class WireNode implements Comparable<WireNode> {
		
		private final WireNode[] connections;
		private int connectionCount;
		
		private IBlockPosition pos;
		private IBlockState state;
		
		private int power;
		
		private WireNode() {
			connections = new WireNode[MAX_WIRE_CONNECTIONS];
		}
		
		@Override
		public int compareTo(WireNode other) {
			return Integer.compare(other.power, power);
		}
	}
}
