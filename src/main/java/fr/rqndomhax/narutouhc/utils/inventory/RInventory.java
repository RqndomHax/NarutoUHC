/*
 * Copyright (c) 2021.
 *  Discord : _Paul#6918
 *  Author : RqndomHax
 *  Github: https://github.com/RqndomHax
 */

package fr.rqndomhax.narutouhc.utils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class RInventory implements InventoryHolder {

    private final Inventory inventory;
    private final PageController pageController;
    private final Player owner;
    private final Map<Integer, Consumer<InventoryClickEvent>> mapShare;
    private final List<RInventoryRunnable> runnableList;

    public RInventory(Player owner, String name, int size) {
        this.owner = owner;
        this.inventory = Bukkit.createInventory(this, size, name);
        this.mapShare = new HashMap<>();
        this.pageController = new PageController(this);
        this.runnableList = new ArrayList<>();
    }

    public RInventory(Player owner, String name, InventoryType inventoryType) {
        this.owner = owner;
        this.inventory = Bukkit.createInventory(this, inventoryType, name);
        this.mapShare = new HashMap<>();
        this.pageController = new PageController(this);
        this.runnableList = new ArrayList<>();
    }

    public void addItem(ItemStack itemStack){
        int slot = inventory.firstEmpty();
        this.setItem(slot, itemStack);
    }

    public void addItem(ItemStack itemStack, Consumer<InventoryClickEvent> consumer){
        int slot = inventory.firstEmpty();
        this.setItem(slot, itemStack, consumer);
    }

    public void setItem(int slot, ItemStack itemStack){
        this.mapShare.remove(slot);
        this.inventory.setItem(slot, itemStack);
    }

    public void setItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> event){
        this.setItem(slot, itemStack);
        if (event != null) this.mapShare.put(slot, event);
    }

    public void setItem(int[] slot, ItemStack itemStack){
        this.setItem(slot, itemStack, null);
    }

    public void setItem(int[] slot, ItemStack itemStack, Consumer<InventoryClickEvent> event){
        for (int i : slot) {
            this.setItem(i, itemStack, event);
        }
    }

    public void setHorizontalLine(int from, int to, ItemStack itemStack) {
        setHorizontalLine(from, to, itemStack, null);
    }

    public void setHorizontalLine(int from, int to, ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        for (int i = from; i <= to; i++) {
            this.setItem(i, itemStack, event);
        }
    }

    public void setVerticalLine(int from, int to, ItemStack itemStack) {
        this.setVerticalLine(from, to, itemStack, null);
    }

    public void setVerticalLine(int from, int to, ItemStack itemStack, Consumer<InventoryClickEvent> event) {
        for (int i = from; i <= to; i += 9) {
            this.setItem(i, itemStack, event);
        }
    }

    public void setPageController(Consumer<PageController> pageController){
        pageController.accept(this.pageController);
        this.pageController.setUp();
    }

    public PageController getPageController(){
        return this.pageController;
    }

    public void update(Runnable runnable, int delay){
        this.runnableList.add(new RInventoryRunnable(runnable, delay));
    }

    public int[] getBoard(int slotfrom, int lenth, int width){
        final int[] board = new int[lenth*width];
        int size = 0;
        int l = 0;
        int w = 0;
        while (board.length != size) {
            board[size] = slotfrom+w+l;
            l++;
            size++;
            if(l == lenth){
                l = 0;
                w += 9;
            }
        }
        return board;
    }

    public void open(){
        this.owner.openInventory(this.inventory);
    }

    protected void onClose(InventoryCloseEvent event){ }
    protected void onOpen(InventoryOpenEvent event){ }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    final Map<Integer, Consumer<InventoryClickEvent>> getMapShare() {
        return mapShare;
    }
    final List<RInventoryRunnable> getRunnableList() { return runnableList; }
}