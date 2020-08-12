package minecraft.common.world.block.handler;

import static minecraft.common.world.block.RedstoneWireBlock.CONNECTION_PROPERTIES;
import static minecraft.common.world.block.RedstoneWireBlock.POWER;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import minecraft.common.world.Direction;
import minecraft.common.world.IServerWorld;
import minecraft.common.world.block.Block;
import minecraft.common.world.block.IBlockPosition;
import minecraft.common.world.block.WireConnection;
import minecraft.common.world.block.state.IBlockState;

public class WireHandler {
	
	private static final int MAX_WIRE_CONNECTIONS = 8;
	
	private static final int INITIAL_CAPACITY = 64;
	
	private WireNode[] nodes;
	private int nodeCount;
	
	private final Map<IBlockPosition, WireNode> positionToNode;
	private final PriorityQueue<WireNode> powerNodes;
	
	private IServerWorld world;
	private Block wireBlock;
	
	public WireHandler() {
		nodes = new WireNode[INITIAL_CAPACITY];
		nodeCount = 0;
	
		positionToNode = new HashMap<>(INITIAL_CAPACITY);
		powerNodes = new PriorityQueue<>(INITIAL_CAPACITY);
		
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

	public void updateWireNetwork(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir) {
		this.world = world;
		wireBlock = state.getBlock();
		
		int sourcePower = world.getPower(pos, IServerWorld.STRONG_POWER_FLAGS);
		int depth = Math.abs(state.get(POWER) - sourcePower);
		
		if (depth != 0) {
			buildGraph(pos, state, fromDir, depth);
			replaceGraphStates();
			
			// TODO: implement updates.
		}
	}
	
	private void buildGraph(IBlockPosition sourcePos, IBlockState sourceState, Direction fromDir, int depth) {
		int nodeIndex = 0;

		WireNode sourceNode = nodes[nodeIndex];
		sourceNode.pos = sourcePos;
		sourceNode.state = sourceState;
		sourceNode.dir = fromDir.getOpposite();
		
		nodeCount = 1;
		
		int prevBreathEnd = 0;
		
		for (int d = 1; d < depth && nodeIndex < nodeCount; d++) {
			prevBreathEnd = nodeCount;
			
			while (nodeIndex < prevBreathEnd)
				findConnections(nodes[nodeIndex++]);
		}
		
		resolveExternalPower(prevBreathEnd);
		// The cached node positions are no longer required.
		positionToNode.clear();
		
		relaxGraphPower();
	}
	
	private void findConnections(WireNode node) {
		node.connectionCount = 0;
		
		for (Direction dir = node.dir; (dir = dir.rotateCW()) != node.dir; )
			findConnectionsTo(node, dir);
	}

	private void findConnectionsTo(WireNode node, Direction dir) {
		WireConnection connection = node.state.get(CONNECTION_PROPERTIES.get(dir));
	
		if (connection != WireConnection.NONE) {
			IBlockPosition sidePos = node.pos.offset(dir);
		
			if (connection != WireConnection.UP) {
				// If the side wire is already part of the graph, 
				// we can skip this direction check entirely.
				WireNode sideNode = positionToNode.get(sidePos);
				
				if (sideNode != null) {
					addConnection(node, sideNode);
					return;
				}
				
				IBlockState sideState = world.getBlockState(sidePos);

				if (sideState.isOf(wireBlock)) {
					addNodeNoCheck(node, sidePos, sideState, dir);
					return;
				}
				
				if (!sideState.isAligned(Direction.DOWN)) {
					IBlockState belowState = getBelowState(node);

					if (belowState.isAligned(dir)) {
						addNode(node, sidePos.down(), dir);
					} else {
						// TODO: consider this:
						//   We can potentially receive external power from
						//   the state at sidePos.down(), if it is a wire.
					}
				}
			}
			
			IBlockState aboveState = getAboveState(node);
			
			if (!aboveState.isAligned(Direction.DOWN) && !aboveState.isAligned(dir))
				addNode(node, sidePos.up(), dir);
		}
	}
	
	private IBlockState getBelowState(WireNode node) {
		if (node.belowState == null)
			node.belowState = world.getBlockState(node.pos.down());
		return node.belowState;
	}

	private IBlockState getAboveState(WireNode node) {
		if (node.aboveState == null)
			node.aboveState = world.getBlockState(node.pos.up());
		return node.aboveState;
	}

	private void addNode(WireNode source, IBlockPosition pos, Direction dir) {
		WireNode node = positionToNode.get(pos);
		
		if (node != null) {
			// Do not add a new node. Instead simply add
			// the missing connection from the source.
			addConnection(source, node);
		} else {
			IBlockState state = world.getBlockState(pos);
			
			if (state.isOf(wireBlock))
				addNodeNoCheck(source, pos, state, dir);
		}
	}

	private void addNodeNoCheck(WireNode source, IBlockPosition pos, IBlockState state, Direction dir) {
		if (nodeCount >= nodes.length)
			reallocNodes(nodes.length << 1);
		
		WireNode node = nodes[nodeCount++];
		
		node.pos = pos;
		node.state = state;
		node.dir = dir;

		// Delete potentially cached states.
		node.aboveState = node.belowState = null;

		// Assume the position does not already exist.
		positionToNode.put(pos, node);
		
		addConnection(source, node);
	}

	private void addConnection(WireNode source, WireNode connection) {
		source.connections[source.connectionCount++] = connection;
	}
	
	private void resolveExternalPower(int lastBreathStart) {
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
		private Direction dir;
		
		private IBlockState belowState;
		private IBlockState aboveState;
		
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
