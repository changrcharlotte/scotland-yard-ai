package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import org.checkerframework.checker.units.qual.A;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard;

import java.util.*;

//the purpose of dijkstra is to add to our scoring functionality; keep track of how close the detectives are so that we can outrun them
// for example, we keep track of the edge weights and compute, for every detective, which of mrX's moves are most unreachable
// thus we compile all of the moves relative to all the detectives and see which are most unreachable still.

public class DijkstraUndirectedSP {

    ArrayList<ArrayList<ScotlandYard.Ticket>> tickTo;          // tickTo[v] = tickets to,  of shortest s->v path
    Integer distTo[];
    EndpointPair<Integer>[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
    IndexMinPQ<Integer> pq;                        // priority queue of vertices

    private void dijkstraUndirectedSP(ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> graph, int mrXLocation, List<Integer> detLocations) {
//        Traverser.forGraph(graph).breadthFirst(StartingNode);
        int totalNodesNum = graph.nodes().size();
        int numNodes = detLocations.size() + 1;
        tickTo = new ArrayList<ArrayList<ScotlandYard.Ticket>>();
        distTo = new Integer[totalNodesNum];
        edgeTo = new EndpointPair[totalNodesNum];

        for (int v = 0; v < numNodes; v++) {
            distTo[v] = Integer.MAX_VALUE;
        }
        distTo[0] = 0; //assuming the first node is literally just mrX's location
        //or perhaps just index based on

        pq = new IndexMinPQ<Integer>(numNodes);
        pq.insert(mrXLocation, distTo[0]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (EndpointPair e : graph.incidentEdges(v)) {
                relax(e, v);
            }


        }
    }
    private void relax (EndpointPair<Integer> e, Integer v){

        Integer w;
        if (e.nodeU() == v) {
            w = (Integer) e.nodeU();
        } else {
            w = (Integer) e.nodeV();
        }

        ImmutableSet<ScotlandYard.Transport> edgeTickets = graph.edgeValue((Integer) e.nodeU(), (Integer) e.nodeV()).orElseThrow();
        Integer distance = edgeTickets.size();

        if (distTo[w] > distTo[v] + distance) {
            distTo[w] = distTo[v] + distance;
            edgeTo[w] = e;

            if(pq.contains(w)){
                pq.decreaseKey(w,distTo[w]);
            }
            else{
                pq.insert(w, distTo[w]);
            }
        }
    }



}