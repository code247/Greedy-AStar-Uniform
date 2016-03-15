import java.io.*;
import java.util.*;

public class SearchUSA {
	int[][] adj;
	int[] visited;
	int size;
	int count;
	public static double tcost;
	HashMap<Integer, String> g;
	
	//Initializing different variables which are to be used throughout the implementation in the constructor
	public SearchUSA() throws IOException {
		ReadInput r = new ReadInput();
		count = 0;
		g = r.keyCity();
		size = g.size();
		//storing the edges between various cities in a adjacency matrix with respective edge weights
		adj = r.readEdges();
		//maintaining an array of keys of nodes which are visited
		visited = new int[size];
	}
	
	//Defining a 'Node' data structure for storing the cities while traversing the map
	public class Node {
		public int key;
		public Double cost;
		public Node parent;
		
		public Node(int a){
			this.key = a;
			this.cost = 0.0;
			this.parent = null;
		}
	}
	
	//Node-oriented implementation of A* search algorithm
	public void astar(int a, int b)throws IOException {
		boolean d = false;
		int goal = b;
		ReadInput r = new ReadInput();
		//Initializing a Priority Queue of Nodes with a special Comparator defined
		PriorityQueue<Node> q = new PriorityQueue<Node>(size, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
		        try {
		        	//Calculating the Heuristic value i.e. h(n) for each node
		        	Double x = r.calcHeur(n1.key, goal);
		        	Double y = r.calcHeur(n2.key, goal);
		        	//Calculating f(n) for the given nodes by adding g(n) and h(n)
					n1.cost = n1.parent.cost + r.returnCost(n1.key, n1.parent.key) + x;
					n2.cost = n2.parent.cost + r.returnCost(n2.key, n2.parent.key) + y;
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(n1.cost < n2.cost)
					return -1;
				if(n1.cost > n2.cost)
					return 1;
				return 0;
		    }
		});
		Node n = new Node(a);
		n.parent = null;
		tcost = 0.0;
		q.add(n);
		System.out.print("List of expanded nodes: ");
		while(!q.isEmpty() && d == false){
			//For each iteration, popping the Node at the front of queue
			Node head = q.poll();
			if(head.parent != null) 
				head.cost = head.parent.cost + r.returnCost(head.key, head.parent.key);
			visited[head.key] = 1;
			if(head.key == b) {
				printPath(head);
				d = true;
				break;
			}
			else
				print(g.get(head.key));
			ArrayList<Integer> k = new ArrayList<>();
			//Storing the unvisited children of a particular node in an ArrayList
			k = getUnvisitedChild(head.key);
			//Adding the obtained children to queue
			for(int j = 0; j<k.size();j++){
				if(!q.contains(k.get(j))){
					Node t = new Node(k.get(j));
					t.parent = head;
					q.add(t);
				}
			}			
		}
	}
	
	public void greedy(int a, int goal)throws IOException{
		boolean d = false;
		ReadInput r = new ReadInput();
		PriorityQueue<Node> q = new PriorityQueue<Node>(size,new Comparator<Node>(){
			@Override
			public int compare(Node a, Node b){
				Double x = 0.0;
				Double y = 0.0;
				try {
					//Calculating the Heuristic value i.e. h(n) for each node
					x = r.calcHeur(a.key, goal);
					y = r.calcHeur(b.key, goal);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				if(x < y)
					return -1;
				if(x > y)
					return 1;
				return 0;		
			}
		});
		Node n = new Node(a);
		n.parent = null;
		q.add(n);
		System.out.print("List of expanded nodes: ");
		while(!q.isEmpty() && d == false){
			Node head = q.poll();
			if(head.parent != null) 
				head.cost = head.parent.cost + r.returnCost(head.key, head.parent.key);
			visited[head.key] = 1;
			if(head.key == goal) {
				printPath(head);
				d = true;
				break;
			}
			print(g.get(head.key));
			ArrayList<Integer> k = new ArrayList<>();
			k = getUnvisitedChild(head.key);
			for(int j = 0; j<k.size();j++){
				Node t = new Node(k.get(j));
				if(!q.contains(t)){
					t.parent = head;
					q.add(t);
				}
			}
		}
	}
	
	public void uniform(int a, int b) throws IOException {
		boolean d = false;
		ReadInput r = new ReadInput();
		//HashMap<Integer,Double> temp = new HashMap<Integer,Double>();
		PriorityQueue<Node> q = new PriorityQueue<Node>(size, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
		        try {
		        	//Calculating the path cost g(n) for each node
					n1.cost = n1.parent.cost + r.returnCost(n1.key, n1.parent.key);
					n2.cost = n2.parent.cost + r.returnCost(n2.key, n2.parent.key);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(n1.cost < n2.cost)
					return -1;
				if(n1.cost > n2.cost)
					return 1;
				return 0;
		    }
		});
		Node n = new Node(a);
		n.parent = null;
		q.add(n);
		System.out.print("List of expanded nodes: ");
		while(!q.isEmpty() && d == false){
			Node head = q.poll();
			//if(head.parent != null) 
				//head.cost = head.parent.cost + r.returnCost(head.key, head.parent.key);
			visited[head.key] = 1;
			if(head.key == b) {
				printPath(head);
				d = true;
				break;
			}
			else
				print(g.get(head.key));
			ArrayList<Integer> k = new ArrayList<>();
			k = getUnvisitedChild(head.key);
			for(int j = 0; j<k.size();j++){
				Node t = new Node(k.get(j));
				if(!q.contains(t)){
					t.parent = head;
					t.cost = t.parent.cost + r.returnCost(t.key, t.parent.key);
					//temp.put(t.key, t.cost);
					q.offer(t);
				}
			}
		}
	}
	
	//Implementing a method to return the ArrayList which contains unvisited children of a node
	public ArrayList<Integer> getUnvisitedChild(int x){
		ArrayList<Integer> children = new ArrayList<>();
		for(int i=0;i<adj[0].length;i++){
			if(adj[x][i] != 0 && visited[i]== 0){
				Integer t = new Integer(i);
				if(!children.contains(t.intValue()))
					children.add(t);
			}
		}
		return children;
	}
	
	//Implementing a method to print the solution path by iterating over parent of each node
	public void printPath(Node goal){
		System.out.println("\nTotal number of expanded nodes: "+ count);
		System.out.print("\nSolution path: ");
		Node x = goal;
		tcost = x.cost;
		ArrayList<Node> ans = new ArrayList<>();
		while(x.parent != null){
			ans.add(x);
			x = x.parent;
		}		
		ans.add(x);
		
		for(int i = ans.size()-1; i>=0;i--)
			System.out.print(g.get(ans.get(i).key)+ ", ");
		System.out.print("\nTotal number of nodes in solution path: "+ans.size());
		System.out.print("\nTotal cost of path: "+ tcost);
	}
	
	//Printing each node after expanding it to console
	public void print(String s){
		count++;
		System.out.print(s+ ", ");
	}
	
	public static void main(String[] args) throws IOException {
		ReadInput r = new ReadInput();
		SearchUSA s = new SearchUSA();
		String a, b, c;
		
		//Reading inputs from the three command line arguments
		a = args[0];
		b = args[1];
		c = args[2];
		int x = r.returnIndex(b);
		int y = r.returnIndex(c);
		
		if("astar".equalsIgnoreCase(a)){
			s.astar(x,y);
		}
		if("greedy".equalsIgnoreCase(a)){
			s.greedy(x,y);
		}
		if("uniform".equalsIgnoreCase(a)){
			s.uniform(x,y);
		}
	}
}
