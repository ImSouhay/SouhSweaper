package app.souhsweaper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Logic.playFlag();
        Image icon=new Image(String.valueOf(Main.class.getResource("bomb.png")));
        Parent root=FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Welcome.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("SouhSweeper");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args){launch(args);
    }
}