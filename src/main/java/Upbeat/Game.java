package Upbeat;

import Game.*;

import org.springframework.stereotype.Component;

@Component
public class Game
{
    private Player player1, player2;
    private boolean p1Ready, p2Ready;
    private Player winner;
    private Player turn;
    private Upbeat upbeat;

    public Game()
    {
    }

    public Game createPlayer(PlayerMessage playerMessage)
    {
        if(player1 == null)
        {
            player1 = new Player(playerMessage.getName());
            System.out.println(player1.getName());
        }
        else if(player2 == null)
        {
            player2 = new Player(playerMessage.getName());
            System.out.println(player2.getName());
        }
        return this;
    }

    public Game ready(PlayerMessage playerMessage)
    {
        if(player1.getName().equals(playerMessage.getName()))
        {
            p1Ready = true;
        }
        else
        {
            p2Ready = true;
        }
        return this;
    }

    public Game deletePlayer(PlayerMessage playerMessage)
    {
        if(player1.getName().equals(playerMessage.getName()) && player2 != null)
        {
            player1 = player2;
            player2 = null;
            System.out.println("delete player1");
        }
        else
        {
            player2 = null;
            System.out.println("delete player2");
        }
        return this;
    }

    public Game setConfig(ConfigMessage configMessage)
    {
        Configuration.setConfig(configMessage.getConfig());
        return this;
    }

    public Game createUpbeat()
    {
        if(p1Ready && p2Ready && upbeat == null)
        {
            upbeat = new Upbeat(player1, player2);
        }
        return this;
    }

}
