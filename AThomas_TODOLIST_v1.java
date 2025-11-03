/*
 * NAME: Antonio Thomas
 * DATE: 11/2/2025
 * VERSION: v1
 * SOURCES USED: // https://www.w3schools.com/java/java_files_write.asp | https://www.w3schools.com/java/java_strings_specchars.asp | https://www.w3schools.com/jsref/jsref_replace.asp
 * COMMENTS: Program that can add, remove, and tracks task completion 
 * 
 * REQUIREMENTS: 
*/

// IMPORT STREAMS // 
import java.io.*; 
import java.util.*; 

public class AThomas_TODOLIST_v1 {
    
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
            System.out.println("3) remove task");
            System.out.println("4) set task as pending");
            System.out.println("5) set task completed");
            System.out.println("6) check task status");

            // ---> User Input
            String input = sc.nextLine();
            System.out.println("You selected: " + input);
            // IF/ELSE or SWITCH for the menu
            switch (input) {
                case "1": 
                System.out.println("You selected view task.");
                viewTask();
                break;

                case "2":
                System.out.println("You selected add task.");
                addTask();
                break;
                case "3":
                System.out.println("You selected remove task.");
                removeTask();
                break;
                case "4":
                System.out.println("set task as pending");
                pendingTask();
                break;
                case "5":
                System.out.println("set task as completed");
                completedTask();
                break;
                case "6":
                System.out.println("check task status.");
                statusTask();
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
    private static void loadTask() {
    tasks = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isBlank()) {
                // Remove task number and ")"
                line = line.replaceFirst("^\\d+\\)\\s*", "");
                tasks.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("No existing task file found. Starting new list.");
    }
}



    // Function 2 - saveTask()    // To save the File
    public static void saveTask() throws IOException {
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
    //DON'T FORGET!!!!
    // Make this class public for task status
    private static void addTask() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input task");
        String taskAdd = sc.nextLine();
        tasks.add(taskAdd);
        System.out.println(taskAdd);
        String status = "pending";
        
        // https://www.w3schools.com/java/java_files_write.asp
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
      writer.write("\n" + (tasks.size()) + ") " + taskAdd + " - " + status);
      System.out.println("Successfully appended to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    
    }
    // Function 5 - removeTask()  // To remove information from the file
    private static void removeTask() {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the task number to remove:");
    int taskNumber = sc.nextInt();
    sc.nextLine(); 

    
    if (taskNumber < 1 || taskNumber > tasks.size()) {
        System.out.println("Invalid task number.");
        return;
    }

    String removed = tasks.remove(taskNumber - 1);
    System.out.println("Removed: " + removed);

    
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
        for (int i = 0; i < tasks.size(); i++) {
            writer.write((i + 1) + ") " + tasks.get(i) + "\n");
        }
    } catch (IOException e) {
        System.out.println("An error occurred while updating the file.");
        e.printStackTrace();
    }
}

    // Error Handling 
    
    // - - - - - - - - - OTHER REQUIREMENTS - - - - - - - - - // 
    // Function 6 - check the status of the task
    private static void pendingTask() throws IOException{
    Scanner sc = new Scanner(System.in);
        System.out.println("Enter the task number to mark as pending:");
        int taskNumber = sc.nextInt();
        sc.nextLine();
        
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("Invalid task number.");
            return;
        }
        
        String originalTask = tasks.get(taskNumber - 1);
        
        if (originalTask.contains(" - completed")) {
           // https://www.w3schools.com/jsref/jsref_replace.asp
            String updatedTask = originalTask.replace(" - completed", " - pending");
            tasks.set(taskNumber - 1, updatedTask);
            saveTask();
            System.out.println("Task marked as pending successfully.");
        }
    
    }
    // Function 7 - check the status of the task
    private static void completedTask() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the task number to mark as complete:");
        int taskNumber = sc.nextInt();
        sc.nextLine();
        
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("Invalid task number.");
            return;
        }
        
        String originalTask = tasks.get(taskNumber - 1);
        
        if (originalTask.contains(" - pending")) {
           // https://www.w3schools.com/jsref/jsref_replace.asp
            String updatedTask = originalTask.replace(" - pending", " - completed");
            tasks.set(taskNumber - 1, updatedTask);
            saveTask();
            System.out.println("Task marked as completed successfully.");
        }
        
    }
    // Function 8 - check the status of the task
    private static void statusTask() {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the task number to check the status:");
    int taskNumber = sc.nextInt();
    sc.nextLine();

    if (taskNumber < 1 || taskNumber > tasks.size()) {
        System.out.println("Invalid task number.");
        return;
    }

    String task = tasks.get(taskNumber - 1);
    if (task.contains(" - completed")) {
        System.out.println("Status: completed");
    } else if (task.contains(" - pending")) {
        System.out.println("Status: pending");
    } else {
        System.out.println("Status: unknown");
    }
}

    // - - - - - - - - - ADDITIONAL FEATURES - - - - - - - - - // 

    // Function 9 - priorityTask() 
    // Function 10 - searchTask() 
    // Function 11 - sortTask() 

}// END OF CLASS
