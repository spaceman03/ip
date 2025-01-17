package spaceman;

import spaceman.commands.Command;
import spaceman.commands.ExitCommand;
import spaceman.data.TaskList;
import spaceman.data.exception.SpacemanException;
import spaceman.ui.Ui;
import spaceman.storage.Storage;
import spaceman.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

/**
 * Entry point of the Spaceman application which serves as a personal assistance chat-bot.
 * Initializes the application and starts the interaction with the user.
 */
public class Spaceman {
    private Ui ui;
    private Storage storage;
    private TaskList tasks;

    /**
     * Sets up the required objects and loads up the data from the storage file.
     * @param filePath path of the file used to store data
     */
    public Spaceman (String filePath) throws IOException {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.readDataFromFile());
        } catch (FileNotFoundException e) {
            Ui.showReadDataError();
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();
            tasks = new TaskList(storage.readDataFromFile());
        }
    }

    /**
     * Runs the program until termination.
     */
    public void run() {
        Ui.showWelcomeMessage();
        runProgramUntilTermination();
    }

    /**
     * Reads the user command and executes it, until the user issues the "bye" command.
     * This method continuously prompts the user for commands and executes them until the user enters "bye".
     */
    private void runProgramUntilTermination() {
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.getUserCommand();
                Command command = Parser.parseCommand(fullCommand, tasks);
                command.execute(tasks);
                isExit = ExitCommand.isExit(command);
                storage.writeToFile(tasks);
            } catch (SpacemanException e) {
                ui.showException(e);
            } catch (IOException e) {
                Ui.showLine();
                System.out.println("Something went wrong: " + e.getMessage());
                Ui.showLine();
            } catch (IndexOutOfBoundsException | ParseException e) {
                ui.showException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Spaceman("./data/spaceman.txt").run();
    }
}