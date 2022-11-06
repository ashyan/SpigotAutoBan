package ashyan;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {
    private AutoBan plugin;
    private FileConfiguration dataconfig = null;
    private File configFile = null;

    File dataFolder;

    private String dataFilename = "AutoBanConfig.yml";
    public DataManager(AutoBan plugin) {
        this.plugin = plugin;
        dataFolder = plugin.getDataFolder().getParentFile();

        saveDefaultConfigFile();

    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(dataFolder, dataFilename);
        }

        this.dataconfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(dataFilename);
        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataconfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if(this.dataconfig == null)
            reloadConfig();

        return this.dataconfig;
    }

    public void saveConfig() {
        if(this.dataconfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfigFile() {
        if(this.configFile == null) {
            this.configFile = new File(dataFolder, dataFilename);

        }

        if (!this.configFile.exists()) {
            this.plugin.saveResource(dataFilename, false);
        }
    }
}
