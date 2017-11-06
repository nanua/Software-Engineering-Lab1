import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class TestCase2 {
    @Test
    public void testQueryBridgeWords(){
        String[] words = new String[]{
                "one",
                "bridge",
                "word",
                "test"
        };
        Graph g = new Graph(words);
        String res = g.queryBridgeWords("bridge","test");
        assertEquals("The bridge word from \"bridge\" to \"test\" is: word", res);
    }
}
