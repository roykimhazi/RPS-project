package com.example.rps;

import android.os.Build;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.RequiresApi;

/**
 * This class represents a node of MCTS.
 */
public class MCTS_Node
{
    private double totalScore; // the total value of all child nodes and rollouts it had.
    private int numberOfVisits; // number of times node has been visited / explored
    private ArrayList<MCTS_Node> childNodes;
    private Pair<Integer, Integer> moveLocation;
    private Player player;   // The player who made the move
    private Computer computer;
    private boolean turn; // True if player turn, else false.
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
        maxChild = -1; // No expansion happened yet.
    }

    /**
     * This function starts the MCTS algorithm.
     * @param player
     * @param computer
     * @return the best move to do.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Pair<Integer,Integer> StartMCTS(Player player, Computer computer)
    {
        StopWatch stopWatch = new StopWatch();
        MCTS_Node mainNode = new MCTS_Node(new Pair<>(-1,-1), player, computer, true);
        mainNode.GetChildren();
        stopWatch.start();

        while(stopWatch.getElapsedTime().getNano() < 2000)
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

    /**
     * This function finds the best node according to UCB1 results.
     * @return best node index.
     */
    public int FindBestNode()
    {
        maxChild++;
        int bestNodeIndex = 0;
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

    /**
     * This function runs MCTS search until it reaches end of game.
     * @return score.
     */
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

    /**
     * This function creates new nodes as children nodes.
     */
    private void GetChildren()
    {

        List<Pair<Integer,Integer>> moveList = computer.get_all_moves_sorted(!turn);
        for (int i = 0; i < moveList.size(); i++)
        {
            childNodes.add(new MCTS_Node(moveList.get(i), player, computer, !turn));
        }
        maxChild = 0;
    }

    /**
     * Formula: Vi + 2 * sqrt((lnN) / ni)
     * Vi - average value (totalScore/numberOfVisits) of given node
     * N - overall visits from PARENT node (numberOfVisits of parent)
     * ni - numberOfVisits of current node
     * @param N
     * @return returns the score of the UCB1 formule for current node.
     */
    public double UCB1(int N)
    {
        double Vi = totalScore / numberOfVisits;
        return Vi + 2 * Math.sqrt((Math.log(N)) / numberOfVisits);
    }

    /**
     * Starts simulation for given node.
     * @param chosenMoveLocation
     * @return score.
     */
    private double StartSimulation(Pair<Integer,Integer> chosenMoveLocation)
    {
        computer.makeMoveMCTS(chosenMoveLocation, turn); // Play chosen move.
        double score = Simulation(true, 20);
        totalScore += score;
        return score;
    }

    /**
     * Runs a simulation of the game.
     * @param turn
     * @param countDown
     * @return score.
     */
    private double Simulation(boolean turn, int countDown)
    {
        Pair<Pair<Integer,Integer>, Double> move_and_rate;
        double rate;
        if (countDown > 0)
        {
            move_and_rate = computer.getBestMove(turn);
            rate = move_and_rate.second;
            if (rate == 10000000 || rate == -10000000 || rate == -666.666)
            {
                if ((turn && rate == 10000000 || rate == -666.666) || (!turn && (rate != 10000000 || rate == -666.666)))
                    return -1;
                return 1;
            }
            else
            {
                computer.makeMoveMCTS(move_and_rate.first, turn);
            }
            return Simulation(!turn, countDown - 1);
        }
        else
        {
            Simulation s = new Simulation(player, computer, player.gameActivity.all_pieces, turn,false);
            rate = s.getRate();
            return rate;
        }
    }
}

