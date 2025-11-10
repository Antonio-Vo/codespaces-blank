import java.util.Scanner;
import java.util.*;

public class codeChallengeNov11th {
    //NOTE:
    // I didn't have time to test much. 
    //time: 9:58 am
    public static void main(String[] args) {
    //    Scanner input = new Scanner(System.in);
    //      int boxes = 0;
    //      int slices = 0;
    //      int guests = 0;
    //     Scanner sc = new Scanner(System.in);
    //     System.out.println("Input number of boxes");
    //      double num1 = Double.parseDouble(input.nextLine());


        System.out.println("Input number of boxes");
        Scanner b = new Scanner(System.in);
        int boxes = b.nextInt();
        b.close();
        System.out.println("Input number of slices per box");
        Scanner s = new Scanner(System.in);
        int slices = s.nextInt();
        System.out.println("Input number of guest");
        Scanner g = new Scanner(System.in);
        int guests = g.nextInt();

        int slicesTotal = (boxes * slices);
       int slicesPerGuest = (slicesTotal / guests);
       double slicesPerGuestRound = Math.floor(slicesPerGuest);
       int leftOver = (slicesTotal & guests);
       System.out.println("slice per guest: " + slicesPerGuestRound);
       System.out.println("left over: " + leftOver);
       // 


        
    }
}