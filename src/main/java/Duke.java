import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private static List<Task> taskList = new ArrayList<>();

    private static Storage storage;

    public static void main(String[] args) {
        final String LOGO = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        final String GREETING_MESSSAGE = "Hello! I'm Duke\n"
                + "What can I do for you?\n";
        final String GOODBYE_MESSAGE = "Bye. Hope to see you again soon!\n";

        System.out.println("Hello from\n" + LOGO);

        printTextWithDivider(GREETING_MESSSAGE);

        storage = new Storage();

        taskList = storage.loadTasks();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String command = sc.nextLine();

            // Quit when user enters "bye"
            if (command.equals("bye")) {
                printTextWithDivider(GOODBYE_MESSAGE);
                break;
            }

            // Execute the command
            executeCommand(command);
        }

    }

    /**
     * Prints out the given text with dividers to the console
     *
     * @param text The specified text to be printed to the console
     */
    public static void printTextWithDivider(String text) {
        final String divider = "-".repeat(80) + "\n";
        System.out.println(divider + text + divider);
    }

    /**
     * Execute the command entered by user
     *
     * @param command The specified command
     */
    private static void executeCommand(String command) {
        // Limit the words to 2
        String[] inputs = command.split(" ", 2);

        try {
            switch (inputs[0]) {
                // List out all tasks
                case ("list"): {
                    listCommand(inputs);
                    break;
                }
                // Add todo task
                case ("todo"): {
                    addTaskCommand(TaskType.TODO, inputs);
                    break;
                }
                // Add deadline task
                case ("deadline"): {
                    addTaskCommand(TaskType.DEADLINE, inputs);
                    break;
                }
                // Add event task
                case ("event"): {
                    addTaskCommand(TaskType.EVENT, inputs);
                    break;
                }
                // Mark task as done
                case ("mark"): {
                    markTaskCommand(inputs);
                    break;
                }
                // Mark task as undone
                case ("unmark"): {
                    unmarkTaskCommand(inputs);
                    break;
                }
                // Delete task
                case ("delete"): {
                    deleteTaskCommand(inputs);
                    break;
                }
                default: {
                    throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(.\n");
                }
            }
        } catch (DukeException e) {
            printTextWithDivider(e.getMessage());
        }
    }

    private static void listCommand(String[] inputs) throws DukeException {
        if (inputs.length == 2) {
            throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(.\n");
        }

        StringBuilder str = new StringBuilder();
        str.append("Here are the tasks in your list:\n");
        for (int i = 0; i < taskList.size(); i++) {
            // Display task as 1-index
            str.append(i + 1).append(".").append(taskList.get(i)).append("\n");
        }
        printTextWithDivider(str.toString());
    }

    private static void addTaskCommand(TaskType taskType, String[] inputs) {
        if (inputs.length == 1 || inputs[1].equals("")) {
            throw new DukeException("OOPS!!! The description of " + taskType.getTaskType() + " cannot be empty.\n");
        }

        Task task;
        switch (taskType) {
            case TODO: {
                task = new Todo(inputs[1]);
                break;
            }
            case DEADLINE: {
                String[] deadlineInputs = inputs[1].split("/by", 2);

                if (deadlineInputs.length == 1 || deadlineInputs[1].equals("")) {
                    throw new DukeException("OOPS!!! The date of a deadline cannot be empty.\n");
                }
                task = new Deadline(deadlineInputs[0], deadlineInputs[1]);
                break;
            }
            case EVENT: {
                String[] eventInputs = inputs[1].split("/at", 2);

                if (eventInputs.length == 1 || eventInputs[1].equals("")) {
                    throw new DukeException("OOPS!!! The date and time of an event cannot be empty.\n");
                }
                task = new Event(eventInputs[0], eventInputs[1]);
                break;
            }
            default: {
                throw new DukeException("OOPS!!! Invalid task type.\n");
            }
        }

        taskList.add(task);

        storage.appendTaskToFile(task);

        String addTaskMessage =  "Got it. I've added this task:\n" +
                "  " + task + "\n" +
                "Now you have " + taskList.size() + " task(s) in the list.\n";

        printTextWithDivider(addTaskMessage);
    }

    private static void markTaskCommand(String[] inputs) throws DukeException {
        if (inputs.length == 1 || inputs[1].equals("")) {
            throw new DukeException("OOPS!!! The task index cannot be empty.\n");
        }
        try {
            // Tasks are stored as 0-index but display as 1-index
            // Minus 1 to get the correct task in the taskList
            int taskIndex = Integer.parseInt(inputs[1]) - 1;
            Task task = taskList.get(taskIndex);
            task.markAsDone();
            storage.writeAllTasksToFile(taskList);

            String str = "Nice! I've marked this as done:\n" +
                    task + "\n";
            printTextWithDivider(str);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new DukeException("OOPS!!! The task index specified is not valid.\n");
        }
    }

    private static void unmarkTaskCommand(String[] inputs) throws DukeException {
        if (inputs.length == 1 || inputs[1].equals("")) {
            throw new DukeException("OOPS!!! The task index cannot be empty.\n");
        }
        try {
            // Tasks are stored as 0-index but display as 1-index
            // Minus 1 to get the correct task in the taskList
            int taskIndex = Integer.parseInt(inputs[1]) - 1;
            Task task = taskList.get(taskIndex);
            task.maskUndone();
            storage.writeAllTasksToFile(taskList);

            String str = "Ok, I've marked this task as not done yet:\n" +
                    task + "\n";
            printTextWithDivider(str);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new DukeException("OOPS!!! The task index specified is not valid.\n");
        }
    }

    private static void deleteTaskCommand(String[] inputs) throws DukeException {
        if (inputs.length == 1 || inputs[1].equals("")) {
            throw new DukeException("OOPS!!! The task index cannot be empty.\n");
        }
        try {
            // Tasks are stored as 0-index but display as 1-index
            // Minus 1 to get the correct task in the taskList
            int taskIndex = Integer.parseInt(inputs[1]) - 1;
            Task task = taskList.get(taskIndex);
            taskList.remove(task);
            storage.writeAllTasksToFile(taskList);

            String str = "Noted. I've removed this task:\n" +
                    "  " + task + "\n" +
                    "Now you have " + taskList.size() + " task(s) in the list.\n";
            printTextWithDivider(str);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new DukeException("OOPS!!! The task index specified is not valid.\n");
        }
    }
}
