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
		
		HashSet<Node> active = new HashSet<Node>();
		
		while(!ready.isEmpty() && !active.isEmpty())
		{
			Iterator<Node> iterActive = active.iterator();
			while(iterActive.hasNext())
			{
				Node currentNode = iterActive.next();
				if(currentNode.getStartCycle()+currentNode.getDelay() < cycle)
				{
					active.remove(currentNode);
					ArrayList<Node> successors = currentNode.getSuccessors();
					for(int i=0; i<successors.size(); i++)
					{
						Node s = successors.get(i);
						//if (s is ready)
						//ready.add(s);
					}
				}
				
			}
			
			if(!ready.isEmpty())
			{
				Node currentNode = ready.poll();
				currentNode.setStartCycle(cycle);
				active.add(currentNode);
			}
			cycle++;
		}
	}

}
