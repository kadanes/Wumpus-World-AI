package Whmpus;

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
    
    void printPosition() {
    	System.out.print("("+row+","+col+","+direction+")");
    }
}