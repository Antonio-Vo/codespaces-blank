import java.lang.Thread;
import java.util.*;
public class VectorExample {
    public static void main(String[] args) throws InterruptedException {
        /*Vector<String> names = new Vector<>();
         names.add("John");
         names.add("Anna");
         names.add("Seth");
         System.out.println(names.firstElement()); // John
         System.out.println(names.lastElement()); // Seth
         System.out.println(names.size()); // 3
         names.clear(); // [] Vector cleared
         */ 

        int size = 1000000;
        List<Integer> arrayList = new ArrayList<>();
        long start = System.currentTimeMillis();
        
        for(int i = 0; i < size; i++){
            arrayList.add(i);
        };

        long end = System.currentTimeMillis();

        System.out.println("Added " + size + " elements to ArrayList: " + (end - start) + " ms");

        List<Integer> vector = new Vector<>();
        start = System.currentTimeMillis();
        
        for(int i = 0; i < size; i++){
            vector.add(i);
        };

        end = System.currentTimeMillis();

        System.out.println("Added " + size + " elements to Vector: " + (end - start) + " ms");

        // - - - - - - - - - - // 

        List<Integer> multithrededList = Collections.synchronizedList(new ArrayList<>()); 
        start = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            for(int i = 0; i < size; i++){
            multithrededList.add(i);
            }
        });
         Thread t2 = new Thread(() -> {
            for(int i = 0; i < size; i++){
            multithrededList.add(i);
            }
        });

        t1.start();
        t2.start(); 
        t1.join(); 
        t2.join(); 
        
        end = System.currentTimeMillis();

        System.out.println("Added elements to ArrayList in a mutithreded way: " + (end - start) + " ms");
        System.out.println("Size is: " + multithrededList.size()); 

       // - - - - - - - - - - // 

        List<Integer> multithrededVector = new Vector<>();
        start = System.currentTimeMillis();

        t1 = new Thread(() -> {
            for(int i = 0; i < size; i++){
            multithrededVector.add(i);
            };
        });

        t2 = new Thread(() -> {
            for(int i = 0; i < size; i++){
            multithrededVector.add(i);
            };
        });

        t1.start();
        t2.start(); 
        t1.join(); 
        t2.join(); 

        end = System.currentTimeMillis();

        System.out.println("Added elements to Vector in a mutithreded way: " + (end - start) + " ms");
        System.out.println("Size is: " + multithrededVector.size()); 
    }// END MAIN 
}// END CLASS





// List<Integer> vector = new Vector<>();
// Start = System.currentTimeMillis();

// for (int i = 0; i <size; i++){
//  arrayList.add(i);
//}
//long end = System.currentTimeMillis();
//System.out.println("Added " + size + "elements to Vector: " + (end -))
// 

//List<Integer> multithreadedList = new ArryList<>();
//start = System.currentTimeMillis();
// Thread t1 = new Thread(() -> {
// multithreadedList.add(i);
//}
//});


//List<Integer> multithreadedList = new ArryList<>();
//start = System.currentTimeMillis();
// Thread t2 = new Thread(() -> {
// multithreadedList.add(i);
//}
//});

// t1.start();
// t2.start();
// t1.join();
// t2.join();
// end = System.currentTimeMillis();
////System.out.println("elements in a multithreaded way to ArrayList: " + (end -));

