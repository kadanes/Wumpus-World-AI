package Whmpus;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Whmpus.Constants.Directions;

public class Agent {

	private World world;
	
    private Set<Coordinates> visited = new HashSet<Coordinates>();
    private Map<Coordinates,Percept> knowledgeBase = new HashMap<Coordinates,Percept>();
    private ArrayList<Coordinates> moves = new ArrayList<Coordinates>();
    
    private JSONArray moveList = new JSONArray();
    
    private Coordinates currentPosition;

    boolean goldCollected = false;
    int runs = 0;
    int maxRuns = 10;

    
    public Agent(World world ) {
    	this.world = world;
    	currentPosition = new Coordinates(1,1,Directions.EAST);    	
    }
    
    void moveAgent() {
    	
        while (!goldCollected) {

        	runs += 1;
        	
        	Coordinates oldPosition = new Coordinates(currentPosition);
        	
        	moveList.put(oldPosition.creatDirectionJSON());
        	
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
               	
        	if (runs > maxRuns) {
        		break;
        	}
        }
        
        if(goldCollected) {
        	        	
        	System.out.println();
        	
        	goBackToStart();
        	
                        	
        	JSONObject movesJSONObject = new JSONObject();
        	try {
        		
				movesJSONObject.put("moves", moveList);
				FileWriter file = new FileWriter("./player-moves.txt");
    			file.write(movesJSONObject.toString());
    			file.close();
    			
    			world.exportMap();
    			
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	
        }
    }
    
    
    private Coordinates getNextMove(Coordinates playerPosition) {
    	
    	Percept percept = world.getPercept(playerPosition);
    	
    	if (percept.glitter) {
    		goldCollected = true;
    		System.out.print("GOT GOLD>> ");
    		
    	} else {
    		
    		if (percept.stench) {
    			
    			Coordinates possibleWhmpusLocation = locateWhmpus();
    			
    			if (possibleWhmpusLocation != null) {
    				
    				possibleWhmpusLocation.direction = playerPosition.determineDirection(possibleWhmpusLocation);
    				return possibleWhmpusLocation;
    			}
    			
    			Coordinates safeLocation = confirmDanger(playerPosition);
    			
    			if (safeLocation != null) {
    				System.out.print("Got Safe Spot>> ");
    				return safeLocation;
    			} else {
    				System.out.print("No Safe Spot>> ");
    				return backTrack();
    			}
    			
    		} else if (percept.breez) {
    		        			
    			Coordinates safeLocation = confirmDanger(playerPosition);
    			
    			if (safeLocation != null) {
    				return safeLocation;
    			}
    			
        		Coordinates playerPositionCopy = new Coordinates(playerPosition);
        		
        		playerPositionCopy.moveAhead();
        		
        		if (checkVisited(playerPositionCopy)) {
        			System.out.print("Found Ahead>> ");
        			return currentPosition.moveAhead();
        			
        		} else if ( getVisitedAdjecentCell(playerPosition) != null) {
        			
        			System.out.print("Found Adjacent>> ");
        			return getVisitedAdjecentCell(playerPosition);
        			
        		} else {
        				return backTrack();
        		}
        		
        	} else {
        		Coordinates unvisitedCoordinate = getUnvisitedSpot(playerPosition);
        		System.out.print("Unvisited Spot>> ");
        		 return unvisitedCoordinate;
        	}
    	}
    	
    	return playerPosition;
    	
    }
    
    void goBackToStart() {
    	
    	Coordinates position = popMove();
    	position.printPosition();
    	    	
    	while(moves.size() > 0) {
    		
    		findSouthWestTiles(position);
    		
    		position = popMove();
    		
    		moveList.put(position.creatDirectionJSON());

    		if (position.equals(new Coordinates(1,1))) {
    			return;
    		}
    	}
    }
    
    
    private void findSouthWestTiles(Coordinates playerPosition) {
    	
    	System.out.print("Finding adjecent of: ");
    	playerPosition.printPosition();
    	System.out.println();
    	
    	int index;
    	for(index = moves.size() - 1; index >= 0; index -= 1 ) {
    		
    		Coordinates possibleShortcutTile = moves.get(index);
    		
    		if((possibleShortcutTile.getRow() == playerPosition.getRow() - 1 )&& (possibleShortcutTile.getCol() == playerPosition.getCol())) {
        		System.out.print("Got South: ");
    			possibleShortcutTile.printPosition();
    			System.out.println();
    			
    			break;
    		} else if ((possibleShortcutTile.getCol() == playerPosition.getCol() - 1) && (possibleShortcutTile.getRow() == playerPosition.getRow())) {
        		System.out.print("Got West: ");
    			possibleShortcutTile.printPosition();
    			System.out.println();
    			
    			break;
    		}	
    	}
    	
    	for (int remIndex = moves.size() - 1; remIndex > index; remIndex -= 1) {
    		
    		moves.remove(remIndex);
       	}
    	
//    	System.out.print("Positions after removing redundant: ");
//    	for(Coordinates move: moves) {
//    		move.printPosition();
//    	}
//    	System.out.println();
        	
    }
    
    private Coordinates popMove() {
    	Coordinates topMove = moves.remove(moves.size() - 1);
    	return topMove;
    }
    
    private Coordinates backTrack() {
    	   
    	if(moves.size() > 0) {
    		moves.remove(moves.size() - 1); 
	     
	        Coordinates basePoint = new Coordinates(moves.get(moves.size() - 1));
       
	        return basePoint;
    	}
    	
    	return null;
        
    }
    
    
    
    private Coordinates	locateWhmpus() {
    	
    	ArrayList<Percept> whmpusPerceptList = new ArrayList<Percept>();
    	
    	for(Percept percept: knowledgeBase.values()) {
    		if(percept.stench) {
    			whmpusPerceptList.add(percept);
    		}
    	}
    	
    	if(whmpusPerceptList.size() >= 3) {
    		
    		Coordinates tile1 = whmpusPerceptList.get(0).position;
    		Coordinates tile2 = whmpusPerceptList.get(1).position;
    		Coordinates tile3 = whmpusPerceptList.get(2).position;
    		
    		int whmpusRow = 0;
    		int whmpusCol = 0;
    		
    		if(tile1.getRow() == tile2.getRow()) {
    			whmpusRow = tile1.getRow();
    			whmpusCol = tile3.getCol();
    		} else if (tile1.getRow() == tile3.getRow()) {
    			whmpusRow = tile1.getRow();
    			whmpusCol = tile2.getCol();
    		} else if (tile1.getCol() == tile2.getCol()) {
    			whmpusCol = tile1.getCol();
    			whmpusRow = tile3.getRow();
    		} else if (tile1.getCol() == tile3.getCol()) {
    			whmpusCol = tile1.getCol();
    			whmpusRow = tile2.getRow();
    		}
    		
    		if (whmpusCol != 0 && whmpusRow!=0) {
    			
    			
    			for(Percept percept: knowledgeBase.values()) {
    	    		percept.stench = false;
    	    	}
    			
    			return new Coordinates(whmpusRow, whmpusCol);	
    		}
    	}
    	
    	return null;
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
     
   
    private boolean checkVisited(Coordinates playerCoordinates) {
    	//playerCoordinates.printPosition();
    	//System.out.println(visited.contains(playerCoordinates));
    	return visited.contains(playerCoordinates);
    }

}