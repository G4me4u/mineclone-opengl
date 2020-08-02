package minecraft.common.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Mat2 {

	/** Element m[c][r], where c is column and r is row. */
	public float m00, m10,
	             m01, m11;
	
	public Mat2() {
		toIdentity();
	}

	public Mat2(float d) {
		toDiagonal(d);
	}

	public Mat2(Mat2 other) {
		set(other);
	}

	public Mat2 toIdentity() {
		return toDiagonal(1.0f);
	}

	public Mat2 toDiagonal(float d) {
		m00 = m11 = d;

		m10 = 0.0f; // r = 0
		m01 = 0.0f; // r = 1

		return this;
	}

	public Mat2 rotateZ(float radians) {
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);

		float n00 = m00 * c + m10 * s;
		float n01 = m01 * c + m11 * s;

		m10 = m10 * c - m00 * s;
		m11 = m11 * c - m01 * s;

		m00 = n00;
		m01 = n01;

		return this;
	}

	public Mat2 scale(Vec2 scl) {
		return scale(scl.x, scl.y);
	}

	public Mat2 scale(float c) {
		return scale(c, c);
	}

	public Mat2 scale(float xs, float ys) {
		m00 *= xs;
		m01 *= xs;

		m10 *= ys;
		m11 *= ys;

		return this;
	}

	public Mat2 add(Mat2 right) {
		return add(right, this);
	}

	public Mat2 add(Mat2 right, Mat2 dest) {
		dest.m00 = m00 + right.m00;
		dest.m01 = m01 + right.m01;

		dest.m10 = m10 + right.m10;
		dest.m11 = m11 + right.m11;

		return dest;
	}

	public Mat2 sub(Mat2 right) {
		return sub(right, this);
	}
	
	public Mat2 sub(Mat2 right, Mat2 dest) {
		dest.m00 = m00 - right.m00;
		dest.m01 = m01 - right.m01;

		dest.m10 = m10 - right.m10;
		dest.m11 = m11 - right.m11;

		return dest;
	}

	public Mat2 mul(Mat2 right) {
		return mul(right, this);
	}

	public Mat2 mul(Mat2 right, Mat2 dest) {
		float n00 = m00 * right.m00 + m10 * right.m01;
		float n01 = m01 * right.m00 + m11 * right.m01;

		float n10 = m00 * right.m10 + m10 * right.m11;
		float n11 = m01 * right.m10 + m11 * right.m11;

		dest.m00 = n00;
		dest.m01 = n01;

		dest.m10 = n10;
		dest.m11 = n11;

		return dest;
	}

	public Vec2 mul(Vec2 right) {
		return mul(right, right);
	}

	public Vec2 mul(Vec2 right, Vec2 dest) {
		float nx = m00 * right.x + m10 * right.y;
		dest.y   = m01 * right.x + m11 * right.y;
		
		dest.x = nx;

		return dest;
	}
	
	public Mat2 invert() {
		return inverseCopy(this);
	}
	
	public Mat2 inverseCopy() {
		return inverseCopy(new Mat2());
	}

	public Mat2 inverseCopy(Mat2 dest) {
		float det = m00 * m11 - m10 * m01;
		
		if (LinMath.nearZero(det))
			return null;

		return dest.set( m11 / det, -m10 / det, 
		                -m01 / det,  m00 / det);
	}
	
	public Mat2 copy() {
		return copy(new Mat2());
	}
	
	public Mat2 copy(Mat2 dest) {
		return dest.set(m00, m10,
		                m01, m11);
	}

	public Mat2 set(Mat2 other) {
		return set(other.m00, other.m10,
		           other.m01, other.m11);
	}
	
	public Mat2 set(float m00, float m10,
	                float m01, float m11) {
		
		this.m00 = m00;
		this.m01 = m01;

		this.m10 = m10;
		this.m11 = m11;
	
		return this;
	}

	public void writeBuffer(FloatBuffer buf, boolean rowMajor) {
		if (rowMajor) {
			buf.put(m00).put(m10)
			   .put(m01).put(m11);
		} else {
			buf.put(m00).put(m01)
			   .put(m10).put(m11);
		}
	}

	public void writeBuffer(ByteBuffer buf, boolean rowMajor) {
		if (rowMajor) {
			buf.putFloat(m00).putFloat(m10)
			   .putFloat(m01).putFloat(m11);
		} else {
			buf.putFloat(m00).putFloat(m01)
			   .putFloat(m10).putFloat(m11);
		}
	}
}
