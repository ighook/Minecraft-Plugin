package org.rocheserver;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class PlayerCommand implements CommandExecutor {
    public static Main plugin;
    public static void setPlugin(Main MainPlugin) {
        plugin = MainPlugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender == null) {
            sender.sendMessage("플레이어만 사용 가능한 명령어입니다.");
            return true;
        }

        // 아이템 생성
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("름의 검");
        meta.setUnbreakable(true); // 내구도를 무한대로 설정
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE); // 아이템 설명에서 내구도 숨김
        meta.setLore(Collections.singletonList(ChatColor.AQUA + "름이 직접 만든 검입니다.")); // 아이템 설명 추가
        meta.setCustomModelData(1);
        item.setItemMeta(meta);

        Player player = (Player) sender;
        player.getInventory().addItem(item);
        player.sendMessage("커스텀 아이템을 추가했습니다.");
        return true;
    }
}
