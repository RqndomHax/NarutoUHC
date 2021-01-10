/*
 * Copyright (c) 2021.
 *  Discord : _Paul#6918
 *  Author : RqndomHax
 *  Github: https://github.com/RqndomHax
 */

package fr.rqndomhax.narutouhc.utils.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public final class RInventoryData {

    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> consumer;

    public RInventoryData(ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        this.itemStack = itemStack;
        this.consumer = consumer;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Consumer<InventoryClickEvent> getConsumer() {
        return consumer;
    }
}