import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCase3 {
    @Test
    public void testQueryBridgeWords(){
        String[] words = new String[]{
                "multiple",
                "bridge",
                "words",
                "test",
                "bridge",
                "second",
                "test",
                "bridge",
                "third",
                "test"
        };
        Graph g = new Graph(words);
        String res = g.queryBridgeWords("bridge","test");
        assertTrue("The bridge words from \"bridge\" to \"test\" are: words, second and third".equals(res) ||
                "The bridge words from \"bridge\" to \"test\" are: second, words and third".equals(res) ||
                "The bridge words from \"bridge\" to \"test\" are: third, words and second".equals(res) ||
                "The bridge words from \"bridge\" to \"test\" are: words, third and second".equals(res) ||
                "The bridge words from \"bridge\" to \"test\" are: third, second and words".equals(res) ||
                "The bridge words from \"bridge\" to \"test\" are: second, third and words".equals(res) );
    }
}
