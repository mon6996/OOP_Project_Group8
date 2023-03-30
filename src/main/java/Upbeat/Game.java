package Upbeat;

import Game.*;
import Exception.*;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Data
public class Game
{
    private Player player1, player2;
    private String nameP1, nameP2;
    private boolean p1Ready, p2Ready, completeSet;
    private Player currentTurn;
    private Player winner;
    private static Region[][] territory;

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
        else if(player2.getName().equals(playerMessage.getName()))
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
        completeSet = true;
        setGame();
        return this;
    }

    public void setGame()
    {
        int m = (int) Configuration.getM();
        int n = (int) Configuration.getN();
        territory = new Region[m][n];
        for (int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                territory[i][j] = new Region(i,j);
            }
        }

        int cityCenterP1 = (int)(Math.random()*(Configuration.getM()))*10 + (int)(Math.random()*(Configuration.getN()));
        int cityCenterP2 = (int)(Math.random()*(Configuration.getM()))*10 + (int)(Math.random()*(Configuration.getN()));

        while (cityCenterP2 == cityCenterP1)
        {
            cityCenterP2 = (int)(Math.random()*(Configuration.getM()))*10 + (int)(Math.random()*(Configuration.getN()));
        }

        player1.setCityCenter(cityCenterP1);
        player2.setCityCenter(cityCenterP2);

        int rowP1 = player1.getCityCenter_m();
        int colP1 = player1.getCityCenter_n();
        int rowP2 = player2.getCityCenter_m();
        int colP2 = player2.getCityCenter_n();
        territory[rowP1][colP1].setCityCenter(player1);
        territory[rowP1][colP1].updateDeposit(Configuration.getInit_center_dep());
        territory[rowP2][colP2].setCityCenter(player2);
        territory[rowP2][colP2].updateDeposit(Configuration.getInit_center_dep());

        this.currentTurn = player1;
    }

    public Game setPlan(PlayerMessage playerMessage)
    {
        Path path;
        if(player1.getName().equals(playerMessage.getName()))
        {
            path = Paths.get("src/construction_plan_p1.txt");
        }
        else
        {
            path = Paths.get("src/construction_plan_p2.txt");
        }
        try
        {
            Files.write(path, playerMessage.getPlan().getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        return this;
    }

    public void doPlan(String plan) throws EvalError
    {
        if(currentTurn.equals(player1))
        {
            player1.doPlan(plan);
        }
        else
        {
            player2.doPlan(plan);
        }
        currentTurn = currentTurn.equals(player1) ? player2 : player1;

        if(player1.getLose())
        {
            winner = player2;
        }
        else if(player2.getLose())
        {
            winner = player1;
        }

        for (int i = 0; i < Configuration.getM(); i++)
        {
            for (int j = 0; j < Configuration.getN(); j++)
            {
                Region region = getRegion(i, j);
                if(region.getDeposit() == 0 || region.getDeposit() == Configuration.getMax_dep() || region.getOwner() == null)
                {
                    continue;
                }
                region.updateInterestPct();
                region.updateInterest();
                region.updateDeposit(region.getInterest());
            }
        }
    }

    public static Region getRegion(int m, int n)
    {
        return territory[m][n];
    }
}
