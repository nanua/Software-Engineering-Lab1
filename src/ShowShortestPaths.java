import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ShowShortestPaths implements Initializable {
    public static ShowShortestPaths showShortestPathsController;
    @FXML
    public Pane showShortestPathsPane;
    @FXML
    public TextField srcTextField;
    @FXML
    public TextField desTextField;

    @FXML
    public TextArea shortestPathsResultTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showShortestPathsController = this;
    }

    public void displayShortestPaths() {
        if (MainGui.graph == null) {
            return;
        }
        String GUID = String.valueOf(System.currentTimeMillis());
        ImageProperty.loadedImages.clear();
        ImageProperty.loadedImages.add("origin");
        ImageProperty.loadedImages.add(GUID);
        ImageProperty.curMax = 0;
        ImageProperty.fileCnt = 1;

        String srcName = srcTextField.getText();
        String desName = desTextField.getText();
        shortestPathsResultTextField.clear();
        ImageProperty.pre = false;
        ImageProperty.stop = false;

        if (desName.equals("")) {
            HashMap<String, String> res = MainGui.graph.findShortestPaths(srcName);
            if (res == null || res.isEmpty()) {
                shortestPathsResultTextField.setText("没有结果");
            } else {
                ImageProperty.nxt = (res.size() > 1);
                String text = String.join("\n", res.values());
                shortestPathsResultTextField.setText(text);
                ImageProperty.curMax = res.size();
                Thread thread = new Thread(() -> {
                    for (String path : res.values()) {
                        if (!ImageProperty.loadedImages.contains(GUID)){
                            return;
                        }
                        Thread t = new ImageLoader(path);
                        t.setDaemon(true);
                        t.start();
                        try {
                            t.join();
                        } catch (Exception e) {
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();

                CheckImageTask task = new CheckImageTask("1");
                ImageProperty.curImageIndex = 1;
                task.start();
            }
        } else {
            List<String> res = MainGui.graph.findShortestPaths(srcName, desName);
            if (res == null || res.isEmpty()) {
                shortestPathsResultTextField.setText("没有从 " + srcName + " 到" + desName + " 的路径");
            } else {
                ImageProperty.nxt = (res.size() > 1);
                String text = String.join("\n", res);
                shortestPathsResultTextField.setText(text);
                ImageProperty.curMax = res.size();
                Thread thread = new Thread(() -> {
                    for (String path : res) {
                        if (!ImageProperty.loadedImages.contains(GUID)){
                            return;
                        }
                        Thread t = new ImageLoader(path);
                        t.setDaemon(true);
                        t.start();
                        try {
                            t.join();
                        } catch (Exception e) {
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
                CheckImageTask task = new CheckImageTask("1");
                task.start();
            }
        }
    }
}
