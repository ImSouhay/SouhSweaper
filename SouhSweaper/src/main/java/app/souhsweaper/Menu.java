package app.souhsweaper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class Menu extends Board{

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Button Quit;
    @FXML
    Button Play;
    @FXML
    Button Next;
    @FXML
    Button Back;


    @FXML
    public void nexttuto(ActionEvent event) throws IOException{
        root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Tuto.fxml")));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    public void Quit(ActionEvent event) {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void backwelc(ActionEvent event) throws IOException{
        root=FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Welcome.fxml")));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    public void nextdiff(ActionEvent event) {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        Board.makeBoard(stage,10,10);
        Board BOARD=new Board();
        BOARD.makeHiddenBoard(10, 10);
    }


}