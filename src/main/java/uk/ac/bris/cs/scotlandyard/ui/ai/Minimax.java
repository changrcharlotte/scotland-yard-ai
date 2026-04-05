package uk.ac.bris.cs.scotlandyard.ui.ai;
import com.google.common.collect.ImmutableList;

import uk.ac.bris.cs.scotlandyard.model.*;

public class Minimax {

        Board board;
        DijkstraUndirectedSP dj;

        private Minimax(Board board){
            this.board = board;
            this.dj = new DijkstraUndirectedSP(board.getSetup().graph);

        }

        private Integer minimax(Move mv, int depth, int alpha, int beta, Boolean maximisingPlayer){

        if (depth==0 || !(board.getWinner().isEmpty())){
            return staticEvaluation(mv);
        }


        if(maximisingPlayer){ //this would be mrX
            int maxEval = Integer.MIN_VALUE;
            ImmutableList<Move> moves = board.getAvailableMoves().asList();
            for(Move child : moves){
                int eval = minimax(child, depth-1, alpha, beta, false );
                //simulate advance here idfk
                alpha = Integer.max(alpha, eval);
                if(beta <= alpha){
                    break;
                }
            }
            return maxEval;
        }
        else{
            int minEval = Integer.MIN_VALUE;
            ImmutableList<Move> moves = board.getAvailableMoves().asList();
            for(Move child: moves){
                int eval = minimax(child, depth-1, alpha, beta, true);
                minEval = Integer.min(minEval, eval);
                beta = Integer.min(beta, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return minEval;
        }

    }



        //the static evaluation of any given point of the chosen move should think about
        // 1) how far away mrX is from the detectives. if you choose mrX to be maximising player then that's maximising distance. when it's detectives turn... idfk that's the problem. are they meant to pick the best move? how would they know where the best move is if they don't know where mrX is?
        //we need static evaluation to be a number so literally just how good is this move.
        private int staticEvaluation(Move mv){
            int dest = mv.accept(new destinationVisitor());
            int eval = dj.findSP(dest, detLocations);
            eval = eval + (board.getSetup().graph.degree(dest));

        };


        private void simulateAdvance(){

        }



}
