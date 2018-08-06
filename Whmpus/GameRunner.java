package Whmpus;

public class GameRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

			World whmpusWorld = new World();
			
			Agent agent = new Agent(whmpusWorld);
		
			agent.moveAgent();
		}

}
