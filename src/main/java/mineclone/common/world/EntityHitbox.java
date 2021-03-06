package mineclone.common.world;

public class EntityHitbox {

	public float x0;
	public float y0;
	public float z0;
	
	public float x1;
	public float y1;
	public float z1;

	public EntityHitbox() {
		x0 = y0 = z0 = 0.0f;
		x1 = y1 = z1 = 0.0f;
	}
	
	public EntityHitbox(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
	}
	
	public void move(float xm, float ym, float zm) {
		x0 += xm;
		y0 += ym;
		z0 += zm;
		
		x1 += xm;
		y1 += ym;
		z1 += zm;
	}
	
	public EntityHitbox expand(float xe, float ye, float ze) {
		float _x0 = x0;
		float _y0 = y0;
		float _z0 = z0;
		
		float _x1 = x1;
		float _y1 = y1;
		float _z1 = z1;

		if (xe > 0.0f) {
			_x1 += xe;
		} else {
			_x0 += xe;
		}
		
		if (ye > 0.0f) {
			_y1 += ye;
		} else {
			_y0 += ye;
		}

		if (ze > 0.0f) {
			_z1 += ze;
		} else {
			_z0 += ze;
		}
		
		return new EntityHitbox(_x0, _y0, _z0, _x1, _y1, _z1);
	}
	
	public boolean collides(EntityHitbox other) {
		if (x0 >= other.x1 || x1 <= other.x0)
			return false;
		if (y0 >= other.y1 || y1 <= other.y0)
			return false;
		if (z0 >= other.z1 || z1 <= other.z0)
			return false;
		
		return true;
	}
	
	public float clipX(EntityHitbox other, float xm) {
		if (y0 >= other.y1 || y1 <= other.y0)
			return xm;
		if (z0 >= other.z1 || z1 <= other.z0)
			return xm;
		
		if (xm < 0.0f && x1 <= other.x0) {
			float max = x1 - other.x0;
			if (max > xm) return max;
		} else if (xm > 0.0f && x0 >= other.x1) {
			float max = x0 - other.x1;
			if (max < xm) return max;
		}
		
		return xm;
	}
	
	public float clipY(EntityHitbox other, float ym) {
		if (x0 >= other.x1 || x1 <= other.x0)
			return ym;
		if (z0 >= other.z1 || z1 <= other.z0)
			return ym;
		
		if (ym < 0.0f && y1 <= other.y0) {
			float max = y1 - other.y0;
			
			if (max > ym)
				return max;
		} else if (ym > 0.0f && y0 >= other.y1) {
			float max = y0 - other.y1;
			
			if (max < ym)
				return max;
		}
		
		return ym;
	}

	public float clipZ(EntityHitbox other, float zm) {
		if (x0 >= other.x1 || x1 <= other.x0)
			return zm;
		if (y0 >= other.y1 || y1 <= other.y0)
			return zm;
		
		if (zm < 0.0f && z1 <= other.z0) {
			float max = z1 - other.z0;
			
			if (max > zm)
				return max;
		} else if (zm > 0.0f && z0 >= other.z1) {
			float max = z0 - other.z1;
			
			if (max < zm)
				return max;
		}
		
		return zm;
	}
	
	public float getCenterX() {
		return (x0 + x1) * 0.5f;
	}

	public float getCenterY() {
		return (y0 + y1) * 0.5f;
	}

	public float getCenterZ() {
		return (z0 + z1) * 0.5f;
	}
}
