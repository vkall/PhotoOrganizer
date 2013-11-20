package commands;

public interface Command {
	
	public void doCommand();
	
	public void undoCommand();
	
	public String commandName();
	
}
