import java.util.ArrayList;


public class DependencyGraphCreater {
	
	private ArrayList<Instruction> instructions;
	
	public DependencyGraphCreater(ArrayList<Instruction> instructions)
	{
		this.instructions = instructions;
	}
	
	public void create()
	{
		int i=instructions.size()-1;
		while(i>=0)
		{
			Instruction instruction=instructions.get(i);
			instruction.setIndex(i);
			if(instruction.getTarget()!=null)
			{
				Register target=instruction.getTarget();
			
			}

			if(instruction.getSource1()!=null)
			{
				Register source1=instruction.getSource1();
				
			}

			if(instruction.getSource2()!=null)
			{
				Register source2=instruction.getSource2();
				
			}
			i--;
		}
		
		Printer.print(instructions);
	}

}
