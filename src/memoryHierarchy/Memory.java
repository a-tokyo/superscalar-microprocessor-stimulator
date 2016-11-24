package memoryHierarchy;

public class Memory {
	String [] main_memory; // Main memory array, length should be 2^16
	int access_time; // Input: Number of cycles to access
	int total_cycles; // Output: Total number of cycles in the current stimulation.
	
	public Memory(int access_time) {
		this.main_memory = new String[65536]; // 2^16
		this.access_time = access_time;
		this.total_cycles = 0;
	}

	public int getAccess_time() {
		return access_time;
	}

	public int getTotal_cycles() {
		return total_cycles;
	}
	
	public String memoryToString(){
		StringBuilder toReturn = new StringBuilder();
		for(int i = 0; i < this.main_memory.length; i++) {
			toReturn.append("[Address: " + i + ", Data: "+ main_memory[i]+"]");
			if (i % 16 == 0 && i!=0){ // for readability the memory is split to 4096 lines.
				toReturn.append("\n");
			}else{
				toReturn.append(", ");
			}
		}
		return toReturn.toString();
	}
	
	public String memoryToString(int start_address, int end_address){
		StringBuilder toReturn = new StringBuilder();
		if(start_address<0 || end_address>= this.main_memory.length || end_address-start_address<0){
			return "Memory indeces: " + start_address + ", " + end_address + " are invalid.";	
		}
		for(int i = start_address; i <= end_address && i < this.main_memory.length && i>=0; i++) {
			toReturn.append("[Address: " + i + ", Data: "+ main_memory[i]+"]");
			if (i % 16 == 0 && i!=0){ // for readability the memory is split to 4096 lines.
				toReturn.append("\n");
			}else{
				toReturn.append(", ");
			}
		}
		return toReturn.toString();
	}
	
//	public static void main(String[] args) {
//		Memory x = new Memory (10);
//		System.out.println(x.memoryToString(0,87));
//	}
	
}