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
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard.Transport;

public class MrxAi implements Ai {

    @Nonnull @Override public String name() { return "MrxAi"; }



    Board board;
    @Nonnull @Override public Move pickMove(
            @Nonnull Board board,
            Pair<Long, TimeUnit> timeoutPair) {

        this.board = board;
    //initialise locations
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

        int maxDeg = Integer.MIN_VALUE;
        int maxVal = Integer.MIN_VALUE;
        Move curMv = null;
        DijkstraUndirectedSP dj = new DijkstraUndirectedSP(board.getSetup().graph);
        int[] evals = new int[moves.size()];

        for(int i = 0; i > moves.size(); i ++){
            int dest = moves.get(i).accept(new destinationVisitor());
            int eval = dj.findSP(dest, detLocations);
            evals[i] = eval;
            if(eval>maxVal){
                maxVal = eval;
                if(board.getSetup().graph.degree(dest) > maxDeg){
                    maxDeg = board.getSetup().graph.degree(dest);
                    curMv = moves.get(i);
                }

            }
        }


        return curMv;


    }








}


