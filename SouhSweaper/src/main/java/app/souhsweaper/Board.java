package app.souhsweaper;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Random;

public class Board extends  Main{

    private static final GridPane gridpane=new GridPane();
    static int[][] board;
    private static StackPane WON;
    private static StackPane LOST;


    public static void makeBoard(Stage stage,int x,int y) {
        Button[][] button =new Button[x][y];
        for(int i=0;i<x;i++) {
            for (int j = 0; j < y; j++) {
                button[i][j] = new Button();
                button[i][j].getStyleClass().add("buttonGame");
                button[i][j].setOnMouseClicked(event -> {
                    if(event.getButton()== MouseButton.SECONDARY) {
                        Logic.flagOrUnflag(event);}else
                    if(event.getButton()== MouseButton.PRIMARY) {
                        try {
                            Logic.destroyOrPlayFlag(event);
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException |
                                 InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                button[i][j].setText("");
                gridpane.add(button[i][j], i, j, 1, 1);
            }
        }

        gridpane.setLayoutX(200);
        gridpane.setLayoutY(0);


        Text LOST_TEXT=new Text("GAME OVER!");
        LOST_TEXT.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 36));
        LOST_TEXT.setFill(Color.FIREBRICK);
        Rectangle LOST_REC=new Rectangle(300, 150, Color.RED);
        LOST = new StackPane(LOST_REC, LOST_TEXT);

        Text WON_TEXT=new Text("YOU WON!");
        WON_TEXT.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 36));
        WON_TEXT.setFill(Color.LIMEGREEN);
        Rectangle WON_REC=new Rectangle(300, 150, Color.GREEN);
        WON = new StackPane(WON_REC, WON_TEXT);

        StackPane root=new StackPane(gridpane, LOST, WON);

        LOST.setVisible(false);
        WON.setVisible(false);


        Scene scene = new Scene(root, 600, 400);
        String GameStyle= String.valueOf(Board.class.getResource("GameStyle.css"));
        scene.getStylesheets().add(GameStyle);
        stage.setScene(scene);
    }

    private static int xxx;
    private static int yyy;


    public void makeHiddenBoard(int x, int y) {
        int set,randx, randy,around;
        board = new int[x][y];
        Random random = new Random();

        xxx=x;
        yyy=y;
        Logic.makeFlaggedArray();
        Logic.makeDestroyedArray();



        int bombsnum = 10;


//        generate bombs
        for (int bombs = 1; bombs <= bombsnum; bombs++) {
            do {
                randx=random.nextInt(x);
                randy=random.nextInt(y);
            }while (board[randx][randy]==-1);
            board[randx][randy]=-1;
        }

        for (int a = 0; a < x; a++) {
            for (int b = 0; b < x; b++) {
            set = board[a][b];
                if (set != -1) {around = 0;
//        ignore this its bad ik
                    if (b != 0) {if (board[a][b - 1] == -1) {around++;}}
                    if (b != 0) {if (a != 0) {if (board[a - 1][b - 1] == -1) {around++;}}}
                    if (b != 0) {if (a != x-1) {if (board[a + 1][b - 1] == -1) {around++;}}}
                    if (a != 0) {if (board[a - 1][b] == -1) {around++;}}
                    if (a != x-1) {if (board[a + 1][b] == -1) {around++;}}
                    if (b != y-1) {if (a != 0) {if (board[a - 1][b + 1] == -1) {around++;}}}
                    if (b != y-1) {if (a != x-1) {if (board[a + 1][b + 1] == -1) {around++;}}}
                    if (b != y-1) {if (board[a][b + 1] == -1) {around++;}}
                    board[a][b]=around;
                }
            }
        }

        boolean[][] WIN_ARRAY =new boolean[x][y];
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board.length;j++){
                if(board[i][j]==-1){WIN_ARRAY[i][j]=false;continue;}
                WIN_ARRAY[i][j]=true;
            }
        }

        setWIN_ARRAY(WIN_ARRAY);
    }
    private static boolean[][] WIN_ARRAY;
    private void setWIN_ARRAY(boolean[][] ARRAY){
        WIN_ARRAY=ARRAY;
    }
    public static boolean[][] getWIN_ARRAY(){
        return WIN_ARRAY;
    }



    public static int[][] getHiddenBoardArray(){
        return board;
    }
    public static int getXX(){return xxx;}
    public static int getYY(){return  yyy;}
    public static StackPane getLostPane(){return LOST;}
    public static StackPane getWonPane(){return WON;}
}
