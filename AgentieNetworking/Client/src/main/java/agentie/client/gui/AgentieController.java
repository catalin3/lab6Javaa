package agentie.client.gui;

import agentie.model.Agent;
import agentie.model.Bilet;
import agentie.model.Zbor;
import agentie.service.AgentException;
import agentie.service.IObserver;
import agentie.service.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.util.ResourceBundle;

public class AgentieController implements Initializable,IObserver{
    private IServer server;
    @FXML
    TableColumn<Zbor, String> columnDestinatie,columnId,columnAeroport,columnPlecare;
    @FXML
    TableColumn<Zbor,Integer>columnLocuri;
    private Agent user;
    @FXML
    private TableView<Zbor> table;
    @FXML
    private TextField cautDest,cautData,numeClient,numeTuristi, adresa,locuri,idZbor;
    ObservableList<Zbor> ss= FXCollections.observableArrayList();
    private ObservableList<Zbor> model;
    public AgentieController() {
        //this.server = server;
        System.out.println("Constructor Controller");

    }


    public AgentieController(IServer server) {
        this.server = server;
        System.out.println("constructor tController cu server param");

    }

    public void setServer(IServer s) {
        server = s;

        //loadTable();

    }

    private void loadTable(ObservableList<Zbor> m) {

            table.getSelectionModel().clearSelection();
            this.model=m;
            table.setItems(model);
            //table.getSelectionModel().selectFirst();
            /*table.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) ->idZbor.setText(String.valueOf(newValue.getId()))
            );*/



    }

    public void login(String id, String pass) throws AgentException {
        Agent userL = new Agent(id, pass, "");
        server.login(userL, this);
        user = userL;
        System.out.println("Autentificarea e ok!!!");

    }
    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
    void logout() {
        try {
            server.logout(user, this);
        } catch (AgentException e) {
            System.out.println("Logout error " + e);
        }

    }

    @FXML void table(ActionEvent actionEvent){
        try {
            ObservableList<Zbor> m=FXCollections.observableArrayList(server.getZbor());
            loadTable(m);
        } catch (AgentException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void cautat(ActionEvent actionEvent){

        try {
            ObservableList<Zbor> m=FXCollections.observableArrayList(server.cautare(cautDest.getText(),cautData.getText()));
            loadTable(m);
        } catch (AgentException e) {
            e.printStackTrace();
        }
    }
    private void initTable(){
        System.out.println("Aici intra ");
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDestinatie.setCellValueFactory(new PropertyValueFactory<>("destiantie"));
        columnAeroport.setCellValueFactory(new PropertyValueFactory<>("aeroport"));
        columnPlecare.setCellValueFactory(new PropertyValueFactory<>("plecare"));
        columnLocuri.setCellValueFactory(new PropertyValueFactory<>("nrLocuri"));
    }
    @FXML
    public  void handleCumparare(ActionEvent actionEvent){
        Zbor z=table.getSelectionModel().getSelectedItem();
        if(z==null)Util.showWarning("Select zbor","Alegeti o destinatie din tabel");
        int id=z.getId();
        String client=numeClient.getText();
        String turist=numeTuristi.getText();
        String adres=adresa.getText();
        int loc=Integer.valueOf(locuri.getText());
        try {
            update(id,client,turist,adres,loc);

        }catch (AgentException e){
            Util.showWarning("Communication error","Your server probably closed connection");

        }
        //loadTable();
    }

    private void update(int id, String client, String turist, String adres, int loc) throws AgentException{
    Bilet b=new Bilet(id,client,turist,adres,loc);
    server.update(b);
    }

    @Override
    public void cumparareBilet(Bilet bilet) throws AgentException {
        //loadTable();
    }
    public void setUser(Agent user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
