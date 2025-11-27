import java.util.Arrays;

public class DijkstrasAlgo {
    static final int INF = Integer.MAX_VALUE;
    static int n = 7;
    static String[] v = {"a", "b", "c", "d", "e", "f", "g"};
    
    public static void dijkstraAlgo(int[][] g, int s) {
        int[] dist = new int[n];
        boolean[] vis = new boolean[n];
        String[] path = new String[n];
        
        Arrays.fill(dist, INF);
        Arrays.fill(vis, false);
        for (int i = 0; i < n; i++) {
            path[i] = v[s];
        }
        
        dist[s] = 0;
        
        for (int i = 0; i < n - 1; i++) {
            int u = minDist(dist, vis);
            if (u == -1) break;
            vis[u] = true;

            for (int j = 0; j < n; j++) {
                if (!vis[j] && g[u][j] != 0 && dist[u] != INF && dist[u] + g[u][j] < dist[j]) {
                    dist[j] = dist[u] + g[u][j];
                    path[j] = path[u] + "->" + v[j];
                }
            }
        }
        
        System.out.println("Shortest paths from " + v[s] + ":");
        for (int i = 0; i < n; i++) {
            if (i != s) {
                if (dist[i] == INF) {
                    System.out.println(v[s] + " -> " + v[i] + " = unreachable");
                } else {
                    System.out.println(v[s] + " -> " + v[i] + " = " + dist[i] + " (" + path[i] + ")");
                }
            }
        }
    }
    
    public static int minDist(int[] dist, boolean[] vis) {
        int min = INF;
        int idx = -1;
        
        for (int i = 0; i < n; i++) {
            if (!vis[i] && dist[i] < min) {
                min = dist[i];
                idx = i;
            }
        }
        return idx;
    }
    
    public static void main(String[] args) {
        int[][] g = {
            {0, 2, 5, 0, 0, 0, 0},
            {2, 0, 1, 2, 0, 0, 0},
            {5, 1, 0, 3, 1, 4, 0},
            {0, 2, 3, 0, 2, 0, 3},
            {0, 0, 1, 2, 0, 0, 5},
            {0, 0, 4, 0, 0, 0, 2},
            {0, 0, 0, 3, 5, 2, 0}
        };
        dijkstraAlgo(g, 0);
    }
}

/*

TITLE: Dijkstra's Algorithm for Link State Routing Protocol (OSPF)

PURPOSE:
This program implements Dijkstra's Algorithm to find the shortest path from a 
source node to all other nodes in a weighted graph. It simulates the routing 
mechanism used by OSPF (Open Shortest Path First) protocol in the Internet for 
optimal packet forwarding.

PROBLEM STATEMENT:
Find the shortest path using Dijkstra's equation for Link State Routing Protocol 
which is used by Open Shortest Path First Protocol (OSPF) in the Internet.

ALGORITHM OVERVIEW:
Dijkstra's Algorithm is a greedy algorithm that finds the shortest path from a 
source vertex to all other vertices in a weighted graph with non-negative edge 
weights. It works by maintaining a set of visited nodes and repeatedly selecting 
the unvisited node with the smallest tentative distance.

WORKING PRINCIPLE:
1. INITIALIZATION PHASE:
   - Set distance to source node = 0
   - Set distance to all other nodes = infinity (INF)
   - Mark all nodes as unvisited
   - Initialize path for all nodes starting with source vertex

2. MAIN ALGORITHM LOOP (Runs n-1 times):
   - Select unvisited node 'u' with minimum distance using minDist()
   - Mark node 'u' as visited
   - For each unvisited neighbor 'j' of 'u':
     * Calculate new distance = dist[u] + edge_weight(u, j)
     * If new distance < current dist[j], update:
       - dist[j] = new distance (relaxation step)
       - path[j] = path to u + "->j" (path tracking)

3. TERMINATION:
   - All reachable nodes have been visited
   - dist[] array contains shortest distances from source
   - path[] array contains complete routes

DATA STRUCTURES USED:
- int[][] g: Adjacency matrix representing network topology
  * g[i][j] = weight/cost of edge from node i to node j
  * g[i][j] = 0 means no direct edge exists between i and j
  
- int[] dist: Stores shortest known distance from source to each node
  * Initially all INF except source (0)
  * Updated during relaxation steps
  
- boolean[] vis: Tracks which nodes have been fully processed
  * false = unvisited, true = visited and processed
  
- String[] path: Stores complete path string from source to each node
  * Format: "a->b->c" showing route taken
  
- String[] v: Array of vertex names ["a", "b", "c", "d", "e", "f", "g"]

KEY FUNCTIONS:

1. dijkstraAlgo(int[][] g, int s):
   - Main function implementing Dijkstra's algorithm
   - Parameters: g = graph adjacency matrix, s = source node index
   - Calculates shortest paths from source to all other nodes
   - Displays results with distance and complete path

2. minDist(int[] dist, boolean[] vis):
   - Helper function to find unvisited node with minimum distance
   - Scans all nodes and returns index of node with smallest dist value
   - Returns -1 if no unvisited nodes remain (handles disconnected graphs)
   - Critical fix: Uses dist[i] < min (not <=) to avoid selecting INF nodes

3. main(String[] args):
   - Entry point of program
   - Defines 7-node network graph as adjacency matrix
   - Calls dijkstraAlgo() with source node 0 (vertex 'a')

GRAPH REPRESENTATION:
The network has 7 routers (a, b, c, d, e, f, g) with the following connections:
- a connects to: b(2), c(5)
- b connects to: a(2), c(1), d(2)
- c connects to: a(5), b(1), d(3), e(1), f(4)
- d connects to: b(2), c(3), e(2), g(3)
- e connects to: c(1), d(2), g(5)
- f connects to: c(4), g(2)
- g connects to: d(3), e(5), f(2)

OSPF CONNECTION:
In real OSPF networks:
- Each router maintains a Link State Database (LSDB) of network topology
- Routers exchange Link State Advertisements (LSAs) to share topology info
- Each router runs Dijkstra's algorithm on LSDB to build Shortest Path Tree (SPT)
- Results populate routing table with next-hop and cost for each destination
- Edge weights based on metrics: bandwidth, delay, hop count, etc.

SAMPLE OUTPUT (from source 'a'):
Shortest paths from a:
a -> b = 2 (a->b)
a -> c = 3 (a->b->c)
a -> d = 4 (a->b->d)
a -> e = 4 (a->b->c->e)
a -> f = 7 (a->b->c->f)
a -> g = 7 (a->b->d->g)

*/