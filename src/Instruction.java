


public class Instruction {
	
	public static String [] validOpcode={"load","store","add","sub","mult","lshift","rshift","loadI","output"};
	public static String [] validOpcodeWithSource1Source2Target={"add","sub","mult","lshift","rshift"};
	public static String validOpcodeWithSource1Target="load";
	public static String validOpcodeWithSource1Source2="store";
	public static String validOpcodeWithTargetImmediateValue="loadI";
	public static String validOpcodeWithImmediateValue="output";

	private int index;
	private String opcode;
	private Register source1, source2;
	private Register target;
	private int immediateValue;
	
	public Instruction(String opcode)
	{
		this.opcode=opcode;
	}
	
	public String toString()
	{	
		//String s=index+": "+opcode+" ";
		String s=opcode+" ";
		
		if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
		{
			s+=" "+source1+" "+source2+" => "+target;	
		}
		else if(opcode.equals(Instruction.validOpcodeWithImmediateValue))
		{
			s+=" "+immediateValue;	
		}
		else if(opcode.equals(Instruction.validOpcodeWithTargetImmediateValue))
		{
			s+=" "+immediateValue+" => "+target;	
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
		{
			s+=" "+source1+" => "+source2;
		}
		else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
		{
			s+=" "+source1+" => "+target;	
		}
		
		return s;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	public Register getSource1() {
		return source1;
	}
	public void setSource1(Register source1) {
		this.source1 = source1;
	}
	public Register getSource2() {
		return source2;
	}
	public void setSource2(Register source2) {
		this.source2 = source2;
	}
	public Register getTarget() {
		return target;
	}
	public void setTarget(Register target) {
		this.target = target;
	}
	public int getImmediateValue() {
		return immediateValue;
	}
	public void setImmediateValue(int immediateValue) {
		this.immediateValue = immediateValue;
	}
	
	public static boolean isValidOpcodeWithSource1Source2Target(String opcode)
	{
		boolean b=false;
		for(int i=0;i<validOpcodeWithSource1Source2Target.length;i++)
		{
			if(opcode.equals(validOpcodeWithSource1Source2Target[i]))
			{
				b=true;
			}
		}
		
		return b;
	}

}
