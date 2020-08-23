package minecraft.client.renderer.world;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import minecraft.client.graphic.BufferAttrib;
import minecraft.client.graphic.BufferAttribType;
import minecraft.client.graphic.BufferLayout;
import minecraft.client.graphic.opengl.VertexArray;
import minecraft.client.graphic.opengl.VertexBuffer;
import minecraft.common.IResource;
import minecraft.common.math.Mat4;
import minecraft.common.math.Vec3;
import minecraft.common.world.BlockHitResult;
import minecraft.common.world.World;

public class BlockSelectionRenderer implements IResource {
	
	private static final int CUBE_VERTEX_COUNT = 36;
	
	private static final float SELECTION_COLOR_R = 0xE0 / 255.0f;
	private static final float SELECTION_COLOR_G = 0xE0 / 255.0f;
	private static final float SELECTION_COLOR_B = 0xFF / 255.0f;
	
	private static final float SELECTION_SCALE = 1.001f;
	
	private final World world;
	private final WorldCamera camera;
	
	private final VertexArray vertexArray;
	private final VertexBuffer cubeBuffer;
	
	private final BlockSelectionShader shader;
	
	private float time;
	private float prevTime;

	public BlockSelectionRenderer(World world, WorldCamera camera) {
		this.world = world;
		this.camera = camera;
		
		vertexArray = new VertexArray();
		cubeBuffer = createCubeBuffer();
		
		vertexArray.addVertexBuffer(cubeBuffer);
		
		shader = new BlockSelectionShader();
	
		time = 0.0f;
	}
	
	private static VertexBuffer createCubeBuffer() {
		VertexBuffer cubeBuffer = new VertexBuffer(new BufferLayout(
			new BufferAttrib("a_Position", BufferAttribType.FLOAT3)
		), CUBE_VERTEX_COUNT);
		
		ByteBuffer buffer = MemoryUtil.memAlloc(cubeBuffer.getCapacity());
		
		// FRONT
		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(1.0f);
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(1.0f);
		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(1.0f);

		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(1.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(1.0f);
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(1.0f);

		// BACK
		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(0.0f);

		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(0.0f);
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(0.0f);

		// BOTTOM
		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(1.0f);
		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(1.0f);

		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(1.0f);
		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(0.0f);

		// TOP 
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(1.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(0.0f);

		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(0.0f);
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(1.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(1.0f);

		// LEFT
		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(1.0f);
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(0.0f);
		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(0.0f);

		buffer.putFloat(0.0f).putFloat(0.0f).putFloat(1.0f);
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(1.0f);
		buffer.putFloat(0.0f).putFloat(1.0f).putFloat(0.0f);

		// RIGHT
		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(1.0f);
		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(1.0f);

		buffer.putFloat(1.0f).putFloat(0.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(0.0f);
		buffer.putFloat(1.0f).putFloat(1.0f).putFloat(1.0f);
		
		buffer.flip();
		
		cubeBuffer.bufferSubData(buffer, 0);
		
		MemoryUtil.memFree(buffer);
		
		return cubeBuffer;
	}
	
	public void update() {
		if (time > 2.0f * (float)Math.PI)
			time -= 2.0f * (float)Math.PI;

		prevTime = time;
		time += 0.5f;
	}

	public void render(float dt) {
		Mat4 viewMatrix = camera.getViewMat().inverseCopy();
		
		Vec3 forward = new Vec3(-viewMatrix.m20, -viewMatrix.m21, -viewMatrix.m22);
		BlockHitResult res = world.castBlockRay(viewMatrix.m30, viewMatrix.m31, viewMatrix.m32, forward);

		if (res != null) {
			shader.setProjViewMat(camera);
			
			Mat4 modlMat = new Mat4();
			modlMat.translate(res.pos.getX(), res.pos.getY(), res.pos.getZ());
			modlMat.scale(SELECTION_SCALE).translate((1.0f - SELECTION_SCALE) * 0.5f);
			
			shader.setModlMat(modlMat);
			
			float t = ((float)Math.sin(prevTime + (time - prevTime) * dt) + 1.0f) * 0.5f;
			float a = 0x40 / 255.0f + 0x3F / 255.0f * t;

			shader.setSelectionColor(SELECTION_COLOR_R, SELECTION_COLOR_G, SELECTION_COLOR_B, a);
			
			vertexArray.bind();
			shader.bind();

			glEnable(GL_BLEND);
			glDrawArrays(GL_TRIANGLES, 0, CUBE_VERTEX_COUNT);
			glDisable(GL_BLEND);
		}
	}

	@Override
	public void close() {
		vertexArray.close();
		cubeBuffer.close();
		
		shader.close();
	}
}
