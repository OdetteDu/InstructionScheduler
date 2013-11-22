import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


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
	
	public HashSet<Node> getRoots() {
		return roots;
	}

	public void setRoots(HashSet<Node> roots) {
		this.roots = roots;
	}

	public HashSet<Node> getLeaves() {
		return leaves;
	}

	public void setLeaves(HashSet<Node> leaves) {
		this.leaves = leaves;
	}
	
	public void run()
	{
		create();
		calculatePriority();
		printTopDown();
	}

	private void create()
	{
		HashMap<Register, LinkedList<Node>> waitForPredecessor = new HashMap<Register, LinkedList<Node>>();
		
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
				//Node successor = waitForPredecessor.remove(target);
				LinkedList<Node> waits = waitForPredecessor.remove(target);
				if(waits != null)
				{
					Iterator<Node> iter = waits.iterator();
					while(iter.hasNext())
					{
						Node successor = iter.next();
						successor.addPredecessor(currentNode);
						currentNode.addSuccessor(successor);
						leaves.remove(successor);
					}
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
//				waitForPredecessor.put(source1, currentNode);
				LinkedList<Node> nodesForSource1 = waitForPredecessor.get(source1);
				if(nodesForSource1 != null)
				{
					nodesForSource1.add(currentNode);
					waitForPredecessor.put(source1, nodesForSource1);
				}
				else
				{
					nodesForSource1 = new LinkedList<Node>();
					nodesForSource1.add(currentNode);
					waitForPredecessor.put(source1, nodesForSource1);
				}
				
				Register source2=instruction.getSource2();
				//use
//				waitForPredecessor.put(source2, currentNode);
				LinkedList<Node> nodesForSource2 = waitForPredecessor.get(source2);
				if(nodesForSource2 != null)
				{
					nodesForSource2.add(currentNode);
					waitForPredecessor.put(source2, nodesForSource2);
				}
				else
				{
					nodesForSource2 = new LinkedList<Node>();
					nodesForSource2.add(currentNode);
					waitForPredecessor.put(source2, nodesForSource2);
				}
			}
			else if(opcode==Instruction.OPCODE.LOAD)
			{
				//s+=" "+source1+" => "+target;	
				Register target=instruction.getTarget();
				//define
//				Node successor = waitForPredecessor.remove(target);
//				if(successor != null)
//				{
//					successor.addPredecessor(currentNode);
//					currentNode.addSuccessor(successor);
//					leaves.remove(successor);
//				}
//				else
//				{
//					roots.add(currentNode);
//				}
				LinkedList<Node> waits = waitForPredecessor.remove(target);
				if(waits != null)
				{
					Iterator<Node> iter = waits.iterator();
					while(iter.hasNext())
					{
						Node successor = iter.next();
						successor.addPredecessor(currentNode);
						currentNode.addSuccessor(successor);
						leaves.remove(successor);
					}
				}
				else
				{
					roots.add(currentNode);
				}
				
				Register source1=instruction.getSource1();
				//use
//				waitForPredecessor.put(source1, currentNode);
				LinkedList<Node> nodesForSource1 = waitForPredecessor.get(source1);
				if(nodesForSource1 != null)
				{
					nodesForSource1.add(currentNode);
					waitForPredecessor.put(source1, nodesForSource1);
				}
				else
				{
					nodesForSource1 = new LinkedList<Node>();
					nodesForSource1.add(currentNode);
					waitForPredecessor.put(source1, nodesForSource1);
				}
			}
			else
			{
				//s+=" "+source1+" "+source2+" => "+target;	
				Register target=instruction.getTarget();

//				Node successor = waitForPredecessor.remove(target);
//				if(successor != null)
//				{
//					successor.addPredecessor(currentNode);
//					currentNode.addSuccessor(successor);
//					leaves.remove(successor);
//				}
//				else
//				{
//					roots.add(currentNode);
//				}
				LinkedList<Node> waits = waitForPredecessor.remove(target);
				if(waits != null)
				{
					Iterator<Node> iter = waits.iterator();
					while(iter.hasNext())
					{
						Node successor = iter.next();
						successor.addPredecessor(currentNode);
						currentNode.addSuccessor(successor);
						leaves.remove(successor);
					}
				}
				else
				{
					roots.add(currentNode);
				}
				
				Register source1=instruction.getSource1();
				LinkedList<Node> nodesForSource1 = waitForPredecessor.get(source1);
				if(nodesForSource1 != null)
				{
					nodesForSource1.add(currentNode);
					waitForPredecessor.put(source1, nodesForSource1);
				}
				else
				{
					nodesForSource1 = new LinkedList<Node>();
					nodesForSource1.add(currentNode);
					waitForPredecessor.put(source1, nodesForSource1);
				}
				
				
				Register source2=instruction.getSource2();
				LinkedList<Node> nodesForSource2 = waitForPredecessor.get(source2);
				if(nodesForSource2 != null)
				{
					nodesForSource2.add(currentNode);
					waitForPredecessor.put(source2, nodesForSource2);
				}
				else
				{
					nodesForSource2 = new LinkedList<Node>();
					nodesForSource2.add(currentNode);
					waitForPredecessor.put(source2, nodesForSource2);
				}
			}
			i--;
		}
	}
	
	private void calculatePriority()
	{
		Iterator<Node> iter = roots.iterator();
		while(iter.hasNext())
		{
			calculatePriority(iter.next(), 0);
		}
	}
	
	private void calculatePriority(Node root, int priority)
	{
		priority += Instruction.DELAYMAP.get(root.getInstruction().getOpcode());
		int prevDelay = root.getPriority();
		if(priority > prevDelay)
		{
			root.setPriority(priority);
			ArrayList<Node> currentPredecessor = root.getPredecessors();
			for(int i=0; i<currentPredecessor.size(); i++)
			{
				Node currentNode = currentPredecessor.get(i);
				calculatePriority(currentNode, priority);
			}
		}
		else
		{
			//already have a bigger delay, no action needed.
		}
		
	}
	
	public void printButtomUp()
	{
		Printer.print(instructions);
		HashSet<Node> printer = new HashSet<Node>();
		Iterator<Node> iter = roots.iterator();
		while (iter.hasNext())
		{
			Printer.printNodeButtomUp(iter.next(),printer);
		}
	}
	
	public void printTopDown()
	{
		Printer.print(instructions);
		HashSet<Node> printer = new HashSet<Node>();
		Iterator<Node> iter = leaves.iterator();
		while (iter.hasNext())
		{
			Printer.printNodeTopDown(iter.next(),printer);
		}
	}

}
