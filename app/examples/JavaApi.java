package examples;

public class JavaApi<T> {

    public void foo(T[] data) {
        for (int i = 0; i < data.length; ++i) {
            System.out.println(data[i]);
        }
    }

    public int multiply(int value1, int value2) {
        return value1 * value2;
    }

}
