package minecraft.client.renderer.world;

import minecraft.client.graphic.opengl.ShaderProgram;
import minecraft.client.graphic.tessellator.Color;
import minecraft.common.math.Mat4;

public class WorldShader extends ShaderProgram {

	private static final String VERT_FILE_PATH = "/assets/shader/worldShader.vert";
	private static final String FRAG_FILE_PATH = "/assets/shader/worldShader.frag";
	
	private final int projMatLocation;
	private final int viewMatLocation;
	private final int texSamplerLocation;

	private final int fogDensityLocation;
	private final int fogGradientLocation;
	private final int fogColorLocation;
	
	public WorldShader() {
		super(VERT_FILE_PATH, FRAG_FILE_PATH);
		
		projMatLocation = getUniformLocation("u_ProjMat");
		viewMatLocation = getUniformLocation("u_ViewMat");
		texSamplerLocation = getUniformLocation("u_TexSampler");

		fogDensityLocation = getUniformLocation("u_FogDensity");
		fogGradientLocation = getUniformLocation("u_FogGradient");
		fogColorLocation = getUniformLocation("u_FogColor");
	
		setTexSampler(0);
	}
	
	public void setCamera(WorldCamera camera) {
		setProjMat(camera.getProjMat());
		setViewMat(camera.getViewMat());
	}
	
	public void setProjMat(Mat4 projMat) {
		uniformMat4(projMatLocation, projMat);
	}
	
	public void setViewMat(Mat4 viewMat) {
		uniformMat4(viewMatLocation, viewMat);
	}
	
	public void setTexSampler(int slot) {
		uniformInt(texSamplerLocation, slot);
	}
	
	public void setFogDensity(float density) {
		uniformFloat(fogDensityLocation, density);
	}

	public void setFogGradient(float gradient) {
		uniformFloat(fogGradientLocation, gradient);
	}

	public void setFogColor(Color color) {
		setFogColor(color.getRedN(), color.getGreenN(), color.getBlueN());
	}

	public void setFogColor(float fogR, float fogG, float fogB) {
		uniformFloat3(fogColorLocation, fogR, fogG, fogB);
	}
}
