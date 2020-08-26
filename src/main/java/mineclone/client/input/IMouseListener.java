package mineclone.client.input;

public interface IMouseListener {

	public void mouseMoved(float mouseX, float mouseY);
	
	public void mouseDragged(int button, float mouseX, float mouseY, float dragX, float dragY);

	public void mousePressed(int button, float mouseX, float mouseY, int modifiers);
	
	public void mouseReleased(int button, float mouseX, float mouseY, int modifiers);
	
	public void mouseScroll(float mouseX, float mouseY, float scrollX, float scrollY);
	
}
