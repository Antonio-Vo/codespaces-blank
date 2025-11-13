
    public class Person {
        String name;
        int age;
    
    public void sayHello() {
        System.out.println("Hello, my name is " + name);
    }
    public class Main  {
    public static void main(String[] args) {
        Person p = new Person();
        p.name = "Alice";
        p.sayHello();
     }
    
    }
}

