package minecraft.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SupplierRegistry<K, V> {

	private final Map<Class<? extends V>, K> elementToId;
	private final Map<K, Supplier<? extends V>> idToSupplier;
	
	public SupplierRegistry() {
		elementToId = new HashMap<Class<? extends V>, K>();
		idToSupplier = new HashMap<K, Supplier<? extends V>>();
	}
	
	public <T extends V> void register(K id, Class<T> elementClazz, Supplier<T> supplier) {
		if (idToSupplier.containsKey(id))
			throw new DuplicateRegisterException("Identifier is already registered: " + id);
		if (elementToId.containsKey(elementClazz))
			throw new DuplicateRegisterException("Element class is already registered: " + elementClazz);
		
		elementToId.put(elementClazz, id);
		idToSupplier.put(id, supplier);
	}

	public boolean containsIdentifier(K id) {
		return idToSupplier.containsKey(id);
	}

	public boolean containsElement(Class<? extends V> elementClazz) {
		return elementToId.containsKey(elementClazz);
	}

	public K getIdentifier(Class<? extends V> elementClazz) {
		return elementToId.get(elementClazz);
	}

	public Supplier<? extends V> getSupplier(K id) {
		return idToSupplier.get(id);
	}
	
	public V createNewElement(K id) {
		Supplier<? extends V> provider = getSupplier(id);
		return provider == null ? null : provider.get();
	}
}
