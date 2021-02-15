import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestVotesTop {

    @Test
    public void main() {
        List<Pair<UUID, Integer>> dummyList = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i <= 10; i++) {
            dummyList.add(Pair.of(UUID.randomUUID(), random.nextInt()));
        }

        System.out.println(dummyList.toString());

        dummyList.sort((playerAVotes, playerBVotes) -> playerAVotes.getRight() < playerBVotes.getRight() ? -1 :
                playerAVotes.getRight().intValue() == playerBVotes.getRight().intValue() ? 0 : 1);

        System.out.println(dummyList.toString());
    }
}
