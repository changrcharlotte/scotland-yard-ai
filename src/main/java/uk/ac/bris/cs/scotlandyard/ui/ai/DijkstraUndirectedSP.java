package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import org.checkerframework.checker.units.qual.A;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//the purpose of dijkstra is to add to our scoring functionality; keep track of how close the detectives are so that we can outrun them
// for example, we keep track of the edge weights and compute, for every detective, which of mrX's moves are most unreachable
// thus we compile all of the moves relative to all the detectives and see which are most unreachable still.

public class DijkstraUndirectedSP {

    ArrayList<ArrayList<ScotlandYard.Ticket>> tickTo;          // tickTo[v] = tickets to,  of shortest s->v path
    Integer distTo[];
    EndpointPair<Integer>[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
    List<Integer> pq;                        // priority queue of vertices

    private void dijkstraUndirectedSP(ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> graph, int mrXLocation, List<Integer> detLocations)
    {
//        Traverser.forGraph(graph).breadthFirst(StartingNode);
        int numNodes = detLocations.size()+ 1;
        tickTo = new ArrayList<ArrayList<ScotlandYard.Ticket>>();
        distTo = new Integer[detLocations.size()+1];
        edgeTo = new EndpointPair[numNodes];

        for (int v = 0; v < numNodes; v++)
            distTo[v] = Integer.MAX_VALUE;
        distTo[0] = 0; //assuming the first node is literally just mrX's location

        pq = new ArrayList<Integer>();



    }

    private void relax(ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> graph, int startingNode) {

        for (EndpointPair e : graph.incidentEdges(startingNode)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
        }
    }
}
