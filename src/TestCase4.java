import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class TestCase4 {
    @Test
    public void testQueryBridgeWords(){
        String[] words = new String[]{
                "word",
                "not",
                "exist",
                "test"
        };
        Graph g = new Graph(words);
        String res = g.queryBridgeWords("a","b");
        assertEquals("No bridge words from \"a\" to \"b\" !", res);
    }
}
