import java.util.HashMap;

public class codeChallngenovtwozero {
    public static void main(String[] args) {
        HashMap<String,String> numbers = new HashMap<>();
        numbers.put("Ant", "213-124-rara");
        numbers.put("bnt", "213-976-rara");
        numbers.put("cnt", "213-975-rara");
        numbers.put("ent", "213-269-rara");
        System.out.println(numbers.get("Ant"));
        System.out.println(numbers.get("bnt"));
        System.out.println(numbers.get("cnt"));
        System.out.println(numbers.get("ent"));
        numbers.remove("Ant");
        System.out.println(numbers.get("Ant"));
        System.out.println(numbers.containsKey("bnt"));;
    }
}