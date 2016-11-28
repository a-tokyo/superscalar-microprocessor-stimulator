package tomasulo;

//import tomasulo.ReOrderBuffer;

import java.util.ArrayList;

import memoryHierarchy.*;
// import f

public class Tomasulo {
	
	// NOTE: according to the project specification, 
	// the tomasulo object needs to have the following parameters:
	// A memory hierarchy
	// a reorder buffer [which will contain the ROBEntries] (size specified by the user)
	// an instruction buffer (size specified by user)
	// a list of reservation stations (size specified by the user, via details of )
	// a register file, a register status table
	// a PC (the start point of which will be specified by the user)
	// Pipeline Width (specified by the user - number of maximum allowed simaltenuous instructions issued to reservation stations
	// a list of data items that specify the value and memory address
	// a list of the amount of reservation stations for each class of instructions (assume 1:1 station:functional unit map; specified by user)
	// the register status table
	// the register file
	int instruction_issued ; 
	int instruction_finished ;
	int branchehit;
	InstructionBuffer instruction_buffer;
	int no_cycle_spanned;
	public InstructionBuffer getInstructionBuffer() {
		return instructionBuffer;
	}


	public void setInstructionBuffer(InstructionBuffer instructionBuffer) {
		this.instructionBuffer = instructionBuffer;
	}


	public ArrayList<Integer> getIndicesOfRS_ToIssued() {
		return IndicesOfRS_ToIssued;
	}


	public void setIndicesOfRS_ToIssued(ArrayList<Integer> indicesOfRS_ToIssued) {
		IndicesOfRS_ToIssued = indicesOfRS_ToIssued;
	}

	MemoryHierarchy memoryHierarchy;
	ReOrderBuffer ROBuffer;
	InstructionBuffer instructionBuffer;
	ReservationStation[] reservationStations;
	RegisterFile regFile;
	RegisterStatusTable regStatusTable;
	int PC, endOfPC;
	int pipelineWidth;
	int howMany_instructionsFinishExecuting; // Number of instructions executed
	int howMany_MispredictionsHappen;
	ArrayList<Integer> IndicesOfRS_ToIssued; // Indices of RS(s) in the order which they were issued 
	
	public static final short fetchDelay  = 1;
	public static final short issueDelay  = 1;
	public static final short writeDelay  = 1;
	public static final short commitDelay = 1;
	
	
	// constructor for an instance of the tomasulo object; should be called from main
	// using parameters entered by the user
	public Tomasulo(int sizeOfROBuffer, int sizeOfInstructionBuffer, String[] infoOfFunctionalUnits, int PC, int endOfPC, int width) {
		
// 		this.ROBuffer = new ReOrderBuffer(
	}
	
	
	// need to implement the phases of Tomasulo's algorithm: fetch -> issue -> execute -> write result -> commit
	// and any other methods necessary to make them work
	
	public void fetch() {
		
	}
	
	public void issue() {
		
	}
	
