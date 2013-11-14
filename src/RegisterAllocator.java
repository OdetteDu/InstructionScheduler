import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import exception.ExtraTokenException;
import exception.ImmediateValueNotIntegerException;
import exception.InvalidArrowException;
import exception.InvalidCommandLineArgumentException;
import exception.InvalidOpcodeException;
import exception.InvalidRegisterNameException;
import exception.RenameConflictException;
import exception.UseUndefinedRegisterException;


public class RegisterAllocator {

	public static int NUM_REGISTERS = 256;
	
	private String filePath;
	private ArrayList<Instruction> instructions;
	private AAllocator allocator;

	public RegisterAllocator(String filePath)
	{
		instructions=new ArrayList<Instruction>();
		this.filePath=filePath;
	}

	public void readFile() throws ImmediateValueNotIntegerException, InvalidOpcodeException, InvalidRegisterNameException, InvalidArrowException, ExtraTokenException, InvalidCommandLineArgumentException
	{
		try
		{
			FileReader fr=new FileReader(filePath);
			BufferedReader br=new BufferedReader(fr);
			Scanner scanner=new Scanner();
			Parser parser=new Parser();

			String temp=br.readLine();
			while(temp!=null)
			{
				ArrayList<String> tokens=scanner.scanLine(temp);
				if(!tokens.isEmpty())
				{
					Instruction instruction=parser.parseLine(tokens);
					instructions.add(instruction);
				}

				temp=br.readLine();
			}
			br.close();
		} 
		catch (FileNotFoundException e) 
		{
			throw new InvalidCommandLineArgumentException("The filePath is invalid or the file can not be found.");
		}
		catch (IOException e) {
			throw new InvalidCommandLineArgumentException("The file is unavailable to open correctly. ");
		} 
	}

	public void allocate() throws UseUndefinedRegisterException, RenameConflictException
	{
		allocator=new AAllocator(instructions);
	}

	public void run() throws ImmediateValueNotIntegerException, UseUndefinedRegisterException, InvalidOpcodeException, InvalidRegisterNameException, InvalidArrowException, ExtraTokenException, InvalidCommandLineArgumentException, RenameConflictException
	{
		readFile();
		allocate();
	}

	public static void main(String args[])
	{
		try{
			if(args.length!=1)
			{
				throw new InvalidCommandLineArgumentException();
			}

			String filePath=args[0];
			RegisterAllocator registerAllocator=new RegisterAllocator(filePath);
			registerAllocator.run();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}





}
