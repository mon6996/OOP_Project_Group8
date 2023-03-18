import Game.Configuration;
import Game.Player;
import Game.Upbeat;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionCommandTest
{
    Path path = Paths.get("src/configuration_file.txt");

    @Test
    void testRelocate()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        Upbeat upbeat = new Upbeat(p1, p2);
        p1.setCityCenter(34);
        p1.move(5); // 4 , 3
        p1.move(4); // 5 , 3
        p1.move(4); // 6 , 3
        p1.move(5); // 6 , 2
        p1.move(5); // 7 , 1
        Upbeat.getRegion(7, 1).setOwner(p1);
        p1.relocate();
        int cur = p1.getCityCrew_m()*10 + p1.getCityCrew_n(); // 71
        int newCity = p1.getCityCenter_m()*10 + p1.getCityCenter_n(); // 71
        assertEquals(cur, newCity);
    }

    @Test
    void testOpponent()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        Upbeat upbeat = new Upbeat(p1, p2);
        p1.setCityCenter(34); // 3 , 4
        p2.setCityCenter(80); // 8 , 0
        Upbeat.getRegion(5, 4).setOwner(p2); // 5 , 4
        long dir = 4; // down
        long dist = 2; // 3 , 4 to 5 ,4
        assertEquals((dist*10)+dir, p1.opponent());

        Upbeat.getRegion(2, 6).setOwner(p2); // 2 , 6
        dir = 2; // upright
        dist = 2; // 3 , 4 to 2 , 6
        assertEquals((dist*10)+dir, p1.opponent());

        Upbeat.getRegion(5, 4).setOwner(null);
        Upbeat.getRegion(2, 6).setOwner(null);
        assertEquals(0, p1.opponent());
    }

    @Test
    void testNearby()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        Upbeat upbeat = new Upbeat(p1, p2);
        int m = p1.getCityCrew_m();
        int n = p1.getCityCrew_n();
        long dir = 4;
        Upbeat.getRegion(m+2, n).setOwner(p2); // dist = 2
        Upbeat.getRegion(m+2, n).updateDeposit(12345L); //digitsDep = 5
        assertEquals(100*2+5, p1.nearby(dir));
    }
}