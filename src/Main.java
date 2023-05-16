public class Main {
    public static void main(String[] args) {

        // constant setting
        int nodeNum = 20;
        int treeNum = 4;
        int linkNum = 11;
        int level = 4;
        double totalRate = 1000.0;


        // link property
        int[] cost = new int[]{18,18,23,9,10,11,13,14,17,20,25};
        int[] downloadLimit = new int[]{8192,25600,51200,2048,4096,10240,25600,5120,10240,25600,51200};
        int[] uploadLimit = new int[]{640,1536,4096,512,512,640,1024,512,1024,5120,5120};

        //create tree instance

        //long start = System.currentTimeMillis();
        CreateTree createTree = new CreateTree(nodeNum,  treeNum,  linkNum, level, totalRate,downloadLimit, uploadLimit, cost,null);
        createTree.build();
        createTree.printInfo();
        createTree.printResult();
        //long end = System.currentTimeMillis();
        //System.out.println("The program execution time is:" + (end - start)/1000.0 + " s ");


        //repeat experiment for same set of nodes
//        createTree.printInfo();
//        for(int i = 3;i<=9;i++){
//            CreateTree rebuild = new CreateTree(nodeNum,  treeNum,  linkNum, i, totalRate,downloadLimit, uploadLimit, cost,createTree.NodeList);
//            rebuild.build();
//            rebuild.printResult();
//        }


    }

}