	public void execute() { 

		for(int i = 0; i < this.reservationStations.length; i++) {
			ReservationStation temp_reservationStations = this.reservationStations[i];
			if(temp_reservationStations.busy) {
				if(temp_reservationStations.op.toLowerCase().equals("load")) {
					if(temp_reservationStations.Qj == -1) {
						String address = Integer.toBinaryString(temp_reservationStations.A);
						while(address.length() < 16)
							address = "0" + address;
						String address1 = Integer.toBinaryString(temp_reservationStations.A + 1);
						while (address1.length() < 16)
							address1 = "0" + address1;
						
						if(temp_reservationStations.islowSet)
							address = address1;
						
						if(temp_reservationStations.isAddress_calculated && Check_ifStore(temp_reservationStations.A)) {
							if(temp_reservationStations.cacheLevel == this.memoryHierarchy.caches.length) { // memory
								if(!this.memoryHierarchy.memory.isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
									temp_reservationStations.isCacheAccessed = true;
									if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
										temp_reservationStations.cacheLevel--;
										temp_reservationStations.isCacheAccessed = false;
									}
								}
							}
							else if(!this.memoryHierarchy.caches[temp_reservationStations.cacheLevel].isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
								temp_reservationStations.isCacheAccessed = true;
								if(temp_reservationStations.cacheLevel == 1) {
									String Byte = this.memoryHierarchy.loadData(address);
									if(Byte != null) {
										temp_reservationStations.isCacheAccessed = false;
										if(!temp_reservationStations.islowSet) {
											temp_reservationStations.lowByte = Byte;
											temp_reservationStations.islowSet = true;
											temp_reservationStations.cacheLevel = this.memoryHierarchy.getCorrespondingCacheLevel(address1);
										}else {
											String result = Byte + temp_reservationStations.lowByte;
											temp_reservationStations.result = Integer.parseInt(convert_to_Decimal(result));
											temp_reservationStations.isAddress_calculated = false;
											howMany_instructionsFinishExecuting++;
											temp_reservationStations.lowByte = null;
											temp_reservationStations.islowSet = false;
											temp_reservationStations.executionCycles_left = 0;
										}
									}
								}
								else if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
									temp_reservationStations.cacheLevel--;
									temp_reservationStations.isCacheAccessed = false;
								}
							}
						}
						else {
							temp_reservationStations.A = temp_reservationStations.FunctionalUnit.execute("calculate_addr", temp_reservationStations.Vj, temp_reservationStations.A);
							temp_reservationStations.isAddress_calculated = true;
							temp_reservationStations.cacheLevel = this.memoryHierarchy.getCorrespondingCacheLevel(address);
						}
					}
				}
				else if(temp_reservationStations.op.toLowerCase().equals("store")) {
					if(temp_reservationStations.Qj == -1 && !temp_reservationStations.startStoring) {
						this.ROBuffer.getBuffer()[temp_reservationStations.dest].setInstructionDestination ( temp_reservationStations.Vj + temp_reservationStations.A); // has2lfeha omar
						String address = Integer.toBinaryString(temp_reservationStations.Vj + temp_reservationStations.A);
						while (address.length() < 16)
							address = "0" + address;
						temp_reservationStations.cacheLevel = memoryHierarchy.getCorrespondingCacheLevel(address);
//						temp_reservationStations.store_cycles_left = mem_heirarchy.store_cycles_left(address);
						temp_reservationStations.startStoring = true;
						howMany_instructionsFinishExecuting++;
					}
				}
				else if(temp_reservationStations.op.toLowerCase().equals("jalr")) {
					temp_reservationStations.result = temp_reservationStations.Vk;
					temp_reservationStations.executionCycles_left = 0;
					howMany_instructionsFinishExecuting++;
				}
				else { //Arithmetic Operations and Branch
				if(temp_reservationStations.is_executionStart) {
					if(temp_reservationStations.executionCycles_left > 0)
						temp_reservationStations.executionCycles_left--;
					if(temp_reservationStations.executionCycles_left == 0) {
						temp_reservationStations.result = temp_reservationStations.FunctionalUnit.execute(temp_reservationStations.op, temp_reservationStations.Vj, temp_reservationStations.Vk);
						howMany_instructionsFinishExecuting++;
						if(temp_reservationStations.op.toLowerCase().equals("branch")) {
							if(temp_reservationStations.result == 1 && (temp_reservationStations.A < 0)) // 2 operands are equal, and Branch was taken
								temp_reservationStations.result = 1; // Correct Prediction
							else if(temp_reservationStations.result == 1 && (temp_reservationStations.A >= 0)) { // 2 operands are equal, and Branch was not taken
								temp_reservationStations.result = 0; // howMany_MispredictionsHappen
								this.howMany_MispredictionsHappen++;
							}
							else if(temp_reservationStations.result == 0 && (temp_reservationStations.A < 0)) { // 2 operands are not equal and Branch was taken
								temp_reservationStations.result = 0;
								this.howMany_MispredictionsHappen++;
							}
							else if(temp_reservationStations.result == 0 && (temp_reservationStations.A >= 0))  // 2 operands are not equal and branch was not taken
								temp_reservationStations.result = 1; // Correct Prediction
						}
					}
				}
				else if (temp_reservationStations.Qj == -1 && temp_reservationStations.Qk == -1) {
					temp_reservationStations.is_executionStart = true;
				}
			}
		}
	}
	}

	
	public void write() {
		int writes = 0; // Number of instruction that can be written
		for(int i = 0; i < this.IndicesOfRS_ToIssued.size(); i++ ) {
			ReservationStation temp_reservationStations = this.reservationStations[IndicesOfRS_ToIssued.get(i)];
			if(temp_reservationStations.op.toLowerCase().equals("store")) {
				if(temp_reservationStations.Qk == -1) {
					if(temp_reservationStations.startStoring) {
						String address = Integer.toBinaryString(temp_reservationStations.A);
						while(address.length() < 16)
							address = "0" + address;

						if(temp_reservationStations.islowBytewriten) {
							LowByteStoring(temp_reservationStations.Vk, temp_reservationStations.A);
							temp_reservationStations.islowBytewriten = false;
							temp_reservationStations.isHighByteWrite = true;
						}else if(temp_reservationStations.isHighByteWrite) {
							HighByteStoring(temp_reservationStations.Vk, temp_reservationStations.A + 1);
							int b = temp_reservationStations.dest;
							this.ROBuffer.getBuffer()[b].setInstructionValue(temp_reservationStations.Vk);
							this.ROBuffer.getBuffer()[b].setReady( true);
							
							temp_reservationStations.startStoring = false;
							temp_reservationStations.flushRS();
							temp_reservationStations.isHighByteWrite = false;	
											
							IndicesOfRS_ToIssued.remove(i);
							writes++;
							if(writes == this.instruction_issued)
								return;
							else
								i--;
						}
						else if(temp_reservationStations.cacheLevel == this.memoryHierarchy.caches.length) { // memory
							if(!this.memoryHierarchy.memory.isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
								temp_reservationStations.isCacheAccessed = true;
								if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
									temp_reservationStations.cacheLevel--;
									temp_reservationStations.isCacheAccessed = false;
								}
							}
						}
						else if(!this.memoryHierarchy.caches[temp_reservationStations.cacheLevel].isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
							temp_reservationStations.isCacheAccessed = true;
							if(temp_reservationStations.cacheLevel == 1 && this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
								temp_reservationStations.islowBytewriten = true;
							}
							else if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
								temp_reservationStations.cacheLevel--;
								temp_reservationStations.isCacheAccessed = false;
							}
						}
					}
				}
			}
			// All instructions but Store
			else if(temp_reservationStations.executionCycles_left == 0) {
				int b = temp_reservationStations.dest;
				
				this.ROBuffer.getBuffer()[b].setReady(true);
				this.ROBuffer.getBuffer()[b].setInstructionValue( temp_reservationStations.result);
				
				for (int j = 0; j < this.reservationStations.length; j++) {
					ReservationStation RS2 = this.reservationStations[j];
					if(RS2.Qj == b) {
						RS2.Vj = temp_reservationStations.result;
						RS2.Qj = -1;
					}
					if(RS2.Qk == b) {
						RS2.Vk = temp_reservationStations.result;
						RS2.Qk = -1;
					}
				}
				temp_reservationStations.flushRS();
				IndicesOfRS_ToIssued.remove(i);
				writes++;
				if(writes == this.instruction_issued)
					return;
				else
					i--;
			}
		}
	}
	
	public void commit() {
		for(int i = 0; i < this.instruction_issued; i++) {
			ROBEntry ROB_entry = this.ROBuffer.getEntry();
			if(ROB_entry == null)
				return;
			
			int dest = ROB_entry.getInstructionDestination(); //Reg or Mem Address if store
			if(ROB_entry.isReady()) { // if ready 
				if(ROB_entry.getInstructionType().toLowerCase().equals("store")) { 
					this.ROBuffer.deQueue();
					instruction_finished++;
				}
				else if(ROB_entry.getInstructionType().toLowerCase().equals("jmp")) {
					this.ROBuffer.deQueue();
					instruction_finished++;
				}
				else if(ROB_entry.getInstructionType().toLowerCase().equals("return")) {
					this.ROBuffer.deQueue();
					instruction_finished++;
				}
				else if(ROB_entry.getInstructionType().toLowerCase().equals("branch")) {
					this.branchehit++;
					instruction_finished++;
					if(ROB_entry.getInstructionValue()  == 1) { // if Branch was predicted correctly
						this.ROBuffer.deQueue();

					} else {  // if not
						this.PC = ROB_entry.PC__value;
						this.ROBuffer.cleanRS();
						this.regStatusTable.emptyRegisterStatusTable();
						flushreservation_stations();
						this.instruction_buffer.Flush();
						//this.low_byte = null;
						//this.low_byte_set = false;
						return;
					       }
				     }
				    else {
					this.regFile.registers[dest] = convert_to_Decimal(""+ROB_entry.getInstructionValue());
					this.regStatusTable.reorderBufferindex[dest] = -1; 
					this.ROBuffer.deQueue();
					instruction_finished++;
				}

			}
		}
	}
	
	
	private void flushreservation_stations() {
		for (int i = 0; i < this.reservationStations.length; i++) {
			reservationStations[i].flushRS();
		}	
	}
	

	
