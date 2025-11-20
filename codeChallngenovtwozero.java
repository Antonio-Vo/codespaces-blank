import java.util.HashMap;

public class codeChallngenovtwozero {
    public static void main(String[] args) {
        HashMap<String,String> numbers = new HashMap<>();
        numbers.put("Ant", "213-124-rara");
        numbers.put("bnt", "213-976-rara");
        numbers.put("cnt", "213-975-rara");
        numbers.put("ent", "213-269-raraaaaaa");
        numbers.put(null, "121212");
        System.out.println(numbers.get(null));
        System.out.println(numbers.get("Ant"));
        System.out.println(numbers.get("bnt"));
        System.out.println(numbers.get("cnt"));
        System.out.println(numbers.get("ent"));
        numbers.remove("Ant");
        System.out.println(numbers.get("Ant"));
        System.out.println(numbers.containsKey("bnt"));;
    }
}
// I have been distracted and didn't complete many of the questions


// below is for the  self exploation and not important for this program
// 
// map store vaule pairs, set stores only unique keys, list stores keys including duplicates 
//


//Section 2:
//
// Q5. Keys are link to respective vaules in the map
// Q6. it gets overwriten
// Q7. the map works as normal 


//3

//Q10. with .get()
//Q12. with .remove()