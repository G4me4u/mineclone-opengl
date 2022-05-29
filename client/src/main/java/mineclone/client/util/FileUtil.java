package mineclone.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

public class FileUtil {

	private static final int BATCH_BUFFER_SIZE = 1024;
	
	public static ByteBuffer readAllBytes(InputStream is) throws IOException {
		if (is == null)
			throw new IllegalArgumentException("Input stream is null!");
		
		List<byte[]> batch = new ArrayList<>();

		byte[] data = new byte[BATCH_BUFFER_SIZE];
		int p = 0;
		
		while (true) {
			int br = is.read(data, p, data.length - p);
			if (br == -1)
				break;
			
			p += br;
		
			if (p >= data.length) {
				batch.add(data);
				
				data = new byte[BATCH_BUFFER_SIZE];
				p = 0;
			}
		}
		
		int tbr = batch.size() * BATCH_BUFFER_SIZE + p;
		ByteBuffer buffer = BufferUtils.createByteBuffer(tbr);
		
		for (byte[] buff : batch)
			buffer.put(buff);
		buffer.put(data, 0, p);
		buffer.flip();
		
		return buffer;
	}
}
