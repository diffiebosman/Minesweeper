package minesweeper;

import java.util.Random;

public class Board {
    int board[][];
    // 0 - 8 = number of adjescent mines
    // -1 = unclicked
    // -2 = mine
    // -3 = flag over mine
    // -4 = flag over non-mine
    
    int height, width, nMines;
    boolean hasWon = false, hasLost = false, firstClick = true;
    
    public Board(int h, int w, int _nMines)
    {
        height = h;
        width = w;
        board = new int[height][width];
        
        for(int row = 0; row < height; row++)
        {
            for(int col = 0; col < width; col++)
            {
                board[row][col] = -1;
            }
        }
        
        nMines = _nMines;   
               
    }
    
    public void print()
    {
        if(hasWon)
        {
            System.out.println("You Win!!");
            return;
        }
        
        if(hasLost)
        {
            System.out.println("You lose :(");
            return;
        }
        
        for(int row = 0; row < height; row++)
        {
            for(int col = 0; col < width; col++)
            {
                if(board[row][col] >= 0) System.out.print(" ");
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
    
    public void placeMines(int n_x, int n_y)
    {
        // randomly places mines on the board
        // n_x and n_y are the coordinates of the player's first click
        // a mine cannot be placed where a player clicked first
        
        Random random = new Random();
        
        for(int i = 0; i < nMines; i++)
        {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            
            while(isMine(x,y) || x == n_x || y == n_y)
            {
                x = random.nextInt(width - 1);
                y = random.nextInt(height - 1);              
            }
            
            board[x][y] = -2; // mine placed
        }
    }
    
    public void click(int x, int y)
    {
        if(board[x][y] == -3 || board[x][y] == -4)
            return;
        
        
        if(firstClick)
        {
            firstClick = false;
            placeMines(x, y);
        }
        if(isMine(x,y))
        {
            hasLost = true;
            return;
        }
        
        // you lose
        
        board[x][y] = getAdjescentMines(x, y);
        if(board[x][y] == 0) clickAdjescent(x, y);
        // if a block has no adjescent mines, 'click' all adjescent blocks
        // this has a recursive effect
        
        if(hasWon()) hasWon = true;      
        
    }
    
    public void rightClick(int x, int y)
    {   
        // right click places a "flag" of where you think a mine is
        // flags are removed by right-clicking again
        // flags can only be placed on previously unclicked areas
        
        if(board[x][y] == -3)
        {
            board[x][y] = -2;
        }
        else if(board[x][y] == -4)
        {
            board[x][y] = -1;
        }
        else if(board[x][y] == -1)
        {
            board[x][y] = -4;
        }
        else if(board[x][y] == -2)
        {
            board[x][y] = -3;
        }
    }
    
    public boolean hasWon()
    {
        for(int row = 0; row < height; row++)
        {
            for(int col = 0; col < width; col++)
            {
                if(board[row][col] == -1)
                    return false;
            }           
        }    
        
        return true;
    }
    
    public boolean isMine(int x, int y)
    {
        return board[x][y] == -2;
    }
    
    public void clickAdjescent(int x, int y)
    {
        // 'clicks' all the adjescent blocks
        // doesn't click blocks with 0-values to prevent stackoverflow
        
        if(x == 0)
        {
            if(y == 0) // top-left corner
            {
                if(board[x+1][y] != 0) click(x + 1, y);
                if(board[x+1][y + 1] != 0) click(x + 1, y + 1);
                if(board[x][y+1] != 0) click(x, y + 1);
            }
            else if(y == height - 1) //bottom-left corner
            {
                if(board[x+1][y] != 0) click(x + 1, y);
                if(board[x+1][y-1] != 0) click(x + 1, y - 1);
                if(board[x][y-1] != 0) click(x, y - 1);                
            }
            else // left border
            {
                if(board[x][y-1] != 0) click(x, y - 1);
                if(board[x+1][y-1] != 0) click(x + 1, y - 1);
                if(board[x+1][y] != 0) click(x + 1, y);               
                if(board[x+1][y+1] != 0) click(x + 1, y + 1);                
                if(board[x][y+1] != 0) click(x, y + 1);                 
            }              
        }
        else if(x == width - 1)
        {
            if(y == 0) // top-right corner
            {
                if(board[x-1][y] != 0) click(x - 1, y);
                if(board[x-1][y+1] != 0) click(x - 1, y + 1);
                if(board[x][y+1] != 0) click(x, y + 1);
            }
            else if(y == height - 1) //bottom-right corner
            {
                if(board[x-1][y] != 0) click(x - 1, y);
                if(board[x-1][y-1] != 0) click(x - 1, y - 1);
                if(board[x][y-1] != 0) click(x, y - 1);                
            }
            else //right border
            {
                if(board[x][y-1] != 0) click(x, y - 1);
                if(board[x-1][y-1] != 0) click(x - 1, y - 1);
                if(board[x-1][y] != 0) click(x -1, y);                
                if(board[x-1][y+1] != 0) click(x - 1, y + 1);                 
                if(board[x][y+1] != 0) click(x, y + 1);               
            }                     
        }
        else if(y == 0) // top border
        {
            if(board[x-1][y] != 0) click(x - 1, y);
            if(board[x-1][y+1] != 0) click(x - 1, y + 1);
            if(board[x][y+1] != 0) click(x, y + 1);
            if(board[x+1][y+1] != 0) click(x + 1, y + 1);
            if(board[x+1][y] != 0) click(x + 1, y);
        }
        else if(y == height - 1) // bottom border
        {
            if(board[x-1][y] != 0) click(x - 1, y);
            if(board[x-1][y-1] != 0) click(x - 1, y - 1);
            if(board[x][y-1] != 0) click(x, y - 1);
            if(board[x+1][y-1] != 0) click(x + 1, y - 1);
            if(board[x+1][y] != 0) click(x + 1, y);          
        }
        else // middle
        {
            if(board[x-1][y-1] != 0) click(x - 1, y - 1);
            if(board[x][y-1] != 0) click(x, y - 1);
            if(board[x+1][y-1] != 0) click(x + 1, y - 1);
            if(board[x+1][y] != 0) click(x + 1, y);
            if(board[x+1][y+1] != 0) click(x + 1, y + 1);
            if(board[x][y+1] != 0) click(x, y + 1);
            if(board[x-1][y+1] != 0) click(x - 1, y + 1);
            if(board[x-1][y] != 0) click(x - 1, y);
        }        
    }
    
    public int getAdjescentMines(int x, int y)
    {
        //gets the number of adjescent mines of a block
        //(horizontal, vertical and diagonal)
        int adj = 0;
        
        if(x == 0)
        {
            if(y == 0) // top-left corner
            {
                if(board[x+1][y] == -2 || board[x+1][y] == -3) adj++;
                if(board[x+1][y+1] == -2 || board[x+1][y+1] == -3) adj++;
                if(board[x][y+1] == -2 || board[x][y+1] == -3) adj++;
            }
            else if(y == height - 1) //bottom-left corner
            {
                if(board[x+1][y] == -2 || board[x+1][y] == -3) adj++;
                if(board[x+1][y-1] == -2 || board[x+1][y-1] == -3) adj++;
                if(board[x][y-1] == -2 || board[x][y-1] == -3) adj++;                
            }
            else // left border
            {
                if(board[x][y-1] == -2 || board[x][y-1] == -3) adj++;
                if(board[x+1][y-1] == -2 || board[x+1][y-1] == -3) adj++;
                if(board[x+1][y] == -2 || board[x+1][y] == -3) adj++;                 
                if(board[x+1][y+1] == -2 || board[x+1][y+1] == -3) adj++;                 
                if(board[x][y+1] == -2 || board[x][y+1] == -3) adj++;                 
            }              
        }
        else if(x == width - 1)
        {
            if(y == 0) // top-right corner
            {
                if(board[x-1][y] == -2 || board[x-1][y] == -3) adj++;
                if(board[x-1][y+1] == -2 || board[x-1][y+1] == -3) adj++;
                if(board[x][y+1] == -2 || board[x][y+1] == -3) adj++;
            }
            else if(y == height - 1) //bottom-right corner
            {
                if(board[x-1][y] == -2 || board[x-1][y] == -3) adj++;
                if(board[x-1][y-1] == -2 || board[x-1][y-1] == -3) adj++;
                if(board[x][y-1] == -2 || board[x][y-1] == -3) adj++;                
            }
            else //right border
            {
                if(board[x][y-1] == -2 || board[x][y-1] == -3) adj++;
                if(board[x-1][y-1] == -2 || board[x-1][y-1] == -3) adj++;
                if(board[x-1][y] == -2 || board[x-1][y] == -3) adj++;                 
                if(board[x-1][y+1] == -2 || board[x-1][y+1] == -3) adj++;                 
                if(board[x][y+1] == -2 || board[x][y+1] == -3) adj++;                 
            }                     
        }
        else if(y == 0) // top border
        {
            if(board[x-1][y] == -2 || board[x-1][y] == -3) adj++;
            if(board[x-1][y+1] == -2 || board[x-1][y+1] == -3) adj++;
            if(board[x][y+1] == -2 || board[x][y+1] == -3) adj++;
            if(board[x+1][y+1] == -2 || board[x+1][y+1] == -3) adj++;
            if(board[x+1][y] == -2 || board[x+1][y] == -3) adj++;
        }
        else if(y == height - 1) // bottom border
        {
            if(board[x-1][y] == -2 || board[x-1][y] == -3) adj++;
            if(board[x-1][y-1] == -2 || board[x-1][y-1] == -3) adj++;
            if(board[x][y-1] == -2 || board[x][y-1] == -3) adj++;
            if(board[x+1][y-1] == -2 || board[x+1][y-1] == -3) adj++;
            if(board[x+1][y] == -2 || board[x+1][y] == -3) adj++;            
        }
        else // middle
        {
            if(board[x-1][y-1] == -2 || board[x-1][y-1] == -3) adj++;
            if(board[x][y-1] == -2 || board[x][y-1] == -3) adj++;
            if(board[x+1][y-1] == -2 || board[x+1][y-1] == -3) adj++;
            if(board[x+1][y] == -2 || board[x+1][y] == -3) adj++;
            if(board[x+1][y+1] == -2 || board[x+1][y+1] == -3) adj++;
            if(board[x][y+1] == -2 || board[x][y+1] == -3) adj++;
            if(board[x-1][y+1] == -2 || board[x-1][y+1] == -3) adj++;
            if(board[x-1][y] == -2 || board[x-1][y] == -3) adj++;
        }
        
        return adj;
    }
}
