package mineclone.server.world;

import java.util.Iterator;

import mineclone.common.world.Direction;
import mineclone.common.world.IServerWorld;
import mineclone.common.world.World;
import mineclone.common.world.block.Blocks;
import mineclone.common.world.block.IBlock;
import mineclone.common.world.block.IBlockPosition;
import mineclone.common.world.block.MutableBlockPosition;
import mineclone.common.world.block.PlantBlock;
import mineclone.common.world.block.PlantType;
import mineclone.common.world.block.signal.SignalType;
import mineclone.common.world.block.signal.wire.IWireHandler;
import mineclone.common.world.block.state.IBlockState;
import mineclone.common.world.chunk.ChunkEntry;
import mineclone.common.world.chunk.IChunkPosition;
import mineclone.common.world.chunk.IWorldChunk;
import mineclone.common.world.flags.SetBlockFlags;
import mineclone.common.world.gen.DiamondNoise;
import mineclone.server.MinecloneServer;
import mineclone.server.world.block.signal.wire.WireHandler;

public class ServerWorld extends World implements IServerWorld {

	private static final int RANDOM_TICK_SPEED = 3;

	private final WireHandler wireHandler;

	public ServerWorld(MinecloneServer server) {
		super(new ServerWorldChunkManager(server));

		this.wireHandler = new WireHandler(this);

		generateWorld();
	}

	@Override
	public void generateWorld() {
		DiamondNoise noise = new DiamondNoise(CHUNKS_X * IWorldChunk.CHUNK_SIZE, random);

		for (int chunkZ = 0; chunkZ < CHUNKS_X; chunkZ++) {
			for (int chunkX = 0; chunkX < CHUNKS_X; chunkX++)
				generateChunk(chunkX, chunkZ, noise);
		}

		for (int chunkZ = 0; chunkZ < CHUNKS_X; chunkZ++) {
			for (int chunkX = 0; chunkX < CHUNKS_X; chunkX++)
				populateChunk(chunkX, chunkZ);
		}
	}

	private void generateChunk(int chunkX, int chunkZ, DiamondNoise noise) {
		int x0 = chunkX * IWorldChunk.CHUNK_SIZE;
		int x1 = x0 + IWorldChunk.CHUNK_SIZE;

		int z0 = chunkZ * IWorldChunk.CHUNK_SIZE;
		int z1 = z0 + IWorldChunk.CHUNK_SIZE;

		for (int z = z0; z < z1; z++) {
			for (int x = x0; x < x1; x++) {
				int y1 = Math.round(32 + 32 * noise.getNoise(x, z));

				setBlock(new MutableBlockPosition(x, y1, z), Blocks.GRASS_BLOCK, SetBlockFlags.NONE);

				for (int y = y1 - 1; y >= y1 - 3; y--)
					setBlock(new MutableBlockPosition(x, y, z), Blocks.DIRT_BLOCK, SetBlockFlags.NONE);
				for (int y = y1 - 4; y > 0; y--)
					setBlock(new MutableBlockPosition(x, y, z), Blocks.STONE_BLOCK, SetBlockFlags.NONE);

				setBlock(new MutableBlockPosition(x, 0, z), Blocks.COBBLESTONE_BLOCK, SetBlockFlags.NONE);
			}
		}
	}

	private void populateChunk(int chunkX, int chunkZ) {
		MutableBlockPosition pos = new MutableBlockPosition();

		int x0 = chunkX * IWorldChunk.CHUNK_SIZE;
		int z0 = chunkZ * IWorldChunk.CHUNK_SIZE;
		int x1 = x0 + IWorldChunk.CHUNK_SIZE;
		int z1 = z0 + IWorldChunk.CHUNK_SIZE;

		for (pos.z = z0; pos.z < z1; pos.z++) {
			for (pos.x = x0; pos.x < x1; pos.x++) {
				pos.y = getHighestPoint(pos) + 1;

				if (random.nextInt(80) == 0) {
					growTree(pos);

					pos.y--;

					setBlock(pos, Blocks.DIRT_BLOCK, SetBlockFlags.NONE);
				} else if (random.nextInt(20) == 0) {
					IBlockState plantState = Blocks.PLANT_BLOCK.getDefaultState();

					PlantType type = PlantType.TYPES[random.nextInt(PlantType.TYPES.length)];
					plantState = plantState.with(PlantBlock.PLANT_TYPE, type);

					setBlockState(pos, plantState, SetBlockFlags.NONE);
				}
			}
		}
	}

