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
    int maxRuns = 10;
    public Agent(World world ) {
    	this.world = world;
    	currentPosition = new Coordinates(1,1,Directions.EAST);
    	    	
    }
    
    void moveAgent() {
        while (runs < maxRuns) {

        	runs += 1;
        	
        	Coordinates oldPosition = new Coordinates(currentPosition);
        	
        	Percept currentPercept = world.getPercept(currentPosition);
        	knowledgeBase.put(currentPosition, currentPercept);
        	moves.add(new Coordinates(currentPosition));
        	visited.add(new Coordinates(currentPosition));
        	
        	currentPosition = getNextMove(currentPosition);
        	
        	System.out.print("From: ");
        	oldPosition.printPosition();
        	System.out.print(" To: ");
        	currentPosition.printPosition();
        	System.out.println();
               	
        }
    }
    
    private Coordinates getNextMove(Coordinates playerPosition) {
    	
    	Percept percept = world.getPercept(playerPosition);
    	
    	if (percept.glitter) {
    		goldCollected = true;
    		System.out.print("GOT GOLD>> ");
    		
    		//BACK TO START
    		
    	} else {
    		
    		if ( percept.stench) {
    			
    			Coordinates safeLocation = confirmDanger(playerPosition);
    			
    			if (safeLocation != null) {
    				System.out.print("Got Safe Spot>> ");
    				return safeLocation;
    			} else {
    				System.out.print("No Safe Spot>> ");
    				return backTrack();
    			}
    			
    		} else if (percept.breez) {
    		    
    			
        		Coordinates playerPositionCopy = new Coordinates(playerPosition);
        		
        		playerPositionCopy.moveAhead();
        		
//        		playerPosition.printPosition();
//        		System.out.print(" Move Ahead ");
//        		playerPositionCopy.printPosition();
//        		System.out.println();
        		
        		if (checkVisited(playerPositionCopy)) {
        			System.out.print("Found Ahead>> ");
        			return currentPosition.moveAhead();
        			
        		} else if ( getVisitedAdjecentCell(playerPosition) != null) {
        			
        			System.out.print("Found Adjacent>> ");
        			return getVisitedAdjecentCell(playerPosition);
        			
        		} else {
        			
        			Coordinates safeLocation = confirmDanger(playerPosition);
        			
        			if (safeLocation != null) {
        				return safeLocation;
        			} else {
        				return backTrack();
        			}
        			
        		}
        		
        	} else {
        		Coordinates unvisitedCoordinate = getUnvisitedSpot(playerPosition);
        		System.out.print("Unvisited Spot>> ");
        		 return unvisitedCoordinate;
        	}
    	}
    	
    	return playerPosition;
    	
    
    }
    
    private Coordinates backTrack() {
   
        moves.remove(moves.size() - 1); 
     
        Coordinates basePoint = new Coordinates(moves.get(moves.size() - 1));
       
        return basePoint;
        
    }
    
    private Coordinates getUnvisitedSpot(Coordinates basePosition) {
    	
    	Coordinates basePositionCopy = new Coordinates(basePosition);
    	
    	if (!checkVisited(basePositionCopy.moveAhead())) {
    		
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		} 
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	if (!checkVisited(basePositionCopy.moveEast())) {
    		
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	if (!checkVisited(basePositionCopy.moveNorth())) {
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	if (!checkVisited(basePositionCopy.moveSouth())) {
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	basePositionCopy = new Coordinates(basePosition);
    	if (!checkVisited(basePositionCopy.moveWest())) {
    		Percept percept = world.getPercept(basePositionCopy);
    		if (!percept.bump) {
    		return basePositionCopy;
    		}
    	}
    	
    	return getRandomDirection(basePosition); 
        	
    }
    
    
    Coordinates getRandomDirection(Coordinates playerPosition) {
    	
 
    	boolean hasBump = true;
    	Coordinates playerPositionCopy = new Coordinates(playerPosition);
    	while(hasBump) {
    		
    		playerPositionCopy = new Coordinates(playerPosition);
    		
    		int Min = 1, Max = 4;
        	int random = Min + (int)(Math.random() * ((Max - Min) + 1));
        	
        	switch (random) {
    		case 1:
    			playerPositionCopy.moveNorth();
    			break;
    		case 2:
    			playerPositionCopy.moveEast();
    			break;
    		case 3:
    			playerPositionCopy.moveWest();
    			break;
    		case 4:
    			playerPositionCopy.moveSouth();
    			break;
    		default:
    			break;
    		}
        	
        	Percept percept = world.getPercept(playerPositionCopy);

        	hasBump = percept.bump;
    	}
    			
    	return playerPositionCopy;
    	
    }
    
    void updateMoves(Coordinates newPoint) {
    	if (!moves.get(moves.size() - 1).equals(newPoint)) {
    		moves.add(newPoint);
    	} else {
    		System.out.println("Point already exists");
    	}
    }
    
    
    private Coordinates confirmDanger(Coordinates playerPosition) {
    	
    	Coordinates playerPositionCopy = new Coordinates(playerPosition);
    	
    	ArrayList<Coordinates> adjacentCellList = world.getAdjacentCells(playerPositionCopy);
    	
    	boolean isRisky = true;
    	
    	for (Coordinates possibleDangerCoordinate: adjacentCellList) {
    		isRisky = checkPreviousPercepts(possibleDangerCoordinate);
    		if (!isRisky) {
    			
    			
    			possibleDangerCoordinate.direction = playerPosition.determineDirection(possibleDangerCoordinate);
    			
    			return possibleDangerCoordinate;
    		}
    	}
    	
    	return null;
    	
    }
    
    boolean checkPreviousPercepts(Coordinates possibleDangerCoordinate) {
    	
    	boolean isRisky = true;
    	
    	ArrayList<Coordinates> perceptLocationList = world.getAdjacentCells(possibleDangerCoordinate);
    	
    	for (Coordinates perceptLocation: perceptLocationList) {
    		
    		if (knowledgeBase.containsKey(perceptLocation)) {
        		Percept positionPercept = knowledgeBase.get(perceptLocation);
        		
        		if(!(positionPercept.breez && positionPercept.stench)) {
        			return false;
        		}
        		
    		}
    	}
    	
    	return isRisky;
    
    }
    
    
    
    private Coordinates getVisitedAdjecentCell(Coordinates playerPosition) {
    	
    	Coordinates playerPositionCopy = new Coordinates(playerPosition);
    	
    	//EAST
    	if(checkVisited(playerPositionCopy.moveEast())) {
    		return playerPositionCopy;
    	}
    	
    	//WEST
    	playerPositionCopy = new Coordinates(playerPosition);
    	if(checkVisited(playerPositionCopy.moveWest())) {
    		return playerPositionCopy;
    	}
    	
    	//NORTH
    	playerPositionCopy = new Coordinates(playerPosition);
    	if(checkVisited(playerPositionCopy.moveNorth())) {
    		return playerPositionCopy;
    	}
    	
    	//SOUTH
    	playerPositionCopy = new Coordinates(playerPosition);
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
    	//playerCoordinates.printPosition();
    	//System.out.println(visited.contains(playerCoordinates));
    	return visited.contains(playerCoordinates);
    }

}