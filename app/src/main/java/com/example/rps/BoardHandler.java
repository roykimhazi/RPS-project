package com.example.rps;

import java.util.HashMap;

public class BoardHandler
{
    private int [][] intBoard;
    private int column;
    private int row;

    public BoardHandler(int column, int row)
    {
        this.column = column;
        this.row = row;
        this.intBoard = new int[column][row];
        mineSpread();
        calculateAreaMines();

    }

    public int[][] getIntBoard()
    {
        return intBoard;
    }

    private void mineSpread()
    {
        /*
         *This function spread the mines around the board
         */
        for (int z = 0; z < (column*row)/4; z++)
        {
            boolean foundPlace = false;
            while (!foundPlace)
            {
                int x = (int) (Math.random() * (row-1));
                int y = (int) (Math.random() * (column-1));
                if (this.intBoard [y][x] != -1)
                {
                    this.intBoard[y][x] = -1;
                    foundPlace = true;
                }
            }
        }
    }
    private void calculateAreaMines()
    {
        /*
         *This function calculate the mines around every square
         */
        int count = 0;

        if (this.intBoard[0][0] != -1)
        {
            if (this.intBoard[0][1] == -1)
                count++;
            if (this.intBoard[1][1] == -1)
                count++;
            if (this.intBoard[1][0] == -1)
                count++;
            this.intBoard[0][0] = count;
        }

        if (this.intBoard[column-1][0] != -1)
        {
            count = 0;
            if (this.intBoard[column-2][0] == -1)
                count++;
            if (this.intBoard[column-2][1] == -1)
                count++;
            if (this.intBoard[column-1][1] == -1)
                count++;
            this.intBoard[column-1][0] = count;
        }

        if (this.intBoard[0][row-1] != -1)
        {
            count = 0;
            if (this.intBoard[0][row-2] == -1)
                count++;
            if (this.intBoard[1][row-2] == -1)
                count++;
            if (this.intBoard[1][row-1] == -1)
                count++;
            this.intBoard[0][row-1] = count;
        }

        if (this.intBoard[column-1][row-1] != -1)
        {
            count = 0;
            if (this.intBoard[column-1][row-2] == -1)
                count++;
            if (this.intBoard[column-2][row-2] == -1)
                count++;
            if (this.intBoard[column-2][row-1] == -1)
                count++;
            this.intBoard[column-1][row-1] = count;
        }




        for (int x = 1; x < row-1; x++)
        {
            int y = 0;
            int counter = 0;
            if (this.intBoard[y][x] != -1)
            {
                if(this.intBoard[y][x - 1] == -1)
                    counter++;
                if(this.intBoard[y + 1][x - 1] == -1)
                    counter++;
                if(this.intBoard[y + 1][x] == -1)
                    counter++;
                if(this.intBoard[y + 1][x + 1] == -1)
                    counter++;
                if(this.intBoard[y][x + 1] == -1)
                    counter++;
                this.intBoard[y][x] = counter;
            }
        }

        for (int x = 1; x < row-1; x++)
        {
            int y = column-1;
            int counter = 0;
            if (this.intBoard[y][x] != -1)
            {
                if(this.intBoard[y][x - 1] == -1)
                    counter++;
                if(this.intBoard[y - 1][x - 1] == -1)
                    counter++;
                if(this.intBoard[y - 1][x] == -1)
                    counter++;
                if(this.intBoard[y - 1][x + 1] == -1)
                    counter++;
                if(this.intBoard[y][x + 1] == -1)
                    counter++;
                this.intBoard[y][x] = counter;
            }
        }

        for (int y = 1; y < column-1; y++)
        {
            int x = 0;
            int counter = 0;
            if (this.intBoard[y][x] != -1)
            {
                if(this.intBoard[y - 1][x] == -1)
                    counter++;
                if(this.intBoard[y - 1][x + 1] == -1)
                    counter++;
                if(this.intBoard[y][x + 1] == -1)
                    counter++;
                if(this.intBoard[y + 1][x + 1] == -1)
                    counter++;
                if(this.intBoard[y + 1][x] == -1)
                    counter++;
                this.intBoard[y][x] = counter;
            }
        }

        for (int y = 1; y < column-1; y++)
        {
            int x = row-1;
            int counter = 0;
            if (this.intBoard[y][x] != -1)
            {
                if(this.intBoard[y - 1][x] == -1)
                    counter++;
                if(this.intBoard[y - 1][x - 1] == -1)
                    counter++;
                if(this.intBoard[y][x - 1] == -1)
                    counter++;
                if(this.intBoard[y + 1][x - 1] == -1)
                    counter++;
                if(this.intBoard[y + 1][x] == -1)
                    counter++;
                this.intBoard[y][x] = counter;
            }
        }


        for (int y = 1; y < column-1; y++)
        {
            for (int x = 1; x < row-1; x++)
            {
                int counter = 0;
                if (this.intBoard[y][x] != -1)
                {
                    if(this.intBoard[y][x - 1] == -1)
                        counter++;
                    if(this.intBoard[y - 1][x - 1] == -1)
                        counter++;
                    if(this.intBoard[y - 1][x] == -1)
                        counter++;
                    if(this.intBoard[y - 1][x + 1] == -1)
                        counter++;
                    if(this.intBoard[y][x + 1] == -1)
                        counter++;
                    if(this.intBoard[y + 1][x + 1] == -1)
                        counter++;
                    if(this.intBoard[y + 1][x] == -1)
                        counter++;
                    if(this.intBoard[y + 1][x - 1] == -1)
                        counter++;
                    this.intBoard[y][x] = counter;
                }
            }
        }
    }

    public String toString()
    {
        /*
         *This function prints all of the numbers at their place on the board
         */
        String strBoard = "";
        for (int y = 0; y < column; y++)
        {
            String strRowBoard = "";
            for (int x = 0; x < row; x++)
            {
                strRowBoard += String.valueOf(this.intBoard[y][x]) + "  ";
            }
            strBoard += strRowBoard + "\n";
        }
        return "Board {\n" + strBoard + "\n}";
    }
}
