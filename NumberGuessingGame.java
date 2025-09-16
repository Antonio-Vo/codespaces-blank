// ✔️ Can create and assign variables
// ✔️ Can use Scanner for user input
// ✔️ Can use Random to generate numbers
// ✔️ Can use if/else to compare guesses
// ✔️ Can use a while loop for multiple attempts
// ✔️ Can count and display the number of attempts
// Antonio Thomas
// 9/16/2025
// v1.0
// Notes: In this program the user has 5 guesses to guess the random target number. The user is told if the guess is too high or too low. user failes if they can't guess the target number within 5 guesses but win other wise.
// Known bugs: None
import java.util.*;

public class NumberGuessingGame {
    public static void main(String[] args) {
        int randomNum = (int)(Math.random() * 100) + 1;
        Scanner input = new Scanner(System.in);
        int lives = 5; // number of guesses the user has before a loss
        System.out.println("This is a number guessing game. Guess a number from 1 to 100. You have " + lives + " lives"  );
        while(lives != 0){
            int guess = input.nextInt(); // gets user input
            if (guess == randomNum) {
                System.out.println("You win!"); // win print
                break;// makes sure the game ends
            } else if(randomNum > guess){
                System.out.println("Too low!");
                lives -= 1; // takes away one guess the user can use
                System.out.println("you have " + lives + " lives");
                if (lives == 0){
                System.out.println("You lose!");
            }
            } else if(randomNum < guess){
                System.out.println("Too high!");
                lives -= 1; // takes away one guess the user can use
                System.out.println("you have " + lives + " lives");
                if (lives == 0){
                System.out.println("You lose!"); // fail print
            }
            }
        }
    }
}
