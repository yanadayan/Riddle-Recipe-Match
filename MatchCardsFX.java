package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import application.view.SceneManager;
import javafx.scene.image.Image;
public class MatchCardsFX extends Application {
    @Override
    
    public void start(Stage primaryStage) {
    	 Image logo = new Image(getClass().getResource("/resources/RRMLOGO.png").toExternalForm());
    	    primaryStage.getIcons().add(logo);
        
        Font.loadFont(getClass().getResource("/application/PressStart2P-Regular.ttf").toExternalForm(), 12);
        SceneManager sceneManager = new SceneManager(primaryStage);
        primaryStage.setWidth(900);
        primaryStage.setHeight(650);
        sceneManager.showHomePageScene();
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }

}