public static String convert_to_Decimal(String number){
		
		/* 
		 100111
		 (2^0)*1
		  */
		int factor=1;
		int result =0;
		int out=0;
		String convert_out="";
		String convert = number ;
		for(int i=convert.length()-1 ;i >-1 ;i--){
			result += factor * convert.charAt(i); //0101
			factor =factor*2;//factor=2  // 1*2
		}
		convert_out=""+result ;
		return convert_out;
		
	}

public static String convert_To_Binary(int number) {
	String temp = Integer.toBinaryString(number);
	if (number >= 0) {
		while(temp.length() < 16)
			temp = "0" + temp;
	}
	else {
		temp = temp.substring(16);
	}
	return temp;
}
	
	public MemoryHierarchy getMemoryHierarchy() {
	return memoryHierarchy;
}


public void setMemoryHierarchy(MemoryHierarchy memoryHierarchy) {
	this.memoryHierarchy = memoryHierarchy;
}


public ReOrderBuffer getROBuffer() {
	return ROBuffer;
}


public void setROBuffer(ReOrderBuffer rOBuffer) {
	ROBuffer = rOBuffer;
}


public ReservationStation[] getReservationStations() {
	return reservationStations;
}


public void setReservationStations(ReservationStation[] reservationStations) {
	this.reservationStations = reservationStations;
}


