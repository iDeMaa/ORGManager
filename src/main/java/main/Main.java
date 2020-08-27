package main;

public class Main {
	
	public static void main(String[] args) {
		ORGManager orgManager = new ORGManager(args[0], args[1]);
		orgManager.start();
	}

}
