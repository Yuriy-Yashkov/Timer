import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));
        primaryStage.setTitle("Computer Shutdown v2.3");
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setMaxWidth(310);
        primaryStage.setMaxHeight(250);
        primaryStage.setMinWidth(310);
        primaryStage.setMinHeight(250);
//        primaryStage.setX(250);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
