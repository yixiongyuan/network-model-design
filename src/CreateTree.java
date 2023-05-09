import java.util.*;

public class CreateTree {

    int levelLimit;
    double totalRate;

    Tree[] TreeList;
    Node[] NodeList;
    Link[] accessLinkList;


    public CreateTree(int nodeNum, int treeNum, int linkNum,int levelLimit,double totalRate,int[] downloadLimit, int[] uploadLimit ,int[] cost) {

        this.levelLimit = levelLimit;
        this.totalRate = totalRate;

        this.TreeList = new Tree[treeNum];
        //这里我根据 index 大小来划分stream （论文中说得按比例）
        for(int i = 0;i<treeNum;i++){
            TreeList[i] = new Tree(i,0,levelLimit,totalRate * (i+1) / ((treeNum + 1)*treeNum/2.0 ));
        }

        this.accessLinkList = new Link[linkNum];
        for(int i = 0;i<linkNum;i++){
            accessLinkList[i] = new Link(downloadLimit[i], uploadLimit[i],cost[i]);
        }
        //根据upload capacity 排序
        Arrays.sort(this.accessLinkList,(x,y) -> x.getUpload() - y.getUpload());



        this.NodeList = new Node[nodeNum];
        for(int i = 0;i<nodeNum;i++){
            NodeList[i] = new Node(i,treeNum,this.accessLinkList);
        }

        //root node setting
        NodeList[0].setIfRoot(true);
        //每一棵树默认 root都在level 0 层
        Arrays.fill(NodeList[0].getConnectToTree(), 0);


    }

