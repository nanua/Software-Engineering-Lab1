import javafx.scene.image.Image;

import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ImageProperty {
    public static Boolean pre = false;
    public static Boolean nxt = false;
    public static Boolean stop = false;
    public static volatile Image curImage;
    public static HashSet<String> loadedImages = new HashSet<>();
    public static int curMax = 0;
    public static Integer fileCnt = 0;
    public static Integer curImageIndex = 0;
    public static volatile Boolean isAborted = true;
    public static Boolean isTooLarge = false;
    public static Boolean isFirstLoading = true;
    public static Lock cntLock = new ReentrantLock(true);
    public static Lock loadedLock = new ReentrantLock(true);
    public static void LoadImage(){
        ShowImage.showImageController.previousLabel.setVisible(pre);
        ShowImage.showImageController.nextLabel.setVisible(nxt);
        ShowImage.showImageController.stopLabel.setVisible(stop);
        ShowImage.showImageController.imageView.setImage(ImageProperty.curImage);
        if (isFirstLoading){
            isFirstLoading = false;
            double scale = Home.homeController.contentPane.getWidth() /
                    ShowImage.showImageController.imageView.getBoundsInParent().getWidth();
            ShowImage.showImageController.imageView.setScaleX(scale);
            ShowImage.showImageController.imageView.setScaleY(scale);
        }
        Home.homeController.contentPane.setCenter(ShowImage.showImageController.imagePane);
    }


}
