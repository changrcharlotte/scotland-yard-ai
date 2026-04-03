package uk.ac.bris.cs.scotlandyard.ui.ai;

import com.google.common.collect.ImmutableSet;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import uk.ac.bris.cs.scotlandyard.model.ScotlandYard;

import java.util.*;

//the purpose of dijkstra is to add to our scoring functionality; keep track of how close the detectives are so that we can outrun them
// for example, we keep track of the edge weights and compute, for every detective, which of mrX's moves are most unreachable
// thus we compile all of the moves relative to all the detectives and see which are most unreachable still.

public class DijkstraUndirectedSP {
//
//    ArrayList<ArrayList<ScotlandYard.Ticket>> tickTo;          // tickTo[v] = tickets to,  of shortest s->v path
    Integer distTo[];               // distTo[v] = distance  of shortest s->v path, IN NUMBER OF TICKETS
    EndpointPair<Integer>[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
    IndexMinPQ<Integer> pqOfVertices;                        // priority queue of vertices
    int mrXlocation;
    List<Integer> detlocations;
    ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> graph;
    int totalNodesNum;

    public DijkstraUndirectedSP(ImmutableValueGraph<Integer, ImmutableSet<ScotlandYard.Transport>> graph) {
        this.graph = graph;
        totalNodesNum = graph.nodes().size();
        distTo = new Integer[totalNodesNum+1]; //+1 since we want to translate the name of the node to the index without minusing 1
        edgeTo = new EndpointPair[totalNodesNum+1];


        //assuming the first node is literally just mrX's location
        //or perhaps just index based on nodes number

    }

    public int findSP(int mrXLocation, List<Integer> detLocations){

        this.detlocations = detLocations;
        this.mrXlocation = mrXLocation;

        int numNodes = detLocations.size() + 1; //+1 for mrX

        for (int v = 0; v < totalNodesNum+1; v++) {
            distTo[v] = Integer.MAX_VALUE;
        }
        distTo[mrXLocation] = 0;


        pqOfVertices = new IndexMinPQ<>(totalNodesNum+1);
        pqOfVertices.insert(mrXLocation, distTo[0]);


        while (!pqOfVertices.isEmpty() && !(checkAllDetectivesDiscovered())) {
            int nextVertex = pqOfVertices.delMin();
            for (EndpointPair edge : graph.incidentEdges(nextVertex)) {
                relax( edge, nextVertex);
            }

        }
        int total = 0;
        for(int detlocation : detlocations){
            total += distTo[detlocation];
        }
        return total;


    }

    private Boolean checkAllDetectivesDiscovered(){
        for (Integer location : detlocations){
            if(distTo[location] == Integer.MAX_VALUE){
                return false;
            }
        }
        return true;
    }

    private void relax ( EndpointPair<Integer> edge, Integer startingNode) {

        Integer endNode;
        if (edge.nodeU() == startingNode) {
            endNode = (Integer) edge.nodeV();
        } else {
            endNode = (Integer) edge.nodeU();
        }

        ImmutableSet<ScotlandYard.Transport> edgeTransports = graph.edgeValue((Integer) edge.nodeU(), (Integer) edge.nodeV()).orElseThrow();
        Integer distance = 0;
        for(ScotlandYard.Transport transport : edgeTransports){
            switch(transport){
                case TAXI -> distance +=1;
                case BUS -> distance += 2;
                case UNDERGROUND -> distance +=3;
                case FERRY -> distance += 5;
            }
        }

        if (distTo[endNode] > distTo[startingNode] + distance) {
            distTo[endNode] = distTo[startingNode] + distance;
            edgeTo[endNode] = edge;

            if(pqOfVertices.contains(endNode)){
                pqOfVertices.decreaseKey(endNode,distTo[endNode]);
            }
            else{
                pqOfVertices.insert(endNode, distTo[endNode]);
            }
        }
    }



}