package mineclone.client.graphic.opengl;

import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import mineclone.client.graphic.ITexture;
import mineclone.common.util.FileUtil;

public class TextureLoader {

	public static ITexture loadTexture(String path) throws IOException {
		return loadTexture(path, true);
	}

	public static ITexture loadTexture(String path, boolean internal) throws IOException {
		int width;
		int height;
		int channels;

		ByteBuffer pixels = null;

		stbi_set_flip_vertically_on_load(true);

		try (MemoryStack stack = stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);

			// Decode the image
			if (internal) {
				InputStream is = TextureLoader.class.getResourceAsStream(path);
				if (is == null)
					throw new IOException("Unable to find image: " + path);
				
				ByteBuffer buffer;
				try {
					buffer = FileUtil.readAllBytes(is);
				} catch (IOException e) {
					throw new IOException("Failed to load image.", e);
				}
				
				pixels = stbi_load_from_memory(buffer, w, h, c, 0);
			} else {
				pixels = stbi_load(path, w, h, c, 0);
			}

			if (pixels == null)
				throw new IOException("Failed to load image: " + stbi_failure_reason());

			width = w.get(0);
			height = h.get(0);
			channels = c.get(0);
		}

		Texture texture = new Texture(pixels, width, height, channels);
		
		stbi_image_free(pixels);
		
		return texture;

	}
}