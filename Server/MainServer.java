package Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Server.Commands.*;
import Client.AskManager;
import Server.Program.CollectionManager;
import Server.Program.CommandManager;
import Server.Program.Console;
import Server.Program.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MainServer {
    public static Logger logger = LogManager.getLogger("ServerLogger");
    public static final int PORT = 3579;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        AskManager askManager = new AskManager(scanner);
        Console console = new Console(scanner, askManager);
        askManager.setConsole(console);

        /*System.out.println("gdf");*/
        FileManager fileManager = new FileManager("lab5.json");
        /*System.out.println("gdf2");*/
        CollectionManager collectionManager = new CollectionManager(fileManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new RemoveByIdCommand(collectionManager),
                new ClearCommand(collectionManager),
                new ExecuteScriptCommand(console),
                new ExitCommand(),
                new RemoveGreaterCommand(collectionManager),
                new RemoveLowerCommand(collectionManager),
                new HistoryCommand(),
                new MinByManufacturerCommand(collectionManager),
                new CountByPriceCommand(collectionManager),
                new FilterStartsWithNameCommand(collectionManager)
        );
        console.setCommandManager(commandManager);





        RequestIn requestIn = new RequestIn(commandManager);
        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestIn);
        server.run();
    }
}
