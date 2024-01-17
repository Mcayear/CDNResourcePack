package cn.vusv.cdnresourcepack;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;

/**
 * author: MagicDroidX
 * NukkitExamplePlugin Project
 */
public class CDNResourcePack extends PluginBase {
    public static CDNResourcePack INSTANCE;
    public Config urlpackConfig;


    @Override
    public void onLoad() {
        //save Plugin Instance
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        //Save resources
        this.saveResource("resource_packs.yml");
        urlpackConfig = new Config(new File(this.getDataFolder(), "resource_packs.yml"), 2);

        //Register the EventListener
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }
}
