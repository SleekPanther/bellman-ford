import java.util.*;

public class BellmanFord {
	private int[][] memoTable;		//memoization table
	private int[][] successors;		//bookkeeping to retrace paths
	private String[] vertexNames;	//convert array indexes in graph[][][] to string names
	int[][][] graph;		//adjacency list. Each row graph[v] contains a list of edges from v. Each edge graph[v][i] contains 2 numbers: the destination vertex & the edge weight
	int startVertex;
	int goalVertex;
	
	public void findShortestPaths(int[][][] inputGraph, String[] inputVertexNames, int inputStartVertex, int inputGoalVertex){
		this.startVertex = inputStartVertex;
		this.goalVertex = inputGoalVertex;
		this.graph=inputGraph;
		this.vertexNames = inputVertexNames;
		int vertexCount = graph.length;

		memoTable = new int[vertexCount][vertexCount];
		successors = new int[vertexCount][vertexCount];
		for(int vertex=0; vertex<vertexCount; vertex++){
			memoTable[0][vertex] = Integer.MAX_VALUE;	//Most nodes cannot reach the goalVertex using 0 edges, so set it to "infinity"
			successors[0][vertex] = -1;		//initialize 1st row with no successor
		}
		memoTable[0][goalVertex] = 0;	//shortest path from Goal Vertex to itself uses 0 edges
		successors[0][goalVertex] = goalVertex;	//path from goalVertex to goalVertex is possible with 0 edges so it's its own successor

		for(int i=1; i<vertexCount; i++){		//shortest path uses up to n-1 edges in a graph with n nodes
			for(int sourceVertex=0; sourceVertex<vertexCount; sourceVertex++){
				ArrayList<ArrayList<Integer>> possiblePathCosts = new ArrayList<ArrayList<Integer>>();	//Holds costs for all possible paths & the successor for each of those paths
				possiblePathCosts.add(new ArrayList<Integer>(Arrays.asList(memoTable[i-1][sourceVertex], successors[i-1][sourceVertex])) );		//inherit value from 1 less edge
				for(int k=0; k<graph[sourceVertex].length; k++){	//loop over all edges incident to sourceVertex
					int destinationVertex = graph[sourceVertex][k][0];
					int weight = graph[sourceVertex][k][1];
					int newCost = Integer.MAX_VALUE;	//initialize to infinity to avoid overflow
					if(memoTable[i-1][destinationVertex] != Integer.MAX_VALUE){		//if the value from previous row is NOT infinity
						newCost = weight + memoTable[i-1][destinationVertex];	//calculate new cost from optimal substructure (avoiding overflow)
					}
					possiblePathCosts.add(new ArrayList<Integer>(Arrays.asList(newCost, destinationVertex)));
				}
				//find min of all possible paths found
				int minCost = possiblePathCosts.get(0).get(0);
				int successor = possiblePathCosts.get(0).get(1);
				for(int j=0; j<possiblePathCosts.size(); j++){
					if(possiblePathCosts.get(j).get(0) < minCost){	//if we find a smaller value, update minCost & successor where that minCost was found
						minCost=possiblePathCosts.get(j).get(0);
						successor=possiblePathCosts.get(j).get(1);
					}
				}
				memoTable[i][sourceVertex]=minCost;
				successors[i][sourceVertex]=successor;
			}
		}
		System.out.println("\tMemoization Table");
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

	//Requires memoization table & successors to be filled
	private void printShortestPaths(){
		//Find shortest paths for from all vertices to the goalVertex
		for(int vertex=0; vertex<graph.length; vertex++){
			System.out.print("Shortest path from "+vertexNames[vertex] + " to " +vertexNames[goalVertex] + ":  ");
			int nextVertex = successors[successors.length -1][vertex];	//initial starting vertex in path, updated until goal vertex is reached
			if(nextVertex == -1){	// -1 represents no successor, so no path exists
				System.out.println("No path exists");
			}
			else{	//Else actually retrace the path
				System.out.print(vertexNames[vertex]);		//print start of the path
				for(int row = successors.length -1; row>=0; row--){		//start from the bottom of successors table decreaseing row each iteration as 1 edge is used up
					if(nextVertex == goalVertex){		//Reached the end condition
						System.out.println("-->"+vertexNames[goalVertex] + "\t(Cost = "+memoTable[memoTable.length-1][vertex]+")");
						break;
					}
					else{
						System.out.print("-->"+vertexNames[nextVertex]);		//print the next vertex found
						nextVertex = successors[row-1][nextVertex];		//find next vertex in path
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		BellmanFord pathFinder = new BellmanFord();
		int[][][] inputGraph = {{ {1, 6}, {2, 7} },
								{ {3, 5}, {4, -4}, {2, 8} },
								{ {3, -3}, {4, 9} },
								{ {1, -2} },
								{ {0, 2}, {3, 7} }
								};
		String[] vertexNames = {"S", "a", "b", "c", "T"};
		int startingVertex =0;
		int goalVertex = 4;
		System.out.println("Graph 1");
		pathFinder.findShortestPaths(inputGraph, vertexNames, startingVertex, goalVertex);

		int[][][] inputGraph2 = {{ {5, -3}, {3, -4} },
								{ {5, 4}, {0, 6} },
								{ {4, -3}, {5, 2} },
								{ {2, -2}, {1, -1} },
								{ {5, 3}, {3, 8} }, 
								{}	//T has no outgoing edges
							};
		String[] vertexNames2 = {"S", "a", "b", "c", "d", "T"};
		goalVertex = 5;
		System.out.println("\n\nGraph 2");
		pathFinder.findShortestPaths(inputGraph2, vertexNames2, startingVertex, goalVertex);
	}

}