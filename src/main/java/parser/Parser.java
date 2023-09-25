package parser;

import data.TaskList;
import data.exception.IncompleteDescriptionException;
import data.exception.InvalidActionException;
import ui.Ui;

public class Parser {
    public static final String MESSAGE_UNKNOWN = "☹ OOPS!!! I'm sorry, but I don't know what that means :-(";

    public static boolean inputValidation (String text, TaskList tasks) throws InvalidActionException,
            IncompleteDescriptionException {
        String[] actionAndDescriptions = text.split(" ");
        String action = actionAndDescriptions[0];
        boolean isExit = false;

        switch (action){
        case "bye":
            isExit = true;
            break;
        case "list":
            Ui.printList(tasks);
            break;
        case "mark":
            tasks.markTask(Integer.parseInt(actionAndDescriptions[1]));
            break;
        case "unmark":
            tasks.unMarkTask(Integer.parseInt(actionAndDescriptions[1]));
            break;
        case "todo":
            tasks.addTodo(text);
            break;
        case "deadline":
            tasks.addDeadline(text);
            break;
        case "event":
            tasks.addEvent(text);
            break;
        case "delete":
            tasks.deleteTask(Integer.parseInt(actionAndDescriptions[1]));
            break;
        default:
            throw new InvalidActionException(MESSAGE_UNKNOWN);
        }
        return isExit;
    }
}