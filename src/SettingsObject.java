import java.util.Hashtable;

public class SettingsObject{
    private Hashtable<String, Object> settings;
    SettingsObject(String[] settings, Object[] defaults, int amount){
        this.settings = new Hashtable<>();
        for(int i = 0; i < amount; i++)
            this.settings.put(settings[i],defaults[i]);
    }

    public Object getSetting(String key){
        return settings.get(key);
    }

    public void setSetting(String key, String value){
        settings.put(key, value);
    }
}