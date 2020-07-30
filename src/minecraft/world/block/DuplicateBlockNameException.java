package minecraft.world.block;

@SuppressWarnings("serial")
public class DuplicateBlockNameException extends RuntimeException {

	private final String name;
	private final Block block;
	
	public DuplicateBlockNameException(String name, Block block) {
		this.name = name;
		this.block = block;
	}
	
	public String getName() {
		return name;
	}
	
	public Block getBlock() {
		return block;
	}
	
	@Override
	public String getMessage() {
		return String.format("Name=%s, Block class=", name, block.getClass());
	}
}
