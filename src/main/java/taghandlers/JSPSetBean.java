package taghandlers;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public class JSPSetBean<T> {
    private Iterator<T> it;
    private Set<T> set;

    public JSPSetBean(Set<T> set){
        this.set = set;
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

        return it.next();
    }

    public Stream<T> elements() {
        return set.stream();
    }
}
