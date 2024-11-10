package Startup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class ConfigManager {
    private static final String CONFIG_FILE="config.json";
    private static final Gson gson=new GsonBuilder().setPrettyPrinting().create();

    //Saving to JSON file
    public static void saveConfig(SystemConfig config){
        try(FileWriter writer=new FileWriter(CONFIG_FILE)){
            gson.toJson(config,writer);
            System.out.println("Configuration Saved");
        }catch(IOException e){
            System.out.println("Error "+e.getMessage());
        }
    }
    //Load from JSON

    public static SystemConfig loadConfig(){
        try(FileReader reader=new FileReader(CONFIG_FILE)){
            return gson.fromJson(reader,SystemConfig.class);
        }catch (IOException e){
            System.out.println("Error loading "+e.getMessage());
            return new SystemConfig();
        }
    }
}
