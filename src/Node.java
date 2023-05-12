import java.util.*;

public class Node {

    private  static Random r = new Random();

    int index;

    double a; // download background transfer
    double b; // upload background transfer


    // 剩余上传/下载空间
    double downRemain;
    double upRemain;

    //是否是根节点
    boolean ifRoot;


    //可用的link列表 和 当前使用的link index
    Link[] accessLinkList;
    int linkIndex;

    // -1 means didn't connect
    // >=0 means level
    // int[3] = 4 代表当前结点 连接在index为3的树的4层
    int[] connectToTree;

    // every tree every child node
    // 当前结点 在每一个棵树中的 子结点有哪些
    List<List<Integer>> childList;

    // 下一个增长的link 性价比
    int nextLinkRatio;


    public Node(int index,int TreeInTotal,Link[] accessLinkList) {

        this.index = index;

        //随机初始化 background transfer

        this.a = r.nextInt(512)+512;
        this.b = r.nextInt(64) + 64;

        this.downRemain = 0;
        this.upRemain = 0;

        ifRoot = false;

        this.connectToTree = new int[TreeInTotal];
        Arrays.fill(connectToTree, -1);

        this.accessLinkList = accessLinkList;
        linkIndex=0;

        this.childList = new LinkedList<>();
        for(int i = 0;i<TreeInTotal;i++){
            childList.add(new LinkedList<>());
        }

        nextLinkRatio = 0;
    }

    public void refresh(int TreeInTotal){

        // refresh the building property
        // keep the same background transfer

        this.downRemain = 0;
        this.upRemain = 0;

        // default -1 not connected
        this.connectToTree = new int[TreeInTotal];
        Arrays.fill(connectToTree, -1);

        linkIndex=0;

        //child list all empty
        this.childList = new LinkedList<>();
        for(int i = 0;i<TreeInTotal;i++){childList.add(new LinkedList<>());}

        nextLinkRatio = 0;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public boolean isIfRoot() {
        return ifRoot;
    }

    public void setIfRoot(boolean ifRoot) {
        this.ifRoot = ifRoot;
    }

    public int[] getConnectToTree() {
        return connectToTree;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Link[] getAccessLinkList() {
        return accessLinkList;
    }

    public void setAccessLinkList(Link[] accessLinkList) {
        this.accessLinkList = accessLinkList;
    }

    public int getLinkIndex() {
        return linkIndex;
    }

    public void setLinkIndex(int linkIndex) {
        this.linkIndex = linkIndex;
    }

    public double getDownRemain() {
        return downRemain;
    }

    public void setDownRemain(double downRemain) {
        this.downRemain = downRemain;
    }

    public double getUpRemain() {
        return upRemain;
    }

    public void setUpRemain(double upRemain) {
        this.upRemain = upRemain;
    }
}
