package com.mosy.kalin.mosy.Helpers;

public class ArrayHelper {

    public static boolean hasValidBitmapContent(byte[] array)
    {
        return array != null && array.length > 0 && !containsOnlyZeros(array);
    }

    public static boolean containsOnlyZeros(final byte[] array)
    {
        int hits = 0;
        for (byte b : array) {
            if (b != 0) {
                hits++;
            }
        }
        return (hits == 0);

// another but slower performance solution
//        LongBuffer longBuffer = ByteBuffer.wrap(array).asLongBuffer();
//        while (longBuffer.hasRemaining()) {
//            if (longBuffer.get() != 0) {
//                return false;
//            }
//        }
//        return true;
    }

}