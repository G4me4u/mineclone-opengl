package minecraft.client.graphic.opengl;

import static org.lwjgl.opengl.GL45.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import minecraft.common.IResource;
import minecraft.common.math.Mat3;
import minecraft.common.math.Mat4;
import minecraft.common.math.Vec2;
import minecraft.common.math.Vec3;
import minecraft.common.math.Vec4;

public class ShaderProgram implements IResource {

	private static final FloatBuffer matBuffer;

	static {
		matBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	private final int programHandle;
	private final int vertexShaderHandle;
	private final int fragmentShaderHandle;

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderHandle = loadRoutine(vertexFile, GL_VERTEX_SHADER);
		fragmentShaderHandle = loadRoutine(fragmentFile, GL_FRAGMENT_SHADER);
		
		programHandle = glCreateProgram();

		glAttachShader(programHandle, vertexShaderHandle);
		glAttachShader(programHandle, fragmentShaderHandle);
		
		glLinkProgram(programHandle);
		glValidateProgram(programHandle);
	}
	
	private static int loadRoutine(String sourcePath, int type) {
		InputStream is = ShaderProgram.class.getResourceAsStream(sourcePath);
		
		if (is == null)
			throw new RuntimeException("Shader source not found: " + sourcePath);
		
		StringBuilder shaderSource = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line);
				shaderSource.append('\n');
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to load shader: " + sourcePath, e);
		}
		
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			int logLength = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
			System.out.println(glGetShaderInfoLog(shaderID, logLength));
			throw new RuntimeException("Could not compile shader: " + sourcePath);
		}
		
		return shaderID;
	}

	protected void uniformInt(int location, int value) {
		glProgramUniform1i(programHandle, location, value);
	}

	protected void uniformIntArray(int location, int[] arr) {
		glProgramUniform1iv(programHandle, location, arr);
	}

	protected void uniformFloat(int location, float value) {
		glProgramUniform1f(programHandle, location, value);
	}

	protected void uniformFloat2(int location, Vec2 vec) {
		uniformFloat2(location, vec.x, vec.y);
	}

	protected void uniformFloat2(int location, float x, float y) {
		glProgramUniform2f(programHandle, location, x, y);
	}

	protected void uniformFloat3(int location, Vec3 vec) {
		uniformFloat3(location, vec.x, vec.y, vec.z);
	}

	protected void uniformFloat3(int location, float x, float y, float z) {
		glProgramUniform3f(programHandle, location, x, y, z);
	}

	protected void uniformVec4(int location, Vec4 vec) {
		uniformFloat4(location, vec.x, vec.y, vec.z, vec.w);
	}

	protected void uniformFloat4(int location, float x, float y, float z, float w) {
		glProgramUniform4f(programHandle, location, x, y, z, w);
	}
	
	protected void uniformMat3(int location, Mat3 m) {
		matBuffer.clear();
		m.writeBuffer(matBuffer, false);
		matBuffer.flip();
		
		glProgramUniformMatrix3fv(programHandle, location, false, matBuffer);
	}

	protected void uniformMat4(int location, Mat4 m) {
		matBuffer.clear();
		m.writeBuffer(matBuffer, false);
		matBuffer.flip();
		
		glProgramUniformMatrix4fv(programHandle, location, false, matBuffer);
	}
	
	protected int getUniformLocation(String uniformName) {
		int location = glGetUniformLocation(programHandle, uniformName);
		
		if (location == -1)
			System.out.println("Unable to find uniform: " + uniformName);
		
		return location;
	}
	
	public void bind() {
		glUseProgram(programHandle);
	}
	
	@Override
	public void dispose() {
		glDetachShader(programHandle, vertexShaderHandle);
		glDetachShader(programHandle, fragmentShaderHandle);
		
		glDeleteShader(vertexShaderHandle);
		glDeleteShader(fragmentShaderHandle);
		
		glDeleteProgram(programHandle);
	}
}
