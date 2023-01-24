package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float LOAD_FACTOR = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;
    private int size;
    private Node<K,V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getIndex(key);
        if (size > threshold) {
            resize();
        }
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(key, value, null);
        } else {
            for (Node<K, V> node = table[bucketIndex]; node != null; node = node.next) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    break;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getIndex(key);
        Node<K,V> node;
        node = table[bucketIndex];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return getHash(key) % table.length;
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int oldThreshold = threshold;
        int newCap = oldCapacity * MAGNIFICATION_FACTOR;
        threshold = oldThreshold * MAGNIFICATION_FACTOR;
        size = 0;
        table = (Node<K,V>[]) new Node[newCap];
        for (Node<K,V> transferingNode : oldTable) {
            while (transferingNode != null) {
                put(transferingNode.key,transferingNode.value);
                transferingNode = transferingNode.next;
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
