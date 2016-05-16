package dao_and_model.connection_pool;

@FunctionalInterface
public interface Proxy<T> {
    T toSrc();
}
