import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Node {
	
	private Instruction instruction;
	private int delay;
	private ArrayList<Node> successors;
	private ArrayList<Node> predecessors;
	
	public Node(Instruction instruction)
	{
		this.instruction = instruction;
		this.successors = new ArrayList<Node>();
		this.predecessors = new ArrayList<Node>();
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void addSuccessor(Node node)
	{
		this.successors.add(node);
	}
	
	public void addPredecessor(Node node)
	{
		this.predecessors.add(node);
	}

	public ArrayList<Node> getSuccessors() {
		return successors;
	}

	public void setSuccessors(ArrayList<Node> successors) {
		this.successors = successors;
	}
	
	@Override
	public String toString()
	{
		String s = "\n{\n";
		s+=instruction + ": "+delay+"\n";
		
		s+="Predecessors: \n";
		Iterator<Node> iterP = this.predecessors.iterator();
		while(iterP.hasNext())
		{
			s+=iterP.next().getInstruction()+"\n";
		}
		//s+="\n";
		
		s+="Successors: \n";
		Iterator<Node> iterS = this.successors.iterator();
		while(iterS.hasNext())
		{
			s+=iterS.next().getInstruction()+"\n";
		}
		//s+="\n";
		s+="}\n";
		
		return s;
	}
	

}
