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
	}

	private void create()
	{
		HashMap<Register, LinkedList<Node>> waitForPredecessor = new HashMap<Register, LinkedList<Node>>();
		Node prevStore = null;
		LinkedList<Node> waitForStore = new LinkedList<Node>();
		Node prevOutput = null;

		int i=instructions.size()-1;
		while(i>=0)
		{
			Instruction instruction=instructions.get(i);
			instruction.setIndex(i);
			Node currentNode = new Node(instruction);
			leaves.add(currentNode);

			Instruction.OPCODE opcode = instruction.getOpcode();

			if(opcode==Instruction.OPCODE.output)
			{
				//s+=" "+immediateValue;	
				waitForStore.add(currentNode);
				roots.add(currentNode);
				
				if(prevOutput != null)
				{
					currentNode.addSuccessor(prevOutput);
					prevOutput.addPredecessor(currentNode);
					leaves.remove(prevOutput);
					roots.remove(currentNode);
				}
				
				prevOutput = currentNode;
			}
			else if(opcode==Instruction.OPCODE.loadI)
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

				waitForStore.add(currentNode);
			}
			else if(opcode==Instruction.OPCODE.store)
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
				
				roots.add(currentNode);

				if(prevStore != null)
				{
					if(waitForStore.isEmpty())//no loads between store
					{
						currentNode.addSuccessor(prevStore);
						prevStore.addPredecessor(currentNode);
						leaves.remove(prevStore);
						roots.remove(currentNode);
					}
					else
					{
						Iterator<Node> iterWaitStore = waitForStore.iterator();
						while(iterWaitStore.hasNext())
						{
							Node c = iterWaitStore.next();
							currentNode.addSuccessor(c);
							c.addPredecessor(currentNode);
							c.addSuccessor(prevStore);
							prevStore.addPredecessor(c);
							leaves.remove(prevStore);
							leaves.remove(c);
							roots.remove(currentNode);
							roots.remove(c);
						}
						waitForStore = new LinkedList<Node>();
					}
				}
				else
				{
					Iterator<Node> iterWaitStore = waitForStore.iterator();
					while(iterWaitStore.hasNext())
					{
						Node c = iterWaitStore.next();
						currentNode.addSuccessor(c);
						c.addPredecessor(currentNode);
						leaves.remove(c);
						roots.remove(currentNode);
					}
					waitForStore = new LinkedList<Node>();
				}
				prevStore = currentNode;
			}
			else if(opcode==Instruction.OPCODE.load)
			{
				//s+=" "+source1+" => "+target;	
				Register target=instruction.getTarget();
				//define
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

				waitForStore.add(currentNode);
			}
			else
			{
				//s+=" "+source1+" "+source2+" => "+target;	
				Register target=instruction.getTarget();

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

		if(prevStore != null)
		{
			Iterator<Node> iterWaitStore = waitForStore.iterator();
			while(iterWaitStore.hasNext())
			{
				Node c = iterWaitStore.next();
				c.addSuccessor(prevStore);
				prevStore.addPredecessor(c);
			}
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
		priority += root.getInstruction().getDelay();
		int prevDelay = root.getPriority();
		if(priority > prevDelay)
		{
			root.setPriority(priority);
			
			Iterator<Node> iter = root.getPredecessors().iterator();
			while(iter.hasNext())
			{
				Node currentNode = iter.next();
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
		//Printer.print(instructions);
		HashSet<Node> printer = new HashSet<Node>();
		Iterator<Node> iter = roots.iterator();
		while (iter.hasNext())
		{
			Printer.printNodeButtomUp(iter.next(),printer);
		}
		System.out.println();
	}

	public void printTopDown()
	{
		//Printer.print(instructions);
		HashSet<Node> printer = new HashSet<Node>();
		Iterator<Node> iter = leaves.iterator();
		while (iter.hasNext())
		{
			Printer.printNodeTopDown(iter.next(),printer);
		}
		System.out.println();
	}

}
