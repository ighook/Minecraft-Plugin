package org.rocheserver;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class PlayerEnterEvent implements Listener {
    public static Main plugin;
    public static void setPlugin(Main MainPlugin) {
        plugin = MainPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        String hexCode = "#00CCFF";
        Color color = Color.decode(hexCode);

        ChatColor customColor = ChatColor.of(color);

        event.setJoinMessage(customColor + playerName + ChatColor.WHITE + " 님이 입장하셨습니다 !");

        Player player = event.getPlayer();
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);

        ItemMeta meta = chestplate.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Iron Chestplate");
        meta.setCustomModelData(1);

        // 최대 체력을 늘리는 코드
        AttributeModifier healthBoost = new AttributeModifier(
                UUID.randomUUID(),
                "healthBoost",
                10.0,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.CHEST
        );
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, healthBoost);

        chestplate.setItemMeta(meta);

        player.getInventory().addItem(chestplate);
    }
}
