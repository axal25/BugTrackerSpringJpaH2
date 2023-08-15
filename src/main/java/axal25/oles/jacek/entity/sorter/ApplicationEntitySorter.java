package axal25.oles.jacek.entity.sorter;

import axal25.oles.jacek.entity.ApplicationEntity;

import java.util.Comparator;
import java.util.stream.Stream;

public class ApplicationEntitySorter {
    public static Stream<ApplicationEntity> sorted(Comparator<? super ApplicationEntity> comparator, Stream<ApplicationEntity> stream) {
        return stream.sorted(comparator);
    }
}
