import java.util.ArrayList;


public class Node {
	
	private Instruction instruction;
	private int delay;
	private ArrayList<Node> successors;
	private ArrayList<Node> predecessors;
	
	public Node(Instruction instruction)
	{
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
	
	

}
