package Game;

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

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void setOwner(Player player)
    {
        owner = player;
    }

    public Player getOwner()
    {
        return owner;
    }

    public void updateDeposit(Long cost)
    {
        deposit += cost;
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

    public long getInterest()
    {
        return (long) interest;
    }

    public boolean isCityCenter()
    {
        return isCityCenter;
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
