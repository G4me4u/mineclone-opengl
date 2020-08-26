package mineclone.client.graphic;

public interface ITextureRegion {

	public ITexture getTexture();
	
	public ITextureRegion getUVRegion(float u0, float v0, float u1, float v1);

	public ITextureRegion getRegion(int xs0, int ys0, int xs1, int ys1);

	default public ITextureRegion getWithAspect(float aspect) {
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
		
		return getUVRegion(u0, v0, u1, v1);
	}

	public float getAspect();
	
	public float getU0();

	public float getV0();
	
	public float getU1();

	public float getV1();

}
