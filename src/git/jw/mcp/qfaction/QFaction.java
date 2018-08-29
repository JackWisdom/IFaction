package git.jw.mcp.qfaction;

import com.google.gson.Gson;
import git.jw.mcp.qfaction.cmd.*;
import git.jw.mcp.qfaction.cmd.admin.FPower;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qfaction.data.ReginData;
import git.jw.mcp.qwzd.QwLib;
import git.jw.mcp.qwzd.configuration.BeanConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class QFaction extends JavaPlugin {
    public static QFaction instance;
    public static int createCost =5;
    public static int powerGainOnline=1;
    public static int donatCost=1;
    public static int acceptCost=1;
    public static int namecost=1;
    public static int upCost=1;
    public static List<String> claimworlds;
    private HashMap<String,ReginData> regins;
    //regins/*.yml
    private HashMap<String,FactionData> factions;
    //factions/
    //Key为owner的名字
    private HashMap<String,PlayerData> players;
    public static HashMap<String,String> inviteMap;
    //players/
    private HashMap<String,Integer> factionOnline;
    private void setUpCmd(){
        new FInfo(this);
        new FLeave(this);
        new FCreate(this);
        new FJoin(this);
        new FAccept(this);
        new FKick(this);
        new FPower(this);
        new FDonate(this);
        new FAlly(this);
        new FUnAlly(this);
        new FClaim(this);
        new FunClaim(this);
        new FUp(this);
        new FDown(this);
        new FSetSpawn(this);
        new FHome(this);
        new FName(this);
        /*
        getCommand("finfo").setExecutor(new FInfo(this));
        getCommand("fleave").setExecutor(new FLeave(this));
        getCommand("fcreate").setExecutor(new FCreate(this));
        getCommand("fjoin").setExecutor(new FJoin(this));
        getCommand("faccept").setExecutor(new FAccept(this));
        getCommand("fkick").setExecutor(new FKick(this));
        */
    }
    @Override
    public void onEnable(){
    instance=this;
    saveDefaultConfig();
    regins=new HashMap<>();
    factions=new HashMap<>();
    players=new HashMap<>();
    inviteMap=new HashMap<>();
    factionOnline=new HashMap<>();
    createCost =getConfig().getInt("create-cost");
    powerGainOnline=getConfig().getInt("power-gain-online");
    donatCost=getConfig().getInt("donate-cost");
    upCost=getConfig().getInt("upcost");
    acceptCost=getConfig().getInt("accept-cost");
    namecost=getConfig().getInt("name-cost");
    claimworlds=getConfig().getStringList("claim-worlds");
    File f=new File(getDataFolder(),"factions");
    File r=new File(getDataFolder(),"regins");
    File p=new File(getDataFolder(),"players");
    if(!f.exists())
        f.mkdir();
    if(!r.exists())
        r.mkdir();
        if(!p.exists())
        p.mkdir();
        for(String s:claimworlds){
            loadReginData(s);
        }
        setUpCmd();
        for(Player player:Bukkit.getOnlinePlayers()){
            PlayerData pd=   loadPlayer(player.getName());
            if(pd.getFaction()!=null&&getFaction(pd.getFaction())==null&&loadFaction(pd.getFaction())==null) {
                pd.setFaction(null);
                return;
            }
            addOnline(pd.getFaction());
        }


        Bukkit.getPluginManager().registerEvents(new Listener(),this);
    }
    @EventHandler
    public void onDisable(){

        for(Player player:Bukkit.getOnlinePlayers()){
            unLoadPlayer(player.getName());
        }
        for(ReginData d :regins.values()){
            BeanConfig  data = new BeanConfig (this, "regins/" +d.getWorld());
            data.save(d);
        }
        for(String s:factions.keySet()){
            unLoadFaction(s);
        }
        regins=null;
        factions=null;
        players=null;
        inviteMap=null;
        factionOnline=null;
    }
    public void addOnline(String faction){
        FactionData f=getFaction(faction);
        if(f==null)
            return;
        if(factionOnline.get(faction)==null){
            factionOnline.put(faction,1);
            return;
        }
        factionOnline.put(faction,factionOnline.get(faction)+1);
    }
    public static void debug(String s){
        //System.out.println(s);
    }
    public int getOnline(String faction){
        return factionOnline.get(faction);
    }
    public void delOnline(String faction ){
        FactionData f=getFaction(faction);
        if(f==null)
            return;
        factionOnline.put(faction,factionOnline.get(faction)-1);
    }
    public void loadReginData(String world){
        File f=new File(getDataFolder(),"regins/"+world+".json");
        if(f.exists()) {
            BeanConfig  data = new BeanConfig (this, "regins/" + world);
            ReginData reginData=data.load(ReginData.class);
            if(reginData!=null)
            regins.put(world,reginData);
            return;
        }
        regins.put(world,new ReginData(world));
    }
    public FactionData loadFaction(FactionData data){
        factions.put(data.getLeader(),data);
         addOnline(data.getLeader());
        return data;
    }
    public FactionData loadFaction(String factionName){
        File f=new File(getDataFolder(),"factions/"+factionName+".json");
        if(f.exists()){
            BeanConfig  config=new BeanConfig( this,"factions/"+factionName);
            FactionData data=config.load(FactionData.class);
            if(data==null){
                if(data.getLeader()==null){
                    removeFaction(factionName);
                    return null;
                }
                data.delete();
                return null;
            }

            factions.put(data.getLeader(),data);
            debug("loading faction"+factionName);
            return data;
        }
        return null;
    }

    public PlayerData loadPlayer(String player){
        File f=new File(getDataFolder(),"players/"+player+".json");
        if(f.exists()){
            BeanConfig config=new BeanConfig(this,"players/"+player);
            PlayerData data=config.load(PlayerData.class);

            try {
                if(data==null||data.getName()==null){
                    throw new NullPointerException();
                }
                players.put(player, data);
            }catch (Exception e){
                f.delete();
                data=new PlayerData(player);
                players.put(player,data);
                debug("ERROR WHILE LOADING");
            }
            return data;
        }
        PlayerData data=new PlayerData(player);
        players.put(player,data);
        debug(players.get(player)+"|"+player);
        return data;
    }
    public void unLoadFaction(String faction){
        if(factions.get(faction)==null){
            return;
        }
        FactionData data=factions.get(faction);
        BeanConfig  config=new BeanConfig( this,"factions/"+faction);
        config.save(data);
        factions.remove(faction);

    }
    public void removeFaction(String faction){
        FactionData data=new FactionData(faction);
        try{
            data.getFile().delete();
        }catch (Exception e){
            debug("an error happened while deleting "+faction);
        }
        factions.remove(faction);
        factionOnline.remove(faction);
        new BukkitRunnable() {
            @Override
            public void run() {
                for(PlayerData da:players.values()){
                    debug(da.getFaction()+"|"+faction);
                    if(da.getFaction()!=null&&da.getFaction().equalsIgnoreCase(faction)){
                        da.setFaction(null);
                        Bukkit.getPlayer(da.getName()).sendMessage("§c您的工会已经解散");
                    }
                }
                for(ReginData reginData:regins.values()){
                    debug("removing"+faction);
                    reginData.removeClaims(faction);
                }
            }
        }.runTaskAsynchronously(this);
        factions.remove(faction);
        factionOnline.remove(faction);
        try{
            data.getFile().delete();
        }catch (Exception e){
            debug("an error happened while deleting "+faction);
        }
    }
    public void unLoadPlayer(String player){
        if(players.get(player)==null){
            debug("404 not found");
            return;
        }
        PlayerData data=players.get(player);
        BeanConfig  config=new BeanConfig (this,"players/"+player);
        debug("dataisnull"+players.get(player));
        config.save(data);
        players.remove(player);
    }

    public ReginData getReginData(World world){
        return regins.get(world.getName());
    }
    public ReginData getReginData(String s){
        return regins.get(s);
    }
    public PlayerData getPlayerData(String s){

        return players.get(s);
    }
    public PlayerData getPlayerData(Player s){
      return   getPlayerData(s.getName());
    }
    public FactionData getFaction(String string){
        return factions.get(string);
    }
}
