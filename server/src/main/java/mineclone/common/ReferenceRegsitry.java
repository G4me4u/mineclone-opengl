package mineclone.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class ReferenceRegsitry<K, V> {

	private final Map<K, V> idToElement;
	private final Map<V, K> elementToId;
	
	public ReferenceRegsitry() {
		idToElement = new HashMap<>();
		elementToId = new IdentityHashMap<>();
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
	
	public int getSize() {
		return idToElement.size();
	}
	
	public Set<V> elements() {
		return Collections.unmodifiableSet(elementToId.keySet());
	}

	public Set<K> keys() {
		return Collections.unmodifiableSet(idToElement.keySet());
	}
}
