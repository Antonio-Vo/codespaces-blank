
    class Animal{
    void makeSound() {
        System.out.println("Some sound");
    }
}   
 class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("Woof");
    }
 }
 public class Main {
    public static void main(String[] args) {
        Animal a = new Dog();
        a.makeSound();; 
    }
 }


