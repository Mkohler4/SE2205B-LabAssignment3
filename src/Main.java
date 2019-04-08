//250954887 - Markus Kohler
//250957075 - Osama Masud

import java.util.*;

public class Main {
    public static void main(String args[])
    {

    }

    public static  int breadthFirstPathSearch(Graph FN, int s, int d)
    {
        //Create a visited list to check if there is a path from s to d
        int[] visitedList = new int[FN.numVertices()];
        //Create a list to identify what nodes have been visited and what nodes have not
        LinkedListQueue<Vertex> list = new LinkedListQueue<>();
        //Create snap shot list to keep track of the path taken through the graph
        List<Vertex> snapShot = new LinkedList<>();
        //Create vertex to hold individual vertex values from the graph
        Vertex v;
        //Set all values in visited list to 0
        for(int i = 0; i < FN.numVertices(); i++)
        {
            visitedList[i] = 0;
        }
        //Enqueue the first node s
        list.enqueue(FN.getVertex(s));
        //Set s to the first vertex
        v = list.first();
        //If node s is not in the graph return 0
        if(v == null)
        {
            return 0;
        }
        //If nod d is not in the graph return 0
        if(FN.getVertex(d) == null)
        {
            return 0;
        }
        //The Max amount of times the loop needs to run to find a path is equal to the number of edges in the graph
        for (int i = 0; i < FN.numEdges(); i++)
        {
            if(visitedList[list.first().getLabel()] == 0)
            {
                //Dequeue vertices one by one
                v = list.dequeue();
                //If node is deqeued mark as visited
                visitedList[v.getLabel()] = 1;
                //After the node is visited add the vertex to the snapshot list
                snapShot.add(v);
                //Check all the outgoing edges from the vertex
                for(int j = 0; j< v.outgoing.size(); j++)
                {
                    //If flow = flowCap don't enqueue the next vertex
                    if(v.outgoing.get(j).flow != v.outgoing.get(j).flowCap)
                    {
                        //enqueue all the adjacent vertices into the list
                        list.enqueue(FN.opposite(v, v.outgoing.get(j)));
                    }
                }
            }
            //For the case that two paths lead to the same vertex meaning the vertex has already been visited
            else
            {
                list.dequeue();
            }
        }
        //After the list is empty check if there was any path from s to d meaning s = 1 and d = 1
        if(visitedList[d] == 1)
        {
            return 1;
        }
        //If there is not a path from s to d return 0
        else
        {
            return 0;
        }
    }
    public static void maximizeFlowNetwork(Graph fN, int s, int t)
    {
      //Variable to store the maximum flow
      int maximum = 0;
      //This loop identifies one specific path in the graph and stores it in the path list
      //Loop while there is still an available path from s to t in the graph
        while(breadthFirstPathSearch(fN, s, t) == 1) {
            //Set the first vertex to the source vertex
            Vertex vertex = fN.getVertex(s);
            List<Vertex> list = new ArrayList<>();
            //Loop till the outgoing edges are not 0 (eg the destination vertex)
            while (vertex.outgoing.size() != 0) {
                //Add the vertex to the path list
                list.add(vertex);
                //Set current to one adjacent vertex
                vertex = fN.opposite(vertex, vertex.outgoing.get(0));
            }
            //Variable holds the lowest flow in a specified path
            int low = fN.getEdge(list.get(0), list.get(1)).flowCap;
            //Loop through the specified path
            for(int i = 0; i < list.size()-1; i++){
                //Find the lowest flow cap taking all edges in the specified path into account
                if(low < fN.getEdge(list.get(i), list.get(i+1)).flowCap - fN.getEdge(list.get(i), list.get(i+1)).flow){
                    low = fN.getEdge(list.get(i), list.get(i+1)).flowCap - fN.getEdge(list.get(i), list.get(i+1)).flow;
                }
            }
            //Increment every edges flow in the specified path by the lowed flow cap found
            for(int i = 0; i< list.size()-2; i++)
            {
                for(int j = 0; j<low; j++) {
                    fN.getEdge(list.get(i), list.get(i + 1)).flow ++;
                }
            }
            //Traverse forward through the specific path again and check if any flows are equal to the flow cap in the specified path
            for(int i = 0; i< list.size()-2;)
            {
                Edge e = fN.getEdge(list.get(i), list.get(++i));
                if(e == null) continue;
                //If a flow is equal to the flow cap remove the edge from the graph
                if(e.flow == e.flowCap) fN.removeEdge(e);
                //An incoming edges tracing back all the way to the source vertex should be removed as well
                //This is so that path cannot be taken again since it won't lead to the source vertex anymore
                if(list.get(i).outgoing.size() == 0){
                    for (Edge edge : list.get(i).incoming) {
                        fN.removeEdge(edge);
                    }
                }
            }
            //increment the maximum flow based off the lowest flow added to the edges
            maximum += low;
            System.out.println(maximum);
        }
    }
}
