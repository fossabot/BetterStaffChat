/*
 * BetterStaffChat - ToggleStaffChatCommand.java
 * Copyright (C) 2021 AusTech Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.austech.betterstaffchat.spigot.command;

import com.google.common.collect.Lists;
import dev.austech.betterstaffchat.common.util.TextUtil;
import dev.austech.betterstaffchat.spigot.BetterStaffChatSpigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleStaffChatCommand extends Command {
    public ToggleStaffChatCommand() {
        super("togglestaffchat", "Allows you to toggle whether or not all your messages to to staffchat.", "", Lists.newArrayList("tsc", "sct", "staffchattoggle"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!sender.hasPermission("betterstaffchat.togglestaffchat")) {
            sender.sendMessage(TextUtil.colorize("&cNo permission."));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(TextUtil.colorize("&cThis command can only be used by players."));
            return true;
        }

        if (sender instanceof Player && BetterStaffChatSpigot.getInstance().getIgnoreStaffChat().contains(((Player) sender).getUniqueId())) {
            sender.sendMessage(TextUtil.colorize("&cYour staff chat is currently disabled."));
            return true;
        }

        if (BetterStaffChatSpigot.getInstance().getToggledStaffChat().contains(((Player) sender).getUniqueId())) {
            BetterStaffChatSpigot.getInstance().getToggledStaffChat().remove(((Player) sender).getUniqueId());
        } else {
            BetterStaffChatSpigot.getInstance().getToggledStaffChat().add(((Player) sender).getUniqueId());
        }

        sender.sendMessage(TextUtil.colorize(BetterStaffChatSpigot.getInstance().getToggledStaffChat().contains(((Player) sender).getUniqueId()) ?
                BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.toggle-on") :
                        BetterStaffChatSpigot.getInstance().getConfig().getString("staffchat.toggle-off"))
        );

        return true;
    }
}
