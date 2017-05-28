import java.util.*;

public class BellmanFord {
	private int[][] memoTable;		//memoization table
	private int[][] successors;		//bookkeeping to retrace paths
	private String[] vertexNames = {"S", "B", "C", "D", "T"};	//convert array indexes in graph[][][] to string names
	int[][][] graph;		//adjacency list. Each row graph[v] contains a list of edges from v. Each edge graph[v][i] contains 2 numbers: the destination vertex & the edge weight
	int startVertex;
	int goalVertex;
	
	public void findShortestPaths(int[][][] inputGraph, int inputStartVertex, int inputGoalVertex){
		startVertex = inputStartVertex;
		goalVertex = inputGoalVertex;
		graph=inputGraph;
		int vertexCount = graph.length;

		memoTable = new int[vertexCount][vertexCount];
		successors = new int[vertexCount][vertexCount];
		for(int vertex=0; vertex<vertexCount; vertex++){
			memoTable[0][vertex] = Integer.MAX_VALUE;	//Most nodes cannot reach the goalVertex using 0 edges, so set it to "infinity"
			successors[0][vertex] = -1;		//initialize with no successor
		}
		memoTable[0][goalVertex] = 0;	//shortest path from Goal Vertex to itself uses 0 edges
		successors[0][goalVertex] = goalVertex;	//goalVertex is a path of cost 0 starting & ending at itself

		for(int i=1; i<vertexCount; i++){		//shortest path uses up to n-1 edges in a graph with n nodes
			for(int sourceVertex=0; sourceVertex<vertexCount; sourceVertex++){
				ArrayList<ArrayList<Integer>> possiblePathCosts = new ArrayList<ArrayList<Integer>>();	//Holds costs for all possible paths, smallest value is chosen
				possiblePathCosts.add(new ArrayList<Integer>(Arrays.asList(memoTable[i-1][sourceVertex], successors[i-1][sourceVertex])) );	//inherit value from 1 less edge
				for(int k=0; k<graph[sourceVertex].length; k++){	//loop over all edges incident to sourceVertex
					int destinationVertex = graph[sourceVertex][k][0];
					int weight = graph[sourceVertex][k][1];
					int newCost = Integer.MAX_VALUE;	//initialize to infinity to avoid overflow
					int newSuccessor = -10;		//initialize to no successor
					if(memoTable[i-1][destinationVertex] != Integer.MAX_VALUE){		//if the value from previous row is NOT infinity
						newCost = weight + memoTable[i-1][destinationVertex];	//calculate new cost from optimal substurcture (avoiding overflow)
						newSuccessor = destinationVertex;
					}
					possiblePathCosts.add(new ArrayList<Integer>(Arrays.asList(newCost, destinationVertex)));
				}
				//find min of all possible paths found
				int minCost = possiblePathCosts.get(0).get(0);
				int successor = possiblePathCosts.get(0).get(1);
				for(int j=0; j<possiblePathCosts.size(); j++){
					if(possiblePathCosts.get(j).get(0) < minCost){
						minCost=possiblePathCosts.get(j).get(0);
						successor=possiblePathCosts.get(j).get(1);
					}
				}
				memoTable[i][sourceVertex]=minCost;
				successors[i][sourceVertex]=successor;
			}
		}
		printTable();
		System.out.println();
		printShortestPaths();
	}

	private void printTable(){
		System.out.print("\t");
		for(int vertex=0; vertex<graph.length; vertex++){
			System.out.print(vertexNames[vertex]+"\t");
		}
		System.out.println("\n-------------------------------------------------------------");

		for(int i=0; i<memoTable.length; i++){
			System.out.print(i+" |\t");
			for(int j=0; j<memoTable[i].length; j++){
				if(memoTable[i][j] == Integer.MAX_VALUE){	//10 digits messes up column alignment so print "inf" instead
					System.out.print("inf"+"\t");
				}
				else{
					System.out.print(memoTable[i][j]+"\t");	//otherwise print the actual value from the table
				}
			}
			System.out.println();
		}
	}

	//Requires memoization talbe to be filled
	private void printShortestPaths(){
		for(int i=0; i<successors.length; i++){
			for(int j=0; j<successors[i].length; j++){
				System.out.print(successors[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		BellmanFord pathFinder = new BellmanFord();
		int[][][] inputGraph = { { {1, 6}, {2, 7} },
							{ {3, 5}, {4, -4}, {2, 8} },
							{ {3, -3}, {4, 9} },
							{ {1, -2} },
							{ {0, 2}, {3, 7} }
							};
		int startingVertex =0;
		int goalVertex = 4;
		pathFinder.findShortestPaths(inputGraph, startingVertex, goalVertex);
	}

}