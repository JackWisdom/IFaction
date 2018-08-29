package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FDonate extends BaseCmd {
    public FDonate(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 2);
    }
    @Override
    protected String getName() {
        return "fdonate";
    }

    @Override
    public boolean exec(CommandSender commandSender, String[] strings) {
        Player p= (Player) commandSender;
        QFaction qf=QFaction.instance;
        PlayerData data= QFaction.instance.getPlayerData(p);
        if(p.getName().equalsIgnoreCase(strings[0])){
            p.sendMessage("§c您不能给自己捐能量");
            return true;
        }
        if(data.getFaction()==null){
            p.sendMessage("§c您没有派系");
            return true;
        }
        FactionData factionData=QFaction.instance.getFaction(data.getFaction());
        if(!factionData.isAdmin(strings[0])){
            p.sendMessage("§c对方并不是帮派管理员");
            return true;
        }
        int i=0;
        try {
            i=Integer.valueOf(strings[1]);
        }catch (Exception e){
            return false;
        }
        if(!data.hasEnoughPower(i+QFaction.donatCost)){
            p.sendMessage("§c手续费: "+QFaction.donatCost+"\n总共: "+(i+QFaction.donatCost)+"\n您的余额: "+data.getPower());
            return true;
        }
        int cost=i+QFaction.donatCost;
        data.addPower(-cost);
        PlayerData ojb=qf.getPlayerData(strings[0]);
        ojb.addPower(i);
        p.sendMessage("§a成功给予对方"+i+"点能量 总共支付了"+cost+"点能量");
        Bukkit.getPlayer(strings[0]).sendMessage("§a您收到了"+i+"点能量\n来自玩家: "+p.getName()+"\n当前余额: "+ojb.getPower());
        return true;
    }
}
