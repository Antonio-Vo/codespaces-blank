/*
 * NAME: 
 * DATE: 
 * VERSION: 
 * SOURCES USED: 
 * COMMENTS: 
 * 
 * REQUIREMENTS: 
*/

// IMPORT STREAMS // 
import java.io.*; 
import java.util.*; 

public class FileExample {
    
    // File Name
    private static final String FILE_NAME = "task.txt";
    // ArrayList 
    private static ArrayList<String> tasks = new ArrayList<>();

    public static void main(String[] args) {
        // Load Task 
        Scanner sc = new Scanner(System.in); 
        boolean running = true; 

        while (running){
            // MENU 
            // ---> Menu List 
            // ---> User Input
            // IF/ELSE or SWITCH for the menu
        }

        // Save Task
        // Close Scanner

    }// END OF MAIN

    // - - - - - - - - - FUNCTIONS  - - - - - - - - - // 
    /*
     * .remove()
     * .canRead()
     * .add()
     * .isEmpty()
     */

    // Function 1 - loadTask()    // To load the File
    private static void loadTask() throws IOException {
        File file = new File("File_Name");
        if (file.exists()) {
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                tasks.add(fileReader.nextLine());
            }
            fileReader.close();
        }
    }

    // Function 2 - saveTask()    // To save the File
    private static void saveTask() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("FILE_NAME"));
        for (String task : tasks) {
            writer.write(task);
            writer.newLine();
        }
        writer.close();
    }
    // Function 3 - viewTask()    // To view the File
    private static void viewTask() {
        if (tasks.isEmpty()) System.out.println("No tasks");
        else for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i+1) + ". " + tasks.get(i));
        }
    }

    // Function 4 - addTask()     // To add information to the file
    // Function 5 - removeTask()  // To remove information from the file
    // Error Handling 
    
    // - - - - - - - - - OTHER REQUIREMENTS - - - - - - - - - // 
    // Function 6 - check the status of the task
    // Function 7 - check the status of the task
    // Function 8 - check the status of the task

    // - - - - - - - - - ADDITIONAL FEATURES - - - - - - - - - // 

    // Function 9 - priorityTask() 
    // Function 10 - searchTask() 
    // Function 11 - sortTask() 

}// END OF CLASS 
