package minecraft.common.math;

public class Vec3 {

	public float x;
	public float y;
	public float z;
	
	public Vec3() {
		this(0.0f);
	}
	
	public Vec3(float c) {
		this(c, c, c);
	}

	public Vec3(float x, float y, float z) {
		set(x, y, z);
	}

	public Vec3(Vec3 other) {
		set(other);
	}

	public Vec3 set(Vec3 other) {
		return set(other.x, other.y, other.z);
	}

	public Vec3 set(float c) {
		return set(c, c, c);
	}

	public Vec3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}

	public Vec3 add(Vec3 other) {
		return add(other.x, other.y, other.z);
	}

	public Vec3 add(float c) {
		return add(c, c, c);
	}

	public Vec3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Vec3 sub(Vec3 other) {
		return sub(other.x, other.y, other.z);
	}

	public Vec3 sub(float c) {
		return sub(c, c, c);
	}

	public Vec3 sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}

	public Vec3 mul(Vec3 other) {
		return mul(other.x, other.y, other.z);
	}

	public Vec3 mul(float c) {
		return mul(c, c, c);
	}

	public Vec3 mul(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;

		return this;
	}

	public Vec3 div(Vec3 other) {
		return div(other.x, other.y, other.z);
	}

	public Vec3 div(float c) {
		return div(c, c, c);
	}

	public Vec3 div(float x, float y, float z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;

		return this;
	}

	public float dot(Vec3 other) {
		return dot(other.x, other.y, other.z);
	}
	
	public float dot(float c) {
		return dot(c, c, c);
	}

	public float dot(float x, float y, float z) {
		return this.x * x +
		       this.y * y +
		       this.z * z;
	}

	public Vec3 cross(Vec3 right) {
		return cross(right.x, right.y, right.z);
	}

	public Vec3 cross(float c) {
		return cross(c, c, c);
	}

	public Vec3 cross(float x, float y, float z) {
		return new Vec3(this.y * z - this.z * y, 
		                this.z * x - this.x * z, 
		                this.x * y - this.y * x);
	}
	
	public float lengthSqr() {
		return dot(this);
	}

	public float length() {
		return (float)Math.sqrt(lengthSqr());
	}

	public Vec3 normalize() {
		float len = length();
		return (len < LinMath.EPSILON) ? set(0.0f) : div(len);
	}

	public Vec3 copy() {
		return new Vec3(this);
	}
}
