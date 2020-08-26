package mineclone.common.world.gen;

import java.util.Arrays;
import java.util.Random;

import mineclone.common.math.LinMath;

public class DiamondNoise {

	private final int size;
	private final int mask;
	
	private float[] noise;
	
	public DiamondNoise(int size, Random random) {
		this.size = size;
		this.mask = size - 1;
		
		if (size <= 0 || (size & mask) != 0)
			throw new IllegalArgumentException("Size is not a positive power of two!");
		
		noise = new float[size * size];
		
		noise[0] = random.nextFloat();
		
		int step = size;
		while (step != 1) {
			float mag = (float)(step - 2) / size * 0.5f;

			int halfStep = step >>> 1;
			for (int y = halfStep; y < size; y += step) {
				// a  *  b
				// * -E- *
				// c  *  d
				float a = getNoise(0, y - halfStep);
				float c = getNoise(0, y + halfStep);

				for (int x = halfStep; x < size; x += step) {
					float b = getNoise(x + halfStep, y - halfStep);
					float d = getNoise(x + halfStep, y + halfStep);
					
					float r = (random.nextFloat() - 0.5f) * mag;
					setNoise(x, y, (a + b + c + d) * 0.25f + r);

					a = b;
					c = d;
				}
			}

			for (int y = halfStep; y < size; y += step) {
				float a = getNoise(0, y - halfStep);
				float e = getNoise(-halfStep, y);

				for (int x = halfStep; x < size; x += step) {
					// *  *  f  *
					// *  a -h- b
					// e -g- D  *
					// *  c  *  *
					float b = getNoise(x + halfStep, y - halfStep);
					float c = getNoise(x - halfStep, y + halfStep);
					float d = getNoise(x, y);
					float f = getNoise(x, y - step);
					
					float r1 = (random.nextFloat() - 0.5f) * mag;
					float r2 = (random.nextFloat() - 0.5f) * mag;
					setNoise(x - halfStep, y, (a + c + d + e) * 0.25f + r1);
					setNoise(x, y - halfStep, (a + b + d + f) * 0.25f + r2);
				
					a = b;
					e = d;
				}
			}
			
			step = halfStep;
		}
		
		normalizeNoise();
	}

	private int getIndex(int x, int y) {
		return (x & mask) + (y & mask) * size;
	}

	private void normalizeNoise() {
		float mn = Float.POSITIVE_INFINITY;
		float mx = Float.NEGATIVE_INFINITY;
		for (float n : noise) {
			if (n < mn)
				mn = n;
			if (n > mx)
				mx = n;
		}
		
		float d = mx - mn;
		if (LinMath.nearZero(d)) {
			Arrays.fill(noise, 0.0f);
		} else {
			for (int i = 0; i < noise.length; i++)
				noise[i] = (noise[i] - mn) / d;
		}
	}

	private void setNoise(int x, int y, float noise) {
		this.noise[getIndex(x, y)] = noise;
	}
	
	public float getNoise(int x, int y) {
		return noise[getIndex(x, y)];
	}
}
