package DaoAndModel.connectionPool;

@FunctionalInterface
public interface Proxy<T> {
    T toSrc();
}
