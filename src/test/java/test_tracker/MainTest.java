package test_tracker;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.LabeledMatchers;

public class MainTest {

//    public void start(Stage stage) throws Exception {
//        Parent mainNode = new Parent() {};
//        stage.setScene(new Scene(mainNode));
//        stage.show();
//        stage.toFront();
//    }

    @Test
    public void testA() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            new Main().start(new Stage()); // Create and
                            assert(DatabaseUtil.getRows( 1) != null);
                            assert(DatabaseUtil.getSections() != null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                    }
                });
            }
        });

        thread.start();// Initialize the thread
//        Thread.sleep(5000); // Time to use the app, with out this, the thread
        // will be killed before you can tell.
    }
}