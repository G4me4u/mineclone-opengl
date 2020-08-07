package minecraft.common;

import java.util.HashMap;
import java.util.Map;

public class ReferenceRegsitry<K, V> {

	private final Map<K, V> idToElement;
	private final Map<V, K> elementToId;
	
	public ReferenceRegsitry() {
		idToElement = new HashMap<>();
		elementToId = new HashMap<>();
	}
	
	public void register(K id, V element) {
		if (idToElement.containsKey(id))
			throw new DuplicateRegisterException("Identifier is already registered: " + id);
		if (elementToId.containsKey(element))
			throw new DuplicateRegisterException("Element class is already registered: " + element);
		
		idToElement.put(id, element);
		elementToId.put(element, id);
	}
	
	public boolean containsIdentifier(K id) {
		return idToElement.containsKey(id);
	}
	
	public boolean containsElement(V element) {
		return elementToId.containsKey(element);
	}
	
	public K getIdentifier(V element) {
		return elementToId.get(element);
	}

	public V getElement(K id) {
		return idToElement.get(id);
	}
}