    public void build(){

        // step 0: initialization
        int curLevel = 0;
        Node root = NodeList[0];

        //default link connection
        // 每一个node先找到一个对应的link满足default需求

        //Arrays.sort(root.accessLinkList,(x,y) -> x.getUpload() - y.getUpload());
        for(Link link:root.getAccessLinkList()){
            if(link.getUpload() >= root.getB() + totalRate){
                root.setUpRemain(link.getUpload() - root.getB());
                root.setDownRemain(link.getDownload() - root.getA());
                break;
            }
            else{root.setLinkIndex(root.getLinkIndex()+1);}
        }

        for(int i = 1;i<NodeList.length;i++){
            Node node = NodeList[i];
            //Arrays.sort(node.accessLinkList,(x,y) -> x.getUpload() - y.getUpload());
            for(Link link:node.getAccessLinkList()){
                if(link.getDownload() >= node.getA() + totalRate){
                    node.setUpRemain(link.getUpload() - node.getB());
                    node.setDownRemain(link.getDownload() - node.getA() - totalRate);
                    break;
                }
                else{node.setLinkIndex(node.getLinkIndex()+1);}
            }
        }


        // step 1: create table A and table B and first time allocation

        PriorityQueue<Tree> treePQ = new PriorityQueue<>(new Comparator<Tree>() {
            @Override
            public int compare(Tree t1, Tree t2) {
                return Double.compare(t2.streamRate,t1.streamRate);
            }
        });
        for(Tree tree:TreeList){treePQ.offer(tree);}

        PriorityQueue<Node> nodePQ = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return Double.compare(n2.getUpRemain(),n1.getUpRemain());
            }
        });
        for(int i = 1;i<NodeList.length;i++){nodePQ.offer(NodeList[i]);}


        while(!treePQ.isEmpty()){

            Tree tree = treePQ.poll();
            Node child = nodePQ.poll();

            getConnected(root,child,tree,curLevel);

//            tree.levelList.get(curLevel+1).add(child.index);
//            tree.setCount(tree.getCount()+1);
//
//            root.childList.get(tree.index).add(child.index);
//            //root.setUpRemain(root.getUpRemain() - tree.getStreamRate());
//
//            child.connectToTree[tree.index] = curLevel+1;
        }


        // step 2: for feasible node in current level add new child

        boolean stopSign = false;

        while(!stopSign){

            // step 3: reach the limit of level
            while(curLevel < levelLimit){

                //找到合适的 parent child tree 然后连接在一起，没找到就往下一层找
                if(isTransfer(curLevel)){

                    int feasibleTreeIndex = fTree(curLevel);
                    if(feasibleTreeIndex == -1){
                        System.out.println("can't find feasible transfer tree");
                        break;
                    }
                    Tree feasibleTree = TreeList[feasibleTreeIndex];

                    Node parentNode = NodeList[fPNode(feasibleTree,curLevel)];
                    Node childNode = NodeList[fCNode(parentNode, feasibleTree,curLevel)];

                    getConnected(parentNode,childNode, feasibleTree,curLevel);

                }else{curLevel++;}
            }

            // step 4: update the node using larger access link go back to step 2
            if(!isTree() && isUpdate()){

                //当没有空间插入新节点时，就尝试找node 扩大容量 然后再从第一层开始找node插入新节点
                Node updateNode = NodeList[updateNode()];

                int curLinkIndex = updateNode.getLinkIndex();
                updateNode.setLinkIndex(curLinkIndex+1);

                updateNode.setUpRemain(updateNode.getUpRemain() + accessLinkList[updateNode.getLinkIndex()].getUpload() - accessLinkList[curLinkIndex].getUpload());
                updateNode.setDownRemain(updateNode.getDownRemain() + accessLinkList[updateNode.getLinkIndex()].getDownload() - accessLinkList[curLinkIndex].getDownload());

                curLevel = 0;
            }else{
                // 结束循环
                if(isTree()){
                    System.out.println("successful");
                }

                if(!isUpdate()){
                    System.out.println("failed");
                }

                stopSign = true;
            }
        }

    }

    //自己写的打印结果的函数
    public void printResult(){

        System.out.println("Link data:");
        for(int i = 0;i<accessLinkList.length;i++){
            Link link = accessLinkList[i];
            System.out.println((i+1) + " " + link.cost + " " + link.upload + " " + link.download);
        }

        System.out.println("Node data:");
        for(Node node : NodeList){

            System.out.println((node.getIndex()+1) + " " + (int)node.getA() + " " + (int)node.getB() + " " + (node.ifRoot ? 1:0));

        }

        int totalCost = 0;
        for(Node node:NodeList){
            totalCost += accessLinkList[node.linkIndex].cost;
        }

        System.out.println("The final total cost = " + totalCost);

        System.out.println();
        System.out.println("-------------------------------------------------------");

        for(Tree tree:TreeList){

            System.out.println("Tree No." + tree.index + " info : ");

            System.out.println("count = " + tree.count + " , stream rate = " + tree.streamRate);

            for(int i = 0; i<tree.levelList.size(); i++){
                List<Integer> levelList = tree.levelList.get(i);
                System.out.print("Level"+ i + " : ");
                for(int j = 0;j<levelList.size();j++){
                    System.out.print(levelList.get(j) + " , ");
                }
                System.out.println();
            }

            System.out.println();
            System.out.println("-------------------------------------------------------");
        }

        for(Node node : NodeList){

            System.out.println("Node No." + node.index + " info:");

            System.out.println("download background = " + node.getA() + " , upload background = " + node.getB());
            System.out.println("download remain = " + node.getDownRemain() + " , upload remain = " + node.getUpRemain());
            System.out.println("access link index = " + node.getLinkIndex());

            for(int i = 0;i<node.connectToTree.length;i++){

                System.out.println("Tree " + i + ": level = " + node.connectToTree[i]);

                System.out.print("Child info: ");
                List<Integer> levelList = node.childList.get(i);
                for(int j = 0;j<levelList.size();j++){
                    System.out.print(levelList.get(j) + " , ");
                }
                System.out.println();
            }

            System.out.println();
            System.out.println("-------------------------------------------------------");

        }
    }

    // 将两个node 连接到一起
    private void getConnected(Node parent,Node child,Tree tree, int level){

        tree.levelList.get(level+1).add(child.index);
        tree.setCount(tree.getCount()+1);

        parent.childList.get(tree.index).add(child.index);
        parent.setUpRemain(parent.getUpRemain() - tree.getStreamRate());

        child.connectToTree[tree.index] = level+1;


    }

    private int fTree(int level){


        int lowestNumberNode = Integer.MAX_VALUE;
        int feasibleIndex = -1;

        for(Tree tree: TreeList){

            boolean flag = false;

            for(Node node:NodeList){
                if(node.connectToTree[tree.getIndex()] == -1){
                    flag = true;
                    break;
                }
            }

            if(!flag){continue;}

            flag = false;
            for(int index:tree.levelList.get(level)){
                if(NodeList[index].getUpRemain() > tree.getStreamRate()){
                    flag = true;
                    break;
                }
            }

            if(flag && tree.getCount() < lowestNumberNode){

                feasibleIndex = tree.getIndex();
                lowestNumberNode = tree.getCount();
            }
        }

        return feasibleIndex;
    }

    private int fPNode(Tree tree, int level){

        double residualUploadCapacity = -1;
        int feasibleParent = -1;

        for(int index:tree.levelList.get(level)){
            Node node = NodeList[index];
            if(node.getUpRemain() > tree.getStreamRate() && node.getUpRemain() > residualUploadCapacity){

                feasibleParent = index;
                residualUploadCapacity = node.getUpRemain();
            }
        }

        return feasibleParent;
    }

    private int fCNode(Node parent,Tree tree,int level){

        double residualUploadCapacity = -1;
        int feasibleChild = -1;


        for(Node node:NodeList){
            if(node.connectToTree[tree.getIndex()]==-1 && node.getUpRemain() > residualUploadCapacity){
                feasibleChild = node.getIndex();
                residualUploadCapacity = node.getUpRemain();
            }
        }


        return feasibleChild;
    }

    private boolean isTransfer(int level){

        for(Tree tree: TreeList){

            boolean flag = false;

            for(Node node:NodeList){
                if(node.connectToTree[tree.getIndex()]==-1){
                    flag = true;
                    break;
                }
            }

            if(!flag){continue;}

            flag = false;
            for(int index : tree.levelList.get(level)){
                if(NodeList[index].getUpRemain() > tree.getStreamRate()){
                    flag = true;
                    break;
                }
            }


            if(flag){return true;}


        }

        return false;
    }
    private boolean isTree(){

        for(Node node:NodeList){
            for(int check:node.connectToTree){
                if(check==-1){return false;}
            }
        }

        return true;
    }

    private boolean isUpdate(){

        for(Node node:NodeList){
            int count = 0;
            for(int level:node.connectToTree){if(level==-1){count++;}}
            if(count == this.TreeList.length){continue;}

            int index = node.getLinkIndex();
            int curUploadCapacity = node.accessLinkList[index].getUpload();

            index++;
            while(index < node.getAccessLinkList().length){

                if(node.accessLinkList[index].getUpload() > curUploadCapacity){
                    return true;
                }

                index++;
            }

        }

        return false;
    }

    //这个函数就是我说的筛选函数，论文没说清楚，我按自己的理解，就是写了一个寻找方法
    private int updateNode(){


        PriorityQueue<Node> minHeap = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {

                return Integer.compare(o1.nextLinkRatio,o2.nextLinkRatio);

            }
        });

        for(Node node:NodeList){
            int count = 0;
            for(int level:node.connectToTree){if(level==-1){count++;}}
            if(count == this.TreeList.length){continue;}

            int index = node.getLinkIndex();
            int curUploadCapacity = node.accessLinkList[index].getUpload();
            int curLinkCost = node.accessLinkList[index].getCost();

            index++;
            while(index < node.getAccessLinkList().length){

                if(node.accessLinkList[index].getUpload() > curUploadCapacity){

                    node.nextLinkRatio = (node.accessLinkList[index].getUpload() - curUploadCapacity) /
                            (node.accessLinkList[index].getCost() - curLinkCost);

                    minHeap.offer(node);
                    if(minHeap.size()>5){minHeap.poll();}

                    index =  node.getAccessLinkList().length;
                }

                index++;
            }

        }



        int minLevel = this.levelLimit+1;
        int candidateNode = -1;

        while(!minHeap.isEmpty()){

            Node node = minHeap.poll();

            int lowest = this.levelLimit+1;
            for(int level:node.connectToTree){
                if(level>=0 && level < lowest){
                    lowest = level;
                }
            }

            if(lowest < minLevel){
                candidateNode = node.index;
            }

        }

        return candidateNode;
    }



}
