package Objects;

import java.io.Console;

public class NumberHolder {
    public int anInt;
    public float aFloat;

    public static void main()
    {
        NumberHolder holder = new NumberHolder();
        holder.anInt = 1;
        holder.aFloat = 2.0f;
        System.out.println("An Int: " + holder.anInt + "A Float: " + holder.aFloat);
    }
}
