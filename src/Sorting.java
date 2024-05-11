import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Sorting {

    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
		ArrayList<Entry<K, V>> sorted = new ArrayList<>(results.entrySet());
		mergeSort(sorted);

		ArrayList<K> sortedUrls = new ArrayList<>();
		for (int i = 0; i < sorted.size(); i++) {
			sortedUrls.add(sorted.get(i).getKey());
		}
		return sortedUrls;
    }

	private static <K, V extends Comparable<V>> void mergeSort(ArrayList<Entry<K, V>> map) {
		int N = map.size();
		if (N < 2) return;

		int middle = N / 2;
		ArrayList<Entry<K, V>> map1 = new ArrayList<>(map.subList(0, middle));
		ArrayList<Entry<K, V>> map2 = new ArrayList<>(map.subList(middle, N));
		mergeSort(map1);
		mergeSort(map2);
		merge(map1, map2, map);
	}

	private static <K, V extends Comparable<V>> void merge(ArrayList<Entry<K, V>> map1, ArrayList<Entry<K, V>> map2, ArrayList<Entry<K, V>> map) {
		int i = 0;
		int j = 0;
		while (i + j < map.size()) {
			if (j == map2.size()) {
				map.set(i + j, map1.get(i));
				i++;
			} else if (i < map1.size() && map1.get(i).getValue().compareTo(map2.get(j).getValue()) > 0) {
				map.set(i + j, map1.get(i));
				i++;
			} else {
				map.set(i + j, map2.get(j));
				j++;
			}
		}
	}

}