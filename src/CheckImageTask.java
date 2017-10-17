import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class CheckImageTask extends Thread {
    public CheckImageTask(String imgName) {
        this.imgName = imgName;
        this.setDaemon(true);
    }

    private String imgName;

    @Override
    public void run() {
        if (!ImageProperty.loadedImages.contains(imgName)) {
            Text t = new Text("Loading");
            t.setId("center-hint");
            Platform.runLater(() -> Home.homeController.contentPane.setCenter(t));
        }
        while (true) {
            if (ImageProperty.loadedImages.contains(imgName)) {
                ImageProperty.curImage = new Image("file:" + imgName + ".png");
                Platform.runLater(() -> ImageProperty.LoadImage());
                return;
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
