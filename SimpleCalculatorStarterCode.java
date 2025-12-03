/* 
NAME: Antonio Thomas
DATE: 9/21/2025 

PROJECT BRIEF: 

Goal:
Learn methods, switch statements, and error handling.
Skills Learned:
Writing methods for modular code
Using if,else if,else or switch to handle different operators
Basic error handling with try/catch
Looping to allow repeated calculations


Concept:
● User enters two numbers and an operator (+, -, ×, ÷).
● Program performs the operation and outputs the result.
● Program repeats until the user chooses to quit.


Checklist:
 ✔ Can write separate methods for each operation (+, -, ×, ÷)
 ✔ Can use if,else if,else or switch  to handle operators
 ✔ Can validate division by zero
 ✔ Can loop until the user quits
 ✔ Can handle invalid input with try/catch

Challenge:
● Add power and exponents (x², x^y)
● Add square root operations (√)
● Bonus Challenge (choose one):
● Add Advanced Operations:
● Add support for: Factorial and Percentages
● Trigonometric Functions (sin, cos, tan).

*/ 
import java.util.*; 
public class SimpleCalculatorStarterCode{ 
    public static void main(String[] args) {
        double result = 0;
        //  User Input - User Input need a scanner
        Scanner input = new Scanner(System.in);  
        System.out.println("Enter name: ");
        String name = input.nextLine();
        System.out.println("Hello " + name + " :D");
        
        // Ask user the user for two numbers 
        System.out.println("input first number: ");
        double num1 = Double.parseDouble(input.nextLine());

        System.out.println("input Seconded number: ");
        double num2 = Double.parseDouble(input.nextLine());
        // Operators - List??
        System.out.println("Operators are: Addition, Subtraction, Multiplication, and Division");
        String operator = input.nextLine();
        
        if (operator.equals("Addition")) {
             result = num1 + num2;
        } else if (operator.equals("Subtraction")){
         result = num1 - num2;
        } else if (operator.equals("Multiplication")) {
         result = num1 * num2;
        } else if (operator.equals("Division")) {
         result = num1 * num2;
        } else {
            System.out.println("error: invailed");
        }
        
        System.out.println(result);
        // Need Addition 
        // if (operator.equals("Addition")) {
        //     System.out.println("test one passed");
        //     System.out.println(num1 + num2);
        //     double result = num1 + num2;
        //     }else{
        //         System.out.println("test one failed");   
        // }
        // // Need Subtraction
        // if (operator.equals("Subtraction")) {
        //     System.out.println("test two passed");
        //     System.out.println(num1 - num2);
        //     double result = num1 - num2;
        //     }else{
        //         System.out.println("test two failed");   
        // }
        // // Need Multiplication
        // if (operator.equals("Multiplication")) {
        //     System.out.println("test three passed");
        //     System.out.println(num1 * num2);
        //     double result = num1 * num2;
        //     }else{
        //         System.out.println("test three failed");   
        // }
        // // Need Division
        // if (operator.equals("Division")) {
        //     System.out.println("test four passed");
        //     System.out.println(num1 / num2);
        //     double result = num1 / num2;
        //     }else{
        //         System.out.println("test four failed");   
        // }
        // // Add dialog 
        // System.out.println("The output is ");
        // confirm the equation that was seletction
        // Make sure the math is mathing (making sure it complies correctly) 

        // Print the results
        
    }
} 
