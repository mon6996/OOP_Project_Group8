package Upbeat;

import Game.*;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Game
{
    private Player player1, player2;
    private String nameP1, nameP2;
    private boolean p1Ready, p2Ready;
    private Player winner;
    private Player turn;
    private Upbeat upbeat;

    public Game()
    {
    }

    public Game createPlayer(PlayerMessage playerMessage)
    {
        String name = playerMessage.getName();
        if(player1 == null)
        {
            player1 = new Player(name);
            nameP1 = player1.getName();
        }
        else if(player2 == null && !name.equals(nameP1))
        {
            player2 = new Player(name);
            nameP2 = player2.getName();
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
        if(player1.getName().equals(playerMessage.getName()))
        {
            if(player2 == null)
            {
                player1 = null;
                nameP1 = null;
            }
            else
            {
                player1 = player2;
                player2 = null;
                nameP1 = player1.getName();
                nameP2 = null;
            }
        }
        else
        {
            player2 = null;
            nameP2 = null;
        }
        return this;
    }

    public Game setConfig(ConfigMessage configMessage)
    {
        String config = "";
        config += "m="+configMessage.getM()+"\n";
        config += "n="+configMessage.getN()+"\n";
        config += "init_plan_min="+configMessage.getInit_plan_min()+"\n";
        config += "init_plan_sec="+configMessage.getInit_plan_sec()+"\n";
        config += "init_budget="+configMessage.getInit_budget()+"\n";
        config += "init_center_dep="+configMessage.getInit_center_dep()+"\n";
        config += "plan_rev_min="+configMessage.getPlan_rev_min()+"\n";
        config += "plan_rev_sec="+configMessage.getPlan_rev_sec()+"\n";
        config += "rev_cost="+configMessage.getRev_cost()+"\n";
        config += "max_dep="+configMessage.getMax_dep()+"\n";
        config += "interest_pct="+configMessage.getInterest_pct();
        Configuration.setConfig(config);
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
