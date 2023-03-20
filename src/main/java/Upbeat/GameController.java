package Upbeat;

import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GameController
{
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private Game game;

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
        return game.setConfig(configMessage);
    }

    @MessageMapping("/setPlan")
    @SendTo("/topic/game")
    public Game setPlan(PlayerMessage playerMessage)
    {
        return game.setPlan(playerMessage);
    }

    @MessageMapping("/upbeat")
    @SendTo("/topic/game")
    public Game createUpbeat()
    {
        return game.createUpbeat();
    }

    @SubscribeMapping("/game")
    public Game sendInitialGame()
    {
        return game;
    }
}
