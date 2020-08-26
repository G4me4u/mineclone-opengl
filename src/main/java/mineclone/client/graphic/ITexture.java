package mineclone.client.graphic;

import java.nio.ByteBuffer;

import mineclone.common.IResource;

public interface ITexture extends ITextureRegion, IResource {

	public void setData(ByteBuffer pixels, int channels);

	default public void bind() {
		bind(0);
	}
	
	public void bind(int slot);

	public int getWidth();

	public int getHeight();
	
	@Override
	default public ITextureRegion getUVRegion(float u0, float v0, float u1, float v1) {
		return new BasicTextureRegion(getTexture(), u0, v0, u1, v1);
	}

	@Override
	default public ITextureRegion getRegion(int xs0, int ys0, int xs1, int ys1) {
		// Note that v0 and v1 are flipped with integer regions.
		float u0 = (float)xs0 / getWidth();
		float v0 = 1.0f - (float)ys1 / getHeight();
		float u1 = (float)xs1 / getWidth();
		float v1 = 1.0f - (float)ys0 / getHeight();

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
