package minecraft.client.graphic;

public enum BufferAttribType {
	
	FLOAT (BufferDataType.FLOAT, 1, 1),
	FLOAT2(BufferDataType.FLOAT, 2, 1),
	FLOAT3(BufferDataType.FLOAT, 3, 1),
	FLOAT4(BufferDataType.FLOAT, 4, 1),
	
	MAT3(BufferDataType.FLOAT, 3, 3),
	MAT4(BufferDataType.FLOAT, 4, 4),
	
	INT (BufferDataType.INT, 1, 1),
	INT2(BufferDataType.INT, 2, 1),
	INT3(BufferDataType.INT, 3, 1),
	INT4(BufferDataType.INT, 4, 1),

	UINT (BufferDataType.UINT, 1, 1),
	UINT2(BufferDataType.UINT, 2, 1),
	UINT3(BufferDataType.UINT, 3, 1),
	UINT4(BufferDataType.UINT, 4, 1),
	
	BOOL(BufferDataType.BOOL, 1, 1);

	private final BufferDataType internalDataType;
	private final int locationCount;
	private final int componentCount;
	
	private final int primitiveCount;
	
	private BufferAttribType(BufferDataType internalDataType, int componentCount, int locationCount) {
		this.internalDataType = internalDataType;
		this.componentCount = componentCount;
		this.locationCount = locationCount;
		
		primitiveCount = componentCount * locationCount;
	}
	
	public BufferDataType getInternalType() {
		return internalDataType;
	}

	public int getComponentCount() {
		return componentCount;
	}
	
	public int getLocationCount() {
		return locationCount;
	}
	
	public int getPrimitiveCount() {
		return primitiveCount;
	}
}
