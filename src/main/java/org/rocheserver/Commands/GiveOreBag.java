package org.rocheserver.Commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rocheserver.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class GiveOreBag implements CommandExecutor {
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
        UUID itemId = UUID.randomUUID();
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "광물만 넣을 수 있는 가방입니다.");
        lore.add(itemId.toString());

        meta.setDisplayName("광물 가방");
        meta.setUnbreakable(true); // 내구도를 무한대로 설정
        meta.setCustomModelData(1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        meta.setLore(lore);
        item.setItemMeta(meta);

        Player player;

        getLogger().info(Arrays.toString(strings));

        if(strings.length > 0) {
            player = Bukkit.getPlayer(strings[0]);
        } else {
            player = (Player) sender;
        }
        player.getInventory().addItem(item);
        player.sendMessage("커스텀 아이템을 추가했습니다.");
        return true;
    }
}
