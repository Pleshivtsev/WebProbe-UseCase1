package testLogic;

@FunctionalInterface
public interface StepMethodArg<T> {
    void run(T t);
}