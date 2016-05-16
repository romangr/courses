package taghandlers;

import dao_and_model.Model;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
* Collection of {@link dao_and_model.Model} implementations.
*/
public class JSPSetBean<T extends Model> implements Iterable<T> {
    private Iterator<T> it;
    private Set<T> set;
    private T currentElement;

    public JSPSetBean(Set<T> set) {
        this.set = set;
    }

    public JSPSetBean(Collection<T> collection) {
        this(new HashSet<>());
        for (T entity : collection) {
            set.add(entity);
        }
    }

    public int getSize() {
        return set.size();
    }

    /**
    * Deprecated: use foreach.
    */
    @Deprecated
    public Iterator<T> getIterator() {
        it = set.iterator();
        return it;
    }

    /**
    * @return next element in set cyclically if it exists, null if set is empty.
    * Deprecated: using {@code elements()} is more properly.
    */
    @Deprecated
    public T getElement() {
        if (set.size() == 0) return null;
        if (it == null || !it.hasNext())
            it = set.iterator();

        return currentElement = it.next();
    }

    /**
    * @return id of last element have been got by {@code getElement()}
    * Deprecated: using {@code elements()} is more properly.
    */
    @Deprecated
    public int getElementId() {
        return (currentElement != null)
                ? currentElement.getId()
                : 0;
    }

    /**
    * @return stream of set's elements
    */
    public Stream<T> elements() {
        return set.stream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        set.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return set.spliterator();
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }
}
