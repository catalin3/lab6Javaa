package agentie.client.gui;

import javafx.scene.control.Alert;

public class Util {

    //private static Logger logger = LogManager.getLogger(Util.class.getName());

    public static void showWarning(String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MPP");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

    }
}
