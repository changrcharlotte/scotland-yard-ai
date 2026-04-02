package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.graph.Traverser;
import jakarta.annotation.Nonnull;
import io.atlassian.fugue.Pair;
import uk.ac.bris.cs.scotlandyard.model.Ai;
import uk.ac.bris.cs.scotlandyard.model.Board;
import uk.ac.bris.cs.scotlandyard.model.Move;
import uk.ac.bris.cs.scotlandyard.model.Piece;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Transport;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard;
public class MrxAi implements Ai {

    @Nonnull @Override public String name() { return "MrxAi"; }

    @Nonnull @Override public Move pickMove(
            @Nonnull Board board,
            Pair<Long, TimeUnit> timeoutPair) {


        List<Integer>  detLocations= new ArrayList<Integer>();
        ImmutableSet<Piece> pieces= board.getPlayers();
        for(Piece piece : pieces){
            if(piece.isDetective()){
                Piece.Detective det = (Piece.Detective)piece;
                int location = board.getDetectiveLocation(det).orElse(-1);
                if (location != -1){
                    detLocations.add(location);
                }
            }
        }



        //i'm assuming the starting position of mrX is random as well

        ImmutableList<Move> moves = board.getAvailableMoves().asList();
        int mrXlocation = moves.get(0).source();

//        // returns a random move, replace with your own implementation
//        return moves.get(new Random().nextInt(moves.size()));



    }

    private int evaluateScore(Board board, int mrXlocation, List<Integer> detLocations){
        return 0;
    }

//    private ImmutableSet<Move> checkNeighbouringNodes(Board board, int mrXlocation, List<Integer> detLocations){
//
//        List<Integer> banned = new ArrayList<Integer>();
//        for(int destination : board.getSetup().graph.adjacentNodes(mrXlocation)){
//            if(detLocations.contains(destination)){
//
//            }
//        }
//        board.
//    }






}


