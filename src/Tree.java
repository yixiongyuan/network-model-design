import java.util.LinkedList;
import java.util.List;

// 每一颗树 用一个类实例表示
public class Tree {

    //tree index
    int index;

    // index of root node
    int root;

    // number of node connected to it
    int count;


    double streamRate;

    // In one tree every level has a list of nodes
    List<List<Integer>> levelList;

    public Tree(int index,int root, int level,double streamRate) {

        this.index = index;
        this.root = root;

        this.streamRate = streamRate;
        //default root node
        this.count = 1;

        levelList = new LinkedList<>();
        for(int i = 0;i<=level;i++){
            levelList.add(new LinkedList<>());
        }

        levelList.get(0).add(root);

    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getStreamRate() {
        return streamRate;
    }

    public void setStreamRate(double streamRate) {
        this.streamRate = streamRate;
    }

    public List<List<Integer>> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<List<Integer>> levelList) {
        this.levelList = levelList;
    }
}
