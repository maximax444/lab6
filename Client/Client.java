package Client;


import common.Exceptions.ConnectionErrorException;
import common.Exceptions.NotInDeclaredLimitsException;
import common.MainConsole;
import common.Request;
import common.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout = 5 * 1000;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts = 5;
    private ClientConsole clientConsole;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;

    public Client(String host, int port, ClientConsole clientConsole) {
        this.host = host;
        this.port = port;
        this.clientConsole = clientConsole;
    }

    public void startWork() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try {
                    connectToServer();
                    processingStatus = processRequestToServer();
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        MainConsole.println("Превышено количество попыток подключения!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        MainConsole.println("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределами возможных значений!");
                        MainConsole.println("Повторное подключение будет произведено немедленно.");
                    } catch (Exception timeoutException) {
                        MainConsole.println("Произошла ошибка при попытке ожидания подключения!");
                        MainConsole.println("Повторное подключение будет произведено немедленно.");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            MainConsole.println("Работа клиента успешно завершена.");
        } catch (NotInDeclaredLimitsException exception) {
            MainConsole.println("Клиент не может быть запущен!");
        } catch (IOException exception) {
            MainConsole.println("Произошла ошибка при попытке завершить соединение с сервером!");
        }
    }

    /**
     * Connecting to server.
     */
    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) MainConsole.println("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            MainConsole.println("Соединение с сервером успешно установлено.");
            MainConsole.println("Ожидание разрешения на обмен данными...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            MainConsole.println("Разрешение на обмен данными получено.");
        } catch (IllegalArgumentException exception) {
            throw new NotInDeclaredLimitsException("Адрес сервера введен некорректно!");
        } catch (IOException exception) {
            throw new ConnectionErrorException("Произошла ошибка при соединении с сервером!");
        }
    }

    /**
     * Server request process.
     */
    private boolean processRequestToServer() {
        Request requestToServer;
        requestToServer = null;
        Response serverResponse;
        serverResponse = null;
        do {
            try {
                if (serverResponse != null) {
                    requestToServer = clientConsole.handle(1);
                } else {
                    requestToServer = clientConsole.handle(1);
                }
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                MainConsole.println(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                MainConsole.println("Произошла ошибка при отправке данных на сервер!");
            } catch (ClassNotFoundException exception) {
                MainConsole.println("Произошла ошибка при чтении полученных данных!");
            } catch (IOException exception) {
                MainConsole.println("Соединение с сервером разорвано!");
                try {
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    MainConsole.println("Попробуйте повторить команду позднее.");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return false;
    }
}
