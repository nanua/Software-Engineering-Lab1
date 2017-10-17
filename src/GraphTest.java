import org.junit.Test;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class GraphTest {
    @Test
    public void randomWalkIterator() throws Exception {
        String text = "To explore strange new worlds,\n" +
                "To seek out new life and new civilizations";
        Input2Graph t = new Input2Graph(text);
        Graph g = t.translate(text);
        Iterator<String> tarIte = g.randomWalkIterator();
        while (tarIte.hasNext()) {
            System.out.println(tarIte.next());
        }
    }

    @Test
    public void generateNewText() throws Exception {
        String text = "To explore strange new worlds,\n" +
                "To seek out new life and new civilizations";
        Input2Graph t = new Input2Graph(text);
        Graph g = t.translate(text);
        System.out.println(g.generateNewText("Seek to explore new and " +
                "exciting synergies"));
    }

    @Test
    public void addWord() throws Exception {
        Graph g = new Graph();
        g.addWord("abc");
    }

    @Test
    public void addWords() throws Exception {
        String[] testInput = {};
        Graph g = new Graph();
        g.addWords(testInput);
        testInput = new String[]{"abc", "def"};
        g.addWords(testInput);
        testInput = new String[]{"a", "a"};
        g.addWords(testInput);
    }

    @Test
    public void writeGraphFile() throws Exception {
        Graph g = new Graph();
        String[] inputWord = {"a"};
        g.addWords(inputWord);
    }

    @Test
    public void findBridgeWords() throws Exception {
        Graph g = new Graph();
        String[] inputWord = {"a", "b", "c", "a", "c", "c"};
        g.addWords(inputWord);
        System.out.println(g.findBridgeWords("a", "c"));
    }

    @Test
    public void testFindShortestPath() throws Exception {
        Graph g = new Graph();

        g.addWordsTest("s", "v1");
        g.addWordsTest("s", "v1");

        g.addWordsTest("s", "v2");
        g.addWordsTest("s", "v2");
        g.addWordsTest("s", "v2");

        g.addWordsTest("s", "v3");
        g.addWordsTest("s", "v3");
        g.addWordsTest("s", "v3");
        g.addWordsTest("s", "v3");

        g.addWordsTest("v1", "v2");
        g.addWordsTest("v1", "v2");
        g.addWordsTest("v1", "v2");

        g.addWordsTest("v1", "v4");
        g.addWordsTest("v1", "v4");

        g.addWordsTest("v2", "v4");
        g.addWordsTest("v2", "v4");

        g.addWordsTest("v2", "v5");
        g.addWordsTest("v2", "v5");

        g.addWordsTest("v3", "v5");

        g.addWordsTest("v4", "v5");

        g.addWordsTest("v4", "T");
        g.addWordsTest("v4", "T");
        g.addWordsTest("v4", "T");

        g.addWordsTest("v5", "T");
        g.addWordsTest("v5", "T");
        g.addWordsTest("v5", "T");
        g.addWordsTest("v5", "T");
        g.addWordsTest("v5", "T");
        /*
        g.addWordsTest("s", "v1");
        g.addWordsTest("s", "v2");
        g.addWordsTest("v1", "T");
        g.addWordsTest("v2", "T");

        */
        System.out.println(g.queryBridgeWords("s","v4"));
    }
    @Test
    public void ttt() throws Exception{
        File[] files = new File(".").listFiles();
        List<File> fs = Arrays.asList(files);
        fs.forEach(file -> System.out.println(file.toPath()));
    }
}