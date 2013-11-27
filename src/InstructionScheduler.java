import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;

public class InstructionScheduler {

	private Machine machine;
	private PriorityQueue<Node> ready;
	private HashSet<Node> backToReady;
	private ArrayList<HashSet<Node>> active;

	public InstructionScheduler(HashSet<Node> leaves)
	{
		machine = new Machine();

		//ready = leaves
		ready = new PriorityQueue<Node>(leaves.size(), new Comparator<Node>(){
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

		backToReady = new HashSet<Node>();

		//active queue
		//HashSet<Node> active = new HashSet<Node>();
		active = new ArrayList<HashSet<Node>>();
	}

	public void run()
	{
		schedule();
		System.out.println(machine);
	}

	private void schedule()
	{
		int cycle = 1;

		while(!ready.isEmpty() || cycle < active.size())
		{
			//for every elements in the active queue
			if(cycle < active.size())
			{
				Iterator<Node> iterActive = active.get(cycle).iterator();
				while(iterActive.hasNext())
				{
					Node currentNode = iterActive.next();
					//if it finished

					//remove from active queue
					currentNode.setCompleted(true);
					active.remove(currentNode);

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
			}


			if(!ready.isEmpty())
			{
				scheduleInstruction(cycle);
			}
			cycle++;
		}
	}

	private void scheduleInstruction(int cycle)
	{
		if(machine.isUnit1Available(cycle) && machine.isUnit2Available(cycle))
		{
			HashSet<Node> rest = getABatchReady();
			if(!rest.isEmpty())
			{
				Node toBeSchedule = breakTie(rest);

				//select which unit to use
				if(machine.canScheduleUnit1(cycle, toBeSchedule.getInstruction()) && machine.canScheduleUnit2(cycle, toBeSchedule.getInstruction()))
				{
					machine.scheduleUnit2(cycle, toBeSchedule.getInstruction());
				}
				else if(machine.canScheduleUnit1(cycle, toBeSchedule.getInstruction()))
				{
					machine.scheduleUnit1(cycle, toBeSchedule.getInstruction());
				}
				else if(machine.canScheduleUnit2(cycle, toBeSchedule.getInstruction()))
				{
					machine.scheduleUnit2(cycle, toBeSchedule.getInstruction());
				}
				else
				{
					System.out.println("Both unit can't schedule instruction "+ toBeSchedule.getInstruction());
				}

				makeActive(toBeSchedule, cycle);	
			}
			
			Iterator<Node> iter = backToReady.iterator();
			while(iter.hasNext())
			{
				ready.add(iter.next());
			}
			backToReady = new HashSet<Node>();

			scheduleInstruction(cycle);
		}
		else if(machine.isUnit1Available(cycle))
		{
			HashSet<Node> rest = filterForUnit1(cycle, getABatchReady());
			while(!ready.isEmpty() && rest.isEmpty())
			{
				rest = filterForUnit1(cycle, getABatchReady());
			}

			if(!rest.isEmpty())
			{
				Node toBeSchedule = breakTie(rest);
				machine.scheduleUnit1(cycle, toBeSchedule.getInstruction());
				makeActive(toBeSchedule, cycle);
			}
			
			Iterator<Node> iter = backToReady.iterator();
			while(iter.hasNext())
			{
				ready.add(iter.next());
			}
			backToReady = new HashSet<Node>();
		}
		else if(machine.isUnit2Available(cycle))
		{
			HashSet<Node> rest = filterForUnit2(cycle, getABatchReady());
			while(!ready.isEmpty() && rest.isEmpty())
			{
				rest = filterForUnit2(cycle, getABatchReady());
			}

			if(!rest.isEmpty())
			{
				Node toBeSchedule = breakTie(rest);
				machine.scheduleUnit2(cycle, toBeSchedule.getInstruction());
				makeActive(toBeSchedule, cycle);
			}
			
			Iterator<Node> iter = backToReady.iterator();
			while(iter.hasNext())
			{
				ready.add(iter.next());
			}
			backToReady = new HashSet<Node>();
		}
	}

	private Node breakTie(HashSet<Node> nodes)
	{
		if(nodes.isEmpty())
		{
			return null;
		}

		Iterator<Node> iter = nodes.iterator();
		Node result = iter.next();
		int count = result.getSuccessors().size();

		while(iter.hasNext())
		{
			Node n = iter.next();
			if(n.getSuccessors().size() > count)
			{
				backToReady.add(result);
				result = n;
				count = n.getSuccessors().size();
			}
			else
			{
				backToReady.add(n);
			}
		}

		return result;
	}

	private HashSet<Node> filterForUnit1(int cycle, HashSet<Node> all)
	{
		HashSet<Node> batch = new HashSet<Node>();

		Iterator<Node> iter = all.iterator();
		while(iter.hasNext())
		{
			Node n = iter.next();
			if(machine.canScheduleUnit1(cycle, n.getInstruction()))
			{
				batch.add(n);
			}
			else
			{
				backToReady.add(n);
			}
		}

		return batch;
	}

	private HashSet<Node> filterForUnit2(int cycle, HashSet<Node> all)
	{
		HashSet<Node> batch = new HashSet<Node>();

		Iterator<Node> iter = all.iterator();
		while(iter.hasNext())
		{
			Node n = iter.next();
			if(machine.canScheduleUnit2(cycle, n.getInstruction()))
			{
				batch.add(n);
			}
			else
			{
				backToReady.add(n);
			}
		}

		return batch;
	}

	private HashSet<Node> getABatchReady()
	{
		HashSet<Node> batch = new HashSet<Node>();

		if(ready.isEmpty())
		{
			return batch;
		}
		else
		{
			Node first = ready.poll();
			batch.add(first);
			while(!ready.isEmpty() && ready.peek().getPriority() == first.getPriority())
			{
				batch.add(ready.poll());
			}
		}

		return batch;
	}

	private void makeActive(Node currentNode, int cycle)
	{
		currentNode.setStartCycle(cycle);//TODO may not be necessary due to the arraylist implementation of active

		//add the node to the complete cycle
		int targetCycle = currentNode.getStartCycle()+currentNode.getDelay();
		while(active.size()<=targetCycle+1)
		{
			active.add(new HashSet<Node>());
		}
		HashSet<Node> activeSet = active.get(targetCycle);

		activeSet.add(currentNode);
		active.set(targetCycle, activeSet);
	}



}
