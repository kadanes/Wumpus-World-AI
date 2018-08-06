package Whmpus;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import Whmpus.Constants.Directions;

class World {

    private int rowCount = 4;
    private int colCount = 4;

    private Coordinates whmpusPosition = new Coordinates(3,1);
    private Coordinates goldPosition = new Coordinates(3,2);
    private Coordinates[] pitPositions = {
        new Coordinates(3 ,3),
        new Coordinates(4,4),
        new Coordinates(1,3)
    };

    
    Percept getPercept(Coordinates playerPosition) {
    	
    	boolean hasStench = false;
    	boolean hasGlitter = false;
    	boolean hasBreez = false;
    	boolean hasBump = false;
    	
    	for (Coordinates pitPosition: pitPositions) {
    		hasBreez = checkIfAdjacent(playerPosition, pitPosition);
    		if (hasBreez) 
    			break;
    	}
    	hasStench = checkIfAdjacent(playerPosition, whmpusPosition);
    	hasGlitter = playerPosition.equals(goldPosition);
    	hasBump = checkBump(playerPosition);
    	
    	return new Percept(hasBreez,hasBump,hasGlitter,hasStench,playerPosition);
    	
    }
    
    public ArrayList<Coordinates> getAdjacentCells(Coordinates playerPosition) {
    	
    	ArrayList<Coordinates> dangerZoneList = new ArrayList<Coordinates>();
    	
    	int playerRow = playerPosition.getRow();
    	int playerCol = playerPosition.getCol();
    	
    	
    	if(playerCol - 1 >= 1) {
    		Coordinates eastCell = new Coordinates(playerRow, playerCol - 1);
    		dangerZoneList.add(eastCell);
    	}
    	
    	if(playerCol + 1 <= colCount) {
    		Coordinates westCell = new Coordinates(playerRow, playerCol + 1);
    		dangerZoneList.add(westCell);
    	}
    	
    	if(playerRow - 1 >= 1) {
    		Coordinates southCell = new Coordinates(playerRow - 1, playerCol);
    		dangerZoneList.add(southCell);
    	}
    	
    	if(playerRow + 1 <= rowCount) {
    		Coordinates northCell = new Coordinates(playerRow + 1, playerCol);
    		dangerZoneList.add(northCell);
    	}
    	
    	return dangerZoneList;
    }
    
    private boolean checkIfAdjacent(Coordinates playerPosition, Coordinates mapElement) {
    	
    	boolean isAdjacent = false;
    	
    	int playerRow = playerPosition.getRow();
    	int playerCol = playerPosition.getCol();
    	
    	int elementRow = mapElement.getRow();
    	int elementCol = mapElement.getCol();
    	
    	if (playerRow == elementRow && ( playerCol == elementCol - 1 || playerCol == elementCol + 1)) {
    		return true;
    	} else if (playerCol == elementCol && ( playerRow == elementRow - 1 || playerRow == elementRow + 1 )) {
    		return true;
    	}
     	
    	return isAdjacent;	
    }
    
    private boolean checkBump(Coordinates playerPosition) {
    	boolean hasBump = false;
    	int playerRow = playerPosition.getRow();
    	int playerCol = playerPosition.getCol();
    	Directions direction = playerPosition.getDirection();
    	
    	if ( playerRow < 0 || playerRow > rowCount || playerCol < 0 || playerCol > colCount) {
    		
    		hasBump = true;
    		return hasBump;
    	}
    	
    	if(direction == Directions.EAST && playerCol >= colCount) {
    		hasBump = true;
    	} else if (direction == Directions.WEST && playerCol <= 1) {
    		hasBump = true;
    	} else if (direction == Directions.SOUTH && playerRow <= 1) {
    		hasBump = true;
    	} else if (direction == Directions.NORTH && playerRow >= rowCount) {
    		hasBump = true;
    	}
    	
    	return hasBump;
    
    }
    
}