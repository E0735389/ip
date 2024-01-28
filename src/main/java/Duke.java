import java.util.Scanner;
import java.util.NoSuchElementException;

public class Duke {
    static final String CHATBOT_NAME = "Echo";
    static final int CELL_WIDTH = 70;

    /**
     * Function to print the banner.
     */
    public void printBanner() {
        // Banner somewhat inspired from SageMath's banner.
        System.out.println("┌" + new String(new char[CELL_WIDTH - 2]).replace("\0", "─") + "┐");
        // Java 8 compatible, for now.
        String firstLine = CHATBOT_NAME + ", the chatbot. Version 0.0.0.";
        System.out.println(String.format("│ %-" + (CELL_WIDTH - 4) + "s │", firstLine));
        String secondLine = "Using Java " + System.getProperty("java.version") + ".";
        System.out.println(String.format("│ %-" + (CELL_WIDTH - 4) + "s │", secondLine));
        System.out.println("└" + new String(new char[CELL_WIDTH - 2]).replace("\0", "─") + "┘");
    }

    /**
     * Function to ask user for a prompt.
     */
    public String askForPrompt() {
        System.out.print(">>> ");
        String result = sc.nextLine();
        // We print out the prompt if input is redirected to make the expected output more readable.
        if (System.console() == null) {
            System.out.println(result);
        }
        return result;
    }

    Scanner sc = new Scanner(System.in);
    Parser parser = new Parser();
    Storage storage = new Storage();

    /**
     * Main function to run the chatbot.
     */
    public void run() {
        storage.loadFromFile(parser);
        printBanner();
        while (true) {
            String prompt;
            try {
                prompt = askForPrompt();
            } catch (NoSuchElementException e) {
                System.out.println();
                try {
                    new ByeCommand().execute(storage);
                } catch (DukeException e2) {
                    System.out.println(e2.getMessage());
                }
                break;
            }

            Command command;
            try {
                command = parser.parse(prompt);
                command.execute(storage);
            } catch (DukeException e) {
                System.out.println(e.getMessage());
                continue;
            }
        }
    }

    public static void main(String[] args) {
        Commands.registerCommands();
        Duke duke = new Duke();
        duke.run();
    }
}
