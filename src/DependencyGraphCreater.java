import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class DependencyGraphCreater {
	
	private ArrayList<Instruction> instructions;
	private HashSet<Node> roots;
	private HashSet<Node> leaves;
	
	public DependencyGraphCreater(ArrayList<Instruction> instructions)
	{
		this.instructions = instructions;
		roots = new HashSet<Node>();
		leaves = new HashSet<Node>();
	}
	
	public void create()
	{
		HashMap<Register, Node> waitForPredecessor = new HashMap<Register, Node>();
		
		int i=instructions.size()-1;
		while(i>=0)
		{
			Instruction instruction=instructions.get(i);
			instruction.setIndex(i);
			Node currentNode = new Node(instruction);
			leaves.add(currentNode);
			
			Instruction.OPCODE opcode = instruction.getOpcode();
			
			if(opcode==Instruction.OPCODE.OUTPUT)
			{
				//s+=" "+immediateValue;	
			}
			else if(opcode==Instruction.OPCODE.LOADI)
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
			else if(opcode==Instruction.OPCODE.STORE)
			{
				//s+=" "+source1+" => "+source2;
				Register source1=instruction.getSource1();
				//use
				waitForPredecessor.put(source1, currentNode);
				
				Register source2=instruction.getSource2();
				//use
				waitForPredecessor.put(source2, currentNode);
			}
			else if(opcode==Instruction.OPCODE.LOAD)
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
			else
			{
				//s+=" "+source1+" "+source2+" => "+target;	
				Register target=instruction.getTarget();

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
				waitForPredecessor.put(source1, currentNode);
				
				Register source2=instruction.getSource2();
				waitForPredecessor.put(source2, currentNode);
			}
			i--;
		}
	}
	
	public void calculateDelay()
	{
		Iterator<Node> iter = roots.iterator();
		while(iter.hasNext())
		{
			calculateDelay(iter.next(), 0);
		}
	}
	
	private void calculateDelay(Node root, int delay)
	{
		delay += Instruction.DELAYMAP.get(root.getInstruction().getOpcode());
		int prevDelay = root.getDelay();
		if(delay > prevDelay)
		{
			root.setDelay(delay);
			ArrayList<Node> currentPredecessor = root.getPredecessors();
			for(int i=0; i<currentPredecessor.size(); i++)
			{
				Node currentNode = currentPredecessor.get(i);
				calculateDelay(currentNode, delay);
			}
		}
		else
		{
			//already have a bigger delay, no action needed.
		}
		
	}
	
	public void print()
	{
		Printer.print(instructions);
		HashSet<Node> printer = new HashSet<Node>();
		Iterator<Node> iter = roots.iterator();
		while (iter.hasNext())
		{
			Printer.print(iter.next(),printer);
		}
	}

}
