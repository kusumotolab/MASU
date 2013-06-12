package sdl.ist.osaka_u.newmasu.gomi;

public interface Delegate<T, R> {
    public R invoke(T arg);
}