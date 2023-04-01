package Game;

import AST.Node;
import Exception.*;
import Parser.*;
import Tokenizer.*;
import Upbeat.Game;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class Player
{
    private String name;
    private Map<String, Long> varPlayer;
    private int cityCenter_m, cityCenter_n;
    private int cityCrew_m, cityCrew_n;
    private long budget;
    private boolean lose;
    private boolean endTurn;
    private int turn = 1;

    public Player(String name)
    {
        this.name = name;
        this.varPlayer = new HashMap<>();
        budget = Configuration.getInit_budget();
    }

    public Map<String, Long> getVar()
    {
        return varPlayer;
    }

    public void lose()
    {
        lose = true;
    }

    public void setCityCenter(int position)
    {
        cityCenter_m = position/10;
        cityCenter_n = position%10;
        cityCrew_m = cityCenter_m;
        cityCrew_n = cityCenter_n;
    }

    public void doPlan(String plan) throws EvalError
    {
        endTurn = false;
        Node p = null;
        try
        {
            Tokenizer tkz = new PlanTokenizer(plan);
            p = new PlanParser(tkz).parse();
        }
        catch (LexicalError | SyntaxError e)
        {
            System.out.println(e.getMessage());
        }

        p.evaluate(varPlayer, this);
        if(!endTurn)
        {
            endTurn = true;
            turn++;
        }
    }

    public void changePlan()
    {
        if(budget >= Configuration.getRev_cost())
        {
            budget -= Configuration.getRev_cost();
        }
    }

    public void done()
    {
        endTurn = true;
        turn++;
        cityCrew_m = cityCenter_m;
        cityCrew_n = cityCenter_n;
    }

    public void relocate()
    {
        if(budget > 0)
        {
            --budget;
            Region cityCenter = Game.getRegion(cityCenter_m, cityCenter_n);
            Region cur = Game.getRegion(cityCrew_m, cityCrew_n);
            int[] next;
            if (cur.getOwner().equals(this))
            {
                int dist = 0;
                while ((cur.getRow() != cityCenter.getRow() && cur.getCol() != cityCenter.getCol()) || cur.getRow() != cityCenter.getRow() || cur.getCol() != cityCenter.getCol())
                {
                    if(cityCenter.getCol() < cur.getCol())
                    {
                        if(cityCenter.getRow() == cur.getRow())
                        {
                            cityCenter = Game.getRegion(cur.getRow(), cur.getCol());
                            dist += cur.getCol() - cityCenter.getCol();
                        }
                        else if(cityCenter.getRow() < cur.getRow())
                        {
                            next = position(3, cityCenter.getRow(), cityCenter.getCol()); //downright
                            cityCenter = Game.getRegion(next[0], next[1]);
                        }
                        else
                        {
                            next = position(2, cityCenter.getRow(), cityCenter.getCol()); //upright
                            cityCenter = Game.getRegion(next[0], next[1]);
                        }
                    }
                    else if(cityCenter.getCol() > cur.getCol())
                    {
                        if(cityCenter.getRow() == cur.getRow())
                        {
                            cityCenter = Game.getRegion(cur.getRow(), cur.getCol());
                            dist += cityCenter.getCol() - cur.getCol();
                        }
                        else if(cityCenter.getRow() < cur.getRow())
                        {
                            next = position(5, cityCenter.getRow(), cityCenter.getCol()); //downleft
                            cityCenter = Game.getRegion(next[0], next[1]);
                        }
                        else
                        {
                            next = position(6, cityCenter.getRow(), cityCenter.getCol()); //upleft
                            cityCenter = Game.getRegion(next[0], next[1]);
                        }
                    }
                    else
                    {
                        if(cityCenter.getRow() < cur.getRow())
                        {
                            cityCenter = Game.getRegion(cur.getRow(), cur.getCol());
                            dist += cur.getRow() - cityCenter.getRow();
                        }
                        else
                        {
                            cityCenter = Game.getRegion(cur.getRow(), cur.getCol());
                            dist += cityCenter.getRow() - cur.getRow();
                        }
                    }
                    dist++;
                }
                int cost = 5*dist + 10;
                if(budget >= cost)
                {
                    budget -= cost;
                    Game.getRegion(cityCenter_m, cityCenter_n).changeCityCenter();
                    cur.setCityCenter(this);
                    cityCenter_m = cityCrew_m;
                    cityCenter_n = cityCrew_n;
                }
            }
        }
        done();
    }

    public void move(long dir)
    {
        if(budget > 0)
        {
            --budget;
        }
        else
        {
            done();
        }

        int[] position = position(dir, cityCrew_m, cityCrew_n);
        int currow = position[0];
        int curcol = position[1];

        Player owner = Game.getRegion(currow, curcol).getOwner();
        if(owner == null || owner.equals(this))
        {
            cityCrew_m = currow;
            cityCrew_n = curcol;
        }
    }

    public void invest(long cost)
    {
        Region cur = Game.getRegion(cityCrew_m, cityCrew_n);
        if(budget - cost > 0)
        {
            --budget;
            if(cur.getDeposit() + cost <= Configuration.getMax_dep())
            {
                budget -= cost;
                cur.updateDeposit(cost);
            }
            else
            {
                long newCost = Configuration.getMax_dep() - cur.getDeposit();
                budget -= newCost;
            }

            if(cur.getOwner() == null)
            {
                cur.setOwner(this);
            }
        }
        else if(budget > 0)
        {
            --budget;
        }
        else
        {
            done();
        }
    }

    public void collect (long cost)
    {
        if(budget > 0)
        {
            Region cur = Game.getRegion(cityCrew_m, cityCrew_n);
            --budget;
            if(cost <= cur.getDeposit() && cur.getOwner().equals(this))
            {
                cur.updateDeposit(-cost);
                budget += cost;
            }
            if(cur.getDeposit() == 0)
            {
                cur.setOwner(null);
            }
        }
        else
        {
            done();
        }
    }

    public void shoot(long dir, long cost)
    {
        if(budget - cost > 0)
        {
            budget -= cost+1;
            int[] shootPosition = position(dir, cityCrew_m, cityCrew_n);
            int opRow = shootPosition[0];
            int opCol = shootPosition[1];
            Region opponent = Game.getRegion(opRow, opCol);
            if(opponent.getOwner() != null)
            {
                if(cost < opponent.getDeposit())
                {
                    opponent.updateDeposit(-cost);
                }
                else
                {
                    opponent.updateDeposit(-opponent.getDeposit());
                    opponent.setOwner(null);
                    if(opponent.isCityCenter())
                    {
                        opponent.getOwner().lose();
                    }
                }
            }
        }
        else if(budget > 0)
        {
            --budget;
        }
        else
        {
            done();
        }
    }

    public long opponent()
    {
        int[][] opPosition = new int[6][2];
        for (int i = 0; i < 6; i++)
        {
            opPosition[i] = new int[]{cityCrew_m, cityCrew_n};
        }
        long dist = 1;
        while (opPosition[0] != null && opPosition[1] != null && opPosition[2] != null && opPosition[3] != null && opPosition[4] != null && opPosition[5] != null)
        {
            for (int i = 0; i < 6; i++)
            {
                if(opPosition[i] == null) continue;
                int[] next = position(i+1, opPosition[i][0], opPosition[i][1]);
                Region opponent = Game.getRegion(next[0], next[1]);
                if(opponent.getOwner() != null && !opponent.getOwner().equals(this))
                {
                    return (dist*10) + i + 1;
                }
                if(opPosition[i][0] == next[0] && opPosition[i][1] == next[1])
                {
                    opPosition[i] = null;
                }
                else
                {
                    opPosition[i] = next;
                }
            }
            dist++;
        }
        return 0L;
    }

    public long nearby(long dir)
    {
        long dist = 0;
        int[] opPosition = position(dir, cityCrew_m, cityCrew_n);
        while (opPosition[0] > 0 && opPosition[0] < Configuration.getM()-1 && opPosition[1] > 0 && opPosition[1] < Configuration.getN()-1)
        {
            dist++;
            Region opponent = Game.getRegion(opPosition[0], opPosition[1]);
            int digitsDeposit = Long.toString(opponent.getDeposit()).length();
            if(opponent.getOwner() != null && !opponent.getOwner().equals(this))
            {
                return 100*dist + digitsDeposit;
            }
            if(opPosition[0] > 0 && opPosition[0] < Configuration.getM()-1 && opPosition[1] > 0 && opPosition[1] < Configuration.getN()-1)
            {
                opPosition = position(dir, opPosition[0], opPosition[1]);
            }
        }
        return 0L;
    }

    private int[] position(long dir, int currow, int curcol)
    {
        long rows = Configuration.getM() - 1;
        long cols = Configuration.getN() - 1;
        switch ((int) dir)
        {
            case 1 ->
            {
                if(currow > 0)
                {
                    --currow;
                }
            }
            case 2 ->
            {
                if(curcol < cols)
                {
                    if (curcol % 2 == 1 && currow > 0)
                    {
                        --currow;
                    }
                    ++curcol;
                }
            }
            case 3 ->
            {
                if(curcol < cols)
                {
                    if (curcol % 2 == 0 && currow < rows)
                    {
                        ++currow;
                    }
                    ++curcol;
                }
            }
            case 4 ->
            {
                if(currow < rows)
                {
                    ++currow;
                }
            }
            case 5 ->
            {
                if(curcol > 0)
                {
                    if (curcol % 2 == 0 && currow < rows)
                    {
                        ++currow;
                    }
                    --curcol;
                }
            }
            case 6 ->
            {
                if(curcol > 0)
                {
                    --curcol;
                    if (curcol % 2 == 1 && currow > 0)
                    {
                        --currow;
                    }
                }
            }
        }
        return new int[] {currow, curcol};
    }
}