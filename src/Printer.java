import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class Printer<E> {

	public static void print(ArrayList<?> toBePrinted)
	{
		for (int i=0; i<toBePrinted.size(); i++)
		{
			System.out.println(toBePrinted.get(i));
		}

		System.out.println();
	}

	public static void print(String name, ArrayList<?> toBePrinted)
	{
		System.out.println(name);
		print(toBePrinted);
	}

	public static void print(HashMap<?, ?> toBePrinted)
	{
		Iterator<?> iter = toBePrinted.keySet().iterator();
		while (iter.hasNext())
		{
			Object key = iter.next();
			System.out.println(key+": "+toBePrinted.get(key));
		}

		System.out.println();
	}

	public static void print(String name, HashMap<?, ?> toBePrinted)
	{
		System.out.println(name);
		print(toBePrinted);
	}

	public static void print(Node root, HashSet<Node> processed)
	{
		if(!processed.contains(root))
		{
			processed.add(root);
			Instruction currentInstruction = root.getInstruction();
			System.out.println(currentInstruction.getIndex()+" [label= \""+currentInstruction+" "+root.getDelay()+"\"];");
			ArrayList<Node> currentPredecessor = root.getPredecessors();
			for(int i=0; i<currentPredecessor.size(); i++)
			{
				Node currentNode = currentPredecessor.get(i);
				System.out.println(root.getInstruction().getIndex() 
						+ " -> " + currentNode.getInstruction().getIndex() +";");
				print(currentNode, processed);
			}
		}

	}
}