public RegisterFile getRegFile() {
	return regFile;
}


public void setRegFile(RegisterFile regFile) {
	this.regFile = regFile;
}


public RegisterStatusTable getRegStatusTable() {
	return regStatusTable;
}


public void setRegStatusTable(RegisterStatusTable regStatusTable) {
	this.regStatusTable = regStatusTable;
}


public InstructionBuffer getInstruction_buffer() {
	return instruction_buffer;
}


public void setInstruction_buffer(InstructionBuffer instruction_buffer) {
	this.instruction_buffer = instruction_buffer;
}


public int getPC() {
	return PC;
}


public void setPC(int pC) {
	PC = pC;
}


public int getEndOfPC() {
	return endOfPC;
}


public void setEndOfPC(int endOfPC) {
	this.endOfPC = endOfPC;
}


public int getPipelineWidth() {
	return pipelineWidth;
}


public void setPipelineWidth(int pipelineWidth) {
	this.pipelineWidth = pipelineWidth;
}


public int getInstruction_issued() {
	return instruction_issued;
}


public void setInstruction_issued(int instruction_issued) {
	this.instruction_issued = instruction_issued;
}


public int getInstruction_finished() {
	return instruction_finished;
}


public void setInstruction_finished(int instruction_finished) {
	this.instruction_finished = instruction_finished;
}


public int getBranchehit() {
	return branchehit;
}


public void setBranchehit(int branchehit) {
	this.branchehit = branchehit;
}


public int getNo_cycle_spanned() {
	return no_cycle_spanned;
}


