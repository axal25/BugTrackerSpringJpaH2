package axal25.oles.jacek.entity;

public interface ComparableFullyFetchedEntity<T> {

    /**
     * Deep copy, making sure that all sub-entities are fetched beforehand
     */
    public T toComparableFullyFetchedEntity();
}
