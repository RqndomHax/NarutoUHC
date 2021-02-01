/*
 * Copyright (c) 2021.
 *  Discord : _Paul#6918
 *  Author : RqndomHax
 *  Github: https://github.com/RqndomHax
 */

package fr.rqndomhax.narutouhc.inventories.host;

import fr.rqndomhax.narutouhc.core.Setup;
import fr.rqndomhax.narutouhc.infos.Maps;
import fr.rqndomhax.narutouhc.inventories.IInfos;
import fr.rqndomhax.narutouhc.managers.MRules;
import fr.rqndomhax.narutouhc.managers.rules.DayCycle;
import fr.rqndomhax.narutouhc.utils.inventory.RInventory;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class IHostWorld {

    private final Setup setup;
    private final Player player;
    private final RInventory inventory;

    public IHostWorld(Setup setup, Player player, RInventory inventory) {
        this.inventory = inventory;
        this.setup = setup;
        this.player = player;

        updateInventory();
    }

    private void updateInventory() {
        for (int i = 0 ; i < inventory.getInventory().getSize() ; inventory.setItem(i, null), i++);

        IInfos.placeInvBorders(inventory.getInventory());

        inventory.setItem(13, IInfos.getWorldHostAbsorption(setup.getGame().getGameInfo().getMRules()), getAbsorption());

        inventory.setItem(20, IInfos.WORLD_HOST_DROPS);

        inventory.setItem(24, IInfos.getWorldHostDifficulty(), changeDifficulty());

        inventory.setItem(31, IInfos.getWorldDayCycle(setup.getGame().getGameInfo().getMRules()), changeDayCycle());

        inventory.setItem(49, IInfos.RETURN_ITEM, e -> {
            player.closeInventory();
            player.openInventory(new IHost(setup, player).getInventory());
        });

        player.updateInventory();
    }

    private Consumer<InventoryClickEvent> changeDayCycle() {

        MRules mRules = setup.getGame().getGameInfo().getMRules();

        return e -> {
            switch (mRules.dayCycle) {
                case DAY:
                    if (e.isLeftClick())
                        mRules.dayCycle = DayCycle.NIGHT;
                    if (e.isRightClick())
                        mRules.dayCycle = DayCycle.NORMAL;
                    break;
                case NIGHT:
                    if (e.isLeftClick())
                        mRules.dayCycle = DayCycle.NORMAL;
                    if (e.isRightClick())
                        mRules.dayCycle = DayCycle.DAY;
                    break;
                case NORMAL:
                    if (e.isLeftClick())
                        mRules.dayCycle = DayCycle.DAY;
                    if (e.isRightClick())
                        mRules.dayCycle = DayCycle.NIGHT;
                    break;
                default: break;
            }

            inventory.setItem(31, IInfos.getWorldDayCycle(mRules), changeDayCycle());
        };

    }

    private Consumer<InventoryClickEvent> changeDifficulty() {

        return e -> {
            World naruto = Bukkit.getWorld(Maps.NARUTO_UNIVERSE.name());
            World noPvp = Bukkit.getWorld(Maps.NO_PVP.name());
            switch (naruto.getDifficulty()) {
                case EASY:
                    if (e.isLeftClick()) {
                        naruto.setDifficulty(Difficulty.NORMAL);
                        noPvp.setDifficulty(Difficulty.NORMAL);
                    }
                    if (e.isRightClick()) {
                        naruto.setDifficulty(Difficulty.PEACEFUL);
                        noPvp.setDifficulty(Difficulty.PEACEFUL);
                    }
                    break;
                case NORMAL:
                    if (e.isLeftClick()) {
                        naruto.setDifficulty(Difficulty.HARD);
                        noPvp.setDifficulty(Difficulty.HARD);
                    }
                    if (e.isRightClick()) {
                        naruto.setDifficulty(Difficulty.EASY);
                        noPvp.setDifficulty(Difficulty.EASY);
                    }
                    break;
                case HARD:
                    if (e.isLeftClick()) {
                        naruto.setDifficulty(Difficulty.PEACEFUL);
                        noPvp.setDifficulty(Difficulty.PEACEFUL);
                    }
                    if (e.isRightClick()) {
                        naruto.setDifficulty(Difficulty.NORMAL);
                        noPvp.setDifficulty(Difficulty.NORMAL);
                    }
                    break;
                case PEACEFUL:
                    if (e.isLeftClick()) {
                        naruto.setDifficulty(Difficulty.EASY);
                        noPvp.setDifficulty(Difficulty.EASY);
                    }
                    if (e.isRightClick()) {
                        naruto.setDifficulty(Difficulty.HARD);
                        noPvp.setDifficulty(Difficulty.HARD);
                    }
                    break;
                default: break;
            }
            inventory.setItem(24, IInfos.getWorldHostDifficulty(), changeDifficulty());
            player.updateInventory();
        };
    }

    private Consumer<InventoryClickEvent> getAbsorption() {

        MRules mRules = setup.getGame().getGameInfo().getMRules();

        return e -> {
            if (mRules.absorption == mRules.maxAbsorption && e.isLeftClick())
                return;

            if (mRules.absorption == 0 && e.isRightClick())
                return;

            if (e.isLeftClick())
                mRules.absorption = mRules.absorption + 1;
            if (e.isRightClick())
                mRules.absorption = mRules.absorption - 1;
            inventory.setItem(13, IInfos.getWorldHostAbsorption(mRules), getAbsorption());
        };
    }

}
