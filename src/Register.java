
public class Register {
	
	private int number;

	public Register(int vr)
	{
		this.number=vr;
	}
	
	public String toString()
	{
		return "r"+number;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Register r = (Register) obj;
		return r.number == this.number;
	}
	
	@Override
	public int hashCode()
	{
		Integer n = number;
		return n.hashCode();
	}
}

