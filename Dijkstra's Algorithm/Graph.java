/*Student Name     : Akash Singh 
  Student ID       : 801022198
  Student EmailID  : asingh40@uncc.edu*/

/*Importing the required packages*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.Map.Entry;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;


/*This is a Interface(i.e. priorityQueue) which declares method to be 
implemented by the class(i.e. MinBinaryHeap) which implements it*/

interface priorityQueue {
	public void enqueue(Edge e); /*This method is used to add an edge to priority queue*/
	public Edge remove();        /*This method is used to remove an edge from priority queue*/
	public boolean isEmpty();    /*This method is used to check whether the the priority queue is Empty*/
}

/*This class(i.e LexicographicComparator) is used to implement an Interface Comparator 
to sort Edge destinations in Alphabetical order */
class LexicographicComparator implements Comparator<Edge> {

	@Override
	public int compare(Edge a, Edge b) {
		return a.dest.name.compareToIgnoreCase(b.dest.name);
	}
}

/*This class(i.e LexicographicComparatorVertex) is used to implement an Interface Comparator 
to sort Vertex in Alphabetical order */
class LexicographicComparatorVertex implements Comparator<Vertex> {

	@Override
	public int compare(Vertex a, Vertex b) {

		return a.name.compareToIgnoreCase(b.name);
	}

}
/*This class implements the Interface priorityQueue
It provides implementation of enqueue,remove and isEmpty method of priority queue Interface*/
class MinBinaryHeap implements priorityQueue {

	ArrayList<Edge> minbinheap = new ArrayList<Edge>();

	public void enqueue(Edge e) {
		minbinheap.add(e);
		//System.out.println("minbinheap contains...1 = " + minbinheap);
		buildMinHeap(minbinheap);
	}
   /* This method(i.e buildMinHeap) is used to build MinHeap to do so it call minHeapify iteratively*/
	private void buildMinHeap(ArrayList<Edge> minbinheap) {

		for (int i = minbinheap.size() / 2; i >= 0; i--) {
			//System.out.println("minbinheap contains...2 = " + minbinheap);
			minHeapify(minbinheap, i);
		}

	}
	/* This method(i.e buildMinHeap) is used to implement minHeap algorithm */
	/* This is based on edge cost*/
	public void minHeapify(ArrayList<Edge> minbinheap, int i) {
		int smallest;
		int left = (2 * i) + 1;
		int right = (2 * i) + 2;
		if (left < minbinheap.size() && minbinheap.get(left).cost < minbinheap.get(i).cost) {
			smallest = left;
		} else {
			smallest = i;
		}

		if (right < minbinheap.size() && minbinheap.get(right).cost < minbinheap.get(smallest).cost) {
			smallest = right;
		}
		if (smallest != i) {
			Edge temp = minbinheap.get(i);
			minbinheap.set(i, minbinheap.get(smallest));
			minbinheap.set(smallest, temp);
			minHeapify(minbinheap, smallest);
		}
		//System.out.println("minbinheap contains...3 = " + minbinheap);
	}
    /*This method is used to extract the first edge from the queue*/
	/*This also calls minHeapify method to make it again a minHeap*/
	public Edge remove() {

		ArrayList<Edge> minbinheap_update = new ArrayList<Edge>();
		Edge minedge = minbinheap.get(0);

		for (int i = 1; i < minbinheap.size(); i++) {
			minbinheap_update.add(minbinheap.get(i));
		}
		minbinheap = minbinheap_update;
		minHeapify(minbinheap, 0);
		return minedge;
	}
	/*This method is used to check if the priority queue is Empty*/
	public boolean isEmpty() {

		if (minbinheap.size() == 0) {
			return true;
		}
		return false;
	}

}

/*This is an Custom Exception class which is used for Graph exceptions*/
class GraphException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GraphException(String name) {
		super(name);
	}
}

/*This class is used to represent Edges in the Graph*/
class Edge {
	public Vertex dest; // Second vertex in Edge
	public double cost; // Edge cost
	public String edge_status; // Edges status can be up or down
    //Constructor to initialise the edges
	public Edge(Vertex d, double c, String s) {
		dest = d;
		cost = c;
		edge_status = s;
	}

}

