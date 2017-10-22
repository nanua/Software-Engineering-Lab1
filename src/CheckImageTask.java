import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class CheckImageTask extends Thread {
    public CheckImageTask(String imgName) {
        this.imgName = imgName;
        this.setDaemon(true);
    }

    private final String imgName;

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
                // 把等待间隔减小
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