public void setNo_cycle_spanned(int no_cycle_spanned) {
	this.no_cycle_spanned = no_cycle_spanned;
}


public int getHowMany_instructionsFinishExecuting() {
	return howMany_instructionsFinishExecuting;
}


public void setHowMany_instructionsFinishExecuting(int howMany_instructionsFinishExecuting) {
	this.howMany_instructionsFinishExecuting = howMany_instructionsFinishExecuting;
}


public int getHowMany_MispredictionsHappen() {
	return howMany_MispredictionsHappen;
}


public void setHowMany_MispredictionsHappen(int howMany_MispredictionsHappen) {
	this.howMany_MispredictionsHappen = howMany_MispredictionsHappen;
}


public static short getFetchdelay() {
	return fetchDelay;
}


public static short getIssuedelay() {
	return issueDelay;
}


public static short getWritedelay() {
	return writeDelay;
}


public static short getCommitdelay() {
	return commitDelay;
}


	public double AMAT(int cache_Level){
		if(cache_Level == 1)
			return (double) this.memoryHierarchy.caches[1].getAccessCycles();
		else {
			double result = 0;
			if(cache_Level == this.memoryHierarchy.caches.length)
				 result = this.memoryHierarchy.memory.getAccessTime();
			else
			     result = this.memoryHierarchy.caches[cache_Level].getAccessCycles();
			for(int i = 1; i < cache_Level; i++) {
				double missRate;
				if(i == 1) {
				missRate = (this.memoryHierarchy.caches[0].getMissRate() + this.memoryHierarchy.caches[1].getMissRate()) / (double) 2;
				}
				else {
				missRate = this.memoryHierarchy.caches[i].getMissRate();
				}
				result *= missRate;
			}
			return result + AMAT(cache_Level - 1);
		}
		
		
	}

public void simulateResults(){
//	no_cycle_spanned=0;
	while (!this.ROBuffer.isEmpty() && !this.instruction_buffer.Empty_Instruction_Buffer() && PC!=endOfPC){
		no_cycle_spanned++;
	}
	
	System.out.println("Total Execution Time is: " + this.no_cycle_spanned + " cycles");
	System.out.println("IPC is : " + (double)this.instruction_finished/ (double)this.no_cycle_spanned);
	System.out.println("AMAT is : " + AMAT(this.memoryHierarchy.caches.length));

	double branch_Mispredict= (double)howMany_MispredictionsHappen/(double)branchehit;
	System.out.println("Branch Misprediction Percentage: " + branch_Mispredict* 100 +"percent");
	
	for(int i = 0; i < this.memoryHierarchy.caches.length; i++) {
		String cache_Name;
		Cache cache = this.memoryHierarchy.caches[i];

		if(i == 0)
			cache_Name = "1 (Instruction is )";
		else if (i == 1)
			cache_Name = "1 (Data is )";
		else
			cache_Name = ""+i+" -->";
		
		double total_access=((double)cache.getHitRate() + (double) cache.getMissRate());
		double hitRatio = (double) cache.getHitRate() /total_access;
		System.out.println("Cache " + cache_Name + " hit ratio: " + hitRatio);
	}
	
	
}
	

	
	public boolean Check_ifStore(int A) {
		//Checks that all stores have a different memory address, then the Load mem address
		for(int i = 0; i < this.ROBuffer.getBuffer().length; i++) {
			ROBEntry b = this.ROBuffer.getBuffer()[i];
			if(b!= null && b.getInstructionType().toLowerCase().equals("store") && b.getInstructionDestination() == A)
				return false;
		}
		return true;
	}
	
	private void LowByteStoring(int vk, int A) {
		String address = Integer.toBinaryString(A);
		while (address.length() < 16)
			address = "0" + address;
		
		String value = convert_To_Binary(vk);
		this.memoryHierarchy.write(address, value.substring(8));
	}
	
	private void HighByteStoring(int vk, int A) {
		String address = Integer.toBinaryString(A);
		while (address.length() < 16)
			address = "0" + address;
		
		String value = convert_To_Binary(vk);
		this.memoryHierarchy.write(address, value.substring(0,8));
	}
	

	
	
}
