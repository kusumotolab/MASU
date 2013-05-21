package sdl.ist.osaka_u.newmasu.Finder;

public interface Delegate<T, R> {
    public R invoke(T arg);
}