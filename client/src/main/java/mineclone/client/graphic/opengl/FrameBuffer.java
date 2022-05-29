package mineclone.client.graphic.opengl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL45.glBlitNamedFramebuffer;
import static org.lwjgl.opengl.GL45.glCreateFramebuffers;
import static org.lwjgl.opengl.GL45.glCreateRenderbuffers;
import static org.lwjgl.opengl.GL45.glCreateTextures;
import static org.lwjgl.opengl.GL45.glNamedFramebufferDrawBuffer;
import static org.lwjgl.opengl.GL45.glNamedFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL45.glNamedFramebufferTexture;
import static org.lwjgl.opengl.GL45.glNamedRenderbufferStorage;
import static org.lwjgl.opengl.GL45.glNamedRenderbufferStorageMultisample;
import static org.lwjgl.opengl.GL45.glTextureParameteri;
import static org.lwjgl.opengl.GL45.glTextureStorage2D;

import java.nio.ByteBuffer;

import mineclone.client.graphic.ITexture;
import mineclone.common.IResource;
import mineclone.common.util.DebugUtil;

public class FrameBuffer implements IResource {
	
	/* TODO: make this dependent on hardware support. */
	private static final int MULTISAMPLE_COUNT = 4;
	
	private final FrameBufferType type;
	private final boolean multisampled;

	private int width;
	private int height;
	
	private int frameBufferHandle;
	private int colorBufferHandle;
	private int depthBufferHandle;

	/* A way to get access to the color buffer if it is a texture. */
	private final FrameBufferTexture colorTextureAttachment;
	
	public FrameBuffer(FrameBufferType type, int width, int height) {
		this.type = type;
		
		multisampled = (type == FrameBufferType.MULTISAMPLED_DEPTH_AND_COLOR);
		
		this.width = width;
		this.height = height;
		
		frameBufferHandle = glCreateFramebuffers();

		FrameBufferTexture colorTextureAttachment = null;
		if (multisampled) {
			colorBufferHandle = createRenderBufferAttachment(frameBufferHandle, GL_COLOR_ATTACHMENT0);
			depthBufferHandle = createRenderBufferAttachment(frameBufferHandle, GL_DEPTH_ATTACHMENT);
		} else {
			if (type.hasColorAttachment()) {
				colorBufferHandle = createTextureAttachment(frameBufferHandle, GL_COLOR_ATTACHMENT0);
				colorTextureAttachment = new FrameBufferTexture(colorBufferHandle);
			}
			
			if (type.hasDepthAttachment())
				depthBufferHandle = createRenderBufferAttachment(frameBufferHandle, GL_DEPTH_ATTACHMENT);
		}
		
		this.colorTextureAttachment = colorTextureAttachment;

		updateAttachmentSize();
	}

	private static int createTextureAttachment(int frameBufferHandle, int attachment) {
		// Create it as a texture that can be sampled.
		int textureHandle = glCreateTextures(GL_TEXTURE_2D);
		
		glTextureParameteri(textureHandle, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTextureParameteri(textureHandle, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		glTextureParameteri(textureHandle, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTextureParameteri(textureHandle, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		glNamedFramebufferTexture(frameBufferHandle, attachment, textureHandle, 0);
		glNamedFramebufferDrawBuffer(frameBufferHandle, attachment);

		return textureHandle;
	}
	
	private static int createRenderBufferAttachment(int frameBufferHandle, int attachment) {
		int bufferHandle = glCreateRenderbuffers();
		
		glNamedFramebufferRenderbuffer(frameBufferHandle, attachment, GL_RENDERBUFFER, bufferHandle);
		
		return bufferHandle;
	}

	private void updateAttachmentSize() {
		if (type.hasColorAttachment()) {
			if (multisampled) {
				updateRenderBufferSize(colorBufferHandle, width, height, true, GL_RGBA8);
			} else {
				updateTextureSize(colorBufferHandle, width, height, GL_RGBA8, GL_RGBA);
				colorTextureAttachment.setSize(width, height);
			}
		}
		
		if (type.hasDepthAttachment())
			updateRenderBufferSize(depthBufferHandle, width, height, multisampled, GL_DEPTH_COMPONENT24);
	}
	
	private static void updateTextureSize(int bufferHandle, int width, int height, int internalFormat, int type) {
		glTextureStorage2D(bufferHandle, 1, width, height, internalFormat);
	}

	private static void updateRenderBufferSize(int bufferHandle, int width, int height, boolean multisampled, int internalFormat) {
		if (multisampled) {
			glNamedRenderbufferStorageMultisample(bufferHandle, MULTISAMPLE_COUNT, internalFormat, width, height);
		} else {
			glNamedRenderbufferStorage(bufferHandle, internalFormat, width, height);
		}
	}
	
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferHandle);
		glViewport(0, 0, width, height);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void setSize(int width, int height) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (width <= 0 || height <= 0)
				throw new IllegalArgumentException("Dimensions must be positive.");
		}
		
		if (this.width != width || this.height != height) {
			this.width = width;
			this.height = height;
			
			updateAttachmentSize();
		}
	}
	
	public void resolveTo(FrameBuffer target) {
		int flags = 0;
		if (target.type.hasColorAttachment())
			flags |= GL_COLOR_BUFFER_BIT;
		if (target.type.hasDepthAttachment())
			flags |= GL_DEPTH_BUFFER_BIT;

		resolve(target.frameBufferHandle, target.width, target.height, flags);
	}

	public void resolveToScreen(int screenWidth, int screenHeight) {
		resolveToScreen(screenWidth, screenHeight, GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void resolveToScreen(int screenWidth, int screenHeight, int flags) {
		resolve(0, screenWidth, screenHeight, flags);
	}
	
	private void resolve(int targetFrameBufferHandle, int targetWidth, int targetHeight, int flags) {
		if (DebugUtil.PERFORM_CHECKS) {
			if (!multisampled)
				throw new IllegalStateException("Resolving is only allowed for multisampled framebuffers!");
		}
		
		glBlitNamedFramebuffer(frameBufferHandle, targetFrameBufferHandle, 0, 0, width, height, 
				0, 0, targetWidth, targetHeight, flags, GL_NEAREST);
	}

	public FrameBufferType getType() {
		return type;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public ITexture getColorTextureAttachment() {
		if (DebugUtil.PERFORM_CHECKS) {
			if (!type.hasColorAttachment())
				throw new IllegalStateException("FrameBuffer does not have a color attachment!");
			if (multisampled)
				throw new IllegalStateException("Multisampled frame buffers can not be sampled!");
		}
		
		return colorTextureAttachment;
	}
	
	@Override
	public void close() {
		if (multisampled) {
			glDeleteRenderbuffers(colorBufferHandle);
		} else {
			glDeleteTextures(colorBufferHandle);
		}

		glDeleteRenderbuffers(depthBufferHandle);
		glDeleteFramebuffers(frameBufferHandle);
	}
	
	private static class FrameBufferTexture implements ITexture {
		
		private final int textureHandle;
		
		private int width;
		private int height;
		
		public FrameBufferTexture(int textureHandle) {
			this.textureHandle = textureHandle;
		}
		
		public void setSize(int width, int height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public ITexture getTexture() {
			return this;
		}

		@Override
		public void setData(ByteBuffer pixels, int channels) {
			throw new UnsupportedOperationException("setData is not allowed on FrameBuffer attachments!");
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
		public void close() {
			// Handled by FrameBuffer#dispose
		}
	}
}
