import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import sun.applet.Main;

public class OpenFile implements Initializable{
    @FXML
    public AnchorPane openFilePane;
    @FXML
    public TextField filePathTextField;
    @FXML
    public TextArea dealtTextArea;

    public static OpenFile openFileController;
    private File wordsFile;


    @FXML
    public void chooseFile(MouseEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("打开文件");
        chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        wordsFile = chooser.showOpenDialog(((Node)(e.getSource())).getScene().getWindow());
        if (wordsFile != null){
            try {
                filePathTextField.setText(wordsFile.getAbsolutePath().toString());
                Input2Graph translator = new Input2Graph(wordsFile);
                MainGui.graph = translator.translate();
                dealtTextArea.setText(translator.getDealtText());
                ImageProperty.loadedImages.clear();

                Thread t = new Thread(() -> {
                    MainGui.graph.writeOriginGraphFile();
                    MainGui.graph.generateImage("origin.dot", "origin.png");
                    ImageProperty.curImage = new Image("file:origin.png");
                    ImageProperty.loadedImages.add("origin");
                });
                t.start();

                CheckImageTask checkImageTask = new CheckImageTask("origin");
                checkImageTask.start();
            }catch (Exception exception){
                filePathTextField.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("加载文件异常，请检查文件类型");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openFileController = this;
    }
}
