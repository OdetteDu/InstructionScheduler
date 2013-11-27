
import java.util.HashSet;
import java.util.Iterator;

public class Node  {
	
	private Instruction instruction;
	private int priority;
	private int numOfDescendants;
	private HashSet<Node> predecessors;
	private HashSet<Node> successors;
	
	private int startCycle;
	//private int delay;
	private boolean completed;
	
	public Node(Instruction instruction)
	{
		this.instruction = instruction;
		
		this.predecessors = new HashSet<Node>();
		this.successors = new HashSet<Node>();
		
		numOfDescendants = -1;
		
		//this.delay = Instruction.DELAYMAP.get(instruction.getOpcode());
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
	
	public int getNumOfDescendants() {
		return numOfDescendants;
	}

	public void setNumOfDescendants(int numOfDescendants) {
		this.numOfDescendants = numOfDescendants;
	}

	public void addPredecessor(Node node)
	{
		this.predecessors.add(node);
	}

	public HashSet<Node> getPredecessors() {
		return predecessors;
	}

	public void setPredecessors(HashSet<Node> predecessors) {
		this.predecessors = predecessors;
	}
	
	public void addSuccessor(Node node)
	{
		this.successors.add(node);
	}

	public HashSet<Node> getSuccessors() {
		return successors;
	}

	public void setSuccessors(HashSet<Node> successors) {
		this.successors = successors;
	}
	
	public int getStartCycle() {
		return startCycle;
	}

	public void setStartCycle(int startCycle) {
		this.startCycle = startCycle;
	}

	public int getDelay() {
		return instruction.getDelay();
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
		Iterator<Node> iter = predecessors.iterator();
		while(iter.hasNext())
		{
			Node p = iter.next();
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