	@Override
	public void growTree(IBlockPosition pos) {
		int treeHeight = 5 + random.nextInt(3);
		int trunkHeight = Math.max(1, treeHeight - 5);

		MutableBlockPosition tmpPos = new MutableBlockPosition(pos);

		for (int i = 0; i < treeHeight; i++) {
			if (i > trunkHeight) {
				int yo = i - trunkHeight;

				for (int zo = -3; zo <= 3; zo++) {
					for (int xo = -3; xo <= 3; xo++) {
						tmpPos.x = pos.getX() + xo;
						tmpPos.z = pos.getZ() + zo;

						if (getBlock(tmpPos) == Blocks.AIR_BLOCK) {
							int distSqr = xo * xo + yo * yo + zo * zo;

							if (distSqr < 3 * 2 * 3)
								setBlock(tmpPos, Blocks.LEAVES_BLOCK, SetBlockFlags.UPDATE_NEIGHBOR_SHAPES | SetBlockFlags.UPDATE_NEIGHBORS);
						}
					}
				}
			}

			if (i < treeHeight - 1) {
				tmpPos.x = pos.getX();
				tmpPos.z = pos.getZ();

				setBlock(tmpPos, Blocks.LOG_BLOCK, SetBlockFlags.UPDATE_NEIGHBOR_SHAPES | SetBlockFlags.UPDATE_NEIGHBORS);
			}

			tmpPos.y++;
		}
	}

	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState newState) {
		return setBlockState(pos, newState, SetBlockFlags.UPDATE_NEIGHBOR_SHAPES | SetBlockFlags.UPDATE_NEIGHBORS);
	}

	@Override
	public boolean setBlockState(IBlockPosition pos, IBlockState newState, int flags) {
		IBlockState oldState = chunkManager.getBlockState(pos);

		if (chunkManager.setBlockState(pos, newState)) {
			IBlock oldBlock = oldState.getBlock();
			IBlock newBlock = newState.getBlock();

			if (oldBlock == newBlock) {
				newBlock.onChanged(this, pos, oldState, newState);
			} else {
				oldBlock.onRemoved(this, pos, oldState);
				newBlock.onAdded(this, pos, newState);

				tryUpdateNeighborShapes(pos, oldState, flags);
				tryUpdateNeighbors(pos, oldState, flags);
			}

			tryUpdateNeighborShapes(pos, newState, flags);
			tryUpdateNeighbors(pos, newState, flags);

			return true;
		}

		return false;
	}

	private void tryUpdateNeighbors(IBlockPosition pos, IBlockState state, int flags) {
		if ((flags & SetBlockFlags.UPDATE_NEIGHBORS) != 0) {
			state.updateNeighbors(this, pos);
		}
	}

	private void tryUpdateNeighborShapes(IBlockPosition pos, IBlockState state, int flags) {
		if ((flags & SetBlockFlags.UPDATE_NEIGHBOR_SHAPES) != 0) {
			state.updateNeighborShapes(this, pos);
		}
	}

	@Override
	public void updateNeighborsExceptFrom(IBlockPosition pos, Direction exceptDir) {
		for (Direction dir : Direction.ALL) {
			if (dir != exceptDir) {
				updateNeighbor(pos.offset(dir));
			}
		}
	}

	@Override
	public void updateNeighbor(IBlockPosition pos) {
		getBlockState(pos).update(this, pos);
	}

	@Override
	public void updateNeighborShapesExceptFrom(IBlockPosition pos, IBlockState state, Direction exceptDir) {
		for (Direction dir : Direction.ALL) {
			if (dir != exceptDir) {
				updateNeighborShape(pos.offset(dir), dir.getOpposite(), pos, state);
			}
		}
	}

	@Override
	public void updateNeighborShape(IBlockPosition pos, Direction dir, IBlockPosition neighborPos, IBlockState neighborState) {
		getBlockState(pos).updateShape(this, pos, dir, neighborPos, neighborState);
	}

	@Override
	public int getSignalExceptFrom(IBlockPosition pos, Direction exceptDir, SignalType type) {
		int highestPower = 0;

		for (Direction dir : Direction.ALL) {
			if (dir != exceptDir) {
				int power = getSignalFrom(pos, dir, type);

				if (power > highestPower)
					highestPower = power;
			}
		}

		return highestPower;
	}

	@Override
	public int getSignalFrom(IBlockPosition pos, Direction dir, SignalType type) {
		Direction opp = dir.getOpposite();
		IBlockPosition neighborPos = pos.offset(dir);
		IBlockState neighborState = getBlockState(neighborPos);

		int signal = neighborState.getSignal(this, neighborPos, opp, type);

		if (signal >= type.max()) {
			return type.max();
		}

		if (neighborState.isSignalConductor(opp, type)) {
			signal = Math.max(signal, getDirectSignalExceptFrom(neighborPos, neighborState, dir, type));

			if (signal >= type.max()) {
				return type.max();
			}
		}

		return type.clamp(signal);
	}

	@Override
	public int getDirectSignalExceptFrom(IBlockPosition pos, Direction exceptDir, SignalType type) {
		return getDirectSignalExceptFrom(pos, null, exceptDir, type);
	}

	private int getDirectSignalExceptFrom(IBlockPosition pos, IBlockState state, Direction exceptDir, SignalType type) {
		int signal = type.min();

		for (Direction dir : Direction.ALL) {
			if (dir != exceptDir && (state == null || state.isSignalConductor(dir.getOpposite(), type))) {
				signal = Math.max(signal, getDirectSignalFrom(pos, dir, type));

				if (signal >= type.max()) {
					return type.max();
				}
			}
		}

		return signal;
	}

	@Override
	public int getDirectSignalFrom(IBlockPosition pos, Direction dir, SignalType type) {
		IBlockPosition neighborPos = pos.offset(dir);
		IBlockState neighborState = getBlockState(neighborPos);

		return type.clamp(neighborState.getDirectSignal(this, neighborPos, dir.getOpposite(), type));
	}

	@Override
	public IWireHandler getWireHandler() {
		return wireHandler;
	}

	@Override
	public void tick() {
		super.tick();

		doRandomUpdates();

		getChunkManager().broadcastDirtyStates();
	}

	private void doRandomUpdates() {
		MutableBlockPosition pos = new MutableBlockPosition();

		for (Iterator<ChunkEntry<IWorldChunk>> it = chunkManager.chunkIterator(); it.hasNext();) {
			ChunkEntry<IWorldChunk> entry = it.next();

			IChunkPosition chunkPos = entry.getChunkPos();
			IWorldChunk chunk = entry.getChunk();

			if (chunk != null && chunk.doesRandomUpdates()) {
				for (int i = 0; i < RANDOM_TICK_SPEED; i++) {
					int rx = random.nextInt(IWorldChunk.CHUNK_SIZE);
					int ry = random.nextInt(IWorldChunk.CHUNK_SIZE);
					int rz = random.nextInt(IWorldChunk.CHUNK_SIZE);

					pos.x = rx + (chunkPos.getChunkX() << IWorldChunk.CHUNK_SHIFT);
					pos.y = ry + (chunkPos.getChunkY() << IWorldChunk.CHUNK_SHIFT);
					pos.z = rz + (chunkPos.getChunkZ() << IWorldChunk.CHUNK_SHIFT);

					IBlockState state = chunk.getBlockState(rx, ry, rz);

					if (state.doesRandomUpdates())
						state.randomUpdate(this, pos, random);
				}
			}
		}
	}

	@Override
	public ServerWorldChunkManager getChunkManager() {
		return (ServerWorldChunkManager)super.getChunkManager();
	}
}
