import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Node  {
	
	private Instruction instruction;
	private int priority;
	private ArrayList<Node> predecessors;
	private ArrayList<Node> successors;
	
	private int startCycle;
	private int delay;
	private boolean completed;
	
	public Node(Instruction instruction)
	{
		this.instruction = instruction;
		
		this.predecessors = new ArrayList<Node>();
		this.successors = new ArrayList<Node>();
		
		this.delay = Instruction.DELAYMAP.get(instruction.getOpcode());
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void addPredecessor(Node node)
	{
		this.predecessors.add(node);
	}

	public ArrayList<Node> getPredecessors() {
		return predecessors;
	}

	public void setPredecessors(ArrayList<Node> predecessors) {
		this.predecessors = predecessors;
	}
	
	public void addSuccessor(Node node)
	{
		this.successors.add(node);
	}

	public ArrayList<Node> getSuccessors() {
		return successors;
	}

	public void setSuccessors(ArrayList<Node> successors) {
		this.successors = successors;
	}
	
	public int getStartCycle() {
		return startCycle;
	}

	public void setStartCycle(int startCycle) {
		this.startCycle = startCycle;
	}

	public int getDelay() {
		return delay;
	}

//	private void setDelay(int delay) {
//		this.delay = delay;
//	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public boolean isReady()
	{
		for (int i=0; i<predecessors.size(); i++)
		{
			Node p = predecessors.get(i);
			if(!p.isCompleted())
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public String toString()
	{
		String s = "\n{\n";
		s+=instruction + ": "+priority+"\n";
		
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

//	@Override
//	public int compareTo(Node o) {
//		//To make the ready queue in decending order, this method will return the opposite result
//		return  o.priority - this.priority;
//		
//	}
	

}
