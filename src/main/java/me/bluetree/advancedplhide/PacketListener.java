package me.bluetree.advancedplhide;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PacketListener extends PacketAdapter {

    public PacketListener(AdvancedPlHide core) {
        super(core, ListenerPriority.NORMAL, PacketType.Play.Server.TAB_COMPLETE);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        if (e.getPlayer() == null || e.getPlayer() instanceof TemporaryPlayer) return;

        PacketContainer packetContainer = e.getPacket();

        StructureModifier<Suggestions> matchModifier = packetContainer.getSpecificModifier(Suggestions.class);

        Suggestions matchedCommands = matchModifier.read(0);
        Suggestions allowedCommands = new Suggestions(matchedCommands.getRange(), new ArrayList<>(matchedCommands.getList()));
        List<String> tohide = new ArrayList<>();
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            tohide.add(plugin.getName().toLowerCase() + ":");
        }
        tohide.add("paper");
        tohide.add("spigot");
        tohide.add("bukkit");
        tohide.add("purpur");
        tohide.add("airplane");
        tohide.add("minecraft");
        tohide.add("votingpluginvaluerequestinput");
        for (Suggestion matchedCommand : matchedCommands.getList()) {
            for (String hide : tohide) {
                if (matchedCommand.getText().startsWith(hide)) {
                    allowedCommands.getList().remove(matchedCommand);
                }
            }
        }
        matchModifier.write(0, allowedCommands);
    }
}
