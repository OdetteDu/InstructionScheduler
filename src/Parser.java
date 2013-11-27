import java.util.ArrayList;

import exception.ExtraTokenException;
import exception.ImmediateValueNotIntegerException;
import exception.InvalidArrowException;
import exception.InvalidOpcodeException;
import exception.InvalidRegisterNameException;


public class Parser {

	public Instruction parseLine(ArrayList<String> line) throws InvalidOpcodeException, InvalidRegisterNameException, InvalidArrowException, ExtraTokenException, ImmediateValueNotIntegerException
	{
		Instruction instruction;
		
		String opcode=line.get(0);
		
		if(Instruction.isArithmetic(opcode))
		{
			if(line.size()>5)
			{
				throw new ExtraTokenException();
			}
			
			Instruction.OPCODE o;
			if(opcode.equals("add"))
			{
				o = Instruction.OPCODE.add;
			}
			else if(opcode.equals("sub"))
			{
				o = Instruction.OPCODE.sub;
			}
			else if(opcode.equals("mult"))
			{
				o = Instruction.OPCODE.mult;
			}
			else if(opcode.equals("lshift"))
			{
				o = Instruction.OPCODE.lshift;
			}
			else if(opcode.equals("rshift"))
			{
				o = Instruction.OPCODE.rshift;
			}
			else
			{
				throw new InvalidOpcodeException(opcode);
			}
			
			Register source1=getRegister(line.get(1));
			Register source2=getRegister(line.get(2));
			checkArrow(line.get(3));
			Register target=getRegister(line.get(4));
			instruction=new Instruction(o);
			instruction.setSource1(source1);
			instruction.setSource2(source2);
			instruction.setTarget(target);
			
		}
		else if(opcode.equals(Instruction.OUTPUT))
		{
			//output
			if(line.size()>2)
			{
				throw new ExtraTokenException();
			}
			instruction=new Instruction(Instruction.OPCODE.output);
			
			try
			{
				int iv=Integer.parseInt(line.get(1));
				instruction.setImmediateValue(iv);
			}
			catch(NumberFormatException e)
			{
				throw new ImmediateValueNotIntegerException(line.get(1));
			}
			
			
		}
		else if(opcode.equals(Instruction.LOADI))
		{
			//loadI
			if(line.size()>4)
			{
				throw new ExtraTokenException();
			}
			Register target=getRegister(line.get(3));
			checkArrow(line.get(2));
			instruction=new Instruction(Instruction.OPCODE.loadI);
			instruction.setTarget(target);
	
			try
			{
				int iv=Integer.parseInt(line.get(1));
				instruction.setImmediateValue(iv);
			}
			catch(NumberFormatException e)
			{
				throw new ImmediateValueNotIntegerException(line.get(1));
			}
		}
		else if(opcode.equals(Instruction.STORE))
		{
			//store
			if(line.size()>4)
			{
				throw new ExtraTokenException();
			}
			Register source1=getRegister(line.get(1));
			Register source2=getRegister(line.get(3));
			checkArrow(line.get(2));
			instruction=new Instruction(Instruction.OPCODE.store);
			instruction.setSource1(source1);
			instruction.setSource2(source2);
		}
		else if(opcode.equals(Instruction.LOAD))
		{
			//load
			if(line.size()>4)
			{
				throw new ExtraTokenException();
			}
			Register source1=getRegister(line.get(1));
			checkArrow(line.get(2));
			Register target=getRegister(line.get(3));
			instruction=new Instruction(Instruction.OPCODE.load);
			instruction.setSource1(source1);
			instruction.setTarget(target);
		}
		else
		{
			throw new InvalidOpcodeException(opcode);
		}
		
		return instruction;
	}
	
	private Register getRegister(String registerName) throws InvalidRegisterNameException
	{
		if(registerName.charAt(0)!='r')
		{
			throw new InvalidRegisterNameException(registerName);
		}
		
		try
		{
			int registerNumber=Integer.parseInt(registerName.substring(1, registerName.length()));
			return new Register(registerNumber);
		}
		catch(NumberFormatException e)
		{
			throw new InvalidRegisterNameException(registerName);
		}
	}
	
	private void checkArrow(String s) throws InvalidArrowException
	{
		if(!s.equals("=>"))
		{
			throw new InvalidArrowException(s);
		}
	}
}
