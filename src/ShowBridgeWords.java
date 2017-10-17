import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import sun.applet.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.beans.binding.Bindings.createDoubleBinding;

public class ShowBridgeWords implements Initializable {
    public static ShowBridgeWords showBridgeWordsController;

    @FXML
    public Pane showBridgeWordsPane;
    @FXML
    public Label bridgeWordsLabel;
    @FXML
    public TextField word1TextField;
    @FXML
    public TextField word2TextField;
    @FXML
    public TextArea newTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showBridgeWordsController = this;
    }

    @FXML
    public void displayBridgeWords() {
        if (MainGui.graph == null){
            return;
        }
        String word1 = word1TextField.getText();
        String word2 = word2TextField.getText();
        List<String> bridgeWords = MainGui.graph.findBridgeWords(word1,word2);
        if (bridgeWords.size() == 0){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("没有桥接词！");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(MainGui.graph.queryBridgeWords(word1,word2));
        alert.showAndWait();

        ImageProperty.pre = false;
        ImageProperty.nxt = false;
        ImageProperty.stop = false;
        ImageProperty.loadedImages.clear();
        ImageProperty.loadedImages.add("origin");


        Thread t = new Thread(()->{
            MainGui.graph.writeBridgeWordsGraphFile(bridgeWords,"red", word1 ,word2);
            MainGui.graph.generateImage("BridgeWords.dot","BridgeWords.png");
            ImageProperty.curImage = new Image("file:BridgeWords.png");
            ImageProperty.loadedImages.add("BridgeWords");
        });
        t.start();
        CheckImageTask task = new CheckImageTask("BridgeWords");
        task.start();

    }

    public void displayNewText() {
        if (MainGui.graph == null)
        {
            return;
        }
        String text = newTextArea.getText();
        if(text == null){
            return;
        }
        String[] orgText = text.replaceAll("(\\W|\\d)+"," ")
                .toLowerCase()
                .split(" +");
        String[] newTexts = MainGui.graph.generateNewText(text).split(" ");
        TextFlow textFlow = new TextFlow();
        int i = 0,j = 0;
        while(j != newTexts.length){
            Text t = new Text(newTexts[j] + " ");
            t.setFont(new Font("Arial Black", 23));
            if (orgText[i].equals(newTexts[j])){
                textFlow.getChildren().add(t);
                ++i; ++j;
            }
            else{
                t.setFill(Color.RED);
                textFlow.getChildren().add(t);
                ++j;
                continue;
            }
        }

        textFlow.setTextAlignment(TextAlignment.CENTER);

        textFlow.prefWidthProperty().bind(Home.homeController.contentPane.widthProperty());
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(textFlow);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToHeight(true);

        Home.homeController.contentPane.setCenter(scrollPane);

    }
}
