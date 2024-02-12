import AdventureModel.AdventureGame;
import javafx.application.Application;
import javafx.stage.Stage;
import views.AdventureGameView;

import java.io.IOException;

/**
 * Class AdventureGameApp.
 */
public class AdventureGameApp extends  Application {

    AdventureGame model;
    AdventureGameView view;

    public static void main(String[] args) {
        launch(args);
    }

    /*
    * JavaFX is a Framework, and to use it we will have to
    * respect its control flow!  To start the game, we need
    * to call "launch" which will in turn call "start" ...
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // this.model = new AdventureGame("AdventureGame", "VisualGame"); // TODO: Uncomment if opening in <group_89>
        this.model = new AdventureGame("VisualGame"); // TODO: Uncomment if opening in <AdventureGame>
        this.view = new AdventureGameView(model, primaryStage);
    }

}
