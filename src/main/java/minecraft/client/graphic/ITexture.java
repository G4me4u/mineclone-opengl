package minecraft.client.graphic;

import java.nio.ByteBuffer;

import minecraft.common.IResource;

public interface ITexture extends ITextureRegion, IResource {

	public void setData(ByteBuffer pixels, int channels);

	default public void bind() {
		bind(0);
	}
	
	public void bind(int slot);

	public int getWidth();

	public int getHeight();
	
	@Override
	default public ITextureRegion getRegion(float u0, float v0, float u1, float v1) {
		return new BasicTextureRegion(getTexture(), u0, v0, u1, v1);
	}

	@Override
	default public float getU0() {
		return 0.0f;
	}

	@Override
	default public float getV0() {
		return 0.0f;
	}

	@Override
	default public float getU1() {
		return 1.0f;
	}

	@Override
	default public float getV1() {
		return 1.0f;
	}

	@Override
	default public float getAspect() {
		return (float)getWidth() / getHeight();
	}
}
