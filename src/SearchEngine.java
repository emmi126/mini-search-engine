import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	public void crawlAndIndex(String url) throws Exception {
		if (url == null || url.isEmpty() || url.trim().isEmpty()) return;
		dfs(url);
	}

	private void dfs(String currUrl) throws Exception {
		if (internet.getVisited(currUrl)) return;

		internet.setVisited(currUrl, true);
		internet.addVertex(currUrl);

		ArrayList<String> links = parser.getLinks(currUrl);
		for (int i = 0; i < links.size(); i++) {
			boolean vertexAdded = internet.addVertex(links.get(i));
			if (vertexAdded) dfs(links.get(i));
			internet.addEdge(currUrl, links.get(i));
		}

		ArrayList<String> words = parser.getContent(currUrl);
		for (int j = 0; j < words.size(); j++) {
			String word = words.get(j).toLowerCase();
			if (!wordIndex.containsKey(word)) wordIndex.put(word, new ArrayList<>());
			if (!wordIndex.get(word).contains(currUrl)) wordIndex.get(word).add(currUrl);
		}
	}

	public void assignPageRanks(double epsilon) {
		ArrayList<String> vertices = internet.getVertices();
		for (String v : vertices) {
			internet.setPageRank(v, 1.000);
		}
		recursiveHelper(vertices, epsilon, null);
	}

	private void recursiveHelper(ArrayList<String> vertices, double epsilon, ArrayList<Double> prevRanks) {
		ArrayList<Double> currRanks = computeRanks(vertices);
		for (int i = 0; i < vertices.size(); i++) {
			internet.setPageRank(vertices.get(i), currRanks.get(i));
		}

		if (prevRanks != null) {
			boolean finished = true;
			int i = 0;
			while (i < vertices.size() && finished) {
				if (Math.abs(prevRanks.get(i) - currRanks.get(i)) >= epsilon) {
					finished = false;
				}
				i++;
			}
			if (finished) return;
		}

		recursiveHelper(vertices, epsilon, currRanks);
	}

	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		ArrayList<Double> currRanks = new ArrayList<>();
		for (String v : vertices) {
			double rank = 0.5;
			for (String w : internet.getEdgesInto(v)) {
				rank += 0.5 * (internet.getPageRank(w) / internet.getOutDegree(w));
			}
			currRanks.add(rank);
		}
		return currRanks;
	}

	public ArrayList<String> getResults(String query) {
		if (query == null || query.isEmpty() || query.trim().isEmpty()) return new ArrayList<>();

		query = query.toLowerCase();
		ArrayList<String> results = new ArrayList<>();

		if (!wordIndex.containsKey(query)) return results;

		HashMap<String, Double> map = new HashMap<>();

		ArrayList<String> relevantUrls = wordIndex.get(query);
		for (String url : relevantUrls) {
			map.put(url, internet.getPageRank(url));
		}

		results = Sorting.fastSort(map);
		return results;
	}
}
