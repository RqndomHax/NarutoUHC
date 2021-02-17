/*
 * Copyright (c) 2021.
 *  Discord : _Paul#6918
 *  Author : RqndomHax
 *  Github: https://github.com/RqndomHax
 */

package fr.rqndomhax.narutouhc.tabscores;

import fr.rqndomhax.narutouhc.core.Setup;
import fr.rqndomhax.narutouhc.game.GamePlayer;
import fr.rqndomhax.narutouhc.game.GameState;
import fr.rqndomhax.narutouhc.utils.scoreboard.FastBoard;
import net.minecraft.server.v1_8_R3.GameProfileBanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class GameScoreboard {

    private static final Map<UUID, FastBoard> boards = new HashMap<>();
    private static Setup setup;
    public static int n;
    public static int nMax;
    public static int compoDuration = 5;
    public static int mainDuration = 15;
    private static List<List<GamePlayer>> players = null;

    public static void init(Setup setup) {
        GameScoreboard.setup = setup;
        runBoard();
    }

    public static void newGameScoreboard(Player player) {
        FastBoard fb = new FastBoard(player);
        fb.updateTitle(setup.getGame().getGameRules().gameTitle);

        boards.put(player.getUniqueId(), fb);
    }

    private static void initPlayers(Set<GamePlayer> gamePlayers) {
        int size = 16;
        int n = -1;

        players = new ArrayList<>();

        for (GamePlayer player : gamePlayers) {
            if (player.role == null)
                continue;

            if (size == 16) {
                size = 0;
                n++;
                players.add(new ArrayList<>());
            }
            players.get(n).add(player);
            size++;
        }

        nMax = (compoDuration * players.size());
    }

    private static boolean showCompo(FastBoard board) {
        if (players == null)
            return false;

        if (n == 0)
            return false;

        if (n < mainDuration)
            return false;

        double current = (double) (n - mainDuration) / compoDuration;

        if (current % 1 != 0)
            return true;

        List<GamePlayer> list = players.get((int) current);
        List<String> compo = new ArrayList<>();

        for (int i = 0; i < 16 && i < list.size(); i++) {
            if (list.get(i) == null || list.get(i).role == null)
                continue;

            if (list.get(i).isDead)
                compo.add(ChatColor.DARK_BLUE + "" + ChatColor.STRIKETHROUGH + list.get(i).role.getRole().getRoleName());
            else
                compo.add(i, ChatColor.DARK_BLUE + "" + list.get(i).role.getRole().getRoleName());
        }
        board.updateTitle("Composition " + (int) (current + 1) + "/" + list.size());
        board.updateLines(compo);
        return true;
    }

    public static void removeGameScoreboard(Player player) {
        boards.remove(player.getUniqueId());
    }

    private static void updateBoard(FastBoard board) {

        if (setup.getGame().getMainTask() != null && setup.getGame().getMainTask().hasRoles && players == null)
            initPlayers(setup.getGame().getGamePlayers());

        if (showCompo(board))
            return;

        GameState state = setup.getGame().getGameState();

        switch (state) {
            case GAME_FINISHED:
                GameScoreboardManager.updateGameFinishedBoard(setup, board);
                break;
            case GAME_TELEPORTATION_INVINCIBILITY:
            case GAME_BORDER:
            case GAME_MEETUP:
                if (setup.getGame().getMainTask() == null)
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

    private static void runBoard() {
        Bukkit.getServer().getScheduler().runTaskTimer(setup.getMain(), () -> {
            for (FastBoard board : boards.values()) {

                if (players != null)
                    n++;

                if (n - mainDuration >= nMax) {
                    n = 0;
                    board.updateLines(new ArrayList<>());
                }

                updateBoard(board);

            }
        }, 0, 20);
    }

}