/*This class is used to represent Vertexs in the Graph*/
class Vertex {
	public String name; // Vertex name
	public List<Edge> adj; // Adjacent vertices
	public double dist; // Cost
	public Vertex prev; // Previous vertex on shortest path
	public int notify;
	public String vertex_status;
	//Constructor to initialise the Vertexs
	public Vertex(String nm) {
		name = nm;
		adj = new LinkedList<Edge>();
		reset();
		vertex_status = "up";
	}

	public void reset() {
		dist = Graph.INFINITY;
		prev = null;
		notify = 0;
	}

}

/*This is the Graph class which has the main method */
/*This class implements all method that relates to changes to the Graph e.g. addedge, edgedown etc*/
public class Graph {
	public static final int INFINITY = Integer.MAX_VALUE;
	private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	static PrintWriter pwriter;

	/*This method is used to addedge as per requirement*/
	public void addEdge(String sourceName, String destName, double cost) {
		Vertex v = getVertex(sourceName);
		Vertex w = getVertex(destName);

		for (Edge e : v.adj) {
			// checking if edge already exist then simply update cost
			if (e.dest.name.equals(destName)) {
				e.cost = cost;
				return;
			}
		}
		v.adj.add(new Edge(w, cost, "up"));
	}

	/*This Method is used to delete edge source=tail_vertex and destination =head_vertex*/
	public void deleteEdge(String sourceName, String destName) {
		// boolean flag = false;
		Vertex v = vertexMap.get(sourceName);
		Vertex w = vertexMap.get(destName);
		if (v!=null && w!= null) {
			//System.out.println("source or destination vertex not exist.. means edge does not exist");
		

		Edge edgetoremove;
		for (Edge e : v.adj) {

			if (e.dest.name.equals(destName)) {
				// edge exists..
				edgetoremove = e;
				v.adj.remove(edgetoremove);
				//System.out.println("edge deleted..");
				return;
			}

		}
		}
		//System.out.println("The edges doest not existed between the given vertexs");
	}
	/*This Method is used to meke the edge down source=tail_vertex and destination =head_vertex*/
	public void edgeDown(String sourceName, String destName) {
		// boolean flag = false;
		Vertex v = vertexMap.get(sourceName);
		Vertex w = vertexMap.get(destName);
		if (v!=null && w!= null) {
			//System.out.println("source or destination vertex not exist.. means edge does not exist");
		
		// Edge edge_to_remove;
		for (Edge e : v.adj) {
			if (e.dest.name.equals(destName)) {
				// edge exists..
				e.edge_status = "down";
				/*
				 * v.adj.remove(edge_to_remove);
				 * System.out.println("edge deleted..");
				 */
				return;
			}
		}
		}
		//System.out.println("The edges doest not existed between the given vertexs");

	}
	/*This Method is used to meke the edge up source=tail_vertex and destination =head_vertex*/
	public void edgeUp(String sourceName, String destName) {
		// boolean flag = false;
		Vertex v = vertexMap.get(sourceName);
		Vertex w = vertexMap.get(destName);
		if (v!=null || w!= null) {
			//System.out.println("source or destination vertex not exist.. means edge does not exist");
		
		

		for (Edge e : v.adj) {
			if (e.dest.name.equals(destName)) {
				// edge exists..
				e.edge_status = "up";
				return;
			}
		}
		}//System.out.println("The edges doest not existed between the given vertexs");
	}
	/*This Method is used to meke the vetex Up*/
	public void vertexUp(String VertexName) {
		Vertex v = vertexMap.get(VertexName);
		if(v!=null){
		v.vertex_status = "up";
		}
	}
	/*This Method is used to meke the vetex Down*/
	public void vertexDown(String VertexName) {
		Vertex v = vertexMap.get(VertexName);
		if(v!=null){
		v.vertex_status = "down";
		}
	}
    /*This method is used to print the path from source vertex to destination vertex*/
	public void printPath(String destName) {
		Vertex w = vertexMap.get(destName);
		if (w == null)
			throw new NoSuchElementException("Destination vertex not found");
		else if (w.dist == INFINITY){
		     System.out.println(destName + " is unreachable");  
		}else {
			printPath(w);
			System.out.println(" "+w.dist);
			//System.out.println();
		}
	}

	
	 /* This method checks If vertexName is present, add it to vertexMap if it is not already present. 
	  In both cases, it returns the Vertex.*/
	 
	private Vertex getVertex(String vertexName) {
		Vertex v = vertexMap.get(vertexName);
		if (v == null) {
			v = new Vertex(vertexName);
			vertexMap.put(vertexName, v);
		}
		return v;
	}

