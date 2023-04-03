package Upbeat;

import Game.*;

import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

@Controller
public class GameController
{
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private Game game;
    @Autowired
    private ConfigMessage config;

    @MessageMapping("/newPlayer")
    @SendTo("/topic/game")
    public Game createPlayer(PlayerMessage playerMessage)
    {
        return game.createPlayer(playerMessage);
    }

    @MessageMapping("/ready")
    @SendTo("/topic/game")
    public Game ready(PlayerMessage playerMessage)
    {
        return game.ready(playerMessage);
    }

    @MessageMapping("/deletePlayer")
    @SendTo("/topic/game")
    public Game deletePlayer(PlayerMessage playerMessage)
    {
        return game.deletePlayer(playerMessage);
    }

    @MessageMapping("/config")
    @SendTo("/topic/game")
    public Game setConfig(ConfigMessage configMessage)
    {
        config = configMessage;
        return game.setConfig(configMessage);
    }

    @MessageMapping("/setPlan")
    @SendTo("/topic/game")
    public Game setPlan(PlayerMessage playerMessage)
    {
        return game.setPlan(playerMessage);
    }

    @MessageMapping("/confirmPlan")
    @SendTo("/topic/game")
    public Game confirmPlan(PlayerMessage playerMessage)
    {
        if(playerMessage.getName().equals(game.getPlayer1().getName()))
        {
            game.setP1Ready(true);
            if (!playerMessage.isFirstPlan())
            {
                game.getPlayer1().changePlan();
            }
        }
        if(playerMessage.getName().equals(game.getPlayer2().getName()))
        {
            game.setP2Ready(true);
            if (!playerMessage.isFirstPlan())
            {
                game.getPlayer2().changePlan();
            }
        }
        return game;
    }

    @MessageMapping("/changPlan")
    @SendTo("/topic/game")
    public Game changPlan(PlayerMessage playerMessage)
    {
        if(playerMessage.getName().equals(game.getPlayer1().getName()))
        {
            game.setP1Ready(false);
        }
        if(playerMessage.getName().equals(game.getPlayer2().getName()))
        {
            game.setP2Ready(false);
        }
        return game;
    }

    @SubscribeMapping("/game")
    public Game sendInitialGame()
    {
        return game;
    }

    @MessageMapping("/newGame")
    @SendTo("/topic/game")
    public Game newGame()
    {
        game = new Game();
        return game;
    }

    @SubscribeMapping("/doPlan")
    public Game doPlan()
    {
        return game.doPlan();
    }

    @SubscribeMapping("/territory")
    public Region[][] sendTerritory()
    {
        return Game.getTerritory();
    }

    @SubscribeMapping("/getConfig")
    public ConfigMessage sendConfig()
    {
        return config;
    }
}
