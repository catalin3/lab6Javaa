package agentie.client.gui;

import agentie.model.Agent;
import agentie.service.AgentException;
import agentie.service.IServer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class LoginController {


    private IServer server;
    private AgentieController agentiCtrl;
    private Agent agent;
    @FXML
    TextField username;
    @FXML
    TextField passwor;

    Parent mainAgentie;

    public void setServer(IServer server) {
        this.server = server;
    }

    public void setParent(Parent p) {
        mainAgentie = p;
    }

    @FXML
    public void pressLogin(ActionEvent actionEvent) {
        //Parent root;
        String nume = username.getText();
        String passwd = passwor.getText();
        agent = new Agent(nume, passwd);


        try {
            server.login(agent, agentiCtrl);
            // Util.writeLog("User succesfully logged in "+crtUser.getId());
            Stage stage = new Stage();
            stage.setTitle(" Window for " + agent.getId());
            stage.setScene(new Scene(mainAgentie));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    agentiCtrl.logout();
                    System.exit(0);
                }
            });

            stage.show();
            agentiCtrl.setUser(agent);
            //ch.setLoggedFriends();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

        } catch (AgentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }
    public void pressCancel (ActionEvent actionEvent){
        System.exit(0);
    }

    public void setUser (Agent user){
        this.agent = user;
    }

    public void setChatController (AgentieController chatController){
        this.agentiCtrl = chatController;
    }
}