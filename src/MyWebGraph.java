import java.util.ArrayList;
import java.util.HashMap;

public class MyWebGraph {
	HashMap<String, WebVertex> vertexList; 
	
	public MyWebGraph () {
		vertexList = new HashMap<String, WebVertex>();
	}
	
	public boolean addVertex(String s) {
		if (s == null || s.isEmpty() || s.trim().isEmpty()) return false;

		if (vertexList.containsKey(s)) return false;
		else {
			vertexList.put(s, new WebVertex(s));
			return true;
		}
	}

	public boolean addEdge(String s, String t) {
		if (s == null || s.isEmpty() || t == null || t.isEmpty()) return false;

		if (vertexList.containsKey(s) && vertexList.containsKey(t)) {
			return vertexList.get(s).addEdge(t);
		} else return false;
	}
	
	public ArrayList<String> getVertices() {
		return new ArrayList<>(vertexList.keySet());
    } 
    
    public ArrayList<String> getEdgesInto(String v) {
		if (v == null || v.isEmpty() || v.trim().isEmpty()) return new ArrayList<>();

		ArrayList<String> indegrees = new ArrayList<>();
		for (String url : this.getVertices()) {
			if (vertexList.get(url).containsEdge(v)) indegrees.add(url);
		}

		return indegrees;
    } 
    
    public ArrayList<String> getNeighbors(String url) {
        return vertexList.get(url).getNeighbors();
    } 

    public int getOutDegree(String url) {
    	// NullPointerException raised if there's no vertex with specified url
        return vertexList.get(url).links.size();
    }        
    
    public void setPageRank(String url, double pr) {
        vertexList.get(url).rank = pr;
    }
    
    public double getPageRank(String url) {
        if (vertexList.containsKey(url)) 
        	return (vertexList.get(url)).rank;
        
        return 0;
    }

    public boolean setVisited(String url, boolean b) {
        if (vertexList.containsKey(url)) {
        	(vertexList.get(url)).visited = b;
        	return true;
        }
        return false;
    }

    public boolean getVisited(String url) {
        if (vertexList.containsKey(url)) 
        	return (vertexList.get(url)).visited;
        
        return false;
    }

       
    public String toString() {
    	String info = "";
        for (String s: vertexList.keySet()) {
        	info += s.toString() + "\n";
        }
        return info;
    }
	
    public class WebVertex {
		private String url;
		private ArrayList<String> links;
		private boolean visited;
		private double rank;
		
		public WebVertex (String url) {
			this.url = url;
			this.links = new ArrayList<String>();
			this.visited = false;
			this.rank = 0;
		}
		
		
		public boolean addEdge(String v) {
			if (!this.links.contains(v)) {
				this.links.add(v);
				return true;
			}
			return false;
		}
		
		
	    public ArrayList<String> getNeighbors() {
	        return this.links;
	    } 
	    
	    
	    public boolean containsEdge(String e) {
	    	return this.links.contains(e);
	    }
		
	    
		public String toString() {
			return this.url + "\t" + this.visited + "\t" + this.rank;
		}
		
	}
	
	

}
