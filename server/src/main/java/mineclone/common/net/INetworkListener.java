package mineclone.common.net;

public interface INetworkListener {

	public void connectionAdded(INetworkConnection connection);

	public void connectionRemoved(INetworkConnection connection);
	
}
