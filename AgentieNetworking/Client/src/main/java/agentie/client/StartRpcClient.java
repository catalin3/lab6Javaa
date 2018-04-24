package agentie.client;

import agentie.client.gui.AgentieController;
import agentie.client.gui.LoginController;
import agentie.network.rpcprotocol.AgentieServerRpcProxy;
import agentie.service.IServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClient  extends Application{
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServer server = new AgentieServerRpcProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("Login.fxml"));
        Parent root=loader.load();


        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);




        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("Agentie.fxml"));
        Parent croot=cloader.load();


        AgentieController chatCtrl =
                cloader.<AgentieController>getController();

        chatCtrl.setServer(server);

        ctrl.setChatController(chatCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP ");
        primaryStage.setScene(new Scene(root, 400, 430));
        primaryStage.show();




    }
}
