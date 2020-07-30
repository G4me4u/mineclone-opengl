package minecraft.graphic.opengl.buffer;

public enum FrameBufferType {

	COLOR(true, false),
	DEPTH(false, true),
	DEPTH_AND_COLOR(true, true),
	MULTISAMPLED_DEPTH_AND_COLOR(true, true);
	
	private final boolean colorAttach;
	private final boolean depthAttach;
	
	private FrameBufferType(boolean colorAttach, boolean depthAttach) {
		this.colorAttach = colorAttach;
		this.depthAttach = depthAttach;
	}
	
	public boolean hasColorAttachment() {
		return colorAttach;
	}

	public boolean hasDepthAttachment() {
		return depthAttach;
	}
}
