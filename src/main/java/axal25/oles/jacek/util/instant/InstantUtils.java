package axal25.oles.jacek.util.instant;

import axal25.oles.jacek.util.biginteger.BigIntegerUtils;

import java.time.Instant;
import java.util.Random;

public class InstantUtils {
    private static final Random random = new Random();

    public static Instant produceRandom(Instant min, Instant max) {
        if (min == null) {
            min = Instant.MIN;
        }
        if (max == null) {
            max = Instant.MAX;
        }

        return Instant.ofEpochSecond(
                getRandomSeconds(min, max),
                getRandomNanos(min, max));
    }

    private static long getRandomSeconds(Instant min, Instant max) {
        return BigIntegerUtils.getRandomSeconds(
                        min.getEpochSecond(),
                        max.getEpochSecond())
                .longValueExact();
    }

    private static long getRandomNanos(Instant min, Instant max) {
        return BigIntegerUtils.getRandomSeconds(
                        min.getNano(),
                        max.getNano())
                .longValueExact();
    }
}
