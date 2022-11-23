package ashyan;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.io.FileWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;


public class DataManager {
    private AutoBan plugin;
    private FileConfiguration dataconfig = null;
    private File configFile = null;
    private File nameFileJSON = null;
    JSONObject jsonObject;

    File dataFolder;

    private String dataFilename = "AutoBanConfig.yml";
    private String nameFilename = "MentionLinks.json";
    public DataManager(AutoBan plugin) {
        this.plugin = plugin;
        dataFolder = plugin.getDataFolder().getParentFile();

        saveDefaultConfigFile();

        jsonObject = new JSONObject();

    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(dataFolder, dataFilename);
        }
        if(this.nameFileJSON == null){
            this.nameFileJSON = new File(dataFolder, nameFilename);
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

    public void writeNameLink(String username, String DC_username){
        this.jsonObject.put(username,DC_username);
        try{
            FileWriter nameLink = new FileWriter(dataFolder + "/" + nameFilename);
            nameLink.write(jsonObject.toJSONString());
            nameLink.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public String readNameLink(String username){
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(dataFolder + "/" + nameFilename));
            JSONObject readObj = (JSONObject)obj;
            if(readObj.get(username) != null){
                String DC_username = (String)readObj.get(username);
                return DC_username;
            }else{
                return "none";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "none";
    }
}
