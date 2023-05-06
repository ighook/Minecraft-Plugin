package org.rocheserver.CustomItem;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.sql.*;
import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public class OreBag implements Listener {
    UUID currentBagUUID;
    String errorMsg = "이 가방에는 광물만 넣을 수 있습니다.";
    private static final Material[] MINERAL_TYPES = {
            Material.COAL,
            Material.COAL_BLOCK,
            Material.COBBLESTONE,
            Material.COPPER_INGOT,
            Material.COPPER_ORE,
            Material.COPPER_BLOCK,
            Material.RAW_COPPER,
            Material.DEEPSLATE_COPPER_ORE,
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
            Material.RAW_GOLD,
            Material.RAW_GOLD_BLOCK,
            Material.IRON_INGOT, // 철 주괴
            Material.IRON_NUGGET, // 철 파편
            Material.IRON_ORE, // 철 광석
            Material.RAW_IRON_BLOCK,
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
            Material.AMETHYST_SHARD,
            Material.AMETHYST_BLOCK,
            Material.AMETHYST_CLUSTER,
            Material.BUDDING_AMETHYST,
            Material.SMALL_AMETHYST_BUD,
            Material.MEDIUM_AMETHYST_BUD,
            Material.LARGE_AMETHYST_BUD,
    };

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws SQLException {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) { // 우클릭했을 경우
            if (item.getType() == Material.STICK &&
                    item.getItemMeta().hasCustomModelData() &&
                    item.getItemMeta().getCustomModelData() == 1) {
                UUID uuid = UUID.fromString(item.getItemMeta().getLore().get(1));
                Inventory inventory = Bukkit.createInventory(null, 27, "광물 가방");
                Inventory loadedInventory = loadInventoryFromDatabase(uuid);
                if(loadedInventory != null) {
                    inventory.setContents(loadedInventory.getContents());
                } else {
                    createBag(uuid);
                }
                currentBagUUID = uuid;
                event.getPlayer().openInventory(inventory);
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        String inventoryName = event.getView().getTitle();

        ItemStack cursorItem = event.getCursor();
        Material cursorItemType = cursorItem.getType();
        if (clickedInventory == null) return;
        getLogger().info(inventoryName);
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
        Inventory inventory = event.getInventory();
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
    public void inventorySave(Inventory inventory) {
        if(currentBagUUID == null) return;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://ighook.cafe24.com:3306/ighook", "ighook", "wlsqja4292!");
            stmt = conn.prepareStatement("UPDATE tb_minecraft_ore_bag SET inventory_data = ? WHERE inventory_id = ?");

            stmt.setString(2, currentBagUUID.toString());
            stmt.setString(1, toBase64(inventory));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void createBag(UUID uuid) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://ighook.cafe24.com:3306/ighook", "ighook", "wlsqja4292!");
            stmt = conn.prepareStatement("INSERT INTO tb_minecraft_ore_bag (inventory_id) VALUES (?)");
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

    }
    public Inventory loadInventoryFromDatabase(UUID uuid) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://ighook.cafe24.com:3306/ighook", "ighook", "wlsqja4292!");
            stmt = conn.prepareStatement("SELECT inventory_data FROM tb_minecraft_ore_bag WHERE inventory_id=?");
            stmt.setString(1, uuid.toString());

            rs = stmt.executeQuery();
            if (rs.next()) {
                String data = rs.getString("inventory_data");
                Inventory inventory;
                if(data != null) {
                    inventory = fromBase64(data);
                } else {
                    inventory = Bukkit.createInventory(null, 27, "광물 가방");
                }

                return inventory;
            } else {
                // 해당 UUID에 대한 데이터가 없을 경우 null 반환
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(event.getView().getTitle().equals("광물 가방")) {
            inventorySave(event.getInventory());
        }
    }
    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
