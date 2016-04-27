package javase10.t02.cp;

@FunctionalInterface
public interface Proxy<T> {
    T toSrc();
}
