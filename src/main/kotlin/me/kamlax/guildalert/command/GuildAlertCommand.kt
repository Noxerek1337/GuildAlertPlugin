package me.kamlax.guildalert.command

import me.kamlax.guildalert.extension.msg
import me.kamlax.guildalert.extension.sendUsage
import me.kamlax.guildalert.helper.ActionBarHelper
import me.kamlax.guildalert.helper.ChatColorHelper
import net.dzikoysk.funnyguilds.basic.user.User
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class GuildAlertCommand : CommandExecutor {
    private val timeMap = hashMapOf<UUID, Long>()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val user = User.get(sender)
            if (!user.hasGuild()) {
                sender.msg("&4Blad: &cNie posiadasz gildii!")
                return true
            }
            if (!user.isOwner) {
                sender.msg("&4Blad: &cNie jestes zalozycielem gildii!")
                return true
            }
            if (args.size < 2) {
                sender.sendUsage()
                return true
            }
            if (timeMap[sender.uniqueId] ?: 0 < System.currentTimeMillis()) {
                timeMap[sender.uniqueId] = System.currentTimeMillis() + 30000L
                val message = ChatColorHelper.fixColor(StringUtils.join(args, " ", 1, args.size))
                when (args[0].toLowerCase()) {
                    "chat" -> user.guild.onlineMembers.forEach { it.player.msg(message) }
                    "title" -> user.guild.onlineMembers.forEach { it.player.sendTitle(message, "") }
                    "subtitle" -> user.guild.onlineMembers.forEach { it.player.sendTitle("", message) }
                    "actionbar" -> user.guild.onlineMembers.forEach { ActionBarHelper.sendActionBar(it.player, message) }
                }
            } else sender.msg("&4Blad: &cTej komendy mozesz uzywac co 30 sekund!")
        }
        return true
    }
}