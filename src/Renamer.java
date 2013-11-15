import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import exception.RenameConflictException;
import exception.UseUndefinedRegisterException;

public class Renamer {

	private int renameCount;
	private int maxRegisterNumber;
	protected ArrayList<Instruction> instructions;

	public Renamer(ArrayList<Instruction> instructions) throws UseUndefinedRegisterException, RenameConflictException 
	{
		maxRegisterNumber=-1;
		renameCount=-1;
		this.instructions=instructions;
	}

	private int getAvailableName() throws RenameConflictException
	{
		renameCount--;
		int newName = InstructionScheduler.NUM_REGISTERS+renameCount;
		if(newName < maxRegisterNumber)
		{
			throw new RenameConflictException();
		}
		
		return newName;
	}

	private void renameRegister(Register r, HashMap<Integer, Integer> renameList)
	{
		if(renameList.get(r.getNumber())!=null)
		{
			int newName=renameList.get(r.getNumber());
			r.setNumber(newName);
			renameRegister(r,renameList);
		}
	}

	public ArrayList<Instruction> renameInstructions() throws UseUndefinedRegisterException, RenameConflictException
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
				if(target.getNumber()>maxRegisterNumber)
				{
					maxRegisterNumber = target.getNumber();
				}
				renameRegister(target,renameList);
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
				if(source1.getNumber()>maxRegisterNumber)
				{
					maxRegisterNumber = source1.getNumber();
				}
				renameRegister(source1,renameList);
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
				if(source2.getNumber()>maxRegisterNumber)
				{
					maxRegisterNumber = source2.getNumber();
				}
				renameRegister(source2,renameList);
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
		
		return instructions;
	}
}
