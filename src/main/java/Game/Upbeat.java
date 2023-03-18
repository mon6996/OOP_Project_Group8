package Game;

import Exception.*;

public class Upbeat
{
    private Player player1, player2;
    private static Region[][] territory;
    private Player currentTurn;
    private Player winner;

    public Upbeat(Player player1, Player player2)
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
        this.player1 = player1;
        this.player2 = player2;

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

    public static Region getRegion(int m, int n)
    {
        return territory[m][n];
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
}
