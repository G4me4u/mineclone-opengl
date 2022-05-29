package mineclone.client.input;

public interface IKeyboardListener {

	public void keyPressed(int key, int mods);

	public void keyRepeated(int key, int mods);

	public void keyReleased(int key, int mods);
	
	public void keyTyped(int codePoint);
	
}
