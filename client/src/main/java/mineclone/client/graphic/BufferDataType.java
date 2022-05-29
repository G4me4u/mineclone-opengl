package mineclone.client.graphic;

public enum BufferDataType {

	FLOAT(4),
	
	BYTE(1),
	UBYTE(1),

	SHORT(2),
	USHORT(2),

	INT(4),
	UINT(4),
	
	BOOL(1);
	
	private final int byteSize;
	
	private BufferDataType(int byteSize) {
		this.byteSize = byteSize;
	}
	
	public int getByteSize() {
		return byteSize;
	}
}
