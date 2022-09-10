package duke.commands;

import duke.storage.Storage;
import duke.task.TaskList;
import duke.task.Todo;
import duke.ui.TextUi;

/**
 * Represents a todo command.
 */
public class TodoCommand extends Command {
    public static final String COMMAND_WORD = "todo";

    private Todo todoTask;

    /**
     * Creates a new instance of todo command and todo task.
     *
     * @param description The description of the todo task.
     */
    public TodoCommand(String description) {
        this.todoTask = new Todo(description);
    }

    /**
     * Adds the todo task to the task list and save it to the local file.
     * @param tasks The list of tasks in Duke.
     * @param ui The TextUi class used to print message in Duke.
     * @param storage The storage used to save the tasks in the local file.
     */
    @Override
    public void execute(TaskList tasks, TextUi ui, Storage storage) {
        tasks.addTask(this.todoTask);
        storage.appendTaskToFile(this.todoTask);
        ui.showAddTaskMessage(this.todoTask, tasks);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
