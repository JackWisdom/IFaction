package git.jw.mcp.qfaction.cmd.admin;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class FPower extends BaseCmd {
    public FPower(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(true);
        setPlayerOnly(false);
        setArgLength((short) 2);
    }
    @Override
    protected String getName() {
        return "fpa";
    }

    @Override
    public boolean exec(CommandSender commandSender, String[] strings) {
        if(QFaction.instance.getPlayerData(strings[0])==null){
            commandSender.sendMessage("玩家不在线");
            return true;
        }
        QFaction.instance.getPlayerData(strings[0]).setPower(Integer.valueOf(strings[1]));
        commandSender.sendMessage(strings[0]+"now"+Integer.valueOf(strings[1]));
        return true;
    }
}
