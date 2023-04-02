import Game.Configuration;
import Game.Player;
import Upbeat.Game;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionCommandTest
{
    Path path = Paths.get("src/configuration_file.txt");
    Game game = new Game();

    @Test
    void testMove()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setGame();
        p1.setCityCenter(34);

        p1.move(5); // 4 , 3
        assertEquals(4, p1.getCityCrew_m());
        assertEquals(3, p1.getCityCrew_n());

        p1.move(4); // 5 , 3
        assertEquals(5, p1.getCityCrew_m());
        assertEquals(3, p1.getCityCrew_n());

        p1.move(4); // 6 , 3
        assertEquals(6, p1.getCityCrew_m());
        assertEquals(3, p1.getCityCrew_n());

        p1.move(5); // 6 , 2
        assertEquals(6, p1.getCityCrew_m());
        assertEquals(2, p1.getCityCrew_n());

        p1.move(5); // 7 , 1
        assertEquals(7, p1.getCityCrew_m());
        assertEquals(1, p1.getCityCrew_n());

        p1.move(1); // 6 , 1
        assertEquals(6, p1.getCityCrew_m());
        assertEquals(1, p1.getCityCrew_n());

        p1.move(2); // 5 , 2
        assertEquals(5, p1.getCityCrew_m());
        assertEquals(2, p1.getCityCrew_n());

        p1.move(3); // 6 , 3
        assertEquals(6, p1.getCityCrew_m());
        assertEquals(3, p1.getCityCrew_n());
    }

    @Test
    void testRelocate()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setGame();
        long budget = p1.getBudget();
        p1.setCityCenter(34);
        p1.move(5); // 4 , 3
        p1.move(4); // 5 , 3
        p1.move(4); // 6 , 3
        p1.move(5); // 6 , 2
        p1.move(5); // 7 , 1
        Game.getRegion(7, 1).setOwner(p1);
        p1.relocate();
        int cur = p1.getCityCrew_m()*10 + p1.getCityCrew_n(); // 71
        int newCity = p1.getCityCenter_m()*10 + p1.getCityCenter_n(); // 71
        long dist = 5; // 3,4 -> 7,1
        long cost = 1 + (5*dist + 10);
        assertEquals(cur, newCity);
        assertEquals(budget - cost, p1.getBudget());

    }

    @Test
    void testInvest()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setGame();
        long budget = p1.getBudget();
        p1.setCityCenter(34);

    }

    @Test
    void testCollect()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setGame();
        long budget = p1.getBudget();
        p1.setCityCenter(34);
    }

    @Test
    void testShoot()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setGame();
        long budget = p1.getBudget();
        p1.setCityCenter(34);
    }

    @Test
    void testOpponent()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setGame();
        p1.setCityCenter(34); // 3 , 4
        p2.setCityCenter(80); // 8 , 0
        Game.getRegion(5, 4).setOwner(p2); // 5 , 4
        long dir = 4; // down
        long dist = 2; // 3 , 4 to 5 ,4
        assertEquals((dist*10)+dir, p1.opponent());

        Game.getRegion(2, 6).setOwner(p2); // 2 , 6
        dir = 2; // upright
        dist = 2; // 3 , 4 to 2 , 6
        assertEquals((dist*10)+dir, p1.opponent());

        Game.getRegion(5, 4).setOwner(null);
        Game.getRegion(2, 6).setOwner(null);
        assertEquals(0, p1.opponent());

    }

    @Test
    void testNearby()
    {
        Configuration.setConfig(path);
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        game.setPlayer1(p1);
        game.setPlayer2(p2);
        game.setGame();
        int m = p1.getCityCrew_m();
        int n = p1.getCityCrew_n();
        long dir = 4;
        Game.getRegion(m+2, n).setOwner(p2); // dist = 2
        Game.getRegion(m+2, n).updateDeposit(12345L); //digitsDep = 5
        assertEquals(100*2+5, p1.nearby(dir));


    }
}