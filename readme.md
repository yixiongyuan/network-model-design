# P2P Multicasting Network Design Problem - Heuristic Approach
https://ieeexplore-ieee-org.proxy.library.nyu.edu/stamp/stamp.jsp?tp=&arnumber=5700190

Reproduce heuristic algorithm and AMPL code for P2P Multicasting Network Design Problem.

## Environment
* Java
* ampl

## Directory tree
```
.
├── src           			//java source code
│   ├── CreateTree.java
│   ├── Link.java
│   ├── Main.java
│   ├── Node.java
│   └── Tree.java
├── final_project_ampl
│   ├── 3.run        		//run script for ampl model
│   ├── problem.dat	 		
│   └── problem.mod		
└── readme.md
```

## Launch project(java=>heuristic, ampl=>optimal)

-   Import java src code
-   Setup TreeNum（default as 1）, Tree level (default as 2), totalRate (Streaming rate)

![setup](./github_img/setup.png)

-   Get result from the heuristic model with its randomly generated network(nodes, links)
![java_generated_data](./github_img/java_generated_data.png)
-   Copy generate node data into problem.dat file, modify Stream rate(sr) based on the result.
    ![setup_ampl](./github_img/setup_ampl.png)
-   run "include 3.run;" in ampl mode, get ampl result

![ampl_result](./github_img/ampl_result.png)

```bash
./ampl

ampl:include 3.run;
```

