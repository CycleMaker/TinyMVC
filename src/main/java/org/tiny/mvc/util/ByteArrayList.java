package org.tiny.mvc.util;

import java.util.ArrayList;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-28 13 :32
 * @description
 */
public class ByteArrayList {

    private static final Integer CHUNK_SIZE = 50;

    private InnerItem current;

    private ArrayList<InnerItem> data;

    public ByteArrayList() {
        current = new InnerItem();
        data = new ArrayList<>();
    }

    public byte[] get() {
        int total = data.size() * CHUNK_SIZE + current.getSize();
        byte[] res = new byte[total];
        for (int i = 0; i < data.size(); i++) {
            System.arraycopy(data.get(i).get(), 0, res, i * CHUNK_SIZE, CHUNK_SIZE);
        }
        System.arraycopy(current.get(), 0, res, data.size() * CHUNK_SIZE, current.size);
        return res;
    }

    public void add(Byte b) {
        current.add(b);
        if (current.size == CHUNK_SIZE) {
            data.add(current);
            current = new InnerItem();
        }
    }

    class InnerItem {
        byte[] arr;
        int size;

        public InnerItem() {
            arr = new byte[CHUNK_SIZE];
            size = 0;
        }

        public int getSize() {
            return size;
        }

        public void add(byte b) {
            arr[size] = b;
            size = size + 1;
        }

        public byte[] get() {
            return arr;
        }
    }
}
