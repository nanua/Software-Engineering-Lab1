import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author ZhongYuhong 1151200119
 * @author WangWei 1150340114
 * 有向图类 ：
 */
public class Graph {
    /**
     * 有向图中的节点
     * 记录指向的点和边的权重
     * 记录有边指向自己的点
     */
    private class Node {

        public Node(String name) {
            this.name = name;
        }

        private HashMap<String, Integer> name2pointerIndex = new HashMap<>();
        private HashSet<String> weakPointers = new HashSet<>();
        private ArrayList<Integer> weights = new ArrayList<>();
        private ArrayList<Node> strongPointers = new ArrayList<>();
        private String name;

        public void insertStrongPointer(String name) {
            if (name2pointerIndex.containsKey(name)) {
                Integer nameIndex = name2pointerIndex.get(name);
                Integer nameWeight = weights.get(nameIndex);
                weights.set(nameIndex, nameWeight + 1);
            } else {
                strongPointers.add(name2node.get(name));
                weights.add(1);
                name2pointerIndex.put(name, weights.size() - 1);
            }
        }

        public void insertWeakPointer(String name) {
            if (!weakPointers.contains(name)) {
                weakPointers.add(name);
            }
        }
    }

    /**
     * 两个最短路径中使用的的优先队列中的节点
     */
    private class NodeInQueue {
        /**
         * 节点名称，唯一确定一个节点
         */
        private String name;
        /**
         * 记录最短路径中这个节点的上一个节点用于回溯
         */
        private NodeInQueue from;
        /**
         * 当前节点的权值，在不同的算法中它的定义不同
         */
        private Integer weight;
        /**
         * A*算法中的已走过的路径的权值标记，避免与当前节点权值混淆
         */
        private Integer orgWeight;

        public NodeInQueue(String name, NodeInQueue from, Integer weight, QueueType t) {
            this.name = name;
            this.from = from;
            if (t == QueueType.A_STAR) {
                ArrayList<Integer> weightList = name2node.get(name).weights;
                if (weightList.isEmpty()) {
                    this.weight = weight;
                } else {
                    this.orgWeight = weight;
                    this.weight = weight + Collections.min(weightList);
                }
            } else {
                this.weight = weight;
            }
        }

