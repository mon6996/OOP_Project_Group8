package Game;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Region
{
    private final int row, col;
    private double deposit = 0.0;
    private double interest;
    private double r;
    private Player owner;
    private boolean isCityCenter;

    public Region(int row, int col)
    {
        this.row = row;
        this.col = col;
        r = (double) Configuration.getInterest_pct();
        interest = deposit*r/100.0;
    }

    public void setOwner(Player player)
    {
        owner = player;
    }

    public void updateDeposit(Long cost)
    {
        if(deposit + cost <= Configuration.getMax_dep())
        {
            deposit += cost;
        }
        else
        {
            deposit = Configuration.getMax_dep();
        }
    }

    public long getDeposit()
    {
        if(owner == null)
        {
            return (long) -deposit;
        }
        return (long) deposit;
    }

    public void updateInterestPct()
    {
        r = Configuration.getInterest_pct()*Math.log10(deposit)*Math.log(owner.getTurn());
    }

    public void updateInterest()
    {
        interest = deposit*r/100.0;
    }

    public void setCityCenter(Player player)
    {
        owner = player;
        isCityCenter = true;
    }

    public void changeCityCenter()
    {
        owner = null;
        isCityCenter = false;
    }
}
