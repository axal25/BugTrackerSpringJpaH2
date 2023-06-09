package axal25.oles.jacek.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class LocalDateUtils {

    public static LocalDate produceRandomPastSinceEpoch() {
        return produceRandom(LocalDate.EPOCH, LocalDate.now());
    }

    public static LocalDate produceRandom(LocalDate min, LocalDate max) {
        if (min == null) {
            min = LocalDate.MIN;
        }
        if (max == null) {
            max = LocalDate.MAX;
        }
        return LocalDate.ofInstant(
                InstantUtils.produceRandom(
                        min.atStartOfDay().toInstant(ZoneOffset.ofHours(0)),
                        max.atStartOfDay().toInstant(ZoneOffset.ofHours(0))),
                ZoneId.systemDefault()
        );
    }
}
