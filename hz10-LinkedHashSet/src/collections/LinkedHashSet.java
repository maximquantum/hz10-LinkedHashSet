package collections;

public class LinkedHashSet implements Set {
	
	private class Node {
		/**
		 * @invar | previous != null
		 * @invar | next != null
		 * @invar | previous.next == this
		 * @invar | next.previous == this
		 * @invar | (value == null) == (this == sentinel)
		 * 
		 * @peerObject
		 */
		private Node previous;
		private Object value;
		/**
		 * @peerObject
		 */
		private Node next;
		
		private int getSize() {
			return this == sentinel ? 0 : 1 + next.getSize();
		}
		
		private void addNodes(Set set) {
			if (this != sentinel) {
				set.add(this);
				next.addNodes(set);
			}
		}
	}
	
	private Set getNodes() {
		HashSet result = new HashSet(size);
		sentinel.next.addNodes(result);
		return result;
	}

	/**
	 * @invar | sentinel != null
	 * @invar | size == sentinel.next.getSize()
	 * @invar | nodesMap != null
	 * @invar For each entry in nodesMap, the entry's value is a node in the linked list.
	 *      | nodesMap.entrySet().stream().map((Object e) -> ((Map.Entry)e).getValue()).allMatch(e -> getNodes().contains(e))
	 * @invar For each node in the linked list, there exists an entry in nodesMap whose value equals that node. 
	 *      | getNodes().stream().allMatch(e -> nodesMap.entrySet().stream().anyMatch(e1 -> ((Map.Entry)e1).getValue().equals(e)))
	 * @invar For each entry in nodesMap, its value's value equals its key.
	 *      | nodesMap.entrySet().stream().allMatch(e -> ((Node)((Map.Entry)e).getValue()).value == ((Map.Entry)e).getKey())
	 * @representationObject
	 */
	private Node sentinel;
	private int size;
	/**
	 * @representationObject
	 */
	private HashMap nodesMap;
	
	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		Node n = sentinel.next;
		for (int i = 0; i < size; i++) {
			result[i] = n.value;
			n = n.next;
		}
		return result;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	private Node getNode(int index) {
		if (index < size / 2) {
			Node n = sentinel.next;
			while (index > 0) {
				n = n.next;
				index--;
			}
			return n;
		} else {
			Node n = sentinel;
			while (index < size) {
				n = n.previous;
				index++;
			}
			return n;
		}
	}
	
	@Override
	public boolean contains(Object object) {
		return nodesMap.containsKey(object);
	}

	/**
	 * @post | size() == 0
	 */
	public LinkedHashSet() {
		sentinel = new Node();
		sentinel.next = sentinel;
		sentinel.previous = sentinel;
		nodesMap = new HashMap(100);
	}
	
	@Override
	public void add(Object object) {
		if (!contains(object)) {
			Node n = new Node();
			n.next = sentinel;
			n.previous = sentinel.previous;
			n.value = object;
			n.next.previous = n;
			n.previous.next = n;
			size++;
			nodesMap.put(object, n);
		}
	}
	
	@Override
	public void remove(Object object) {
		if (contains(object)) {
			Node n = (Node)nodesMap.get(object);
			n.next.previous = n.previous;
			n.previous.next = n.next;
			size--;
			nodesMap.remove(object);
		}
	}
}