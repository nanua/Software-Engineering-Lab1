import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class TestCase1 {
    @Test
    public void testQueryBridgeWords(){
        String[] words = new String[]{
          "no",
          "bridge",
          "words",
          "test"
        };
        Graph g = new Graph(words);
        String res = g.queryBridgeWords("bridge","words");
        assertEquals("No bridge words from \"bridge\" to \"words\" !", res);
    }
}
