package minecraft.graphic.opengl.texture;

public class BasicTextureRegion implements ITextureRegion {

	private final ITexture texture;
	
	private final float u0;
	private final float v0;
	private final float u1;
	private final float v1;

	public BasicTextureRegion(ITexture texture, float u0, float v0, float u1, float v1) {
		this.texture = texture;
		
		this.u0 = u0;
		this.v0 = v0;
		this.u1 = u1;
		this.v1 = v1;
	}
	
	@Override
	public ITexture getTexture() {
		return texture;
	}

	@Override
	public ITextureRegion getRegion(float u0, float v0, float u1, float v1) {
		float du = this.u1 - this.u0;
		float dv = this.v1 - this.v0;
		
		float nu0 = this.u0 + du * u0;
		float nv0 = this.v0 + dv * v0;
		
		float nu1 = this.u0 + du * u1;
		float nv1 = this.v0 + dv * v1;
		
		return new BasicTextureRegion(texture, nu0, nv0, nu1, nv1);
	}

	@Override
	public float getAspect() {
		float du = this.u1 - this.u0;
		float dv = this.v1 - this.v0;
		return texture.getAspect() * du / dv;
	}
	
	@Override
	public float getU0() {
		return u0;
	}

	@Override
	public float getV0() {
		return v0;
	}

	@Override
	public float getU1() {
		return u1;
	}

	@Override
	public float getV1() {
		return v1;
	}
}
