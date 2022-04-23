package com.example.rps;

import android.os.Build;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;


import androidx.annotation.RequiresApi;

public class MCTS_Node
{
    private double totalScore; // the total value of all child nodes and rollouts it had.
    private int numberOfVisits; // number of times node has been visited / explored
    private ArrayList<MCTS_Node> childNodes;
    private Pair<Integer, Integer> moveLocation;
    private Player player;   // The player who made the move
    private Computer computer;
    private boolean turn; // True if player turn, else false.
//    private static StopWatch stopWatch = new StopWatch();
    // Keeps the index of the maximal child the node has visited -
    // after each expansion, the number will grow by one, as long as it's smaller than the amount of possible moves.
    private int maxChild;

    public MCTS_Node(Pair<Integer, Integer> moveLocation, Player player, Computer computer, boolean turn)
    {
        totalScore = 0;
        numberOfVisits = 0;
        childNodes = new ArrayList<MCTS_Node>();
        this.moveLocation = moveLocation;
        this.player = new Player(player);
        this.computer = new Computer(computer, player);
        this.turn = turn;
        maxChild = -1;          // No expansion happened yet.
    }

    /// <summary>
    /// Find the move that will most likely lead to the AI's win.
    /// </summary>
    /// <returns>Location of best move on board.</returns>
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Pair<Integer,Integer> StartMCTS(Player player, Computer computer)
    {
        StopWatch stopWatch = new StopWatch();
        MCTS_Node mainNode = new MCTS_Node(new Pair<>(-1,-1), player, computer, true);
        mainNode.GetChildren();
        stopWatch.start();

        while(stopWatch.getElapsedTime().getNano() < 1000)
        {
            int bestNodeIndex = mainNode.FindBestNode();
            mainNode.totalScore += mainNode.childNodes.get(bestNodeIndex).RunMCTS();
            mainNode.numberOfVisits++;
        }
        stopWatch.stop();
        return mainNode.childNodes.get(mainNode.FindBestNode()).getMovePair();
    }

    public Pair<Integer, Integer> getMovePair()
    {
        return moveLocation;
    }

    /// <summary>
    /// Finds the best node according to UCB1 results.
    /// </summary>
    /// <returns>The index of the best node in the nodes.</returns>
    public int FindBestNode()
    {
        maxChild++;
        int bestNodeIndex = 0;
        // Find node that has the best UCB1 score
        if (maxChild > 0)
        {
            double bestNodeScore = childNodes.get(0).UCB1(numberOfVisits);
            double curNodeScore;
            for (int i = 1; i < maxChild && i < childNodes.size(); i++)
            {
                curNodeScore = childNodes.get(i).UCB1(numberOfVisits);
                if (curNodeScore > bestNodeScore)
                {
                    bestNodeScore = curNodeScore;
                    bestNodeIndex = i;
                }
            }
        }
        return bestNodeIndex;
    }

    /// <summary>
    /// Runs MCTS search until it reaches end of game.
    /// </summary>
    /// <returns></returns>
    public double RunMCTS()
    {
        double score = 0;
        if (numberOfVisits == 0)
        {
            totalScore = StartSimulation(moveLocation);
        }
        else
        {
            if (numberOfVisits == 1)
                GetChildren();
            int bestNodeIndex = FindBestNode();
            score = childNodes.get(bestNodeIndex).RunMCTS();
            totalScore += score;
        }
        numberOfVisits++;
        return score;
    }

    private void GetChildren()
    {

        List<Pair<Integer,Integer>> moveList = computer.get_all_moves_sorted(!turn);
        for (int i = 0; i < moveList.size(); i++)
        {
            childNodes.add(new MCTS_Node(moveList.get(i), player, computer, !turn));
        }
        maxChild = 0;
    }

    /// <summary>
    /// Returns the score of the UCB1 formule for current node.
    /// Formula: Vi + 2 * sqrt((lnN) / ni)
    ///     Vi - average value (totalScore/numberOfVisits) of given node
    ///     N - overall visits from PARENT node (numberOfVisits of parent)
    ///     ni - numberOfVisits of current node
    /// </summary>
    /// <returns></returns>
    public double UCB1(int N)
    {
        double Vi = totalScore / numberOfVisits;
        return Vi + 2 * Math.sqrt((Math.log(N)) / numberOfVisits);
    }



    /// <summary>
    /// Starts simulation for given node.
    /// </summary>
    private double StartSimulation(Pair<Integer,Integer> chosenMoveLocation)
    {
        // Place chosen move on board.
        computer.makeMoveMCTS(chosenMoveLocation, turn);

        double score = Simulation(true, 40);
        totalScore += score;
        return score;
    }

    /// <summary>
    /// Runs a simulation of the game;
    /// </summary>
    /// <param name="player"></param>
    /// <returns></returns>
    private double Simulation(boolean turn, int countDown)
    {
        Pair<Pair<Integer,Integer>, Double> move_and_rate;
        double rate;
        if (countDown > 0)
        {
            move_and_rate = computer.getBestMove(turn);
            rate = move_and_rate.second;
            if (rate == 10000000 || rate == -10000000)
            {
                if ((turn && rate == 10000000) || (!turn && rate != 10000000))
                    return -1;
                return 1;
            }
            else
            {
                computer.makeMoveMCTS(move_and_rate.first, turn);
            }
            for (int key : player.getPieces().keySet())
            {
                System.out.println("Player piece at loc: " + key );// player.getPieces().get(key));
            }
            for (int key : computer.getPieces().keySet())
            {
                System.out.println("PC piece at loc: " + key ); //computer.getPieces().get(key));
            }
            return Simulation(!turn, countDown - 1);

        }
        else
        {
            Simulation s = new Simulation(player,computer,player.gameActivity.all_pieces,0,turn,false,0,0);
            rate = s.getRate();
            return rate;
        }
    }

}

