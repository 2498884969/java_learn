package com.test;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

import java.nio.charset.Charset;

public class BloomTest {

    @Test
    public void test1() {
        // 1. 布隆过滤器判定存在的可能存在，不存在的一定不存在
        // 2. 布隆过滤器需要进行数据的预加载
        BloomFilter bf  = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 10000,
                0.0001);

        // 1. 放入100000个基础数据
        for (int i=0; i< 10000; i++) {
            bf.put(String.valueOf(i));
        }

        int counts = 0;
        for (int i=0; i<1000; i++){
            boolean isExist = bf.mightContain("imooc" + i);
            if (isExist) {
                counts ++ ;
            }
        }
        System.err.println(counts);
    }

}
