import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.beans.binding.Bindings.createDoubleBinding;

public class ShowImage implements Initializable{
    @FXML
    public ImageView imageView;
    @FXML
    public Pane imagePane;
    @FXML
    public Label stopLabel;
    @FXML
    public Label previousLabel;
    @FXML
    public Label nextLabel;
    @FXML
    public ScrollPane imageScroller;
    @FXML
    public StackPane imageHolder;

    private Boolean isControlPressed = false;
    public static ShowImage showImageController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageHolder.minWidthProperty().bind(createDoubleBinding(() ->
                imageScroller.getViewportBounds().getWidth(),imageScroller.viewportBoundsProperty()));
        imagePane.setOnKeyPressed((KeyEvent event)->{
            if (event.getCode() == KeyCode.CONTROL){
                isControlPressed = true;
            }
        });
        imagePane.setOnKeyReleased((KeyEvent event)->{
            if (event.getCode() == KeyCode.CONTROL){
                isControlPressed = false;
            }
        });
        imageView.setOnScroll((ScrollEvent event)->{
           double delta = event.getTextDeltaY();
           if (isControlPressed == false){
               return;
           }
           if (delta > 0){
               imageView.setScaleX(imageView.getScaleX() + 0.05);
               imageView.setScaleY(imageView.getScaleY() + 0.05);
           }else{
               if (imageView.getBoundsInParent().getWidth() < imagePane.getWidth() * 1.2 &&
                       imageView.getBoundsInParent().getHeight() < imagePane.getHeight() * 1.2)
               {

                   return;
               }
               imageView.setScaleX(imageView.getScaleX() - 0.05);
               imageView.setScaleY(imageView.getScaleY() - 0.05);
           }
           event.consume();
        });
        showImageController = this;
    }
    @FXML
    public void showNextImage() {
        ImageProperty.curImageIndex += 1;
        ImageProperty.pre = true;
        ImageProperty.nxt = ImageProperty.curImageIndex < ImageProperty.curMax;
        ImageProperty.stop = false;

        CheckImageTask task = new CheckImageTask(ImageProperty.curImageIndex.toString());
        task.start();
    }

    @FXML
    public void showPreviousImage() {
        if (ImageProperty.curImageIndex < 1){
            return;
        }
        ImageProperty.curImageIndex -= 1;
        ImageProperty.pre = ImageProperty.curImageIndex != 1;
        ImageProperty.nxt = true;
        ImageProperty.stop = false;

        CheckImageTask task = new CheckImageTask(ImageProperty.curImageIndex.toString());
        task.start();
    }

    @FXML
    public void stopWalking() {
        ImageProperty.isAborted = true;
        ImageProperty.pre = ImageProperty.curImageIndex > 1;
        ImageProperty.nxt = false;
        ImageProperty.stop = false;
        CheckImageTask task = null;
        if (ImageProperty.curImageIndex == 0){
            task = new CheckImageTask("origin");
        }else{
            task = new CheckImageTask(ImageProperty.curImageIndex.toString());
        }
        task.start();

    }

    public void saveToFile(MouseEvent event) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("图片另存为");
        chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg"));

        File outputFile = chooser.showSaveDialog(((Node)(event.getSource())).getScene().getWindow());
        if (outputFile == null){
            return;
        }
        String extension = outputFile.getName().substring(outputFile.getName().lastIndexOf('.') + 1);
        BufferedImage bImage = SwingFXUtils.fromFXImage(ImageProperty.curImage, null);
        try {
            ImageIO.write(bImage, extension, outputFile);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatino");
            alert.setHeaderText(null);
            alert.setContentText("图片保存完成");
            alert.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
