package engine.network;

import engine.entity.Player;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;

public class PlayerManager {

    public static void filterPlayers(List<Player> players, int[] playerIDsToKeep) {
        // Convert the int[] to a List<Integer> for easy searching
        List<Integer> idsToKeep = Arrays.stream(playerIDsToKeep).boxed().toList();
        
        // Use an iterator to safely remove players from the list while iterating
        Iterator<Player> iterator = players.iterator();
        
        while (iterator.hasNext()) {
            Player player = iterator.next();
            // If the player's ID is not in the list of IDs to keep, remove them
            if (!idsToKeep.contains(player.id)) {
                iterator.remove();
            }
        }
    }
}