        /**
         * 两个NodeInQueue对象不同的定义是它们的name属性不同
         *
         * @return hashcode base on String 'name'
         */
        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.hashCode() == obj.hashCode();
        }
    }

    /**
     * 随机游走迭代器
     */
    private class WalkIterator implements Iterator<String> {
        private HashSet<String> edgeSet;
        private String nextNode;
        private String curNode;
        private boolean isFirst = true;

        WalkIterator() {
            this.edgeSet = new HashSet<>();
            if (name2node.isEmpty()) {
                curNode = null;
                nextNode = null;
                isFirst = false;
            } else {
                List nameList = Arrays.asList(name2node.keySet().toArray());
                Collections.shuffle(nameList);
                curNode = (String) nameList.get(0);
                setNextNode();
            }
        }

        @Override
        public boolean hasNext() {
            return nextNode != null || this.isFirst;
        }

        @Override
        public String next() {
            if (this.isFirst) {
                this.isFirst = false;
                return this.curNode;
            }
            if (!hasNext()) {
                return null;
            } else {
                edgeSet.add(String.format("%s->%s", curNode, nextNode));
                curNode = nextNode;
                setNextNode();
                return curNode;
            }
        }

        private void setNextNode() {
            List adjNameList = Arrays.asList(name2node.get(curNode)
                    .name2pointerIndex.keySet().toArray());
            if (adjNameList.isEmpty()) {
                nextNode = null;
            } else {
                Collections.shuffle(adjNameList);
                String adjName = (String) adjNameList.get(0);
                if (!edgeSet.contains(String.format("%s->%s", curNode, adjName))) {
                    nextNode = adjName;
                } else {
                    nextNode = null;
                }
            }
        }
    }

    public Graph() {
    }

    public Graph(String[] words) {
        addWords(words);
    }

    /**
     * 节点名称到节点实例映射
     */
    private HashMap<String, Node> name2node = new HashMap<>();

    /**
     * Graph初始化变量，记录addWord方法
     */
    private Node lastNode = null;
    private String lastName = null;

    /**
     * 绘图相关变量
     * nodes ： 节点
     * arrows ： 有向边
     * isGraphUpdated ： nodes 和 arrows 是否对应了最新的的图
     * OPENER CLOSER ： dot文件头和文件尾
     */
    private List<String> nodes = null;
    private List<String> arrows = null;
    private Boolean isGraphUpdated = false;
    final static String OPENER = "digraph g{\ngraph[dpi=200];\nbgcolor=\"gray68\";\n";
    final static String CLOSER = "\n}";

    /**
     * 标志NodeInQueue对象对应何种算法，不同算法中weight属性的意义不同
     */
    private enum QueueType {
        A_STAR, DIJKSTRA
    }

    /**
     * 向有向图中插入单词
     *
     * @param name 单词
     * @return 单词是否插入成功，只有当单词为Null或空串时会失败
     */
    public Boolean addWord(String name) {
        if (name == null || name.equals("")) {
            return false;
        }

        isGraphUpdated = false;

        Node curNode = name2node.get(name);
        if (curNode == null) {
            curNode = new Node(name);
            name2node.put(name, curNode);
        }

        if (lastNode != null) {
            lastNode.insertStrongPointer(name);
            curNode.insertWeakPointer(lastName);
        }
        lastName = name;
        lastNode = curNode;
        return true;
    }

    /**
     * 多次调用addWrod方法插入多个单词
     *
     * @param words 多个单词，Array类型
     * @return 插入是否成功，有一次或多次addWord返回false，该函数返回false
     */
    public Boolean addWords(String[] words) {
        Boolean ret = true;
        for (String word : words) {
            ret = addWord(word) ? ret : false;
        }
        return ret;
    }

    /**
     * 获取nodes和arrows元素，便于写入dot文件
     */
    public void generateOriginGraphElements() {
        nodes = new LinkedList<>();
        arrows = new LinkedList<>();
        if (name2node.size() != 1) {
            for (String curName : this.name2node.keySet()) {
                nodes.add("\"" + curName + "\"");
                Node curNode = this.name2node.get(curName);
                for (Node adjNode : curNode.strongPointers) {
                    String desName = adjNode.name;
                    arrows.add(String.format("\"%s\"->\"%s\"[label=%d]", curName, desName,
                            curNode.weights.get(curNode.name2pointerIndex.get(desName))));
                }
            }
        }
        isGraphUpdated = true;
    }

    /**
     * 写入原始有向图dot文件
     */
    public void writeOriginGraphFile() {
        if (!isGraphUpdated) {
            generateOriginGraphElements();
        }
        Path graphFile = Paths.get(".", "origin.dot");
        Charset charset = Charset.forName("ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(graphFile, charset, StandardOpenOption.CREATE
                , StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(OPENER);
            writer.write(String.join(";\n", nodes));
            writer.write(";\n");
            writer.write(String.join(";\n", arrows));
            writer.write(CLOSER);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在初始在有向图的基础上高亮一条路径
     *
     * @param path     路径
     * @param color    高亮颜色
     * @param fileName 高亮dot文件的名称
     */
    public void writePathGraphFile(String path, String color, String fileName) {
        // 如果有新的addWord调用，则更新有向图元素
        if (!isGraphUpdated) {
            generateOriginGraphElements();
        }
        Path graphFile = Paths.get(".", fileName);
        Charset charset = Charset.forName("ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(graphFile, charset, StandardOpenOption.CREATE
                , StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(OPENER);
            for (String node : nodes) {
                String temp = node.replaceAll("\"", "");
                Pattern pattern = Pattern.compile("(^|.*>)" + temp + "(-.*|$)");
                Matcher matcher = pattern.matcher(path);
                writer.write(matcher.matches() ?
                        node + "[style=filled color=" + color + "];\n" : node + ";\n");
            }
            for (String arrow : arrows) {
                if (!path.contains("->")) {
                    path = "$$$";
                }
                String temp = arrow.replaceAll("\\[.*", "").replaceAll("\"", "");
                Pattern pattern = Pattern.compile("(^|.*>)" + temp + "(-.*|$)");
                Matcher matcher = pattern.matcher(path);
                writer.write(matcher.matches() ?
                        arrow + "[color=" + color + "];" : arrow + ";\n");
            }
            writer.write(CLOSER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 高亮桥接词dot文件
     *
     * @param bridgeWords 桥接器
     * @param color       高亮颜色
     */
    public void writeBridgeWordsGraphFile(List<String> bridgeWords, String color, String word1, String word2) {
        if (!isGraphUpdated) {
            generateOriginGraphElements();
        }
        Path graphFile = Paths.get(".", "BridgeWords.dot");
        Charset charset = Charset.forName("ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(graphFile, charset, StandardOpenOption.CREATE
                , StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(OPENER);
            for (String bridgeWord : bridgeWords) {
                writer.write("\"" + bridgeWord + "\"" + "[style=filled color=" + color + "];\n");
            }
            writer.write("\"" + word1 + "\"" + "[style=filled color=yellow];\n");
            writer.write("\"" + word2 + "\"" + "[style=filled color=yellow];\n");
            writer.write(String.join(";\n", nodes));
            writer.write(";\n");
            writer.write(String.join(";\n", arrows));
            writer.write(CLOSER);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找桥接词
     *
     * @param srcName word1
     * @param tarName word2
     * @return 两点之间的桥接词
     */
    public List<String> findBridgeWords(String srcName, String tarName) {
        Node srcNode = this.name2node.get(srcName);
        Node tarNode = this.name2node.get(tarName);
        if (srcNode == null || tarNode == null) {
            return new LinkedList<>();
        }
        return srcNode.name2pointerIndex.keySet()
                .stream()
                .filter((String adjName) -> (tarNode.weakPointers.contains(adjName)))
                .collect(Collectors.toList());
    }

    /**
     * 单源最短路径
     *
     * @param srcName 源点
     * @return Map类型的最短路径
     */
    public HashMap<String, String> findShortestPaths(String srcName) {
        if (!name2node.containsKey(srcName)) {
            return new HashMap<>();
        }
        HashMap<String, String> paths = new HashMap<>();
        PriorityQueue<NodeInQueue> pq = new PriorityQueue<>((NodeInQueue n1, NodeInQueue n2)
                -> (Integer.compare(n1.weight, n2.weight)));
        pq.add(new NodeInQueue(srcName, null, 0, QueueType.DIJKSTRA));
        HashSet<NodeInQueue> added = new HashSet<>();
        while (!pq.isEmpty()) {
            NodeInQueue curNodeInQueue = pq.poll();
            if (added.contains(curNodeInQueue)) {
                continue;
            }
            added.add(curNodeInQueue);
            Node curNode = name2node.get(curNodeInQueue.name);
            for (int i = 0; i < curNode.strongPointers.size(); i++) {
                pq.add(new NodeInQueue(curNode.strongPointers.get(i).name, curNodeInQueue,
                        curNode.weights.get(i), QueueType.DIJKSTRA));
            }
        }
        for (NodeInQueue node : added) {
            NodeInQueue thisNode = node;
            String path = node.name;
            node = node.from;
            while (node != null) {
                path = String.format("%s->%s", node.name, path);
                node = node.from;
            }
            paths.put(thisNode.name, path);
        }
        return paths;
    }

    /**
     * 两点之间的所有最短路径
     *
     * @param srcName 源点
     * @param tarName 终点
     * @return 最短路径列表
     */
    public List<String> findShortestPaths(String srcName, String tarName) {
        if (!name2node.containsKey(srcName) || !name2node.containsKey(tarName)) {
            return new ArrayList<>();
        }
        List<String> results = new ArrayList<>();
        Integer minWeight = 0;
        PriorityQueue<NodeInQueue> pq = new PriorityQueue<>((NodeInQueue n1, NodeInQueue n2) ->
                (Integer.compare(n1.weight, n2.weight)));
        pq.add(new NodeInQueue(srcName, null, 0, QueueType.A_STAR));
        while (true) {
            while (!pq.isEmpty() && !pq.peek().name.equals(tarName)) {
                NodeInQueue curNode = pq.poll();
                Node curNodeInGraph = name2node.get(curNode.name);
                for (int i = 0; i < curNodeInGraph.weights.size(); ++i) {
                    pq.add(new NodeInQueue(curNodeInGraph.strongPointers.get(i).name,
                            curNode, curNode.orgWeight + curNodeInGraph.weights.get(i), QueueType.A_STAR));
                }
            }
            if (pq.isEmpty()) {
                return results;
            }
            NodeInQueue curNode = pq.poll();
            String path;
            if (minWeight == 0 || Objects.equals(minWeight, curNode.weight)) {
                minWeight = curNode.weight;
                path = curNode.name;
                curNode = curNode.from;
            } else {
                return results;
            }
            while (curNode != null) {
                path = String.format("%s->%s", curNode.name, path);
                curNode = curNode.from;
            }
            results.add(path);
        }
    }

    /**
     * 随机游走
     *
     * @return 随机游走diedaiqi
     */
    public Iterator<String> randomWalkIterator() {
        return new WalkIterator();
    }

    /**
     * 同步生成有向图图像
     *
     * @param dotPath dot文件路径
     * @param picPath 目标图像路径
     */
    public void generateImage(String dotPath, String picPath) {
        try {
            //set path = "./Graphviz/bin/"
            // 降低图片画质，减少内存占用
            String command = "cmd /c dot -Tpng -Gdpi=63 " + dotPath + " -o " + picPath;
            String[] env = new String[]{"PATH=" + Paths.get(".").toAbsolutePath().normalize().toString() + "\\Graphviz\\bin"};
            Process p = Runtime.getRuntime().exec(command, env);
            p.waitFor();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void addWordsTest(String src, String des) {
        if (name2node.get(src) == null) {
            name2node.put(src, new Node(src));
        }
        if (name2node.get(des) == null) {
            name2node.put(des, new Node(des));
        }
        name2node.get(src).insertStrongPointer(des);
        name2node.get(des).insertWeakPointer(src);
    }


    //课程要求函数签名

    /**
     * 静态方法，将有向图输出到目标路径
     *
     * @param g
     * @param path
     * @throws InvalidPathException
     */
    public static void showDirectedGraph(Graph g, String path) throws InvalidPathException {
        //判断目标路径是否存在
        Paths.get(path);
        g.writeOriginGraphFile();
        g.generateImage("origin.dot", path);
    }

    /**
     * 查询桥接词
     *
     * @param word1
     * @param word2
     * @return 按照所有要求的所有桥接词
     */
    public String queryBridgeWords(String word1, String word2) {
        List<String> result = findBridgeWords(word1, word2);
        if (result == null || result.isEmpty()) {
            return "No bridge words from \"" + word1 + "\" to \"" + word2 + "\" !";
        }
        String res;
        if (result.size() == 1) {
            res = "The bridge word from \"" + word1 + "\" to \"" + word2 + "\" is: " + result.get(0);
        } else {
            res = "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: ";
            res += String.join(", ", result);
            int lastWordIndex = res.lastIndexOf(", ");
            res = res.substring(0, lastWordIndex) + " and " + res.substring(lastWordIndex + 2);
        }
        return res;
    }

    /**
     * 生成新文本
     *
     * @param inputText 输入文本
     * @return 新文本
     */
    public String generateNewText(String inputText) {
        String[] splitResult = inputText.replaceAll("(\\W|\\d)+", " ")
                .toLowerCase()
                .split(" +");
        StringBuilder builder = new StringBuilder();
        builder.append(splitResult[0]);
        for (int i = 0; i <= splitResult.length - 2; i++) {
            builder.append(" ");
            List<String> findResult = findBridgeWords(splitResult[i], splitResult[i + 1]);
            if (findResult.size() != 0) {
                Collections.shuffle(findResult);
                builder.append(findResult.get(0));
                builder.append(" ");
            }
            builder.append(splitResult[i + 1]);
        }
        return builder.toString();
    }

    /**
     * 计算最短路径
     *
     * @param word1
     * @param word2
     * @return
     */
    public String calcShortestPath(String word1, String word2) {
        return String.join("\n", findShortestPaths(word1, word2));
    }

    /**
     * 随机游走
     *
     * @return 随机游走路径
     */
    public String randomWalk() {
        List<String> list = new ArrayList<>();
        (new WalkIterator()).forEachRemaining(list::add);
        return String.join("->", list);
    }

}
