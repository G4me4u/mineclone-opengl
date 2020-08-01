package minecraft.renderer.world;

import minecraft.graphic.opengl.shader.ShaderProgram;
import minecraft.graphic.tessellator.color.Color;
import minecraft.math.Mat4;

public class BlockSelectionShader extends ShaderProgram {

	private static final String VERT_FILE_PATH = "/assets/shader/selectionShader.vert";
	private static final String FRAG_FILE_PATH = "/assets/shader/selectionShader.frag";
	
	private final int projViewMatLocation;
	private final int modlMatLocation;
	private final int selectionColorLocation;
	
	public BlockSelectionShader() {
		super(VERT_FILE_PATH, FRAG_FILE_PATH);
		
		projViewMatLocation = getUniformLocation("u_ProjViewMat");
		modlMatLocation = getUniformLocation("u_ModlMat");
		selectionColorLocation = getUniformLocation("u_SelectionColor");
	}
	
	public void setProjViewMat(WorldCamera camera) {
		setProjViewMat(camera.getProjViewMatrix());
	}
	
	public void setProjViewMat(Mat4 projViewMat) {
		uniformMat4(projViewMatLocation, projViewMat);
	}
	
	public void setModlMat(Mat4 modlMat) {
		uniformMat4(modlMatLocation, modlMat);
	}

	public void setSelectionColor(Color color) {
		setSelectionColor(color.getRedN(), color.getGreenN(), color.getBlueN(), color.getAlphaN());
	}
	
	public void setSelectionColor(float r, float g, float b, float a) {
		uniformFloat4(selectionColorLocation, r, g, b, a);
	}
}
