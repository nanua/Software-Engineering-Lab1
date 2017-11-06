import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlackBoxTest {
    private Graph labOneTestGraph;

    @Before
    public void setUp() throws Exception {
        String labOneTestString = "In the big data time, servitization becomes one of the important # development trends - of the IT world.\n" +
                "More and more software resources are developed and existed in the format of services in Internet.\n" +
                "By the study of the RESEP approach, the meaning of this word.";
        Input2Graph input2Graph = new Input2Graph();
        this.labOneTestGraph = input2Graph.translate(labOneTestString);

    }

    @Test
    public void queryBridgeWords1() throws Exception {
        String word1 = null;
        String word2 = null;
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"null\" to \"null\" !", result);
    }


    @Test
    public void queryBridgeWords2() throws Exception {
        String word1 = null;
        String word2 = "the";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"null\" to \"the\" !", result);
    }


    @Test
    public void queryBridgeWords3() throws Exception {
        String word1 = "important";
        String word2 = "trends";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("The bridge word from \"important\" to \"trends\" is: development", result);
    }


    @Test
    public void queryBridgeWords4() throws Exception {
        String word1 = "the";
        String word2 = "by";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"the\" to \"by\" !", result);
    }


    @Test
    public void queryBridgeWords5() throws Exception {
        String word1 = "the";
        String word2 = "of";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("The bridge words from \"the\" to \"of\" are: study, meaning and format", result);
    }


    @Test
    public void queryBridgeWords6() throws Exception {
        String word1 = "wangwei";
        String word2 = "zhongyuhong";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"wangwei\" to \"zhongyuhong\" !", result);
    }


    @Test
    public void queryBridgeWords7() throws Exception {
        String word1 = "wangwei";
        String word2 = "the";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"wangwei\" to \"the\" !", result);
    }


    @Test
    public void queryBridgeWords8() throws Exception {
        Input2Graph input2Graph = new Input2Graph();
        Graph graph = input2Graph.translate("");
        String word1 = "the";
        String word2 = "of";
        String result = graph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"the\" to \"of\" !", result);
    }


    @Test
    public void queryBridgeWords9() throws Exception {
        Input2Graph input2Graph = new Input2Graph();
        Graph graph = input2Graph.translate("the the");
        String word1 = "the";
        String word2 = "the";
        String result = graph.queryBridgeWords(word1, word2);
        assertEquals("The bridge word from \"the\" to \"the\" is: the", result);
    }


    @Test
    public void queryBridgeWords10() throws Exception {
        Input2Graph input2Graph = new Input2Graph();
        Graph graph = input2Graph.translate("the if the");
        String word1 = "the";
        String word2 = "if";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"the\" to \"if\" !", result);
    }


    @Test
    public void queryBridgeWords11() throws Exception {
        String word1 = "王巍";
        String word2 = "钟宇宏";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"王巍\" to \"钟宇宏\" !", result);
    }


    @Test
    public void queryBridgeWords12() throws Exception {
        String word1 = "\77\77";
        String word2 = "\77\76";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"\77\77\" to \"\77\76\" !", result);
    }


    @Test
    public void queryBridgeWords13() throws Exception {
        String word1 = "the";
        String word2 = "the";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"the\" to \"the\" !", result);
    }


    @Test
    public void queryBridgeWords14() throws Exception {
        String word1 = "";
        String word2 = "";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"\" to \"\" !", result);
    }


    @Test
    public void queryBridgeWords15() throws Exception {
        String word1 = "";
        String word2 = "a";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"\" to \"a\" !", result);
    }


    @Test
    public void queryBridgeWords16() throws Exception {
        String word1 = "a";
        String word2 = "b";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"a\" to \"b\" !", result);
    }


    @Test
    public void queryBridgeWords17() throws Exception {
        String word1 = "a";
        String word2 = "ab";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"a\" to \"ab\" !", result);
    }


    @Test
    public void queryBridgeWords18() throws Exception {
        String word1 = "ab";
        String word2 = "cd";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"ab\" to \"cd\" !", result);
    }


    @Test
    public void queryBridgeWords19() throws Exception {
        String word1 = "a王";
        String word2 = "the";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"a王\" to \"the\" !", result);
    }


    @Test
    public void queryBridgeWords20() throws Exception {
        String word1 = "a王";
        String word2 = "b巍";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"a王\" to \"b巍\" !", result);
    }


    @Test
    public void queryBridgeWords21() throws Exception {
        String word1 = "a\77";
        String word2 = "the";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"a\77\" to \"the\" !", result);
    }


    @Test
    public void queryBridgeWords22() throws Exception {
        String word1 = "a\77";
        String word2 = "b\77";
        String result = labOneTestGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from \"a\77\" to \"b\77\" !", result);
    }


    @Test
    public void queryBridgeWords23() throws Exception {
        Input2Graph input2Graph = new Input2Graph();

        Graph graph = input2Graph.translate("is is");
        String word1 = "is";
        String word2 = "is";
        String result = graph.queryBridgeWords(word1, word2);
        assertEquals("The bridge word from \"is\" to \"is\" is: is", result);
    }

}