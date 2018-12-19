package Whmpus;

import org.json.JSONException;
import org.json.JSONObject;

import Whmpus.Constants.Directions;

class Coordinates {
    private int row;
    private int col;
    public Directions direction = null;

    
    public Coordinates(Coordinates point) {
		// TODO Auto-generated constructor stub
    	row = point.row;
    	col = point.col;
    	if( point.direction != null) {
    		direction = point.direction;
    	}
	}
    
    public Coordinates(int row, int col, Directions direction) {
        this.row = row;
        this.col = col;
        this.direction = direction;
    }
    
    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
        this.direction = Directions.NONE;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public Directions getDirection() {
		return direction;
	}
    
    @Override
    public boolean equals(Object obj) {
    	
    	if(this == obj) {
    		
    		return true;
    		
    	} else if (obj instanceof Coordinates) {
    		
    		Coordinates other = (Coordinates)obj;
    		if (row == other.getRow() && col == other.getCol()) {
    			return true; 
    		}  
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + row;
        result = 31 * result + col;
        return result;
    }
    
    Coordinates moveEast() {
    	col += 1;
    	direction = Directions.EAST;
    	return this;
    }
    
    Coordinates moveWest() {
    	col -= 1;
    	direction = Directions.WEST;
    	return this;
    }
    
    Coordinates moveNorth() {
    	row += 1;
    	direction = Directions.NORTH;
    	return this;
    }
    
    Coordinates moveSouth() {
    	row -= 1;
    	direction = Directions.SOUTH;
    	return this;
    }
    
    Coordinates moveAhead() {
    	switch (direction) {
		case NORTH:
			moveNorth();
			break;
			
		case SOUTH:
			moveSouth();
			break;
			
		case EAST:
			moveEast();
			break;
			
		case WEST:
			moveWest();
			break;

		default:
			break;
		}
    	
    	return this;
    }
    
    public Directions determineDirection(Coordinates end) {
    	
    	
    	int startRow = row;
    	int startCol = col;
    	
    	int endRow = end.getRow();
    	int endCol = end.getCol();
    	
    	if (endCol == startCol - 1) {
    		return Directions.WEST;
    	} else if (endCol == startCol + 1) {
    		return Directions.EAST;
    	} else if ( endRow == startRow - 1 ) {
    		return Directions.SOUTH;
    	} else if ( endRow == startRow + 1 ) {
    		return Directions.NORTH;
    	}
    	
    	return Directions.NONE;
    }
    
    JSONObject creatDirectionJSON() {
    	JSONObject coordinateObject = new JSONObject();
    	try {
			coordinateObject.put("row",row);
	    	coordinateObject.put("col",col);
	    	coordinateObject.put("direction",direction);
	    	return coordinateObject;
	    	
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public void printPosition() {
    	System.out.print("("+row+","+col+","+direction+")");
    }
}