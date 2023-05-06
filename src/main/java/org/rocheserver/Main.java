package org.rocheserver;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
//        PlayerEnterEvent.setPlugin(this);
//        PlayerEnterEvent playerEnterEvent = new PlayerEnterEvent();

//        MineralInventory.setPlugin(this);
//        MineralInventory mineralInventory = new MineralInventory();

        getCommand("additem").setExecutor(new PlayerCommand());
        getServer().getPluginManager().registerEvents(new PlayerEnterEvent(), this);
        getServer().getPluginManager().registerEvents(new MineralInventory(), this);
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Roche Server 플러그인 활성화");
    }

    @Override
    public void onDisable() {
        getLogger().info("Roche Server 플러그인 비활성화");
    }
}
