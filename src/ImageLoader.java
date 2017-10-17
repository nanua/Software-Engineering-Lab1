public class ImageLoader extends Thread {
    public ImageLoader(String path){
        this.setDaemon(true);
        this.path = path;
    }
    String path;
    @Override
    public void run() {
        ImageProperty.cntLock.lock();
        String curFileName = ImageProperty.fileCnt.toString();
        ImageProperty.fileCnt += 1;
        ImageProperty.cntLock.unlock();

        MainGui.graph.writePathGraphFile(path, "red", curFileName + ".dot");
        MainGui.graph.generateImage(curFileName + ".dot", curFileName + ".png");

        ImageProperty.loadedLock.lock();
        ImageProperty.loadedImages.add(curFileName);
        ImageProperty.loadedLock.unlock();
    }
}
