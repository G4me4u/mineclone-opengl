package minecraft.client.graphic;

import minecraft.common.math.LinMath;

public interface ITextureRegion {

	public ITexture getTexture();
	
	public ITextureRegion getRegion(float u0, float v0, float u1, float v1);
	
	default public ITextureRegion getWithAspect(float aspect) {
		if (aspect < LinMath.EPSILON)
			throw new IllegalArgumentException("Aspect ratio is near zero!");
		
		float du = 1.0f;
		float dv = 1.0f;
		
		float ratio = aspect / getAspect();
		if (ratio > 1.0f) {
			dv /= ratio;
		} else {
			du *= ratio;
		}

		float u0 = (1.0f - du) * 0.5f;
		float v0 = (1.0f - dv) * 0.5f;
		float u1 = u0 + du;
		float v1 = v0 + dv;
		
		return getRegion(u0, v0, u1, v1);
	}

	public float getAspect();
	
	public float getU0();

	public float getV0();
	
	public float getU1();

	public float getV1();

}
