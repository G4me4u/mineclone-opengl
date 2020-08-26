package mineclone.common.world.block.handler;

import static mineclone.common.world.block.RedstoneWireBlock.CONNECTIONS;
import static mineclone.common.world.block.RedstoneWireBlock.POWER;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.block.Block;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.WireConnection;
import mineclone.common.world.block.state.IBlockState;

public class WireHandler {
	
	private static final int MAX_WIRE_CONNECTIONS = 8;
	
	private static final int INITIAL_CAPACITY = 64;
	
	private final Block wireBlock;

	private WireNode[] nodes;
	private int nodeCount;
	
	private final Map<IBlockPosition, WireNode> positionToNode;
	private final PriorityQueue<WireNode> powerNodes;
	
	public WireHandler(Block wireBlock) {
		this.wireBlock = wireBlock;
		
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

	public void updateWireNetworkFrom(IServerWorld world, IBlockPosition pos, IBlockState state, Direction fromDir) {
		int externalPower = world.getPowerFrom(pos, fromDir, IServerWorld.STRONG_POWER_FLAGS);
		
		if (externalPower < state.get(POWER)) {
			int otherPower = world.getPowerExceptFrom(pos, fromDir, IServerWorld.STRONG_POWER_FLAGS);

			if (otherPower > externalPower)
				externalPower = otherPower;
		}
		
		Direction sourceDir = fromDir.isHorizontal() ? fromDir.getOpposite() : Direction.HORIZONTAL[0];
		buildAndUpdateGraph(world, pos, state, sourceDir, externalPower);
	}
	
	public void updateWireNetwork(IServerWorld world, IBlockPosition pos, IBlockState state) {
		int externalPower = world.getPower(pos, IServerWorld.STRONG_POWER_FLAGS);
		buildAndUpdateGraph(world, pos, state, Direction.HORIZONTAL[0], externalPower);
	}
	
	private void buildAndUpdateGraph(IServerWorld world, IBlockPosition pos, IBlockState state, Direction sourceDir, int externalPower) {
		buildGraph(world, pos, state, sourceDir, externalPower);
		
		if (nodeCount != 0) {
			replaceGraphStates(world);
			
			// TODO: implement updates.
		}
	}
	
	private void buildGraph(IServerWorld world, IBlockPosition sPos, IBlockState sState, Direction sDir, int sExternalPower) {
		nodeCount = 0;

		int oldPower = sState.get(POWER);

		// External power does not include the power from wires of the network,
		// so also update it if it has an invalid state in its network.
		int expectedPower = Math.max(findEdgePowerFromWires(world, sPos, sState), sExternalPower);
		
		if (oldPower != expectedPower) {
			WireNode sourceNode = addNode(sPos, sState, sDir, 0);

			// Since depth is zero based, we should subtract 1.
			int maxDepth = Math.max(oldPower, expectedPower) - 1;
			int nodeIndex = 0;

			// Since d is the depth of the nodes in the current breath, it should
			// start at zero. When d is maxDepth we are not allowing new nodes.
			for (int d = 0; d <= maxDepth && nodeIndex < nodeCount; d++) {
				int prevBreathEnd = nodeCount;
				
				while (nodeIndex < prevBreathEnd)
					findConnections(world, nodes[nodeIndex++], (d != maxDepth));
			}

			int outsidePower = findPowerFromWire(world, sourceNode, maxDepth);
			int sourcePower = Math.max(outsidePower, sExternalPower);
			
			if (sourcePower > oldPower) {
				increaseAndRelaxSourcePower(world, maxDepth, sourcePower);
			} else {
				findAndRelaxGraphPower(world, maxDepth, sourcePower);
			}

			positionToNode.clear();
		}
	}
	
	private void findConnections(IServerWorld world, WireNode node, boolean allowNewNodes) {
		Direction dir = node.dir;
		
		do {
			findConnectionsTo(world, node, dir, allowNewNodes);
			dir = dir.rotateCW();
		} while (dir != node.dir);
	}

	private void findConnectionsTo(IServerWorld world, WireNode node, Direction dir, boolean allowNewNodes) {
		WireConnection connection = node.state.get(CONNECTIONS.get(dir));
	
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

				if (isNetworkWire(sideState)) {
					if (allowNewNodes)
						addNodeFrom(node, sidePos, sideState, dir);
					
					return;
				}
				
				if (!sideState.isAligned(Direction.DOWN) && !sideState.isAligned(dir.getOpposite())) {
					IBlockState belowState = getBelowState(world, node);

					if (belowState.isAligned(dir)) {
						tryAddNodeAt(world, node, sidePos.down(), dir, allowNewNodes);
					} else {
						node.wireDirectionFlags |= dir.getFlag();
					}
				}
			}
			
			IBlockState aboveState = getAboveState(world, node);
			
			if (!aboveState.isAligned(Direction.DOWN) && !aboveState.isAligned(dir))
				tryAddNodeAt(world, node, sidePos.up(), dir, allowNewNodes);
		}
	}
	
	private IBlockState getBelowState(IServerWorld world, WireNode node) {
		if (node.belowState == null)
			node.belowState = world.getBlockState(node.pos.down());
		return node.belowState;
	}

	private IBlockState getAboveState(IServerWorld world, WireNode node) {
		if (node.aboveState == null)
			node.aboveState = world.getBlockState(node.pos.up());
		return node.aboveState;
	}

	private void tryAddNodeAt(IServerWorld world, WireNode source, IBlockPosition pos, Direction dir, boolean allowNewNodes) {
		WireNode node = positionToNode.get(pos);
		
		if (node != null) {
			// Do not add a new node. Instead simply add
			// the missing connection from the source.
			addConnection(source, node);
		} else if (allowNewNodes) {
			IBlockState state = world.getBlockState(pos);
			
			if (isNetworkWire(state))
				addNodeFrom(source, pos, state, dir);
		}
	}

	private void addNodeFrom(WireNode source, IBlockPosition pos, IBlockState state, Direction dir) {
		addConnection(source, addNode(pos, state, dir, source.depth + 1));
	}

	private WireNode addNode(IBlockPosition pos, IBlockState state, Direction dir, int depth) {
		if (nodeCount >= nodes.length)
			reallocNodes(nodes.length << 1);
		
		WireNode node = nodes[nodeCount++];
		
		node.pos = pos;
		node.state = state;
		node.dir = dir;

		node.depth = depth;

		node.connectionCount = 0;
		
		// Delete potentially cached states.
		node.aboveState = node.belowState = null;

		node.power = node.wireDirectionFlags = 0;
		
		// Assume the position does not already exist.
		positionToNode.put(pos, node);
		
		return node;
	}

	private void addConnection(WireNode source, WireNode connection) {
		source.connections[source.connectionCount++] = connection;
	}
	
	private void increaseAndRelaxSourcePower(IServerWorld world, int maxDepth, int sourcePower) {
		nodes[0].power = sourcePower;
		
		for (int i = 1; i < nodeCount; i++) {
			WireNode node = nodes[i];

			int oldPower = node.state.get(POWER);
			int newPower = sourcePower - node.depth;
			
			node.power = Math.max(oldPower, newPower);
		}
	}
	
	private void findAndRelaxGraphPower(IServerWorld world, int maxDepth, int sourcePower) {
		if (sourcePower != 0) {
			WireNode sourceNode = nodes[0];
			sourceNode.power = sourcePower;
			powerNodes.add(sourceNode);
		}
		
		// Find the remaining power
		for (int i = 1; i < nodeCount; i++) {
			WireNode node = nodes[i];
			
			int externalPower = world.getPower(node.pos, IServerWorld.STRONG_POWER_FLAGS);
			int outsidePower = findPowerFromWire(world, node, maxDepth);
			
			node.power = Math.max(externalPower, outsidePower);
			
			if (node.power != 0)
				powerNodes.add(node);
		}
		
		relaxGraphPower();
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
	
	private int findPowerFromWire(IServerWorld world, WireNode node, int maxDepth) {
		int outsidePower = 0;

		if (node.depth >= maxDepth) {
			outsidePower = findEdgePowerFromWires(world, node.pos, node.state);
		} else if (node.wireDirectionFlags != 0) {
			IBlockPosition downPos = node.pos.down();
			
			for (Direction dir : Direction.HORIZONTAL) {
				if ((node.wireDirectionFlags & dir.getFlag()) != 0) {
					int power = getPowerFromWireAt(world, downPos.offset(dir));
					
					if (power > outsidePower)
						outsidePower = power;
				}
			}
		}

		return outsidePower;
	}

	private int findEdgePowerFromWires(IServerWorld world, IBlockPosition pos, IBlockState state) {
		int outsidePower = 0;
		
		for (Direction dir : Direction.HORIZONTAL) {
			int power = findEdgePowerFromWire(world, pos, state, dir);
			
			if (power > outsidePower)
				outsidePower = power;
		}
		
		return outsidePower;
	}
	
	private int findEdgePowerFromWire(IServerWorld world, IBlockPosition pos, IBlockState state, Direction dir) {
		WireConnection connection = state.get(CONNECTIONS.get(dir));
		
		if (connection != WireConnection.NONE) {
			IBlockPosition sidePos = pos.offset(dir);
		
			if (connection == WireConnection.UP) {
				return getPowerFromWireAt(world, sidePos.up());
			} else { // connection == WireConnection.SIDE
				IBlockState sideState = world.getBlockState(sidePos);
				
				if (isNetworkWire(sideState))
					return getPowerFromWireAt(world, sidePos);

				if (!sideState.isAligned(Direction.DOWN) && !sideState.isAligned(dir.getOpposite()))
					return getPowerFromWireAt(world, sidePos.down());
			}
		}
		
		return 0;
	}
	
	private int getPowerFromWireAt(IServerWorld world, IBlockPosition pos) {
		if (!positionToNode.containsKey(pos)) {
			IBlockState state = world.getBlockState(pos);
			
			if (isNetworkWire(state))
				return state.get(POWER) - 1;
		}
		
		return 0;
	}
	
	private void replaceGraphStates(IServerWorld world) {
		for (int i = 0; i < nodeCount; i++) {
			WireNode node = nodes[i];
			
			IBlockState state = node.state.with(POWER, node.power);
			
			if (state != node.state) {
				node.state = state;
				
				world.setBlockState(node.pos, state, false);
			}
		}
	}
	
	private boolean isNetworkWire(IBlockState state) {
		return state.isOf(wireBlock);
	}
	
	private static class WireNode implements Comparable<WireNode> {
		
		private IBlockPosition pos;
		private IBlockState state;
		private Direction dir;

		private int depth;
		
		private final WireNode[] connections;
		private int connectionCount;
		
		private IBlockState belowState;
		private IBlockState aboveState;
		
		private int power;
		private int wireDirectionFlags;
		
		private WireNode() {
			connections = new WireNode[MAX_WIRE_CONNECTIONS];
		}
		
		@Override
		public int compareTo(WireNode other) {
			return Integer.compare(other.power, power);
		}
	}
}
