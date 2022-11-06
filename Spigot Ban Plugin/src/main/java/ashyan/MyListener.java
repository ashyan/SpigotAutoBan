package ashyan;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.BanList;

import java.util.logging.Logger;

import static org.bukkit.BanList.Type.NAME;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class MyListener implements org.bukkit.event.Listener {
    JDA jda;
    @EventHandler
    public void onConnectAttempt(PlayerDeathEvent event) {
        Logger logger = Bukkit.getLogger();
        BanList banned = Bukkit.getBanList(NAME);
        banned.addBan(event.getEntity().getName(), "lol you died", null, null);
        logger.info("Banned " + event.getEntity().getName());
        event.getEntity().kickPlayer("erm you died");
        Guild guild = jda.getGuildsByName("locked armor's server", true).get(0);
        TextChannel channel = guild.getTextChannelsByName("bot-chat", true).get(0);
        channel.sendMessage(event.getDeathMessage()).queue();
    }


}
