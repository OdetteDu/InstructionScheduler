import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class DependencyGraphCreater {
	
	private ArrayList<Instruction> instructions;
	
	public DependencyGraphCreater(ArrayList<Instruction> instructions)
	{
		this.instructions = instructions;
	}
	
	public void create()
	{
		HashMap<Register, Node> waitForPredecessor = new HashMap<Register, Node>();
		HashSet<Node> roots = new HashSet<Node>();
		HashSet<Node> leaves = new HashSet<Node>();
		
		int i=instructions.size()-1;
		while(i>=0)
		{
			Instruction instruction=instructions.get(i);
			instruction.setIndex(i);
			Node currentNode = new Node(instruction);
			leaves.add(currentNode);
			
			String opcode = instruction.getOpcode();
			if(Instruction.isValidOpcodeWithSource1Source2Target(opcode))
			{
				//s+=" "+source1+" "+source2+" => "+target;	
				Register target=instruction.getTarget();
				//define
				Node successor = waitForPredecessor.remove(target);
				if(successor != null)
				{
					successor.addPredecessor(currentNode);
					leaves.remove(successor);
				}
				else
				{
					roots.add(currentNode);
				}
				
				Register source1=instruction.getSource1();
				//use
				waitForPredecessor.put(source1, currentNode);
				
				Register source2=instruction.getSource2();
				//use
				waitForPredecessor.put(source2, currentNode);
			}
			else if(opcode.equals(Instruction.validOpcodeWithImmediateValue))
			{
				//s+=" "+immediateValue;	
			}
			else if(opcode.equals(Instruction.validOpcodeWithTargetImmediateValue))
			{
				//s+=" "+immediateValue+" => "+target;	
				Register target=instruction.getTarget();
				//define
				Node successor = waitForPredecessor.remove(target);
				if(successor != null)
				{
					successor.addPredecessor(currentNode);
					leaves.remove(successor);
				}
				else
				{
					roots.add(currentNode);
				}
			}
			else if(opcode.equals(Instruction.validOpcodeWithSource1Source2))
			{
				//s+=" "+source1+" => "+source2;
				Register source1=instruction.getSource1();
				//use
				waitForPredecessor.put(source1, currentNode);
				
				Register source2=instruction.getSource2();
				//use
				waitForPredecessor.put(source2, currentNode);
			}
			else if(opcode.equals(Instruction.validOpcodeWithSource1Target))
			{
				//s+=" "+source1+" => "+target;	
				Register target=instruction.getTarget();
				//define
				Node successor = waitForPredecessor.remove(target);
				if(successor != null)
				{
					successor.addPredecessor(currentNode);
					leaves.remove(successor);
				}
				else
				{
					roots.add(currentNode);
				}
				
				Register source1=instruction.getSource1();
				//use
				waitForPredecessor.put(source1, currentNode);
			}
			
//			if(instruction.getTarget()!=null)
//			{
//				Register target=instruction.getTarget();
//				//define
//				Node successor = waitForPredecessor.remove(target);
//				if(successor != null)
//				{
//					successor.addPredecessor(currentNode);
//					leaves.remove(successor);
//				}
//				else
//				{
//					roots.add(currentNode);
//				}
//			}

//			if(instruction.getSource1()!=null)
//			{
//				Register source1=instruction.getSource1();
//				//use
//				waitForPredecessor.put(source1, currentNode);
//			}
//
//			if(instruction.getSource2()!=null)
//			{
//				Register source2=instruction.getSource2();
//				//use
//				waitForPredecessor.put(source2, currentNode);
//			}
			i--;
		}
		
		Printer.print(instructions);
		HashSet<Node> printer = new HashSet<Node>();
		Iterator<Node> iter = roots.iterator();
		while (iter.hasNext())
		{
			Printer.print(iter.next(),printer);
		}
	}

}
