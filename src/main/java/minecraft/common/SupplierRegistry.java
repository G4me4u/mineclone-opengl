package minecraft.common;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SupplierRegistry<K, V> {

	private final Map<K, Supplier<? extends V>> idToSupplier;
	private final Map<Class<? extends V>, K> elementToId;
	
	public SupplierRegistry() {
		idToSupplier = new HashMap<>();
		elementToId = new IdentityHashMap<>();
	}
	
	public <T extends V> void register(K id, Class<T> elementClazz, Supplier<T> supplier) {
		if (idToSupplier.containsKey(id))
			throw new DuplicateRegisterException("Identifier is already registered: " + id);
		if (elementToId.containsKey(elementClazz))
			throw new DuplicateRegisterException("Element class is already registered: " + elementClazz);
		
		idToSupplier.put(id, supplier);
		elementToId.put(elementClazz, id);
	}

	public boolean containsIdentifier(K id) {
		return idToSupplier.containsKey(id);
	}

	public <E extends V> boolean containsElement(E element) {
		return elementToId.containsKey(element.getClass());
	}

	public <E extends V> K getIdentifier(E element) {
		return elementToId.get(element.getClass());
	}

	public Supplier<? extends V> getSupplier(K id) {
		return idToSupplier.get(id);
	}
	
	public V createNewElement(K id) {
		Supplier<? extends V> provider = getSupplier(id);
		return provider == null ? null : provider.get();
	}
}
