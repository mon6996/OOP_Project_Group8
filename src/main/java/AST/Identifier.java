package AST;

import Exception.EvalError;
import Game.Configuration;
import Game.Player;
import Game.Upbeat;

import java.util.Map;

public class Identifier implements Expr
{
    private String name;

    public Identifier(String name)
    {
        this.name = name;
    }

    public Long eval(Map<String, Long> bindings, Player player) throws EvalError
    {
        if(name.equals("rows"))
        {
            return Configuration.getM();
        }
        else if(name.equals("cols"))
        {
            return Configuration.getN();
        }
        else if(name.equals("currow"))
        {
            return (long) player.getCityCrew_m();
        }
        else if(name.equals("curcol"))
        {
            return (long) player.getCityCrew_n();
        }
        else if(name.equals("budget"))
        {
            return player.getBudget();
        }
        else if(name.equals("deposit"))
        {
            return Upbeat.getRegion(player.getCityCrew_m(), player.getCityCrew_n()).getDeposit();
        }
        else if(name.equals("int"))
        {
            return Upbeat.getRegion(player.getCityCrew_m(), player.getCityCrew_n()).getInterest();
        }
        else if(name.equals("maxdeposit"))
        {
            return Configuration.getMax_dep();
        }
        else if(name.equals("random"))
        {
            return (long) (Math.random() * 1000);
        }
        else if (bindings.containsKey(name))
        {
            return bindings.get(name);
        }
        else
        {
            bindings.put(name, 0L);
            return 0L;
        }
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append(name);
    }
}