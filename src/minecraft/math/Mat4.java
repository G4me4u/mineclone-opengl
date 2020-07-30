package minecraft.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Mat4 {

	/** Element m[c][r], where c is column and r is row. */
	public float m00, m10, m20, m30,
	             m01, m11, m21, m31,
	             m02, m12, m22, m32,
	             m03, m13, m23, m33;
	
	public Mat4() {
		toIdentity();
	}

	public Mat4(float d) {
		toDiagonal(d);
	}

	public Mat4(Mat4 other) {
		set(other);
	}

	public Mat4 toIdentity() {
		return toDiagonal(1.0f);
	}

	public Mat4 toDiagonal(float d) {
		m00 = m11 = m22 = m33 = d;

		m10 = m20 = m30 = 0.0f; // r = 0
		m01 = m21 = m31 = 0.0f; // r = 1
		m02 = m12 = m32 = 0.0f; // r = 2
		m03 = m13 = m23 = 0.0f; // r = 3

		return this;
	}

	public Mat4 toOrthographic(float left, float right, float top, float bottom, float near, float far) {
		toIdentity();

		m00 = 2.0f / (right - left);
		m11 = 2.0f / (top - bottom);
		m22 = 2.0f / (near - far);

		m30 = (left + right) / (left - right);
		m31 = (bottom + top) / (bottom - top);
		m32 = (far + near) / (far - near);

		return this;
	}

	public Mat4 toPerspective(float fovRadians, float aspect, float near, float far) {
		toIdentity();

		float q = (float)(1.0 / Math.tan(fovRadians * 0.5));

		m00 = q / aspect;
		m11 = q;
		m22 = -(far + near) / (far - near);
		m33 = 0.0f;

		m23 = -1.0f;
		m32 = -2.0f * far * near / (far - near);

		return this;
	}

	public Mat4 rotateX(float radians) {
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);

		float n10 = m10 * c + m20 * s;
		float n11 = m11 * c + m21 * s;
		float n12 = m12 * c + m22 * s;
		float n13 = m13 * c + m23 * s;

		m20 = m20 * c - m10 * s;
		m21 = m21 * c - m11 * s;
		m22 = m22 * c - m12 * s;
		m23 = m23 * c - m13 * s;

		m10 = n10;
		m11 = n11;
		m12 = n12;
		m13 = n13;

		return this;
	}

	public Mat4 rotateY(float radians) {
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);

		float n00 = m00 * c - m20 * s;
		float n01 = m01 * c - m21 * s;
		float n02 = m02 * c - m22 * s;
		float n03 = m03 * c - m23 * s;

		m20 = m00 * s + m20 * c;
		m21 = m01 * s + m21 * c;
		m22 = m02 * s + m22 * c;
		m23 = m03 * s + m23 * c;

		m00 = n00;
		m01 = n01;
		m02 = n02;
		m03 = n03;

		return this;
	}

	public Mat4 rotateZ(float radians) {
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);

		float n00 = m00 * c + m10 * s;
		float n01 = m01 * c + m11 * s;
		float n02 = m02 * c + m12 * s;
		float n03 = m03 * c + m13 * s;

		m10 = m10 * c - m00 * s;
		m11 = m11 * c - m01 * s;
		m12 = m12 * c - m02 * s;
		m13 = m13 * c - m03 * s;

		m00 = n00;
		m01 = n01;
		m02 = n02;
		m03 = n03;

		return this;
	}

	public Mat4 rotate(float radians, Vec3 axis) {
		return rotate(radians, axis.x, axis.y, axis.z);
	}

	public Mat4 rotate(float radians, float ax, float ay, float az) {
		return mul(new Mat4().setRotation(radians, ax, ay, az));
	}
	
	public Mat4 setRotation(float radians, Vec3 axis) {
		return setRotation(radians, axis.x, axis.y, axis.z);
	}

	public Mat4 setRotation(float radians, float ax, float ay, float az) {
		float c = (float)Math.cos(radians);
		float s = (float)Math.sin(radians);
		float omc = 1.0f - c;

		float xy = ax * ay;
		float xz = ax * az;
		float yz = ay * az;

		float xs = ax * s;
		float ys = ay * s;
		float zs = az * s;

		m00 = ax * ax * omc + c;
		m01 = xy * omc + zs;
		m02 = xz * omc - ys;

		m10 = xy * omc - zs;
		m11 = ay * ay * omc + c;
		m12 = yz * omc + xs;

		m20 = xz * omc + ys;
		m21 = yz * omc - xs;
		m22 = az * az * omc + c;

		return this;
	}

	public Mat4 translate(Vec3 trs) {
		return translate(trs.x, trs.y, trs.z);
	}

	public Mat4 translate(float c) {
		return translate(c, c, c);
	}

	public Mat4 translate(float xt, float yt, float zt) {
		m30 += m00 * xt + m10 * yt + m20 * zt;
		m31 += m01 * xt + m11 * yt + m21 * zt;
		m32 += m02 * xt + m12 * yt + m22 * zt;

		// This should always yield 1.0 but leave
		// it anyway for mathematical correctness.
		m33 += m03 * xt + m13 * yt + m23 * zt;

		return this;
	}

	public Mat4 scale(Vec3 scl) {
		return scale(scl.x, scl.y, scl.z);
	}

	public Mat4 scale(float c) {
		return scale(c, c, c);
	}

	public Mat4 scale(float xs, float ys, float zs) {
		m00 *= xs;
		m01 *= xs;
		m02 *= xs;
		m03 *= xs;

		m10 *= ys;
		m11 *= ys;
		m12 *= ys;
		m13 *= ys;

		m20 *= zs;
		m21 *= zs;
		m22 *= zs;
		m23 *= zs;

		return this;
	}

	public Mat4 add(Mat4 right) {
		return add(right, this);
	}

	public Mat4 add(Mat4 right, Mat4 dest) {
		dest.m00 = m00 + right.m00;
		dest.m01 = m01 + right.m01;
		dest.m02 = m02 + right.m02;
		dest.m03 = m03 + right.m03;

		dest.m10 = m10 + right.m10;
		dest.m11 = m11 + right.m11;
		dest.m12 = m12 + right.m12;
		dest.m13 = m13 + right.m13;

		dest.m20 = m20 + right.m20;
		dest.m21 = m21 + right.m21;
		dest.m22 = m22 + right.m22;
		dest.m23 = m23 + right.m23;

		dest.m30 = m30 + right.m30;
		dest.m31 = m31 + right.m31;
		dest.m32 = m32 + right.m32;
		dest.m33 = m33 + right.m33;
		
		return dest;
	}

	public Mat4 sub(Mat4 right) {
		return sub(right, this);
	}
	
	public Mat4 sub(Mat4 right, Mat4 dest) {
		dest.m00 = m00 - right.m00;
		dest.m01 = m01 - right.m01;
		dest.m02 = m02 - right.m02;
		dest.m03 = m03 - right.m03;

		dest.m10 = m10 - right.m10;
		dest.m11 = m11 - right.m11;
		dest.m12 = m12 - right.m12;
		dest.m13 = m13 - right.m13;

		dest.m20 = m20 - right.m20;
		dest.m21 = m21 - right.m21;
		dest.m22 = m22 - right.m22;
		dest.m23 = m23 - right.m23;

		dest.m30 = m30 - right.m30;
		dest.m31 = m31 - right.m31;
		dest.m32 = m32 - right.m32;
		dest.m33 = m33 - right.m33;
		
		return dest;
	}

	public Mat4 mul(Mat4 right) {
		return mul(right, this);
	}

	public Mat4 mul(Mat4 right, Mat4 dest) {
		float n00 = m00 * right.m00 + m10 * right.m01 + m20 * right.m02 + m30 * right.m03;
		float n01 = m01 * right.m00 + m11 * right.m01 + m21 * right.m02 + m31 * right.m03;
		float n02 = m02 * right.m00 + m12 * right.m01 + m22 * right.m02 + m32 * right.m03;
		float n03 = m03 * right.m00 + m13 * right.m01 + m23 * right.m02 + m33 * right.m03;

		float n10 = m00 * right.m10 + m10 * right.m11 + m20 * right.m12 + m30 * right.m13;
		float n11 = m01 * right.m10 + m11 * right.m11 + m21 * right.m12 + m31 * right.m13;
		float n12 = m02 * right.m10 + m12 * right.m11 + m22 * right.m12 + m32 * right.m13;
		float n13 = m03 * right.m10 + m13 * right.m11 + m23 * right.m12 + m33 * right.m13;

		float n20 = m00 * right.m20 + m10 * right.m21 + m20 * right.m22 + m30 * right.m23;
		float n21 = m01 * right.m20 + m11 * right.m21 + m21 * right.m22 + m31 * right.m23;
		float n22 = m02 * right.m20 + m12 * right.m21 + m22 * right.m22 + m32 * right.m23;
		float n23 = m03 * right.m20 + m13 * right.m21 + m23 * right.m22 + m33 * right.m23;

		float n30 = m00 * right.m30 + m10 * right.m31 + m20 * right.m32 + m30 * right.m33;
		float n31 = m01 * right.m30 + m11 * right.m31 + m21 * right.m32 + m31 * right.m33;
		float n32 = m02 * right.m30 + m12 * right.m31 + m22 * right.m32 + m32 * right.m33;
		float n33 = m03 * right.m30 + m13 * right.m31 + m23 * right.m32 + m33 * right.m33;

		dest.m00 = n00;
		dest.m01 = n01;
		dest.m02 = n02;
		dest.m03 = n03;

		dest.m10 = n10;
		dest.m11 = n11;
		dest.m12 = n12;
		dest.m13 = n13;

		dest.m20 = n20;
		dest.m21 = n21;
		dest.m22 = n22;
		dest.m23 = n23;

		dest.m30 = n30;
		dest.m31 = n31;
		dest.m32 = n32;
		dest.m33 = n33;

		return dest;
	}

	public Vec4 mul(Vec4 right) {
		return mul(right, right);
	}

	public Vec4 mul(Vec4 right, Vec4 dest) {
		float nx = m00 * right.x + m10 * right.y + m20 * right.z + m30 * right.w;
		float ny = m01 * right.x + m11 * right.y + m21 * right.z + m31 * right.w;
		float nz = m02 * right.x + m12 * right.y + m22 * right.z + m32 * right.w;
		dest.w   = m03 * right.x + m13 * right.y + m23 * right.z + m33 * right.w;

		dest.x = nx;
		dest.y = ny;
		dest.z = nz;

		return dest;
	}

	public Vec3 mul(Vec3 right) {
		return mul(right, right);
	}

	public Vec3 mul(Vec3 right, Vec3 dest) {
		float nx = m00 * right.x + m10 * right.y + m20 * right.z + m30;
		float ny = m01 * right.x + m11 * right.y + m21 * right.z + m31;
		dest.z   = m02 * right.x + m12 * right.y + m22 * right.z + m32;
		
		dest.x = nx;
		dest.y = ny;

		return dest;
	}
	
	public Mat4 invert() {
		return inverseCopy(this);
	}

	public Mat4 inverseCopy() {
		return inverseCopy(new Mat4());
	}

	public Mat4 inverseCopy(Mat4 dest) {
		Vec4 al = new Vec4(m00, m10, m20, m30);
		Vec4 bl = new Vec4(m01, m11, m21, m31);
		Vec4 cl = new Vec4(m02, m12, m22, m32);
		Vec4 dl = new Vec4(m03, m13, m23, m33);
		
		Vec4 ar = new Vec4(1.0f, 0.0f, 0.0f, 0.0f);
		Vec4 br = new Vec4(0.0f, 1.0f, 0.0f, 0.0f);
		Vec4 cr = new Vec4(0.0f, 0.0f, 1.0f, 0.0f);
		Vec4 dr = new Vec4(0.0f, 0.0f, 0.0f, 1.0f);
		
		// First row check
		if (LinMath.nearZero(al.x)) {
			if (!LinMath.nearZero(bl.x)) {
				al.add(       1.0f, bl.y / bl.x, bl.z / bl.x, bl.w / bl.x);
				ar.add(br.x / bl.x, br.y / bl.x, br.z / bl.x, br.w / bl.x);
			} else if (!LinMath.nearZero(cl.x)) {
				al.add(       1.0f, cl.y / cl.x, cl.z / cl.x, cl.w / cl.x);
				ar.add(cr.x / cl.x, cr.y / cl.x, cr.z / cl.x, cr.w / cl.x);
			} else if (!LinMath.nearZero(dl.x)) {
				al.add(       1.0f, dl.y / dl.x, dl.z / dl.x, dl.w / dl.x);
				ar.add(dr.x / dl.x, dr.y / dl.x, dr.z / dl.x, dr.w / dl.x);
			} else {
				return null; // Non-invertible
			}
		} else if (al.x != 1.0f) {
			ar.div(al.x);
			al.div(al.x);
		}
		
		if (bl.x != 0.0f) {
			br.sub(ar.x * bl.x, ar.y * bl.x, ar.z * bl.x, ar.w * bl.x);
			bl.sub(       bl.x, al.y * bl.x, al.z * bl.x, al.w * bl.x);
		}
		if (cl.x != 0.0f) {
			cr.sub(ar.x * cl.x, ar.y * cl.x, ar.z * cl.x, ar.w * cl.x);
			cl.sub(       cl.x, al.y * cl.x, al.z * cl.x, al.w * cl.x);
		}
		if (dl.x != 0.0f) {
			dr.sub(ar.x * dl.x, ar.y * dl.x, ar.z * dl.x, ar.w * dl.x);
			dl.sub(       dl.x, al.y * dl.x, al.z * dl.x, al.w * dl.x);
		}
		
		// Second row check
		if (LinMath.nearZero(bl.y)) {
			if (!LinMath.nearZero(cl.y)) {
				bl.add(cl.x / cl.y,        1.0f, cl.z / cl.y, cl.w / cl.y);
				br.add(cr.x / cl.y, cr.y / cl.y, cr.z / cl.y, cr.w / cl.y);
			} else if (!LinMath.nearZero(dl.y)) {
				bl.add(dl.x / dl.y,        1.0f, dl.z / dl.y, dl.w / dl.y);
				br.add(dr.x / dl.y, dr.y / dl.y, dr.z / dl.y, dr.w / dl.y);
			} else {
				return null; // Non-invertible
			}
		} else if (bl.y != 1.0f) {
			br.div(bl.y);
			bl.div(bl.y);
		}
		
		if (al.y != 0.0f) {
			ar.sub(br.x * al.y, br.y * al.y, br.z * al.y, br.w * al.y);
			al.sub(       0.0f,        al.y, bl.z * al.y, bl.w * al.y);
		}
		if (cl.y != 0.0f) {
			cr.sub(br.x * cl.y, br.y * cl.y, br.z * cl.y, br.w * cl.y);
			cl.sub(       0.0f,        cl.y, bl.z * cl.y, bl.w * cl.y);
		}
		if (dl.y != 0.0f) {
			dr.sub(br.x * dl.y, br.y * dl.y, br.z * dl.y, br.w * dl.y);
			dl.sub(       0.0f,        dl.y, bl.z * dl.y, bl.w * dl.y);
		}
		
		// Third row check
		if (LinMath.nearZero(cl.z)) {
			if (!LinMath.nearZero(dl.z)) {
				cl.add(dl.x / dl.z, dl.y / dl.z,        1.0f, dl.w / dl.z);
				cr.add(dr.x / dl.z, dr.y / dl.z, dr.z / dl.z, dr.w / dl.z);
			} else {
				return null; // Non-invertible
			}
		} else if (cl.z != 1.0f) {
			cr.div(cl.z);
			cl.div(cl.z);
		}
		
		if (al.z != 0.0f) {
			ar.sub(cr.x * al.z, cr.y * al.z, cr.z * al.z, cr.w * al.z);
			al.sub(       0.0f,        0.0f,        al.z, cl.w * al.z);
		}
		if (bl.z != 0.0f) {
			br.sub(cr.x * bl.z, cr.y * bl.z, cr.z * bl.z, cr.w * bl.z);
			bl.sub(       0.0f,        0.0f,        bl.z, cl.w * bl.z);
		}
		if (dl.z != 0.0f) {
			dr.sub(cr.x * dl.z, cr.y * dl.z, cr.z * dl.z, cr.w * dl.z);
			dl.sub(       0.0f,        0.0f,        dl.z, cl.w * dl.z);
		}
		
		// Fourth row check
		if (LinMath.nearZero(dl.w)) {
			return null; // Non-invertible
		} else if (dl.w != 1.0f) {
			dr.div(dl.w);
			//dl.div(dl.w);
		}
		
		// Subtracting from the left rows
		// is not needed for the last pass.
		if (al.w != 0.0f) {
			ar.sub(dr.x * al.w, dr.y * al.w, dr.z * al.w, dr.w * al.w);
			//al.sub(        0.0,         0.0,         0.0,        al.w);
		}
		if (bl.w != 0) {
			br.sub(dr.x * bl.w, dr.y * bl.w, dr.z * bl.w, dr.w * bl.w);
			//bl.sub(        0.0,         0.0,         0.0,        bl.w);
		}
		if (cl.w != 0) {
			cr.sub(dr.x * cl.w, dr.y * cl.w, dr.z * cl.w, dr.w * cl.w);
			//cl.sub(        0.0,         0.0,         0.0,        cl.w);
		}

		// Return resulting rows in matrix 
		// form.
		return dest.set(ar.x, ar.y, ar.z, ar.w,
		                br.x, br.y, br.z, br.w,
		                cr.x, cr.y, cr.z, cr.w,
		                dr.x, dr.y, dr.z, dr.w);
	}
	
	public Mat4 copy() {
		return copy(new Mat4());
	}
	
	public Mat4 copy(Mat4 dest) {
		return dest.set(m00, m10, m20, m30,
		                m01, m11, m21, m31,
		                m02, m12, m22, m32,
		                m03, m13, m23, m33);
	}

	public Mat4 set(Mat4 other) {
		return set(other.m00, other.m10, other.m20, other.m30,
		           other.m01, other.m11, other.m21, other.m31,
		           other.m02, other.m12, other.m22, other.m32,
		           other.m03, other.m13, other.m23, other.m33);
	}
	
	public Mat4 set(float m00, float m10, float m20, float m30,
	                float m01, float m11, float m21, float m31, 
	                float m02, float m12, float m22, float m32,
	                float m03, float m13, float m23, float m33) {
		
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;

		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	
		return this;
	}

	public void writeBuffer(FloatBuffer buf, boolean rowMajor) {
		if (rowMajor) {
			buf.put(m00).put(m10).put(m20).put(m30)
			   .put(m01).put(m11).put(m21).put(m31)
			   .put(m02).put(m12).put(m22).put(m32)
			   .put(m03).put(m13).put(m23).put(m33);
		} else {
			buf.put(m00).put(m01).put(m02).put(m03)
			   .put(m10).put(m11).put(m12).put(m13)
			   .put(m20).put(m21).put(m22).put(m23)
			   .put(m30).put(m31).put(m32).put(m33);
		}
	}

	public void writeBuffer(ByteBuffer buf, boolean rowMajor) {
		if (rowMajor) {
			buf.putFloat(m00).putFloat(m10).putFloat(m20).putFloat(m30)
			   .putFloat(m01).putFloat(m11).putFloat(m21).putFloat(m31)
			   .putFloat(m02).putFloat(m12).putFloat(m22).putFloat(m32)
			   .putFloat(m03).putFloat(m13).putFloat(m23).putFloat(m33);
		} else {
			buf.putFloat(m00).putFloat(m01).putFloat(m02).putFloat(m03)
			   .putFloat(m10).putFloat(m11).putFloat(m12).putFloat(m13)
			   .putFloat(m20).putFloat(m21).putFloat(m22).putFloat(m23)
			   .putFloat(m30).putFloat(m31).putFloat(m32).putFloat(m33);
		}
	}
}
