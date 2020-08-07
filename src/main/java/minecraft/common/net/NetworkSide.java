package minecraft.common.net;

public enum NetworkSide {

	CLIENT, SERVER;
	
	public NetworkSide getOpposite() {
		return (this == CLIENT) ? SERVER : CLIENT;
	}
}
