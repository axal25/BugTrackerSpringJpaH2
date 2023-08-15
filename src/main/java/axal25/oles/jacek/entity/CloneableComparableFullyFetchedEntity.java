package axal25.oles.jacek.entity;

public interface CloneableComparableFullyFetchedEntity<T> extends Cloneable, Comparable<T> {

    /**
     * Deep copy, making sure that all sub-entities are fetched beforehand
     */
    T toClonedComparableFullyFetchedEntity();
}
