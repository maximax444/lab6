package Server;

import common.Exceptions.ClosingSocketException;
import common.Exceptions.ConnectionErrorException;
import common.Exceptions.OpeningServerSocketException;
import common.MainConsole;
import common.Request;
import common.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    private int port;
    private int soTimeout;
    private ServerSocket serverSocket;
    private RequestIn requestIn;

    public Server(int port, int soTimeout, RequestIn requestIn) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestIn = requestIn;
    }

    /**
     * Begins server operation.
     */
    public void run() {
        try {
            openServerSocket();
            boolean processingStatus = true;
            while (processingStatus) {
                try (Socket clientSocket = connectToClient()) {
                    processingStatus = processClientRequest(clientSocket);
                } catch (ConnectionErrorException | SocketTimeoutException exception) {
                    MainConsole.println(exception.getMessage());
                    break;
                } catch (IOException exception) {
                    MainConsole.println("Произошла ошибка при попытке завершить соединение с клиентом!");
                    MainServer.logger.error("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch (OpeningServerSocketException exception) {
            MainConsole.println("Сервер не может быть запущен!");
            MainServer.logger.error("Сервер не может быть запущен!");
        }
    }

    /**
     * Finishes server operation.
     */
    private void stop() {
        try {
            if (serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            MainConsole.println("Работа сервера успешно завершена.");
        } catch (ClosingSocketException exception) {
            MainConsole.println("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            MainConsole.println("Произошла ошибка при завершении работы сервера!");
            MainServer.logger.error("Произошла ошибка при завершении работы сервера!");
        }
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
        } catch (IllegalArgumentException exception) {
            MainConsole.println("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            MainConsole.println("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Connecting to client.
     */
    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            MainConsole.println("Прослушивание порта '" + port + "'...");
            MainServer.logger.info("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            MainConsole.println("Соединение с клиентом успешно установлено.");
            MainServer.logger.info("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch (SocketTimeoutException exception) {
            MainConsole.println("Превышено время ожидания подключения!");
            MainServer.logger.error("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        } catch (IOException exception) {
            MainConsole.println("Произошла ошибка при соединении с клиентом!");
            MainServer.logger.error("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException("Произошла ошибка подключения!");
        }
    }

    /**
     * The process of receiving a request from a client.
     */
    private boolean processClientRequest(Socket clientSocket) {
        Request userRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = requestIn.handle(userRequest);
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            } while (responseToUser.getResponseStatus() != 2);
            return false;
        } catch (ClassNotFoundException exception) {
            MainConsole.println("Произошла ошибка при чтении полученных данных!");
        } catch (InvalidClassException | NotSerializableException exception) {
            MainConsole.println("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (userRequest == null) {
                MainConsole.println("Непредвиденный разрыв соединения с клиентом!");
            } else {
                MainConsole.println("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }
}
