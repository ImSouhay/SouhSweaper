package app.souhsweaper;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class Logic extends Board{

    private static boolean PRE_LOAD=true;
    private static boolean[][] flagged;
    private static boolean[][] destroyed;
    private static final String BOMB_SOURCE= String.valueOf(Logic.class.getResource("bomb.png"));
    private static final String FLAG_SOURCE= String.valueOf(Logic.class.getResource("flag.png"));
    private static ImageView BOMB;
    private static final String BUZZ_SOURCE=String.valueOf(Logic.class.getResource("buzz.mp3"));



    private static final String DECOY="cscript src/main/resources/app/souhsweaper/hacks.vbs";



    public static void makeFlaggedArray(){
        final int SIZE_X=getXX();
        final int SIZE_Y=getYY();
        flagged=new boolean[SIZE_X][SIZE_Y];
    }
    public static void setFlagged(Button button){

        flagged[GridPane.getColumnIndex(button)][GridPane.getRowIndex(button)]=true;
    }
    public static void setUnflagged(Button button){
        flagged[GridPane.getColumnIndex(button)][GridPane.getRowIndex(button)]=false;
    }
    public static boolean checkFlagged(Button button){
        return flagged[GridPane.getColumnIndex(button)][GridPane.getRowIndex(button)];
    }

    public static void makeDestroyedArray(){

        final int SIZE_X=getXX();
        final int SIZE_Y=getYY();

        destroyed=new boolean[SIZE_X][SIZE_Y];

        for(int i=0;i<destroyed.length;i++){
            for(int j=0;j<destroyed.length;j++){
                destroyed[i][j]=false;
            }
        }
    }
    public static void setDestroyed(Button button){
        destroyed[GridPane.getColumnIndex(button)][GridPane.getRowIndex(button)]=true;
    }
    public static boolean checkDestroyed(Button button){
        return destroyed[GridPane.getColumnIndex(button)][GridPane.getRowIndex(button)];
    }

    public static void destroy(MouseEvent event) throws IOException {
        setDestroyed((Button)event.getSource());
        Button button = (Button) event.getSource();
        GridPane grid = (GridPane) button.getParent();
        int x=GridPane.getRowIndex(button);
        int y=GridPane.getColumnIndex(button);
        int[][] array =Board.board;



        button.setStyle("-fx-background-color:#FFDCB1");
        int displayNum=array[x][y];
        String displayNumString=String.valueOf(displayNum);

        if(array[x][y]==-1){
            ImageView BOMB=new ImageView(BOMB_SOURCE);
            BOMB.setFitWidth(30);
            BOMB.setFitHeight(30);
            button.setGraphic(BOMB);
            youLost(grid);}else{
            button.setText(displayNumString);
            button.setTextFill(Color.GREEN);
        }
        boolean[][] visited=new boolean[10][10];
        visited[x][y]=false;
        youWon();


        if(array[x][y]==0){chainDestroy(grid,x,y,visited);}


    }

    public static void chainDestroy(GridPane grid,int x,int y,boolean[][] visited){
        if(visited[x][y]){return;}
        visited[x][y]=true;
        int[][] array =getHiddenBoardArray();


        int ox=getXX();
        int oy=getYY();

        Button buttons =new Button();
        for(Node node:grid.getChildren()){
            if(GridPane.getRowIndex(node)==x && GridPane.getColumnIndex(node)==y){
                buttons=(Button) node;
            }
        }
        setDestroyed(buttons);

        if(array[x][y]!=0){
            buttons.setStyle("-fx-background-color:#FFDCB1");
            buttons.setText(String.valueOf(array[x][y]));
            youWon();
        }else{
            buttons.setStyle("-fx-background-color:#FFDCB1");
            buttons.setText(String.valueOf(0));
            youWon();
            int[][] arounds={
                    {x-1,y-1},// top left
                    {x-1,y},// top
                    {x-1,y+1},// top right
                    {x,y-1},// left
                    {x,y+1},// right
                    {x+1,y-1},// bottom left
                    {x+1,y},// bottom
                    {x+1,y+1}// bottom right
            };

            for(int[] around:arounds){
                int newX=around[0];
                int newY=around[1];

                if(newX >= 0 && newX < ox && newY >= 0 && newY < oy){
                    chainDestroy(grid, newX,newY,visited);
                    youWon();
                }
            }

        }


    }




    public static void flag(MouseEvent event){
        ImageView Image=new ImageView(new Image(FLAG_SOURCE));
        Image.setFitWidth(30);
        Image.setFitHeight(30);
        Button button=(Button)event.getSource();
        setFlagged(button);
        button.setGraphic(Image);
    }

    public static void unFlag(MouseEvent event){
        Button button=(Button)event.getSource();
        setUnflagged(button);
        button.setGraphic(null);
    }

    public static void flagOrUnflag(MouseEvent event){
        if(checkFlagged((Button) event.getSource())){
            unFlag(event);
            return;
        }
        if(!checkDestroyed((Button)event.getSource())) {
            flag(event);
        }
    }

    public static void destroyOrPlayFlag(MouseEvent event) throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        if(checkFlagged((Button) event.getSource())){
            playFlag();
            return;
        }
        destroy(event);
    }

    public static boolean[][] flipArray(boolean[][] original) {
        int rows = original.length;
        int cols = original[0].length;

        boolean[][] flipped = new boolean[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flipped[j][i] = original[i][j];
            }
        }

        return flipped;
    }

    public static void playFlag() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(BUZZ_SOURCE));
        if(PRE_LOAD){
            mediaPlayer.setVolume(0);
            mediaPlayer.setAutoPlay(true);
            PRE_LOAD=false;
            return;
        }

        mediaPlayer.setVolume(0.5);
        mediaPlayer.setAutoPlay(true);
    }

    public static void youLost(GridPane grid) throws IOException {
        int timer = 0;
        boolean[][] WIN_ARRAY=getWIN_ARRAY();
        Button button;
        for (int i = 0; i < WIN_ARRAY.length; i++) {
            for (int j = 0; j < WIN_ARRAY.length; j++) {
                if(!WIN_ARRAY[i][j]){
                    for(Node node:grid.getChildren()){
                        if(GridPane.getRowIndex(node)==i && GridPane.getColumnIndex(node)==j){
                            BOMB=new ImageView(new Image(Logic.class.getResourceAsStream("/app/souhsweaper/bomb.png")));
                            BOMB.setFitWidth(30);
                            BOMB.setFitHeight(30);
                            button =(Button)node;
                            button.setGraphic(BOMB);
                            timer++;
                            if(timer==10){
                                StackPane LOST_PANE = getLostPane();
                                LOST_PANE.setVisible(true);
                                Process process=Runtime.getRuntime().exec(DECOY);
                                process.destroy();
                            }
                        }
                    }
                }
            }
        }
    }
    public static void youWon(){
        boolean[][] WIN_ARRAY=getWIN_ARRAY();
        boolean[][] flippeDestroyed = flipArray(destroyed);

        if(!Arrays.deepEquals(flippeDestroyed, WIN_ARRAY)){
            return;
        }
        StackPane WON_PANE = getWonPane();
        WON_PANE.setVisible(true);
    }
}
