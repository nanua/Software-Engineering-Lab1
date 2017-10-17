import java.io.*;
import java.nio.file.Files;

public class Input2Graph {


    public Input2Graph(){}
    public Input2Graph(String path){
        this.file = new File(path);
    }
    public Input2Graph(File file){
        this.file = file;
    }

    /**
     * 重新设置文本文件路径,并设置this.file
     * @param path 文本文件路径
     */
    public void setPath(String path){
        this.file = new File(path);
    }

    /**
     * 获取正在使用的文件路径
     * @return 如果this.file存在，返回绝对路径，否则返回null
     */
    public String getPath(){
        if (this.file != null && this.file.exists())
            return this.file.getAbsolutePath();
        else
            return null;
    }

    /**
     * 获取处理过的
     * @return
     */
    public String getDealtText(){
        return dealtText;
    }

    /**
     * 使用默认的文件输入，转化为Graph
     * @return 转化后的Graph
     */
    public Graph translate(){
        String text = null;
        try{
            text = String.join(" ", Files.readAllLines(this.file.toPath()));
        }catch (Throwable e){
            throw new IllegalArgumentException(e.getMessage());
        }
        return translate(text);
    }

    /**
     * 使用给定输入text转化为Graph类型
     * @param text
     * @return 转化后的Graph
     */
    public Graph translate(String text){
        Graph g = new Graph();
        String[] words = text.replaceAll("(\\W|\\d)+"," ")
                             .toLowerCase()
                             .split(" +");
        dealtText = String.join(" ",words);
        g.addWords(words);
        return g;
    }

    /**文本文件*/
    private File file = null;
    /**文本处理结果*/
    private String dealtText = null;


}
