package minecraft.math;

public class Vec2 {

	public float x;
	public float y;
	
	public Vec2() {
		this(0.0f);
	}
	
	public Vec2(float c) {
		this(c, c);
	}

	public Vec2(float x, float y) {
		set(x, y);
	}

	public Vec2(Vec2 other) {
		set(other);
	}

	public Vec2 set(Vec2 other) {
		return set(other.x, other.y);
	}

	public Vec2 set(float c) {
		return set(c, c);
	}

	public Vec2 set(float x, float y) {
		this.x = x;
		this.y = y;
		
		return this;
	}

	public Vec2 add(Vec2 other) {
		return add(other.x, other.y);
	}
	
	public Vec2 add(float c) {
		return add(c, c);
	}

	public Vec2 add(float x, float y) {
		this.x += x;
		this.y += y;

		return this;
	}

	public Vec2 sub(Vec2 other) {
		return sub(other.x, other.y);
	}
	
	public Vec2 sub(float c) {
		return sub(c, c);
	}

	public Vec2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;

		return this;
	}

	public Vec2 mul(Vec2 other) {
		return mul(other.x, other.y);
	}
	
	public Vec2 mul(float c) {
		return mul(c, c);
	}

	public Vec2 mul(float x, float y) {
		this.x *= x;
		this.y *= y;

		return this;
	}

	public Vec2 div(Vec2 other) {
		return div(other.x, other.y);
	}
	
	public Vec2 div(float c) {
		return div(c, c);
	}

	public Vec2 div(float x, float y) {
		this.x /= x;
		this.y /= y;

		return this;
	}
	
	public float dot(Vec2 other) {
		return dot(other.x, other.y);
	}

	public float dot(float c) {
		return dot(c, c);
	}

	public float dot(float x, float y) {
		return this.x * x +
		       this.y * y;
	}

	public float lengthSqr() {
		return dot(this);
	}

	public float length() {
		return (float)Math.sqrt(lengthSqr());
	}

	public Vec2 normalize() {
		float len = length();
		return (len < LinMath.EPSILON) ? set(0.0f) : div(len);
	}

	public Vec2 copy() {
		return new Vec2(this);
	}
}
