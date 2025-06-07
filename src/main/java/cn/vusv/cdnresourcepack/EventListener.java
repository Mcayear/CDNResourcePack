package cn.vusv.cdnresourcepack;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author: MagicDroidX
 * NukkitExamplePlugin Project
 */
public class EventListener implements Listener {

    private final CDNResourcePack plugin;

    public EventListener(CDNResourcePack plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPacket(DataPacketSendEvent event) {
        DataPacket packet = event.getPacket();
        if (packet instanceof ResourcePacksInfoPacket && packet.packetId() == ProtocolInfo.RESOURCE_PACKS_INFO_PACKET) {
            if (!((ResourcePacksInfoPacket) event.getPacket()).getCDNEntries().isEmpty()) return;
            Iterator<Map.Entry<String, Object>> iterator = plugin.urlpackConfig.getAll().entrySet().iterator();
            if (!iterator.hasNext()) return;
            event.setCancelled();

            ResourcePacksInfoPacket proxyPacket = (ResourcePacksInfoPacket) packet.clone();
            List<ResourcePacksInfoPacket.CDNEntry> cdnEntries = new ObjectArrayList<>();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();

                // UUIDVersion是资源包的UUID和版本的组合，格式为UUID_Version。仅当客户端尚未缓存资源包时，客户端才会尝试下载它。
                String UUIDVersion = entry.getKey();
                // URL是从中下载资源包的URL。此URL必须提供包含manifest.json文件的zip文件，该文件位于另一个文件夹中。manifest不能位于zip文件的根目录。
                String URL = String.valueOf(entry.getValue());
                plugin.getLogger().info("CDN:\nUUIDVersion: " + UUIDVersion + "\nURL: " + URL);
                cdnEntries.add(new ResourcePacksInfoPacket.CDNEntry(UUIDVersion, URL));
            }

            proxyPacket.setCDNEntries(cdnEntries);
            proxyPacket.encode();
            event.getPlayer().dataPacket(proxyPacket);
        }
    }
}