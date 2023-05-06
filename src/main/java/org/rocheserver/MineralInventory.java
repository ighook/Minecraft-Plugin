package org.rocheserver;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Set;

import static org.bukkit.Bukkit.getLogger;

public class MineralInventory implements Listener {
    Inventory mineralInventory = Bukkit.createInventory(null, 27, "광물 가방");
    String errorMsg = "이 가방에는 광물만 넣을 수 있습니다.";
    private static final Material[] MINERAL_TYPES = {
            Material.COAL,
            Material.COAL_BLOCK,
            Material.COBBLESTONE,
            Material.COPPER_INGOT,
            Material.COPPER_ORE,
            Material.COPPER_BLOCK,
            Material.DIAMOND,
            Material.DIAMOND_ORE,
            Material.DIAMOND_BLOCK,
            Material.EMERALD,
            Material.EMERALD_ORE,
            Material.EMERALD_BLOCK,
            Material.GOLD_INGOT,
            Material.GOLD_NUGGET,
            Material.GOLD_ORE, // 금 광석
            Material.DEEPSLATE_GOLD_ORE, // 심층암 금 광석
            Material.GOLD_BLOCK, // 금 블록
            Material.IRON_INGOT, // 철 주괴
            Material.IRON_NUGGET, // 철 파편
            Material.IRON_ORE, // 철 광석
            Material.DEEPSLATE_IRON_ORE, // 심층암 철 광석
            Material.IRON_BLOCK, // 철 블록
            Material.NETHERITE_INGOT, // 네더라이트 주괴
            Material.NETHERITE_SCRAP, // 네더라이트 파편
            Material.REDSTONE, // 레드스톤 가루
            Material.REDSTONE_ORE, // 레드스톤 광석
            Material.DEEPSLATE_REDSTONE_ORE, // 심층암 레드스톤 광석
            Material.REDSTONE_BLOCK, // 레드스톤 블록
            Material.LAPIS_BLOCK, // 청금석 블록
            Material.LAPIS_ORE, // 청금석 광석
            Material.DEEPSLATE_LAPIS_ORE, // 심층암 청금석 광석
            Material.LAPIS_LAZULI, // 청금석
    };

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        getLogger().info("onPlayerInteract");
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) { // 우클릭했을 경우

            if (item.getType() == Material.STICK) { // 오른손에 스틱이 있을 경우
                openMineralInventory(player); // 광물 전용 인벤토리를 열어줍니다.
                getLogger().info("openMineralInventory");
            }
        }
    }
    private void openMineralInventory(Player player) {
//        mineralInventory = Bukkit.createInventory(null, 27, "광물 가방"); // 27개 슬롯으로 이루어진 광물 전용 인벤토리 생성
        player.openInventory(mineralInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        String inventoryName = event.getView().getTitle();

        ItemStack cursorItem = event.getCursor();
        Material cursorItemType = cursorItem.getType();


        if (clickedInventory == null) return;



        if(inventoryName.equals("광물 가방")) {
            if(event.isShiftClick()) {
                ItemStack clickedItem = event.getCurrentItem();
                Material itemType = clickedItem.getType();
                if (!isMineral(itemType)) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.YELLOW + errorMsg);
                }
            } else {
                int rawSlot = event.getRawSlot();
                getLogger().info("rawSlot: " + rawSlot);
                if (rawSlot >= 0 && rawSlot <= 26) {

                    if (event.getAction() == InventoryAction.PLACE_ALL ||
                            event.getAction() == InventoryAction.PLACE_ONE ||
                            event.getAction() == InventoryAction.PLACE_SOME) {

                        if (!isMineral(cursorItemType)) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.YELLOW + errorMsg);
                        }
                    }
                }
            }
        }
    }

    private boolean isMineral(Material material) {
        for (Material mineral : MINERAL_TYPES) {
            if (mineral.equals(material)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryName = event.getView().getTitle();
        Map<Integer, ItemStack> draggedItems = event.getNewItems();

        if(inventoryName.equals("광물 가방")) {
            Set<Integer> rawSlot = event.getRawSlots();
            boolean isMineralInventory = false;
            for (int slot : rawSlot) {
                if (slot >= 0 && slot <= 26) {
                    isMineralInventory = true;
                    break;
                }
            }

            if (isMineralInventory) {
                for (Map.Entry<Integer, ItemStack> entry : draggedItems.entrySet()) {
                    ItemStack draggedItem = entry.getValue();
                    Material itemType = draggedItem.getType();
                    if (!isMineral(itemType)) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.YELLOW + errorMsg);
                        break;
                    }
                }
            }
        }
    }
}
