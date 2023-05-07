package axal25.oles.jacek.util;

import java.math.BigInteger;
import java.util.Random;

public class BigIntegerUtils {

    private static final Random random = new Random();

    public static BigInteger getRandomSeconds(long min, long max) {
        return getRandomSeconds(
                BigInteger.valueOf(min),
                BigInteger.valueOf(max));
    }


    public static BigInteger getRandomSeconds(BigInteger min, BigInteger max) {
        BigInteger range = max.subtract(min);

        BigInteger randomOffset =
                BigInteger.ZERO.equals(range)
                        ? BigInteger.ZERO
                        : BigInteger.valueOf(
                                Math.abs(
                                        random.nextLong()))
                        .mod(range);

        return min.add(randomOffset);
    }
}
