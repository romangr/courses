package taghandlers;

import DaoAndModel.Model;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class JSPSetBean<T extends Model> implements Iterable<T>{
    private Iterator<T> it;
    private Set<T> set;
    private T currentElement;

    public JSPSetBean(Set<T> set){
        this.set = set;
    }

    public JSPSetBean(Collection<T> collection) {
        this(new HashSet<>());
        for (T entity : collection) {
            set.add(entity);
        }
    }

    public int getSize(){
        return set.size();
    }

    public Iterator<T> getIterator() {
        it = set.iterator();
        return it;
    }

    @Deprecated
    public T getElement() {
        if (it == null || !it.hasNext())
            it = set.iterator();

        return currentElement = it.next();
    }

    @Deprecated
    public int getElementId() {
        return currentElement.getId();
    }

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
