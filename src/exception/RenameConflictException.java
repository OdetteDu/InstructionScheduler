package exception;

public class RenameConflictException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RenameConflictException()
	{
		super("There is no enough virtual register so that the rename generator can't find a suitable name to return. \n"
				+ "Please go to the InstructionScheduler class, and change the NUM_REGISTER to a larger value.");
	}
}
