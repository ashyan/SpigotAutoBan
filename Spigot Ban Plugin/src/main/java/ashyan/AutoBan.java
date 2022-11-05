package ashyan;

import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.BanList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Logger;

import static org.bukkit.BanList.Type.NAME;

public class AutoBan extends JavaPlugin {
    @Override
    public void onEnable() {
        Logger logger = Bukkit.getLogger();
        getServer().getPluginManager().registerEvents(new MyListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                BanList banned = Bukkit.getBanList(NAME);
                for(BanEntry player : banned.getBanEntries()) {
                    String playerName = player.getTarget();
                    LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime banDate = player.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if(ChronoUnit.SECONDS.between(banDate, now) >= 15) {
                        banned.pardon(playerName);
                        logger.info("Unbanned " + playerName);
                    }
                }
            }
        }, 0, 20 * 5);
    }
}
