package parrtim.javaexercises;

public class CSharp implements ProgrammingLanguage {

    private String name = "C#";
    private String exampleHelloWorld = "public class Hello1\n" +
            "{\n" +
            "   public static void Main()\n" +
            "   {\n" +
            "      System.Console.WriteLine(\"Hello, World!\");\n" +
            "   }\n" +
            "}";

    @Override
    public String ExampleHelloWorld() {
        return exampleHelloWorld;
    }

//    @Override
//    public String GetName() {
//        return name;
//    }
}
