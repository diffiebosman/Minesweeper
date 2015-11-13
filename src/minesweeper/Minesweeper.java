package minesweeper;

public class Minesweeper {
    public static void main(String[] args) {
        // this file was just to test the text-based functionality
        Board board =  new Board(7, 7, 5);      
        board.click(0,0);        
        board.print();
    }
}
