/*
 * Copyright (c) 2021.
 *  Discord : _Paul#6918
 *  Author : RqndomHax
 *  Github: https://github.com/RqndomHax
 */

package fr.rqndomhax.narutouhc.scoreboards;

import fr.rqndomhax.narutouhc.core.Setup;
import fr.rqndomhax.narutouhc.managers.game.GameState;
import fr.rqndomhax.narutouhc.utils.scoreboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameScoreboard {

    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private final Setup setup;
    private int i;

    public GameScoreboard(Setup setup) {
        this.setup = setup;
    }

    public void newGameScoreboard(Player player) {
        FastBoard fb = new FastBoard(player);
        fb.updateTitle(setup.getGame().getGameInfo().getMRules().gameTitle);

        boards.put(player.getUniqueId(), fb);
    }

    public void removeGameScoreboard(Player player) {
        boards.remove(player.getUniqueId());
    }

    private void updateBoard(FastBoard board) {

        GameState state = setup.getGame().getGameInfo().getGameState();

        switch (state) {
            case GAME_TELEPORTATION_INVINCIBILITY:
            case GAME_BORDER:
            case GAME_MEETUP:
            case GAME_FINISHED:
                if (setup.getGame().getGameInfo().getMainTask() == null)
                    GameScoreboardManager.updateLobbyBoard(setup, board);
                else
                    GameScoreboardManager.updateNarutoBoard(setup, board);
                break;
            case LOBBY_WAITING:
            case LOBBY_TELEPORTING:
                GameScoreboardManager.updateLobbyBoard(setup, board);
                break;
            case GAME_INVINCIBILITY:
            case GAME_PREPARATION:
            case GAME_TELEPORTING:
                GameScoreboardManager.updatePreparationBoard(setup, board);
                break;
            default:
                GameScoreboardManager.updateLobbyBoard(setup, board);
                break;
        }


    }

    public void runBoard() {
        Bukkit.getServer().getScheduler().runTaskTimer(setup.getMain(), () -> {
            for (FastBoard board : boards.values()) {

                updateBoard(board);

                i++;
            }
        }, 0, 20);
    }

}
