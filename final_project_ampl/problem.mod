# Required number of constants to understand the given
# Number of Nodes and Links
param E > 0 integer;
param V > 0 integer;
param T > 0 integer;
param L > 0 integer;
param M > 0;

#//#########################################################
set Links := 1..E;
set Nodes := 1..V;
set Trees := 1..T;
set Levels := 1..L;



#Constants
param a {v in Nodes} >= 0;
param b {v in Nodes} >= 0;
param c {k in Links} > 0;
param d {k in Links} > 0;
param u {k in Links} > 0;
param r {v in Nodes} >=0 binary;
# streaming rate of tree t 
param sr {t in Trees} > 0;




############################################################
#Generation of the variables required for optimization

############################################################
#Variables for the problem
############################################################
var x {v in Nodes, w in Nodes,t in Trees, l in Levels} >= 0 binary;
var y {v in Nodes, k in Links} >= 0 binary;




############################################################
#Objective function- Maximize the Throughput
############################################################
minimize MinLinkCost: 
sum{v in Nodes} ( sum{k in Links}  y[v,k] * c[k]);


############################################################
#Constraints
############################################################

subj to cons_one_parent {w in Nodes, t in Trees}:
sum{v in Nodes} (if(v != w) then sum{l in Levels} ( x[v,w,t,l] ) else 0 ) = 1 - r[w];

subj to cons_two_root {v in Nodes}:
sum{w in Nodes} (if(w != v) then sum{t in Trees} ( x[v,w,t,1] ) else 0) <= M * r[v];

subj to cons_three_level {v in Nodes, t in Trees, l in Levels}:
if( l < L ) then 
sum{w in Nodes} (if(w != v) then (x[v,w,t,l+1]) else 0) <= 
M * sum{w in Nodes} (if(w != v) then (x[w,v,t,l]) else 0);

subj to cons_four_link {v in Nodes}:
sum{k in Links} (y[v,k]) = 1;

subj to cons_five_download {v in Nodes}:
a[v] + sum{t in Trees} (sr[t]) <= sum{k in Links}(y[v,k] * d[k]);

subj to cons_six_upload {v in Nodes}:
b[v] + sum{w in Nodes} (if (w != v) then ( sum{t in Trees}(sum {l in Levels} (x[v,w,t,l] * sr[t]))) else 0 ) <= 
sum{k in Links}(y[v,k] * u[k])

############################################################
