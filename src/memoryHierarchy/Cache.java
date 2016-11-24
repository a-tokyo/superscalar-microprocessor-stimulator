package memoryHierarchy;

public class Cache {
	int size; // size of cache
	int lineSize; // line size of cache 
	int m; // associativity
	
	int totalHits;
	int totalMisses;
	
	public int getSize() {
		return size;
	}
	public int getLine_size() {
		return lineSize;
	}
	public int getM() {
		return m;
	}
	public int getTotal_hits() {
		return totalHits;
	}
	public int getTotal_misses() {
		return totalMisses;
	}
}