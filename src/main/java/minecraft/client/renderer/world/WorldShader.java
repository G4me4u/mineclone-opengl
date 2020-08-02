package minecraft.client.renderer.world;

import minecraft.client.graphic.opengl.ShaderProgram;
import minecraft.common.math.Mat4;

public class WorldShader extends ShaderProgram {

	private static final String VERT_FILE_PATH = "/assets/shader/worldShader.vert";
	private static final String FRAG_FILE_PATH = "/assets/shader/worldShader.frag";
	
	private final int projViewMatLocation;
	private final int texSamplerLocation;
	
	public WorldShader() {
		super(VERT_FILE_PATH, FRAG_FILE_PATH);
		
		projViewMatLocation = getUniformLocation("u_ProjViewMat");
		texSamplerLocation = getUniformLocation("u_TexSampler");
	
		setTexSampler(0);
	}
	
	public void setProjViewMat(WorldCamera camera) {
		setProjViewMat(camera.getProjViewMatrix());
	}
	
	public void setProjViewMat(Mat4 projViewMat) {
		uniformMat4(projViewMatLocation, projViewMat);
	}
	
	public void setTexSampler(int slot) {
		uniformInt(texSamplerLocation, slot);
	}
}
