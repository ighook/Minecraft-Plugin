package org.rocheserver;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class CustomItem {
    public static Main plugin;
    public static void setPlugin(Main MainPlugin) {
        plugin = MainPlugin;
    }
    public void newItem() {
        ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("눈물");
        meta.setUnbreakable(true); // 내구도를 무한대로 설정
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // 아이템 설명에서 내구도 숨김
        meta.setLore(Collections.singletonList(ChatColor.AQUA + "눈물의 힘을 느낄 수 있는 아이템입니다.")); // 아이템 설명 추가
        item.setItemMeta(meta);
    }

}
