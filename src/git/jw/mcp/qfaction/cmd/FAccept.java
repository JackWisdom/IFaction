package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FAccept extends BaseCmd {
    public FAccept(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 1);
    }
    @Override
    protected String getName() {
        return "faccept";
    }

    @Override
    public boolean exec(CommandSender commandSender, String[] strings) {
        Player p= (Player) commandSender;
        QFaction qf=QFaction.instance;
        PlayerData data= QFaction.instance.getPlayerData(p);
        if(data.getFaction()==null){
            p.sendMessage("§c您没有派系");
            return true;
        }
        FactionData factionData=QFaction.instance.getFaction(data.getFaction());
        if(!factionData.isAdmin(p.getName())){
           p.sendMessage("§c您并不是帮派管理员");
            return true;
        }
        if(QFaction.inviteMap.get(strings[0])==null||(!QFaction.inviteMap.get(strings[0]).equalsIgnoreCase(data.getFaction()))){
            p.sendMessage("§c对方并没有申请加入你的工会哟");
            return true;
        }
        if(qf.getPlayerData(strings[0])==null){
            p.sendMessage("§c对方不在线 无法操作");
            return true;
        }
        if(qf.getPlayerData(strings[0]).getFaction()!=null){
            p.sendMessage("§c对方已经加入了其他的工会");
            QFaction.inviteMap.remove(strings[0]);
            return true;
        }
        if(!data.hasEnoughPower(QFaction.acceptCost)){
            p.sendMessage("§c您需要"+QFaction.acceptCost+"点能量来给工会纳新");
            return true;
        }
        qf.getPlayerData(strings[0]).setFaction(data.getFaction());
        Bukkit.broadcastMessage("§a恭喜玩家"+strings[0]+"加入了"+data.getFaction()+"的工会");
        return true;
    }
}
