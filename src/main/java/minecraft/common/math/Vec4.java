package minecraft.common.math;

public class Vec4 {

	public float x;
	public float y;
	public float z;
	public float w;

	public Vec4() {
		this(0.0f);
	}
	
	public Vec4(float c) {
		this(c, c, c, c);
	}

	public Vec4(float x, float y, float z, float w) {
		set(x, y, z, w);
	}

	public Vec4(Vec4 other) {
		set(other);
	}

	public Vec4 set(Vec4 other) {
		return set(other.x, other.y, other.z, other.w);
	}

	public Vec4 set(float c) {
		return set(c, c, c, c);
	}
	
	public Vec4 set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
		return this;
	}

	public Vec4 add(Vec4 other) {
		return add(other.x, other.y, other.z, other.w);
	}

	public Vec4 add(float c) {
		return add(c, c, c, c);
	}

	public Vec4 add(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;

		return this;
	}

	public Vec4 sub(Vec4 other) {
		return sub(other.x, other.y, other.z, other.w);
	}
	
	public Vec4 sub(float c) {
		return sub(c, c, c, c);
	}

	public Vec4 sub(float x, float y, float z, float w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;

		return this;
	}

	public Vec4 mul(Vec4 other) {
		return mul(other.x, other.y, other.z, other.w);
	}
	
	public Vec4 mul(float c) {
		return mul(c, c, c, c);
	}

	public Vec4 mul(float x, float y, float z, float w) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		this.w *= w;

		return this;
	}

	public Vec4 div(Vec4 other) {
		return div(other.x, other.y, other.z, other.w);
	}
	
	public Vec4 div(float c) {
		return div(c, c, c, c);
	}

	public Vec4 div(float x, float y, float z, float w) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		this.w /= w;

		return this;
	}

	public float dot(Vec4 other) {
		return dot(other.x, other.y, other.z, other.w);
	}

	public float dot(float c) {
		return dot(c, c, c, c);
	}

	public float dot(float x, float y, float z, float w) {
		return this.x * x + 
		       this.y * y + 
		       this.z * z + 
		       this.w * w;
	}

	public float lengthSqr() {
		return dot(this);
	}

	public float length() {
		return (float)Math.sqrt(lengthSqr());
	}

	public Vec4 normalize() {
		float len = length();
		return (len < LinMath.EPSILON) ? set(0.0f) : div(len);
	}
	
	public Vec4 copy() {
		return new Vec4(this);
	}
}
