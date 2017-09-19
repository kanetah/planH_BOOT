package top.kanetah.planH.tools;

import java.util.*;

public final class TreeMultiValueMap<K, V> implements MultiValueMap<K, V> {
    private TreeMap<K, List<V>> mSource = new TreeMap<>();

    @Override
    public void add(K key, V value) {
        if (key != null) {
            if (!mSource.containsKey(key))
                mSource.put(key, new ArrayList<>(2));
            mSource.get(key).add(value);
        }
    }

    @Override
    public void add(K key, List<V> values) {
        for (V value : values) {
            add(key, value);
        }
    }

    @Override
    public void set(K key, V value) {
        mSource.remove(key);
        add(key, value);
    }

    @Override
    public void set(K key, List<V> values) {
        mSource.remove(key);
        add(key, values);
    }

    @Override
    public void set(Map<K, List<V>> map) {
        mSource.clear();
        mSource.putAll(map);
    }

    @Override
    public List<V> remove(K key) {
        return mSource.remove(key);
    }

    @Override
    public void clear() {
        mSource.clear();
    }

    @Override
    public Set<K> keySet() {
        return mSource.keySet();
    }

    @Override
    public List<V> values() {
        List<V> allValues;
        allValues = new ArrayList<>();
        Set<K> keySet = mSource.keySet();
        for (K key : keySet) {
            allValues.addAll(mSource.get(key));
        }
        return allValues;
    }

    @Override
    public List<V> getValues(K key) {
        return mSource.get(key);
    }

    @Override
    public V getValue(K key, int index) {
        List<V> values = mSource.get(key);
        if (values != null && index < values.size())
            return values.get(index);
        return null;
    }

    @Override
    public int size() {
        return mSource.size();
    }

    @Override
    public boolean isEmpty() {
        return mSource.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return mSource.containsKey(key);
    }

    public K firstKey() {
        return mSource.firstKey();
    }

    public void removeValue(V value) {
        mSource.forEach((k, vs) -> vs.remove(value));
    }
}
