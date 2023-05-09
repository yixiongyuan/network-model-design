public class Main {
    public static void main(String[] args) {

        // 常量设置
        int nodeNum = 30;
        int treeNum = 4;
        int linkNum = 11;
        int level = 4;
        double totalRate = 1500;

        // link property
        int[] cost = new int[]{14,18,23,9,10,15,16,11,17,24,25};
        int[] downloadLimit = new int[]{8192,25600,51200,2048,4096,10240,20480,5120,10240,25600,51200};
        int[] uploadLimit = new int[]{640,1536,4096,512,512,640,1024,512,1024,5120,5120};

        //create tree 类实例 构建树 并输出结果
        CreateTree createTree = new CreateTree(nodeNum,  treeNum,  linkNum, level, totalRate,downloadLimit, uploadLimit, cost);
        createTree.build();
        createTree.printResult();

    }

}
