import java.util.ArrayList;


public class Machine {

	public static Instruction NOP = new Instruction(Instruction.OPCODE.nop);

	ArrayList<Instruction> unit1;
	ArrayList<Instruction> unit2;

	public Machine()
	{
		unit1 = new ArrayList<Instruction>();
		unit2 = new ArrayList<Instruction>();
	}

	public boolean isUnit1Available(int cycle)
	{
		if (unit1.size() <= cycle)
		{
			return true;
		}
		else
		{
			return unit1.get(cycle).equals(NOP);
		}
	}

	public boolean isUnit2Available(int cycle)
	{
		if (unit2.size() <= cycle)
		{
			return true;
		}
		else
		{
			return unit2.get(cycle).equals(NOP);
		}
	}

	public boolean canScheduleUnit1(int cycle, Instruction instruction)
	{
		Instruction.OPCODE opcode = instruction.getOpcode();
		if(opcode == Instruction.OPCODE.mult)
		{
			//unable to perform mult
			return false;
		}

		if(opcode == Instruction.OPCODE.output)
		{
			if(!isUnit2Available(cycle))
			{
				if(unit2.get(cycle).getOpcode() == Instruction.OPCODE.output)
				{
					//unable to perform output
					return false;
				}
			}
		}

		return true;
	}

	public boolean canScheduleUnit2(int cycle, Instruction instruction)
	{
		Instruction.OPCODE opcode = instruction.getOpcode();
		if(opcode == Instruction.OPCODE.load || opcode == Instruction.OPCODE.store)
		{
			//unable to perform load or store
			return false;
		}

		if(opcode == Instruction.OPCODE.output)
		{
			if(!isUnit1Available(cycle))
			{
				if(unit1.get(cycle).getOpcode() == Instruction.OPCODE.output)
				{
					//unable to perform output
					return false;
				}
			}
		}
		return true;
	}

	public void scheduleUnit1(int cycle, Instruction instruction)
	{
		//schedule
		while(unit1.size()<=cycle)
		{
			unit1.add(NOP);
			unit2.add(NOP);
		}
		unit1.set(cycle, instruction);

	}

	public void scheduleUnit2(int cycle, Instruction instruction)
	{
		//schedule
		while(unit2.size()<=cycle)
		{
			unit1.add(NOP);
			unit2.add(NOP);
		}
		unit2.set(cycle, instruction);
	}

	private String getCycle(int cycle)
	{
		return "[ "+unit1.get(cycle)+" ; "+unit2.get(cycle)+" ]\n";
	}

	@Override
	public String toString()
	{
		//removeEndNop();
		String s="";
		for(int i=1; i<unit1.size(); i++)
		{
			s+=getCycle(i);
		}
		return s;
	}
	
	private void removeEndNop()
	{
		for(int i=unit1.size()-1; i>0; i--)
		{
			if(unit1.get(i).getOpcode() == Instruction.OPCODE.nop && unit2.get(i).getOpcode() == Instruction.OPCODE.nop)
			{
				unit1.remove(i);
				unit2.remove(i);
			}
			else
			{
				break;
			}
		}
	}
}
