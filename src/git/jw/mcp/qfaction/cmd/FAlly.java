package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FAlly extends BaseCmd {
    public FAlly(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 1);
    }

    @Override
    protected String getName() {
        return "fally";
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
        if(factionData.getAllies().size()>=5){
            p.sendMessage("§c您不能拥有超过5个同盟");
            return true;
        }
        p.sendMessage("§a已经成功单向添加盟友:"+strings[0]);
        factionData.addAlly(strings[0]);
        return true;
    }
}
