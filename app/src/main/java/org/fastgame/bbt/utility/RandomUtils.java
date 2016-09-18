package org.fastgame.bbt.utility;

import java.util.Random;

/**
 * 处理一些随机事件
 *
 * @Author: MrLi
 * @Since: 2016/9/18
 */
public class RandomUtils {

    public static int getRandomNumber(int n) {
        return new Random(System.currentTimeMillis() + new Random().nextInt(200)).nextInt(n);
    }

    public static boolean isBingo(int target, int pool) {
        return target > getRandomNumber(pool);
    }

    public static void main(String[] args) {

        int times = 100000000;
        int bingoTime = 0;
        for (int i = 0; i < times; i++) {
           if (isBingo(250, 1000)) {
               bingoTime++;
           }
        }

        print("result:" + bingoTime + " / " + times + "," + bingoTime * 1.0 / times);
    }

    public static void print(String n) {
        System.out.println(n);
    }

}
