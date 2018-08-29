package git.jw.mcp.qfaction;

import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qfaction.data.ReginData;
import git.jw.mcp.qwzd.QwLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Listener implements org.bukkit.event.Listener {
    QFaction qf=QFaction.instance;
    private HashMap<Player,Boolean> applyEffect;
    public Listener(){
        applyEffect=new HashMap<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p:applyEffect.keySet()){
                    if(!p.isOnline())
                        continue;
                    if(applyEffect.get(p)){
                        applyEffectOnline(p);
                    }
                    applyEffectOffline(p);
                }
                applyEffect.clear();
            }
        }.runTaskTimer(QFaction.instance,50,20);
        //string name boolean is online

        HashMap<String,String> improve=new HashMap<>();
        //玩家名字 上一次进入的地区
        QwLib.startTimer(QFaction.instance, new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p:Bukkit.getOnlinePlayers()){
                    PlayerData playerData=QFaction.instance.getPlayerData(p.getName());
                    playerData.tickOnline();
                }
                improve.clear();
            }
        },300);
        QwLib.startTimer(QFaction.instance, new BukkitRunnable() {
            //世界保护
            @Override
            public void run() {
              //  QFaction.debug("TST");
                for(Player p:Bukkit.getOnlinePlayers()){
                    PlayerData playerData=QFaction.instance.getPlayerData(p.getName());
                    String world=p.getLocation().getWorld().getName();
                    if(!QFaction.claimworlds.contains(world)){
                        continue;
                    }
                    String claimedFaction=    QFaction.instance.getReginData(world).getClaimed(p.getLocation().getChunk());

                    QFaction.debug("上一次进入"+improve.get(p.getName()));

                    QFaction.debug("进入"+claimedFaction);
                    if(improve.get(p.getName())!=null){
                        if (claimedFaction==null){
                            p.sendMessage("您进入了野外");
                        }else {
                            if(!improve.get(p.getName()).equalsIgnoreCase(claimedFaction)) {
                                p.sendMessage("您进入了" + claimedFaction + "的领地");
                            }
                        }

                    }else {
                        if(claimedFaction!=null) {
                            p.sendMessage("您进入了" + claimedFaction + "的领地");
                        }
                    }
                    improve.put(p.getName(),claimedFaction);
                    if(!QFaction.claimworlds.contains(world))
                        continue;
                    if(QFaction.instance.getReginData(world)==null)
                        continue;
                    ReginData reginData=  QFaction.instance.getReginData(world);
                    if(!reginData.isClaimed(p.getLocation().getChunk()))
                        continue;

                    if(claimedFaction==null){
                        //没被领取
                        continue;
                    }
                    FactionData factionData=QFaction.instance.getFaction(claimedFaction);
                    boolean online=factionData!=null;
                    if(playerData.getFaction()==null){
                        if(online){
                            applyEffect.put(p ,true);
                            continue;
                        }
                        applyEffect.put(p ,false);
                        //领取了+自己没faction 这里添加效果
                        continue;
                    }
                    //检测是否是自家派系
                    if(playerData.getFaction().equalsIgnoreCase(claimedFaction)){
                        continue;
                    }
                    //检测是否是别家派系

                    if(online){
                        //对面在线
                        if(factionData.getAllies().contains(playerData.getFaction())){
                            //是盟友
                            continue;
                        }
                        applyEffect.put(p ,false);
                        continue;
                    }
                    applyEffect.put(p ,false);

                }

            }
        },5);
    }
    private void applyEffectOnline(Player p){
        /*
        p.removePotionEffect(PotionEffectType.GLOWING);
        p.removePotionEffect(PotionEffectType.SLOW);
        p.removePotionEffect(PotionEffectType.SLOW_DIGGING);*/
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,120,0),true);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,120,0),true);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,120,0),true);
    }
    private void applyEffectOffline(Player p){/*
        p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        */
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,120,2),true);
        //p.setVelocity(new Vector(0,2,0));
    }
    @EventHandler
    public void onInteracT(PlayerInteractEvent event){
        if(event.getHand()!=EquipmentSlot.HAND){
            return;
        }
        if(event.getAction()!= Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if(!event.getClickedBlock().getType().isInteractable()){
            return;
        }
        Player p=event.getPlayer();
        String world=p.getWorld().getName();
        ReginData reginData=  QFaction.instance.getReginData(world);

        if(!reginData.isClaimed(p.getLocation().getChunk())) {
            QFaction.debug(reginData.getClaimed(p.getLocation().getChunk()));
            return;
        }
        String faction=reginData.getClaimed(p.getLocation().getChunk());
        if(qf.getFaction(faction)!=null)
            //如果帮会有人在线则返回
            return;

        if(qf.getPlayerData(p).getFaction()==null||!qf.getPlayerData(p).getFaction().equalsIgnoreCase(faction)){
            p.sendMessage("您不不是改帮会成员");
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
     PlayerData pd=  qf.loadPlayer(event.getPlayer().getName());
        if(pd.getFaction()!=null&&qf.getFaction(pd.getFaction())==null&&qf.loadFaction(pd.getFaction())==null) {
            pd.setFaction(null);
            return;
        }
        qf.addOnline(pd.getFaction());

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player p=event.getPlayer();

        PlayerData data=qf.getPlayerData(p);
        if(data.getFaction()!=null) {
            qf.delOnline(data.getFaction());
            if(qf.getOnline(data.getFaction())<=0){
                qf.unLoadFaction(data.getFaction());
            }
        }
        qf.unLoadPlayer(p.getName());
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void ondeath(PlayerDeathEvent event){
        PlayerData pd=QFaction.instance.getPlayerData(event.getEntity());
        if(pd.getFaction()==null)
            return;
        FactionData fd=QFaction.instance.getFaction(pd.getFaction());
        if(fd.getSpawn()!=null){
            event.getEntity().setBedSpawnLocation(fd.getSpawn());
        }else if(Bukkit.getPlayer(fd.getLeader()).getBedSpawnLocation()!=null){
            event.getEntity().setBedSpawnLocation(Bukkit.getPlayer(fd.getLeader()).getBedSpawnLocation());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getEntity().spigot().respawn();
            }
        }.runTaskLater(QFaction.instance,10);
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        PlayerData pd=QFaction.instance.getPlayerData(event.getPlayer());
        if(pd.getFaction()==null) {
            event.setFormat(event.getFormat().replace("[faction]", ""));
        return;
        }
        event.setFormat(event.getFormat().replace("[faction]", qf.getFaction(pd.getFaction()).getCustomName()));
    }
}
