import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Home implements Initializable {

    @FXML
    public BorderPane contentPane;

    @FXML
    public GridPane menuPane;

    @FXML
    public GridPane mainPane;

    static Home homeController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeController = this;
    }

    /**
     * 主界面 "打开文件" 按钮响应事件
     * 显示打开文件界面
     */
    @FXML
    private void showOpenFilePane(){
        contentPane.setBackground(null);
        contentPane.setCenter(OpenFile.openFileController.openFilePane);
    }

    /**
     * 主界面 "显示图片" 按钮响应事件
     * 显示当前图片
     */
    @FXML
    private void showImagePane() {
        if (ImageProperty.isFirstLoading) {
            return;
        }
        if (ImageProperty.curImage == null) {
            Text t = new Text("没有数据\n请添加文件");
            t.setId("center-hint");
            Home.homeController.contentPane.setCenter(t);
        } else {
            Home.homeController.contentPane.setCenter(ShowImage.showImageController.imagePane);
        }
    }

    /**
     * 主界面 "桥接词" 按钮响应事件
     * 显示桥接词界面
     */
    @FXML
    public void showBridgeWordsPane() {
        contentPane.setBackground(null);
        contentPane.setCenter(ShowBridgeWords.showBridgeWordsController.showBridgeWordsPane);
    }

    /**
     * 主界面 "最短路径响应界面"
     * 显示最短路径界面
     */
    @FXML
    private void showShortestPathsPane() {
        contentPane.setBackground(null);
        contentPane.setCenter(ShowShortestPaths.showShortestPathsController.showShortestPathsPane);
    }

    static String pathString = "";

    @FXML
    public void showRandomWalk() {
        if (MainGui.graph == null || !ImageProperty.isAborted){
            return;
        }
        ImageProperty.isAborted = false;
        pathString = "";
        Iterator<String> it = MainGui.graph.randomWalkIterator();
        ImageProperty.fileCnt = 1;
        ImageProperty.curImageIndex = 0;
        ImageProperty.curMax = 1;
        String GUID = String.valueOf(System.currentTimeMillis());
        ImageProperty.loadedImages.clear();
        ImageProperty.loadedImages.add("origin");
        ImageProperty.loadedImages.add(GUID);

        ImageProperty.pre = false;
        ImageProperty.nxt = false;
        ImageProperty.stop = true;

        (new CheckImageTask("1")).start();

        Thread t = new Thread(() -> {
            while (it.hasNext()) {
                if (pathString.equals("")){
                    pathString = it.next();
                }else {
                    pathString = pathString + "->" + it.next();
                }
                ImageLoader loader = new ImageLoader(pathString);
                loader.start();
                try {
                    // 把等待间隔减小
                    sleep(1000);
                } catch (Exception e) {
                }
                if (ImageProperty.isAborted || !ImageProperty.loadedImages.contains(GUID)){
                    return;
                }
                ImageProperty.curImageIndex += 1;
                CheckImageTask task = new CheckImageTask(ImageProperty.curImageIndex.toString());
                task.start();
                ImageProperty.curMax = ImageProperty.curImageIndex;
            }
            ShowImage.showImageController.stopWalking();
        });
        t.setDaemon(true);
        t.start();
    }
}
