import java.util.EnumMap;
import java.util.Map;




public class Instruction {
	
	public static String [] ARITHMETIC={"add","sub","mult","lshift","rshift"};
	public static String LOAD="load";
	public static String STORE="store";
	public static String LOADI="loadI";
	public static String OUTPUT="output";
	
	public static enum OPCODE {add, sub, mult, lshift, rshift, load, loadI, store, output, nop};
	public static Map<OPCODE, Integer> DELAYMAP = new EnumMap<OPCODE, Integer>(OPCODE.class);
	static
	{
		DELAYMAP.put(OPCODE.add, 1);
		DELAYMAP.put(OPCODE.sub, 1);
		DELAYMAP.put(OPCODE.mult, 3);
		DELAYMAP.put(OPCODE.lshift, 1);
		DELAYMAP.put(OPCODE.rshift, 1);
		DELAYMAP.put(OPCODE.load, 5);
		DELAYMAP.put(OPCODE.loadI, 1);
		DELAYMAP.put(OPCODE.store, 5);
		DELAYMAP.put(OPCODE.output, 1);
		DELAYMAP.put(OPCODE.nop, 1);
	}

	private int index;
	private OPCODE opcode;
	private Register source1, source2;
	private Register target;
	private int immediateValue;
	
	public Instruction(OPCODE opcode)
	{
		this.opcode=opcode;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Instruction in = (Instruction) obj;
		return this.opcode == in.opcode && 
			   this.source1 == in.source1 &&
			   this.source2 == in.source2 &&
			   this.target == in.target &&
			   this.immediateValue == in.immediateValue;
	}
	
	@Override
	public int hashCode()
	{
		return opcode.hashCode() + source1.hashCode() + source2.hashCode() + target.hashCode() + immediateValue;
	}
	
	public String toString()
	{	
		String s=""+opcode;
		
		if(opcode==OPCODE.output)
		{
			s+=" "+immediateValue;	
		}
		else if(opcode==OPCODE.loadI)
		{
			s+=" "+immediateValue+" => "+target;	
		}
		else if(opcode==OPCODE.store)
		{
			s+=" "+source1+" => "+source2;
		}
		else if(opcode==OPCODE.load)
		{
			s+=" "+source1+" => "+target;	
		}
		else if(opcode==OPCODE.nop)
		{
			//s=s.substring(0, s.length()-1);
		}
		else
		{
			s+=" "+source1+", "+source2+" => "+target;
		}	
		
		return s;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


	public OPCODE getOpcode() {
		return opcode;
	}

	public void setOpcode(OPCODE opcode) {
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
	
	public static boolean isArithmetic(String opcode)
	{
		boolean b=false;
		for(int i=0;i<ARITHMETIC.length;i++)
		{
			if(opcode.equals(ARITHMETIC[i]))
			{
				b=true;
			}
		}
		
		return b;
	}

	public int getDelay() {
		return DELAYMAP.get(opcode);
	}

}
