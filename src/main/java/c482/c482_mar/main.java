package c482.c482_mar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/** @author  Marco Alvarez
 * this class creates an app and configures window contents and dimensions
 * */

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("/c482/views/mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 930, 430);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

}