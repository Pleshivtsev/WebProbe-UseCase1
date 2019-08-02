package testLogic;

@FunctionalInterface
public interface StepMethodDblArgs<T> {

    void run(T arg1, T arg2);
}
