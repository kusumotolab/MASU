package sdl.ist.osaka_u.newmasu.NodeFinder;

public interface Delegate<T, R> {
    public R invoke(T arg);
}