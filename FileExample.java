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

    public static void main(String[] args) throws IOException {
        loadTask();
        // Load Task 
        Scanner sc = new Scanner(System.in); 
        boolean running = true; 

        while (running){
            // MENU 
            // ---> Menu List 
            System.out.println("input the one of the following.");
            System.out.println("1) view task");
            System.out.println("2) add task");
            System.out.println("3) load task");
            // ---> User Input
            String input = sc.nextLine();
            System.out.println("You selected: " + input);
            // IF/ELSE or SWITCH for the menu
            switch (input) {
                case "1": 
                System.out.println("You selected view task.");
                break;

                case "2":
                System.out.println("You selected add task.");
                addTask();
                break;
                case "3":
                System.out.println("You selected load task.");
                break;
            
                default:
                System.out.println("Invalid input");
                break;
            }
            // if (input.equals("1")) {
            //     System.out.println("AAAAAAA");
            // }
        }

        // Save Task
        saveTask();
        // Close Scanner
        sc.close();
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
        File file = new File(FILE_NAME);
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
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
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
    
    private static void addTask() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input task");
        String taskAdd = sc.nextLine();
        tasks.add(taskAdd);
        System.out.println(taskAdd);
        
        // https://www.w3schools.com/java/java_files_write.asp
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
      writer.write("\n" + taskAdd);
      System.out.println("Successfully appended to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    
    }
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
