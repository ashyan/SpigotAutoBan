package ashyan;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.BanList;

import java.util.logging.Logger;

import static org.bukkit.BanList.Type.NAME;

public class MyListener implements org.bukkit.event.Listener {
    @EventHandler
    public void onConnectAttempt(PlayerDeathEvent event) {
        Logger logger = Bukkit.getLogger();
        BanList banned = Bukkit.getBanList(NAME);
        banned.addBan(event.getEntity().getName(), "lol you died", null, null);
        logger.info("Banned " + event.getEntity().getName());
        event.getEntity().kickPlayer("lol you died");
//        for(BanEntry player : banned.getBanEntries()) {
//            String playerName = player.getTarget();
//            message = "Get fucked, ";
//            if(playerName.equals(event.getName())) {
//                LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//                LocalDateTime banDate = player.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//                if(ChronoUnit.DAYS.between(banDate, now) >= 7) {
//                    banned.pardon(playerName);
//                    message = "Welcome back, ";
//                }
//                break;
//            }
//
//        }
//        Bukkit.broadcastMessage(message + event.getName() + "!");
    }
}
