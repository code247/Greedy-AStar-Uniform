import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class ReadInput {
	public static int size;
	public static int[][] mat;
	public static ArrayList<String> cities = new ArrayList<>();
	public static HashMap<Integer, String> mymap = new HashMap<Integer, String>();
	public static HashMap<Integer, Double[]> latlongmap = new HashMap<Integer, Double[]>();
	
	//Initializing different variables which are to be used throughout the implementation in the constructor
	public ReadInput() throws IOException{
		cities = readCity();
		size = cities.size();
		mat = new int[size][size];
		mat = readEdges();
		mymap = keyCity();
		latlongmap = readLatLong();
	}
	
	//Implementing a method to read the names of all cities from given file
	public ArrayList<String> readCity()throws IOException {
        String line = null;
		FileReader fileReader = new FileReader("usroads.txt");
		BufferedReader b = new BufferedReader(fileReader);
		try {
			while((line = b.readLine()) != null) {
				//Removing all the spaces in each line 
				line = line.replaceAll("\\s","");
				if(line.contains("city")) {
					String a = line.substring(5, line.indexOf(","));
					int t = line.indexOf(",") + 1;
					if(!cities.contains(a))
						cities.add(a);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			b.close();
		}
		return cities;
	}
	
	//Implementing a method to store the cities in a HashMap	
	public HashMap<Integer, String> keyCity()throws IOException {
		for(int i =0; i<size;i++){
			mymap.put(i, cities.get(i));
		}
		return mymap;
	}
	
	//Implementing a method to read the Latitude and Longitude values of all cities from given file and store them in HashMap
	public HashMap<Integer, Double[]> readLatLong()throws IOException {
        int index = 0;
		String line = null;
		FileReader fileReader = new FileReader("usroads.txt");
		BufferedReader b = new BufferedReader(fileReader);
		try {
			while((line = b.readLine()) != null) {
				line = line.replaceAll("\\s","");
				if(line.contains("city")){
					String c = line.substring(line.indexOf(",") + 1,line.lastIndexOf(","));
					String d = line.substring(line.lastIndexOf(",")+1,line.lastIndexOf(")"));
					Double lat = Double.parseDouble(c);
					Double lon = Double.parseDouble(d);
					Double[] temp = new Double[]{lat,lon};
					latlongmap.put(index, temp);
					index += 1;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			b.close();
		}
		return latlongmap;
	}
	
	//Implementing a method to store the edges and their weights in a adjacency matrix
	public int[][] readEdges() throws IOException {
		String line = null;
		FileReader fileReader = new FileReader("usroads.txt");
		BufferedReader b = new BufferedReader(fileReader);
		try {
			while((line = b.readLine()) != null) {
				line = line.replaceAll("\\s","");
				if(line.contains("road")) {
					String a = line.substring(5, line.indexOf(","));
					String c = line.substring(line.indexOf(",") + 1,line.lastIndexOf(","));
					int cost = Integer.parseInt(line.substring(line.lastIndexOf(",")+1,line.lastIndexOf(")")));
					int x = cities.indexOf(a);
					int y = cities.indexOf(c);
					mat[x][y] = cost;
					mat[y][x] = cost;
				}
			}
		}
		finally {
			b.close();
		}
		return mat;
	}
	
	//Implementing a method to return the weight of edge between given cities
	public int returnCost(int x, int y) throws IOException{
		return mat[x][y];
	}
	
	//Implementing a method to return the Latitude value of a given city
	public double returnLatitude(int x)throws IOException {
		Double[] temp = latlongmap.get(x);
		return temp[0];
	}
	
	//Implementing a method to return the Longitude value of a given city
	public double returnLongitude(int x)throws IOException {
		Double[] temp = latlongmap.get(x);
		return temp[1];
	}
	
	//Implementing a method to calculate the Heuristic value for given set of two cities
	public double calcHeur(int a, int b) throws IOException {
		double Lat1 = returnLatitude(a);
		double Lat2 = returnLatitude(b);
		double Long1 = returnLongitude(a);
		double Long2 = returnLongitude(b);
		double a1 = Math.pow((69.5 * (Lat1 - Lat2)),2);
		double b1 = 69.5 * Math.cos((Lat1 + Lat2)/360 * Math.PI);
		double c1 = Long1 - Long2;
		double d1 = b1 * c1;
		double d = a1 + Math.pow(d1, 2);
		double h = Math.sqrt(d);
		return h;
	}
	
	//Following implementation is inspired from a Stack Overflow post
	public int returnIndex(String a)throws IOException {
		for (Entry<Integer, String> entry : mymap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(a)) {
                return entry.getKey();
            }
        }
		return -1;
	}
}
