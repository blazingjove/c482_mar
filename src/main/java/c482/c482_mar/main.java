package c482.c482_mar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/** main class creates the application.
 * extends Application
 * <b>JAVADOCS located in the following location c482_mar/doc/index.html.</b>
 */
public class main extends Application {
    /** This is the main method.
     * first method that gets called to create the app with given parameters when you run your java program.
     * @param stage is the javafx parameter that is input to open the scene */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("/c482/views/mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 930, 430);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

}