import Game.*;
import Parser.*;
import Tokenizer.*;
import Exception.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanParserTest
{
    @Test
    void testSyntaxErrorInParser () throws LexicalError
    {
        //case1 "you" is variable must assign value
        String S = "you and me";
        Tokenizer T = new PlanTokenizer(S);
        Map<String, Long> var = new HashMap<>();
        PlanParser P = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P.parse());

        //case2 direction is incorrect
        S = "move right";
        T = new PlanTokenizer(S);
        PlanParser PP = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> PP.parse());

        //case3 "butget" is variable it has not been assign value yet.
        S = "butget - cost";
        T = new PlanTokenizer(S);
        PlanParser PPP = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> PPP.parse());

        //case4 "budget" is special var cannot be assigned for it
        S = "budget = 8";
        T = new PlanTokenizer(S);
        PlanParser P4 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P4.parse());

        // case5 "if" is reserved word cannot be assigned for it
        S = "if = 8";
        T = new PlanTokenizer(S);
        PlanParser P5 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P5.parse());

        //case6 else if come first if
        S = "else if (opponentLoc % 10 - 4) then move down if (opponentLoc % 10 - 5) then move downleft";
        T = new PlanTokenizer(S);
        PlanParser P6 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P6.parse());

        //case7 has if but no else
        S = "if (opponentLoc % 10 - 5) then move downleft";
        T = new PlanTokenizer(S);
        PlanParser P7 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P7.parse());

        //case8 The if word must be followed by then.
        S = "if (opponentLoc % 10 - 5) { move downleft } else { move up }";
        T = new PlanTokenizer(S);
        PlanParser P8 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P8.parse());

        //case9 only opening parenthesis
        S = "cost = 10 ^ nearby upleft % 100 + 1)";
        T = new PlanTokenizer(S);
        PlanParser P9 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P9.parse());

        //case10 only closing parenthesis
        S = "cost = 10 ^ (nearby upleft % 100 + 1";
        T = new PlanTokenizer(S);
        PlanParser P10 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P10.parse());

        //case11 Comment using /
        S = "m = 0  / number of random moves";
        T = new PlanTokenizer(S);
        PlanParser P11 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P11.parse());

        //case12 "opponent" doesn't need any word to follow.
        S = "cost = 10 ^ (opponent upright % 100 + 1)";
        T = new PlanTokenizer(S);
        PlanParser P12 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P12.parse());

        //case13 "nearby" must have direction to follow
        S = "cost = 10 ^ (nearby % 100 + 1)";
        T = new PlanTokenizer(S);
        PlanParser P13 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P13.parse());

        //case14 "invest" must have integer or variable have to be assigned to follow
        S = "if (budget - cost) then invest downright cost else {}";
        T = new PlanTokenizer(S);
        PlanParser P14 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P14.parse());

        //case15 "shoot" must have direction to follow
        S = "if (budget - cost) then shoot 88 cost else {}";
        T = new PlanTokenizer(S);
        PlanParser P15 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P15.parse());

        //case16 "shoot" must have direction to follow and after the "direction" followed by cost.
        S = "if (budget - cost) then shoot downright else {}";
        T = new PlanTokenizer(S);
        PlanParser P16 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P16.parse());

        //case17 There are no construction plans, there are comments.
        S = "# number of random shoot moves";
        T = new PlanTokenizer(S);
        PlanParser P17 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P17.parse());

        //case18 "done" is reserved word not a variable with a value
        S = "s = done collect s ";
        T = new PlanTokenizer(S);
        PlanParser P18 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P18.parse());

        // case19 "must" must have direction to follow
        S = "move 5  ";
        T = new PlanTokenizer(S);
        PlanParser P19 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P19.parse());

        //case20 "budget" is special var it has not been assign value yet.
        S = "s = 88888888 budget = s  ";
        T = new PlanTokenizer(S);
        PlanParser P20 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P20.parse());

        //case21 "maxdeposit" is special var it has not been assign value yet.
        S = "s = 88888888 maxdeposit = s  ";
        T = new PlanTokenizer(S);
        PlanParser P21 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P21.parse());
    }

    @Test
    void testAssignment () throws LexicalError, SyntaxError, EvalError
    {
        //case1
        String t = " t = 99 ";
        Map<String, Long> var1 = new HashMap<>();
        var1.put("t", 99L);
        Player p1 = new Player("Au");
        p1.doPlan(t);
        assertEquals(var1,p1.getVar());

        //case2
        t = "t = t + 99";
        var1 = new HashMap<>();
        p1 = new Player("Au");

        var1.put("t", 99L);
        p1.doPlan(t);   // doplan turn1
        assertEquals(var1,p1.getVar());

        var1.replace("t", 99L+99L);
        p1.doPlan(t);  //doplan turn2
        assertEquals(var1,p1.getVar());

        //case3
        t = "m = 78";
        var1 = new HashMap<>();
        p1 = new Player("Au");

        var1.put("m", 78L);
        p1.doPlan(t);   // doplan turn1
        assertEquals(var1,p1.getVar());

        //case4
        t = "m = m + 999";
        var1 = new HashMap<>();
        p1 = new Player("Au");

        //case5
        var1.put("m", 999L);
        p1.doPlan(t);   // doplan turn1
        assertEquals(var1,p1.getVar());

        var1.replace("m", 999L+999L);
        p1.doPlan(t);  //doplan turn2
        assertEquals(var1,p1.getVar());


        t = "m = 0";
        var1 = new HashMap<>();
        p1 = new Player("Au");

        var1.put("m", 0L);
        p1.doPlan(t);   // doplan turn1
        assertEquals(var1,p1.getVar());

        p1.doPlan(t);  //doplan turn2
        assertEquals(var1,p1.getVar());

    }

    @Test
    void testSyntaxAssignment () throws LexicalError
    {
        // special var
        String S = "budget = 99999";
        Tokenizer T = new PlanTokenizer(S);
        Map<String, Long> var = new HashMap<>();
        PlanParser P = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P.parse());

        // special var
        S = "curcol = 99999999";
        T = new PlanTokenizer(S);
        PlanParser PP = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> PP.parse());

        //special var
        S = "deposit = 9999";
        T = new PlanTokenizer(S);
        PlanParser P3 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P3.parse());

        S = "deposit = deposit - 9999";
        T = new PlanTokenizer(S);
        PlanParser P6 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P6.parse());

        //command
        S = "invest = 800";
        T = new PlanTokenizer(S);
        PlanParser P4 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P4.parse());

        //command
        S = "invest = invest - 1";
        T = new PlanTokenizer(S);
        PlanParser P5 = new PlanParser(T);
        assertThrows(SyntaxError.class, () -> P5.parse());
    }


}