	/*This method is used to print the path using recursion with from source vertex to destination vertex*/
	private void printPath(Vertex dest) {
		if (dest.prev != null) {
			printPath(dest.prev);
			System.out.print(" ");
		}
		//System.out.print(dest.name);
		System.out.print(dest.name);
	}

	
	 /* This method is used Initializes the vertex output info prior to running any shortest path
	  algorithm.*/
	 
	private void clearAll() {
		for (Vertex v : vertexMap.values())
			v.reset();
	}

	/*This method implements the Dijktstra Algorithm*/
	public void shortestPath(String startName) {
		MinBinaryHeap pq = new MinBinaryHeap();

		Vertex start = vertexMap.get(startName);
		// check if vertex is up
		if (start == null)
			throw new NoSuchElementException("Start vertex not found");

		clearAll();
		pq.enqueue(new Edge(start, 0, "up"));
		start.dist = 0;

		int nodesSeen = 0;
		while (!pq.isEmpty() && nodesSeen < vertexMap.size()) {
			Edge vrec = pq.remove();
			if (vrec.edge_status.equals("down")) {
				continue;
			}
			Vertex v = vrec.dest;
			if (v.vertex_status.equals("down")) {
				continue;
			}
			if (v.notify != 0) { // already processed v
				continue;
			}
			v.notify = 1;
			nodesSeen++;

			for (Edge e : v.adj) {
				if (e.edge_status.equals("down")) {
					continue;
				}
				Vertex w = e.dest;
				if (w.vertex_status.equals("down")) {
					continue;
				}
				double cost_vw = e.cost;

				if (cost_vw < 0)
					throw new GraphException("Graph has negative edges");

				if (w.dist > v.dist + cost_vw) {
					w.dist = v.dist + cost_vw;
					w.prev = v;
					pq.enqueue(new Edge(w, w.dist, "up"));
				}
			}
		}
	}
	/*This method implements the BFS Algorithm to find the all vertex which are reachable 
	 *from the given Start Node*/
    /*Time Complexity of each BFS call is O(V+E), where V is # of vertex and E is # of Edges
	Since this method is called for all vertex, the total time complexity becomes V(V+E)
	This V(V+E) time complexity does not consider the time taken for sorting*/
	public void graphTraversalBFS(String startName) {
		clearAll();

		Vertex start = vertexMap.get(startName);
		if (start.vertex_status.equals("down")) {
			return;
		}
		//System.out.println("start vertex = " + start.name);
		System.out.println(start.name);
		if (start == null)
			throw new NoSuchElementException("Start vertex not found");

		Queue<Vertex> q = new LinkedList<Vertex>();
		q.add(start);
		start.dist = 0;
		ArrayList<Vertex> listofadjVertex = new ArrayList<Vertex>();
		while (!q.isEmpty()) {
			Vertex v = q.remove();
			if (v.vertex_status.equals("down")) {
				continue;
			}
			if (v.name.equals(start.name)) {

			} else {
				listofadjVertex.add(v);
			}
			for (Edge e : v.adj) {
				if (e.edge_status.equals("down")) {
					continue;
				}
				Vertex w = e.dest;
				if (w.vertex_status.equals("down")) {
					continue;
				}
				// System.out.println(w.name);
				if (w.dist == INFINITY) {
					w.dist = v.dist + 1;
					w.prev = v;
					q.add(w);
				}
			}
		}
		//This is used to sort Vertex in Alphabetical order
		Collections.sort(listofadjVertex, new LexicographicComparatorVertex());
		for (int i = 0; i < listofadjVertex.size(); i++) {
			//System.out.println("  " + listofadjVertex.get(i).name);
			System.out.println("   " + listofadjVertex.get(i).name);
		}
		// System.out.println(" "+v.name);
	}
    /*This method is used to print the graph as per the requirement*/
	public void printGraphsConnects(Graph g) {

		TreeMap<String, Vertex> vertexMapSorted = new TreeMap<String, Vertex>();
		for (Entry<String, Vertex> entry : g.vertexMap.entrySet()) {
			//System.out.println(entry.getKey() + "--" + entry.getValue());
			vertexMapSorted.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Vertex> entry1 : vertexMapSorted.entrySet()) {
			//System.out.println(entry1.getKey() + "--" + entry1.getValue());

			Vertex v = entry1.getValue();
			if(v.vertex_status.equals("down")){
				System.out.println(v.name+" "+"DOWN");	
			}else{
				System.out.println(v.name);
			}
			
			ArrayList<Edge> listofadjEdge = new ArrayList<Edge>();
			for (Edge e : v.adj) {
				listofadjEdge.add(e);
				Collections.sort(listofadjEdge, new LexicographicComparator());
			}

			for (int i = 0; i < listofadjEdge.size(); i++) {
				if(listofadjEdge.get(i).edge_status.equals("down")){
					//System.out.println(listofadjEdge.get(i).dest.name + " " + listofadjEdge.get(i).cost);
					System.out.println("  "+listofadjEdge.get(i).dest.name+" "+ listofadjEdge.get(i).cost+" "+"DOWN");
				}else{
					//System.out.println(listofadjEdge.get(i).dest.name + "  " + listofadjEdge.get(i).cost);
					System.out.println("  "+listofadjEdge.get(i).dest.name  + " " + listofadjEdge.get(i).cost);
				}		
			}
		}
	}
	/*This method is used to print all reachable points from each vertex*/
	public void reachableVertex(Graph g) {

		TreeMap<String, Vertex> vertexMapSorted = new TreeMap<String, Vertex>();
		for (Entry<String, Vertex> entry : g.vertexMap.entrySet()) {
			vertexMapSorted.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Vertex> entry1 : vertexMapSorted.entrySet()) {
			g.graphTraversalBFS(entry1.getKey());
		}

	}
     /*Program execution begin from this method
	 It reads both network file and queries file
	 Constructs a bidirectional graph from network file(the first argument)  
	 Read the query file and and excuted each query and prints the response in output file*/
	public static void main(String[] args) throws IOException {
		Graph g = new Graph();
		try {
			FileReader fin = new FileReader(args[0]);
			BufferedReader graphFile = new BufferedReader(fin);
			// Read the edges and insert
			String line;
			while ((line = graphFile.readLine()) != null) {
				//System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);

				try {
					if (st.countTokens() != 3) {
						System.err.println("Skipping ill-formatted line " + line);
						continue;
					}
					// System.out.println("correct");
					String source = st.nextToken();
					String dest = st.nextToken();
					double cost = Double.parseDouble(st.nextToken());
					g.addEdge(source, dest, cost);
					g.addEdge(dest, source, cost);
				} catch (NumberFormatException e) {
					System.err.println(" catch..Skipping ill-formatted line " + line);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		/*System.out.println("File read...");
		System.out.println(g.vertexMap.size() + " vertices");
		System.out.println(g.vertexMap);*/

		/*FileReader fquery = new FileReader(args[1]);
		BufferedReader queryFile = new BufferedReader(fquery);*/
		pwriter = new PrintWriter(System.out);
		// Read the edges and insert
		String query;
		boolean loop_break = false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (!loop_break) {
			query = reader.readLine();
			if(query==null){
				System.out.println("End of Input : Hence Exiting!");
				loop_break=true;
				continue;
			}
				
			String queries[] = query.split(" ");
			String queryToken = queries[0];
			// Using switch to excecute each case according to query from file
			switch (queryToken) {
			case "print": {
				g.printGraphsConnects(g);
				break;
			}
			case "addedge": {
				String sou = queries[1];
				String des = queries[2];
				double cos = Double.valueOf(queries[3]);
				g.addEdge(sou, des, cos);
				break;
			}
			case "deleteedge": {
				String sou = queries[1];
				String des = queries[2];
				g.deleteEdge(sou, des);
				break;
			}
			case "edgedown": {
				String sou = queries[1];
				String des = queries[2];
				g.edgeDown(sou, des);
				break;
			}
			case "edgeup": {
				String sou = queries[1];
				String des = queries[2];
				g.edgeUp(sou, des);
				break;
			}
			case "vertexdown": {
				for(int i=1; i<queries.length;i++)
					g.vertexDown(queries[i]);
				break;
			}
			case "vertexup": {
				for(int i=1; i<queries.length;i++)
					g.vertexUp(queries[i]);
				break;
			}
			case "path": {
				String sou = queries[1];
				String des = queries[2];
				g.shortestPath(sou);
				g.printPath(des);
				break;
			}
			case "reachable": {
				g.reachableVertex(g);	
				break;
			}
			case "quit": {
				  loop_break=true;
				  break;
			}
			default:
               System.out.println("Query not identified");
			}
		}
		pwriter.flush();
		pwriter.close();
	}
}