package org.rocheserver;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.rocheserver.CustomItem.OreBag;
import org.rocheserver.Commands.GiveOreBag;

public final class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getCommand("GiveOreBag").setExecutor(new GiveOreBag());
        getServer().getPluginManager().registerEvents(new PlayerEnterEvent(), this);
        getServer().getPluginManager().registerEvents(new OreBag(), this);
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Roche Server 플러그인 활성화");
    }

    @Override
    public void onDisable() {
        getLogger().info("Roche Server 플러그인 비활성화");
    }
}
