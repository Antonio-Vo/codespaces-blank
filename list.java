import java.util.*;
public class list {
    public static void main(String[] args) {
        List<String> groceries = new ArrayList<>();
        groceries.add("Cheese");
        groceries.add("Mayo");
        groceries.add("Milk");
        groceries.add(1, "Bacon");
        groceries.set(2, "Eggs");
        System.out.println(groceries);
        System.out.println(groceries.indexOf("Cheese"));
        groceries.remove(2);
        System.out.println(groceries);
        System.out.println(groceries.get(1));
        System.out.println(groceries.contains("Eggs"));

        //rad for loop
        for(int i = 0; i < groceries.size(); i++){
            System.out.println("Index " + i + "Vaule: " + groceries.get(1));
        }
        // ENHANCED FOR LOOP
        for(String str : groceries){
            System.out.print(str + " ");
        }
    }// end main
}// end class
