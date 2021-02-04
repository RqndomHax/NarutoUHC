/*
 * Copyright (c) 2021.
 *  Discord : _Paul#6918
 *  Author : RqndomHax
 *  Github: https://github.com/RqndomHax
 */

package fr.rqndomhax.narutouhc.listeners;

import fr.rqndomhax.narutouhc.core.Setup;
import fr.rqndomhax.narutouhc.inventories.IInfos;
import fr.rqndomhax.narutouhc.inventories.host.IHost;
import fr.rqndomhax.narutouhc.managers.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ELobbyCancel implements Listener {

    private final Setup setup;

    public ELobbyCancel(Setup setup) {
        this.setup = setup;
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.RIGHT_CLICK_AIR))
            return;
        if (!setup.getGame().getGameState().equals(GameState.LOBBY_WAITING))
            return;
        if (e.getItem() == null || !e.getItem().equals(IInfos.MAIN_HOST_ITEM))
            return;
        e.getPlayer().openInventory(new IHost(setup, e.getPlayer()).getInventory());
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!setup.getGame().getGameState().equals(GameState.LOBBY_WAITING))
            return;
        if (e.getItemDrop().getItemStack().equals(IInfos.MAIN_HOST_ITEM))
            e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        if (!setup.getGame().getGameState().equals(GameState.LOBBY_WAITING))
            return;
        if (e.getCurrentItem() == null)
            return;
        if (e.getCurrentItem().equals(IInfos.MAIN_HOST_ITEM))
            e.setCancelled(true);
    }
}
