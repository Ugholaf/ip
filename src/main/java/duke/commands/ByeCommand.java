package duke.commands;

import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.TextUi;

public class ByeCommand extends Command {
    public static final String COMMAND_WORD = "bye";

    @Override
    public void execute(TaskList tasks, TextUi ui, Storage storage) {
        ui.showGoodByeMessage();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}