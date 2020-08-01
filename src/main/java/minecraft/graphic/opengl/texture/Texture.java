package minecraft.graphic.opengl.texture;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_R8;
import static org.lwjgl.opengl.GL30.GL_RG;
import static org.lwjgl.opengl.GL30.GL_RG8;
import static org.lwjgl.opengl.GL45.glCreateTextures;
import static org.lwjgl.opengl.GL45.glTextureParameteri;
import static org.lwjgl.opengl.GL45.glTextureStorage2D;
import static org.lwjgl.opengl.GL45.glTextureSubImage2D;

import java.nio.ByteBuffer;

public class Texture implements ITexture {

	private final int width;
	private final int height;
	
	private final int textureHandle;
	
	private final int dataFormat;
	
	public Texture(int width, int height) {
		this.width = width;
		this.height = height;

		textureHandle = createTexture(width, height, GL_RGBA8);
		
		dataFormat = GL_RGBA;
	}

	public Texture(ByteBuffer pixels, int width, int height, int channels) {
		this.width = width;
		this.height = height;
		
		int internalFormat, dataFormat;
		
		switch (channels) {
		case 1:
			internalFormat = GL_R8;
			dataFormat = GL_RED;
			break;
		case 2:
			internalFormat = GL_RG8;
			dataFormat = GL_RG;
			break;
		case 3:
			internalFormat = GL_RGB8;
			dataFormat = GL_RGB;
			break;
		case 4:
			internalFormat = GL_RGBA8;
			dataFormat = GL_RGBA;
			break;
		default:
			throw new IllegalArgumentException("channels must be 1-4");
		}
		
		textureHandle = createTexture(width, height, internalFormat);
		
		this.dataFormat = dataFormat;
		
		setData(pixels, channels);
	}
	
	private static int createTexture(int width, int height, int internalFormat) {
		if (width == 0 || height == 0)
			throw new IllegalArgumentException("Texture size must be positive!");
		
		int textureHandle = glCreateTextures(GL_TEXTURE_2D);
		
		glTextureStorage2D(textureHandle, 1, internalFormat, width, height);
		
		// Use nearest neighbor for scaling
		glTextureParameteri(textureHandle, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTextureParameteri(textureHandle, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		
		// Repeat texture when wrapped
		glTextureParameteri(textureHandle, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTextureParameteri(textureHandle, GL_TEXTURE_WRAP_T, GL_REPEAT);
	
		return textureHandle;
	}
	
	@Override
	public void setData(ByteBuffer data, int channels) {
		if (width * height * channels != data.remaining())
			throw new IllegalArgumentException("Data must be entire texture!");
		
		int alignment = 4;
		if (dataFormat != GL_RGBA8 && (width & 3) != 0) {
			// Pixel rows are not divisible by 4.
			alignment = 2 - (width & 1);
		}
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, alignment);
		glTextureSubImage2D(textureHandle, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data);
	}

	@Override
	public void bind(int slot) {
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, textureHandle);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public ITexture getTexture() {
		return this;
	}

	@Override
	public void dispose() {
		glDeleteTextures(textureHandle);
	}
}
