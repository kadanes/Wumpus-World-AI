package Whmpus;

import java.io.*;
import java.util.*;

import Whmpus.Constants.Directions;

public class Agent {

	private World world;
	
    private Set<Coordinates> visited = new HashSet<Coordinates>();
    private Map<Coordinates,Percept> knowledgeBase = new HashMap<Coordinates,Percept>();
    private ArrayList<Coordinates> moves = new ArrayList<Coordinates>();
    
    private Coordinates currentPosition;

    boolean goldCollected = false;
    int runs = 0;
    
    public Agent(World world ) {
    	this.world = world;
    	currentPosition = new Coordinates(1,1,Directions.EAST);
    	    	
    }
    
    void moveAgent() {
        while (runs < 6) {

        	runs += 1;
        	
        	System.out.print("From: ");
        	currentPosition.printPosition();
        	
        	Percept currentPercept = world.getPercept(currentPosition);
        	knowledgeBase.put(currentPosition, currentPercept);
        	moves.add(new Coordinates(currentPosition));
        	visited.add(new Coordinates(currentPosition));
        	
        	currentPosition = getNextMove(currentPosition);
        	
        	System.out.print(" To: ");
        	currentPosition.printPosition();
        	System.out.println();
               	
        }
    }
    
    private Coordinates getNextMove(Coordinates playerPosition) {
    	
    	Percept percept = world.getPercept(playerPosition);
    	
    	if (percept.glitter) {
    		goldCollected = true;
    		//BACK TO START
    		
    	} else {
    		
    		if (percept.breez || percept.stench) {
    		    
        		Coordinates playerPositionCopy = playerPosition;
        		
        		if ( checkVisited(playerPositionCopy.moveAhead())) {
        			System.out.print("Found in visited ahead.");
        			return currentPosition.moveAhead();
        			
        		} else if ( getVisitedAdjecentCell(playerPosition) != null) {
        			
        			System.out.print("Found onother coordinate adjacent that's visisted. ");
        			return getVisitedAdjecentCell(playerPosition);
        			
        		} else {
        			System.out.print(" <<Back tracing>> ");
            		return backTrack();
            		
        		}
        		
        	} else {
        		System.out.print(" <<Unvisited spot>> ");
        		return getUnvisitedSpot(playerPosition);
        	}
    	}
    	
    	return playerPosition;
    	
    
    }
    
    private Coordinates backTrack() {
        //Move back one step
   	
    
        moves.remove(moves.size() - 1); 
     
        Coordinates basePoint = new Coordinates(moves.get(moves.size() - 1));
       
        return basePoint;
        
    }
    
    private Coordinates getUnvisitedSpot(Coordinates basePosition) {
    	
    	Coordinates basePositionCopy = new Coordinates(basePosition);
    	
    	
    	
    	if (!checkVisited(basePositionCopy.moveAhead())) {
    		
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePosition.moveAhead();
    		} 
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	
    	if (!checkVisited(basePosition.moveEast())) {
    		
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	if (!checkVisited(basePosition.moveNorth())) {
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	basePositionCopy.moveSouth();
    	if (!checkVisited(basePositionCopy.moveSouth())) {
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	basePositionCopy.moveWest();
    	if (!checkVisited(basePositionCopy.moveWest())) {
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	return getRandomDirection(basePosition); 
        	
    }
    
    
    Coordinates getRandomDirection(Coordinates playerPosition) {
    	Coordinates playerPositionCopy = new Coordinates(playerPosition);
    	
    	int Min = 1, Max = 4;
    	int random = Min + (int)(Math.random() * ((Max - Min) + 1));
    	
    	switch (random) {
		case 1:
			playerPositionCopy.moveNorth();
			
			break;
		case 2:
			playerPositionCopy.moveEast();

		case 3:
			playerPositionCopy.moveWest();

		case 4:
			playerPositionCopy.moveSouth();
			
		default:
			break;
		}
    	
    	Percept percept = world.getPercept(playerPositionCopy);
    	if(!percept.bump) {
    		return playerPositionCopy;
    	} else {
    		getRandomDirection(playerPositionCopy);
    	}
    	
    	return playerPosition;
    	
    }
    
    void updateMoves(Coordinates newPoint) {
    	if (!moves.get(moves.size() - 1).equals(newPoint)) {
    		moves.add(newPoint);
    	} else {
    		System.out.println("Point already exists");
    	}
    }
    
    
    
    private Coordinates getVisitedAdjecentCell(Coordinates playerPosition) {
    	
    	Coordinates playerPositionCopy = playerPosition;
    	
    	//EAST
    	if(checkVisited(playerPositionCopy.moveEast())) {
    		return playerPositionCopy;
    	}
    	
    	//WEST
    	playerPositionCopy = playerPosition;
    	if(checkVisited(playerPositionCopy.moveWest())) {
    		return playerPositionCopy;
    	}
    	
    	//NORTH
    	playerPositionCopy = playerPosition;
    	if(checkVisited(playerPositionCopy.moveNorth())) {
    		return playerPositionCopy;
    	}
    	
    	//SOUTH
    	playerPositionCopy = playerPosition;
    	if(checkVisited(playerPositionCopy.moveSouth())) {
    		return playerPositionCopy;
    	}
    	
    	return null;
    }
     
    //FUNCTION TO CONFIRM WHUMPUS/PIT
    //
    ///
    ////
    /////
    
    private boolean checkVisited(Coordinates playerCoordinates) {
    	    	
//    	System.out.println("\n\nVisited: ");
//    	
//    	for(Coordinates visit: visited) {
//    		visit.printPosition();
//    		System.out.print(", ");
//    	}
//    	System.out.println("\n");
    	
    	return visited.contains(playerCoordinates);
    }

}