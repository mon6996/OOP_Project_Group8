package Upbeat;

import Game.*;

import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import Game.ChangePlanMessage;
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

    @SubscribeMapping("/game")
    public Game sendInitialGame()
    {
        return game;
    }

    @SubscribeMapping("/territory")
    public Region[][] sendTerritory()
    {
        return Game.getTerritory();
    }

    @MessageMapping("/changePlan")
    public void changePlan(ChangePlanMessage changePlanMessage) {
        // Since the changePlanMessage is not used, we can remove it from the method signature
        // and use a new instance of ChangePlanMessage when calling convertAndSend()
        template.convertAndSend("/topic/changePlan", new ChangePlanMessage(changePlanMessage.getStatus()));
    }

    public void confirmPlan(ChangePlanMessage changePlanMessage) {
        template.convertAndSend("/topic/planConfirmation", new ChangePlanMessage(changePlanMessage.getStatus()));
    }


    @SubscribeMapping("/getConfig")
    public ConfigMessage sendConfig()
    {
        return config;
    }
}
