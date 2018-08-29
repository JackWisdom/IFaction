package git.jw.mcp.qfaction.data;

import git.jw.mcp.qfaction.QFaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;

public class FactionData implements Serializable {
    //factions里多个文件
    public String leader;
    public HashSet<String> coLeader;
    public HashSet<String> allies;
    public String customName;
    public Location spawn;
    public Location getSpawn() {
        if(spawn==null&& Bukkit.getPlayer(leader).getBedSpawnLocation()!=null){
            return Bukkit.getPlayer(leader).getBedSpawnLocation();
        }
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }


    //仅用于聊天显示的customname
    public FactionData(String player){
        this.leader=player;
        this.coLeader=new HashSet<>();
        this.allies=new HashSet<>();
        customName=player+"的工会";
    }

    public String getCustomName() {
        if (customName==null)
            return getLeader();
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
    public boolean isLeader(String s){
        return getLeader().equalsIgnoreCase(s);
    }
    public boolean isCoLeader(String s){
        return this.coLeader.contains(s);
    }
    public boolean isAdmin(String s){
        return isLeader(s)||isCoLeader(s);
    }
    public void addAlly(String faction){
    allies.add(faction);
    }
    public HashSet<String> getAllies(){
        return allies;
    }
    public void removeAllay(String s){
        allies.remove(s);
    }
    public String getLeader(){
        return leader;
    }
    public HashSet<String> getCoLeaders(){
        return coLeader;
    }
    public void addCoLeader(String player){
        getCoLeaders().add(player);
    }
    public void removeCoLeader(String player){
        getCoLeaders().remove(player);
    }
    public File getFile(){
        return new File(QFaction.instance.getDataFolder(),"factions/"+getLeader()+".json");
    }

    public void delete() {
        getFile().delete();
    }
}
