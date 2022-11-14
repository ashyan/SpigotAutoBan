package ashyan;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.time.format.DateTimeFormatter;


public class MyListener implements org.bukkit.event.Listener {
    DiscordBot discordBot;
    BanManager banManager;

    public MyListener(DiscordBot discordBot, BanManager banManager) {
        this.discordBot = discordBot;
        this.banManager = banManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        banManager.onDeath(event);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, KK:mma");
        String unbanStr = banManager.getUnbanDate(event.getEntity().getName()).format(formatter);

        String death = event.getDeathMessage();

        String message = death + ". They will be unbanned at " + unbanStr;
        this.discordBot.sendMesageToChannel(message);
    }

}
