import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;

public class InstructionScheduler {

	public void run(HashSet<Node> leaves)
	{
		schedule(leaves);
	}

	private void schedule(HashSet<Node> leaves)
	{
		int cycle = 1;

		//ready = leaves
		PriorityQueue<Node> ready = new PriorityQueue<Node>(leaves.size(), new Comparator<Node>(){
			@Override
			public int compare(Node o1, Node o2) 
			{
				//To make the ready queue in descending order, this method will return the opposite result
				return o2.getPriority() - o1.getPriority();
			}
		});
		Iterator<Node> iterLeaves = leaves.iterator();
		while(iterLeaves.hasNext())
		{
			ready.add(iterLeaves.next());
		}

		//active queue
		//HashSet<Node> active = new HashSet<Node>();
		ArrayList<HashSet<Node>> active = new ArrayList<HashSet<Node>>();

		while(!ready.isEmpty() && !active.isEmpty())
		{
			//for every elements in the active queue
			Iterator<Node> iterActive = active.get(cycle).iterator();
			while(iterActive.hasNext())
			{
				Node currentNode = iterActive.next();
				//if it finished

				//remove from active queue
				currentNode.setCompleted(true);
				active.remove(currentNode);

				//all its successor might be ready
//				ArrayList<Node> successors = currentNode.getSuccessors();
//				for(int i=0; i<successors.size(); i++)
//				{
//					Node s = successors.get(i);
//					if (s.isReady())
//					{
//						ready.add(s);
//					}
//				}
				
				Iterator<Node> iter = currentNode.getSuccessors().iterator();
				while(iter.hasNext())
				{
					Node s = iter.next();
					if (s.isReady())
					{
						ready.add(s);
					}
				}
			}

			if(!ready.isEmpty())
			{
				Node currentNode = ready.poll();
				currentNode.setStartCycle(cycle);//TODO may not be necessary due to the arraylist implementation of active
				
				//output the scheduled instruction
				
				
				//add the node to the complete cycle
				int targetCycle = currentNode.getStartCycle()+currentNode.getDelay();
				HashSet<Node> activeSet = active.get(targetCycle);
				if(activeSet == null)
				{
					activeSet = new HashSet<Node>();
				}
				activeSet.add(currentNode);
				active.set(targetCycle, activeSet);
			}
			cycle++;
		}
	}
	
	private void print(Node n1, Node n2)
	{
		System.out.println("["+n1.getInstruction()+";"+n2.getInstruction()+"]");
	}

}
