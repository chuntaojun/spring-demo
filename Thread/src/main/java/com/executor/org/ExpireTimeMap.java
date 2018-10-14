package com.executor.org;


import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;

import java.util.Objects;

/**
 * @author liaochuntao
 */
public class ExpireTimeMap<K, V, T extends Long> {

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * The smallest table capacity for which bins may be treeified.
     * (Otherwise the table is resized if too many nodes in a bin.)
     * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
     * between resizing and treeification thresholds.
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     * The number of key-value mappings contained in this map.
     */
    transient int size;

    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     */
    transient int modCount;

    /**
     * The next size value at which to resize (capacity * load factor).
     *
     * @serial
     */
    // (The javadoc description is true upon serialization.
    // Additionally, if the table array has not been allocated, this
    // field holds the initial array capacity, or zero signifying
    // DEFAULT_INITIAL_CAPACITY.)
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    final float loadFactor;

    public ExpireTimeMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    static class Node<K, V, T> {
        final int hash;
        final K key;
        V value;
        T time;
        Node<K, V, T> next;

        Node(int hash, K key, V value, T time, Node<K, V, T> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.time = time;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final T getTime() {
            return time;
        }

        @Override
        public final String toString() {
            return key + "=" + value;
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value) ^ Objects.hashCode(time);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Node) {
                Node<?, ?, ?> e = (Node<?, ?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()) && Objects.equals(time, e.getTime()))
                    return true;
            }
            return false;
        }
    }

    ExpireTimeMap.Node<K, V, T> newNode(int hash, K key, V value, T time, ExpireTimeMap.Node<K, V, T> next) {
        return new ExpireTimeMap.Node<>(hash, key, value, time, next);
    }

    static class Entry<K, V, T> extends Node {
        Entry(int hash, Object key, Object value, Object time, Node next) {
            super(hash, key, value, time, next);
        }
    }

    static class Item<V, T> {
        V value;
        T time;

        public Item(V value, T time) {
            this.value = value;
            this.time = time;
        }


    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static int indexFor(int h, int length) {
        return h & (length - 1);
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    transient ExpireTimeMap.Node<K, V, T>[] table;

    public V put(K key, V value) {
        return putVal(hash(key), key, value, (T) Long.valueOf(System.currentTimeMillis() + 6 * 60 * 1000), false, true);
    }

    public V put(K key, V value, String time) {
        if (time instanceof String) {
            return putVal(hash(key), key, value, setExpireTime(time), false, true);
        }
        throw new ClassFormatException("The time must be java.lang.String");
    }

    public Item get(Object key) {
        ExpireTimeMap.Node<K,V,T> e;
        return (e = getNode(hash(key), key)) == null ? null : new Item(e.value, e.time);
    }

    public Item remove(Object key) {
        ExpireTimeMap.Node<K,V, T> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : new Item(e.value, e.time);
    }

    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    final V putVal(int hash, K key, V value, T time, boolean onlyIfAbsent,
                   boolean evict) {
        ExpireTimeMap.Node<K, V, T>[] tab;
        ExpireTimeMap.Node<K, V, T> p;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, time, null);
        else {
            ExpireTimeMap.Node<K, V, T> e = null;
            K k;
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, time, null);
                    break;
                }
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        return null;
    }

    final ExpireTimeMap.Node<K, V, T> getNode(int hash, Object key) {
        ExpireTimeMap.Node<K, V, T>[] tab;
        ExpireTimeMap.Node<K, V, T> first, e;
        int n;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    final ExpireTimeMap.Node<K, V, T> removeNode(int hash, Object key, Object value,
                                                 boolean matchValue, boolean movable) {
        ExpireTimeMap.Node<K, V, T>[] tab;
        ExpireTimeMap.Node<K, V, T> p;
        int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
            ExpireTimeMap.Node<K, V, T> node = null, e;
            K k;
            V v;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key ||
                                    (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
            if (node != null && (!matchValue || (v = node.value) == value ||
                    (value != null && value.equals(v)))) {
                if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                return node;
            }
        }
        return null;
    }


    final ExpireTimeMap.Node<K, V, T>[] resize() {
        ExpireTimeMap.Node<K, V, T>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        ExpireTimeMap.Node<K, V, T>[] newTab = (ExpireTimeMap.Node<K, V, T>[]) new ExpireTimeMap.Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                ExpireTimeMap.Node<K, V, T> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else { // preserve order
                        ExpireTimeMap.Node<K, V, T> loHead = null, loTail = null;
                        ExpireTimeMap.Node<K, V, T> hiHead = null, hiTail = null;
                        ExpireTimeMap.Node<K, V, T> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    final T setExpireTime(String strTime) {
        char timeType = strTime.charAt(strTime.length() - 1);
        Long timeNumer = Long.valueOf(strTime.substring(0, strTime.length() - 2));
        if (timeType == 's' || timeType == 'S') {
            return (T) new Long(System.currentTimeMillis() + timeNumer * 1000);
        } else if (timeType == 'm' || timeType == 'M') {
            return (T) new Long(System.currentTimeMillis() + timeNumer * 60 * 1000);
        } else if (timeType == 'h' || timeType == 'H') {
            return (T) new Long(System.currentTimeMillis() * timeNumer * 60 * 60 * 1000);
        }
        throw new RuntimeException("当前仅支持秒(s|S)、分(m|M)、时(h|H)");
    }
}
