import java.util.*;

public class BellmanFord {
	private int[][] memoTable;
	private String[] vertexNames = {"S", "B", "C", "D", "T"};
	
	public void findShortestPaths(int[][][] graph, int startingvertex, int goalVertex){
		int vertexCount = graph.length;
		int edgeCount=0;
		for(int i=0; i<vertexCount; i++){	//Loop over entire graph & count the number of edges
			for(int j=0; j<graph[i].length; j++){
				edgeCount++;
			}
		}

		memoTable = new int[edgeCount][vertexCount];
		for(int vertex=0; vertex<vertexCount; vertex++){
			memoTable[0][vertex] = Integer.MAX_VALUE;	//No path to a node with 0 edges, so set in "infinity"
		}
		memoTable[0][goalVertex] = 0;	//shortest path from Goal Vertex to itself uses 0 edges

		// for(int i=1; i<edgeCount; i++){	//or edgeCount -1?
		// 	for(int sourceVertex=0; sourceVertex<vertexCount; sourceVertex++){
		// 		memoTable[i][sourceVertex]=memoTable[i-1][sourceVertex];	//inherit value from 1 less edge

		// 		ArrayList<Integer> possiblePathWeights = new ArrayList<Integer>();
		// 		possiblePathWeights.add(memoTable[i][sourceVertex]);	//copy existing value
		// 		for(int k=0; k<graph[i].length; k++){
		// 			System.out.println("edge " + i "-->"+ graph[i][vertex][1] + "(weight="+graph[i][vertex][2]);
		// 			// int destinationVertex = graph[i][vertex][1];
		// 			// int weight = graph[i][k][2];
		// 			// possiblePathWeights.add(weight + memoTable[i-1][destinationVertex]);
		// 		}
		// 		memoTable[i][sourceVertex]=Collections.min(possiblePathWeights);	//find min of all possible paths found
		// 	}
		// }

		printTable();
	}

	//Also print headers?
	private void printTable(){
		for(int i=0; i<memoTable.length; i++){
			for(int j=0; j<memoTable[i].length; j++){
				System.out.print(memoTable[i][j]+"\t");
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
		int startingvertex =0;
		int goalVertex = 4;
		pathFinder.findShortestPaths(inputGraph, startingvertex, goalVertex);
	}

}