package parrtim.javaexercises;

public class Java implements ProgrammingLanguage {

    private String name = "Java";
    private String exampleHelloWorld = "public class HelloWorld\n" +
            "{\n" +
            "   public static void main(String[] args) {\n" +
            "       System.out.println(\"Hello World!\");\n" +
            "   }\n" +
            "}";

    @Override
    public String ExampleHelloWorld() {
        return exampleHelloWorld;
    }

    @Override
    public String GetName() {
        return name;
    }
}
