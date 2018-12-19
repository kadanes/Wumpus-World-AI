package Whmpus;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Whmpus.Constants.Directions;

class World {
	
	

    private int rowCount = 4;
    private int colCount = 4;

    private boolean isWhmpusDead = false;
    private boolean isGoldTaken = false;
  
    // //World 1
    // private Coordinates whmpusPosition = new Coordinates(3,1);
    // private Coordinates goldPosition = new Coordinates(3,2);
    // private Coordinates[] pitPositions = {
    //     new Coordinates(3 ,3),
    //     new Coordinates(4,4),
    //     new Coordinates(1,3)
    // };
   //World 2
//   private Coordinates whmpusPosition = new Coordinates(1,3);
//   private Coordinates goldPosition = new Coordinates(2,3);
//   private Coordinates[] pitPositions = {
//       new Coordinates(3 ,1),
//       new Coordinates(3,3),
//       new Coordinates(4,4)
//   };
  //World 3
  private Coordinates whmpusPosition = new Coordinates(1,3);
  private Coordinates goldPosition = new Coordinates(4,3);
  private Coordinates[] pitPositions = {
      new Coordinates(3 ,1),
      new Coordinates(4,4)
  };

    
    public void exportMap() {
    	
    	
    	JSONObject map = new JSONObject();
    	
    	JSONArray pitPositionsJSONArray = new JSONArray();
    	
    	for(Coordinates pit: pitPositions) {
    		pitPositionsJSONArray.put(pit.creatDirectionJSON());
    	}
    	
    	try {
			map.put("pits", pitPositionsJSONArray);
		  	map.put("whmpus", whmpusPosition.creatDirectionJSON());
	    	map.put("gold", goldPosition.creatDirectionJSON());
	    	
	    	FileWriter file = new FileWriter("./game-map.json");
	    	file.write(map.toString());
	    	file.close();
	    			
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
      	
    }
    
    public boolean goldTaken() {
		return isGoldTaken;
	}
   
    public boolean isWhmpusDead() {
    	return isWhmpusDead;
    }
    
    public Coordinates getGoldPosition() {
		return goldPosition;
	}
    
    public Coordinates getWhmpusPosition() {
		return whmpusPosition;
	}
    
    public Coordinates[] getPitPositions() {
		return pitPositions;
	}
    
    
    Percept getPercept(Coordinates playerPosition) {
    	
    	boolean hasStench = false;
    	boolean hasGlitter = false;
    	boolean hasbreeze = false;
    	boolean hasBump = false;
    	
    	for (Coordinates pitPosition: pitPositions) {
    		hasbreeze = checkIfAdjacent(playerPosition, pitPosition);
    		if (hasbreeze) 
    			break;
    	}
    	hasStench = checkIfAdjacent(playerPosition, whmpusPosition) && !isWhmpusDead;
    	hasGlitter = playerPosition.equals(goldPosition);
    	hasBump = checkBump(playerPosition);
    	
    	return new Percept(hasbreeze,hasBump,hasGlitter,hasStench,playerPosition);
    	
    }
    
    
    public void killWhmpus() {
    	isWhmpusDead = true;
    }
    
    public void takeGold() {
		isGoldTaken = true;
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
    	
    	if(direction == Directions.EAST && playerCol > colCount) {
    		hasBump = true;
    	} else if (direction == Directions.WEST && playerCol < 1) {
    		hasBump = true;
    	} else if (direction == Directions.SOUTH && playerRow < 1) {
    		hasBump = true;
    	} else if (direction == Directions.NORTH && playerRow > rowCount) {
    		hasBump = true;
    	}
    	
    	return hasBump;
    
    }
    
   
}