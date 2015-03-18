@Override
public double score(SimpleGraph<String, DefaultEdge> graph, String source, String target) {
    
    int nNeighbours = 0;
    int nNeighboursOfNeigbours = 0;
    
    Set<String> targetNeighbours = new HashSet<String>();

    for (DefaultEdge u : graph.edgesOf(target)) {
        String friend = graph.getEdgeTarget(u);

        if (friend.equals(target)) {
            friend = graph.getEdgeSource(u);
        }
        
        targetNeighbours.add(friend);
    }
    
    for (DefaultEdge u : graph.edgesOf(source)) {
        String friend = graph.getEdgeTarget(u);
        
        if (friend.equals(source)) {
            friend = graph.getEdgeSource(u);
        }
        
        if (targetNeighbours.contains(friend)) {
            nNeighbours += 1;
        }
        
        for (DefaultEdge v : graph.edgesOf(friend)) {
            String friendOfFriend = graph.getEdgeTarget(v);

            if (friendOfFriend.equals(friend)) {
                friendOfFriend = graph.getEdgeSource(v);
            }

            if (targetNeighbours.contains(friendOfFriend)) {
                nNeighboursOfNeigbours += 1;
            }
        }
    }
    
    return 2 * nNeighbours + nNeighboursOfNeigbours;
}