import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import exception.UseUndefinedRegisterException;

public class AAllocator {

	private int renameCount;
	protected ArrayList<Instruction> instructions;

	public AAllocator(int numPhysicalRegisters, ArrayList<Instruction> instructions) throws UseUndefinedRegisterException 
	{
		renameCount=-2;
		this.instructions=instructions;
		renameInstructions();
		Printer.print(instructions);
	}

	private int getAvailableName()
	{
		return renameCount--;
	}

	private void rename(Register r, HashMap<Integer, Integer> renameList)
	{
		if(renameList.get(r.getNumber())!=null)
		{
			int newName=renameList.get(r.getNumber());
			r.setNumber(newName);
			rename(r,renameList);
		}
	}

	private void renameInstructions() throws UseUndefinedRegisterException
	{
		HashMap<Integer, Integer> liveRegisters=new HashMap<Integer,Integer>();
		ArrayList<Integer> definedVr=new ArrayList<Integer>();
		HashMap<Integer, Integer> renameList=new HashMap<Integer,Integer>();

		int i=instructions.size()-1;
		while(i>=0)
		{
			Instruction instruction=instructions.get(i);
			if(instruction.getTarget()!=null)
			{
				Register target=instruction.getTarget();
				rename(target,renameList);
				if(liveRegisters.get(target.getNumber())!=null)
				{
					liveRegisters.remove(target.getNumber());
				}
				else
				{
					if(definedVr.contains(target.getNumber()))
					{
						int availableName=getAvailableName();
						renameList.put(target.getNumber(),availableName);
						target.setNumber(availableName);
					}

				}
				definedVr.add(target.getNumber());
			}

			if(instruction.getSource1()!=null)
			{
				Register source1=instruction.getSource1();
				rename(source1,renameList);
				if(liveRegisters.get(source1.getNumber())==null)
				{
					if(definedVr.contains(source1.getNumber()))
					{
						int availableName=getAvailableName();
						renameList.put(source1.getNumber(),availableName);
						source1.setNumber(availableName);
					}
					liveRegisters.put(source1.getNumber(), i);
				}
			}

			if(instruction.getSource2()!=null)
			{
				Register source2=instruction.getSource2();
				rename(source2,renameList);
				if(liveRegisters.get(source2.getNumber())==null)
				{
					if(definedVr.contains(source2.getNumber()))
					{
						int availableName=getAvailableName();
						renameList.put(source2.getNumber(),availableName);
						source2.setNumber(availableName);
					}
					liveRegisters.put(source2.getNumber(), i);
				}
			}
			i--;
		}

		if(!liveRegisters.isEmpty())
		{
			String exceptionMessage="";
			Iterator<Integer> iter=liveRegisters.keySet().iterator();
			while(iter.hasNext())
			{
				exceptionMessage+="r"+iter.next()+" ";
			}
			throw new UseUndefinedRegisterException(exceptionMessage);
		}
	}
}
