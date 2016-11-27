package assembler;
import java.util.Arrays;

import utils.Utils;

public class Assembler {
	
	/*
	 * This one takes an assembly instruction in string form, case insensitive and space insensitive
	 * Returns A string containing the corresponding binary machine code in 16 bits
	 * Exceptions thrown: illegal argument exception if the register entered is not between reg0 and reg7 inclusive
	 * or if the instruction is not the microprocessor's instruction set
	 */
	public static String assemble(String instruction){
		StringBuilder machineCodeBuilder = new StringBuilder();
		instruction = instruction.trim().toLowerCase();
		String keyword = instruction.substring(0, instruction.indexOf(" "));
		String [] instructionOperands = instruction.substring(instruction.indexOf(" ")+1, instruction.length()).split(",");
		// just like javascript maping, trimming white spaces from all elements off array
		Arrays.stream(instructionOperands).map(String::trim).toArray(unused -> instructionOperands);
		switch (keyword) {
		case "lw":
			machineCodeBuilder.append("000");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			
			break;
		case "sw":
			machineCodeBuilder.append("001");
			break;
		case "jmp":
			machineCodeBuilder.append("010");
			break;
		case "beq":
			machineCodeBuilder.append("011");
			break;
		case "jalr":
			machineCodeBuilder.append("100");
			break;
		case "ret":
			machineCodeBuilder.append("101");
			break;
		case "add":
			machineCodeBuilder.append("111");
			break;
		case "sub":
			machineCodeBuilder.append("111");
			break;
		case "addi":
			machineCodeBuilder.append("111");
			break;
		case "nand":
			machineCodeBuilder.append("111");
			break;
		case "mul":
			machineCodeBuilder.append("111");
			break;
		default: throw new IllegalArgumentException("Syntax Error, Invalid Instruction");
		}
		System.out.println(keyword);
		System.out.println(Utils.arrayStringToString(instructionOperands));
		return machineCodeBuilder.toString();
	}

	public static String registerToBinary(String register) {
		register = register.toLowerCase();
		if (!register.startsWith("reg")){
			throw new IllegalArgumentException("Syntax Error in Register field");
		}
		switch (Integer.parseInt(register.substring(3))) {
		case 0:
			return "000";
		case 1:
			return "001";
		case 2:
			return "010";
		case 3:
			return "011";
		case 4:
			return "100";
		case 5:
			return "101";
		case 6:
			return "110";
		case 7:
			return "111";
		}
		throw new IllegalArgumentException("Syntax Error, Invalid Register Number");
	}
	
	public static void main(String[] args) {
//		System.out.println(assemble("  lw reg7 , reg7, regC   "));
	}
}