package com.xkcoding.bf.helper;

/**
 * @author xugangq
 * @date 2021/6/17 20:12
 */

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

public class BloomFilterHelper<T> {

    private int numHashFunctions;

    private int bitSize;

    private Funnel<T> funnel;

    /**
     * 根据"预计要插入多少数据"和”期望的误判率“,计算出bit数组长度和哈希葛素
     *
     * @param funnel
     * @param expectedInsertions 预计要插入多少数据
     * @param fpp                期望的误判率
     */
    public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel不能为空");
        this.funnel = funnel;
        // 计算bit数组长度
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        // 哈希函数的个数K
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    /**
     * 基于murmur3_128哈希算法，计算出索引位置
     *
     * @param value
     * @return
     */
    public int[] murmurHashOffset(T value) {
        //offset[a]=b表示，表示第a个哈希函数，计算出的索引位置为b
        int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int)hash64;
        int hash2 = (int)(hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }

        return offset;
    }

    /**
     * 计算bit数组长度
     *
     * @param expectedInsertions 预计要插入多少数据
     * @param fpp                期望的误判率
     * @return 位数组大小
     */
    private int optimalNumOfBits(long expectedInsertions, double fpp) {
        if (fpp == 0) {
            // 设定最小期望长度
            fpp = Double.MIN_VALUE;
        }
        int sizeOfBitArray = (int)(-expectedInsertions * Math.log(fpp) / (Math.log(2) * Math.log(2)));
        return sizeOfBitArray;
    }

    /**
     * 计算出哈希函数的个数K
     *
     * @param expectedInsertions 预计要插入多少数据
     * @param bitSize            位数组大小
     * @return 哈希函数的个数
     */
    private int optimalNumOfHashFunctions(long expectedInsertions, long bitSize) {
        int countOfHash = Math.max(1, (int)Math.round((double)bitSize / expectedInsertions * Math.log(2)));
        return countOfHash;
    }
}
