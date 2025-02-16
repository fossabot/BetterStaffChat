/*
 * BetterStaffChat - PlayerListener.java
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

package dev.austech.betterstaffchat.bungeecord.listener;

import dev.austech.betterstaffchat.bungeecord.BetterStaffChatBungeeCord;
import dev.austech.betterstaffchat.bungeecord.util.LuckPermsUtil;
import dev.austech.betterstaffchat.bungeecord.util.StaffChatUtil;
import dev.austech.betterstaffchat.common.util.TextUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        if (event.getMessage().startsWith("/")) return;

        String prefix = BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.prefix");
        if (prefix.startsWith("/")) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        
        if (player.hasPermission("betterstaffchat.messages.send") && event.getMessage().startsWith(prefix) && event.getMessage().length() > prefix.length() && !prefix.equals("")) {
            event.setCancelled(true);

            if (BetterStaffChatBungeeCord.getInstance().getIgnoreStaffChat().contains(player.getUniqueId())) {
                player.sendMessage(TextUtil.colorizeToComponent("&cYour staff chat is currently disabled."));
                return;
            }

            StaffChatUtil.getInstance().broadcast(StaffChatUtil.getInstance().getFormattedMessage(player, event.getMessage().substring(prefix.length())), "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(player, ChatColor.stripColor(TextUtil.colorize(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.staffchat-format")
                    .replace("%player_name%", player.getName())
                    .replace("%message%", event.getMessage().substring(prefix.length()))
                    .replace("%server%", player.getServer().getInfo().getName()))));
        } else if (player.hasPermission("betterstaffchat.messages.send") && BetterStaffChatBungeeCord.getInstance().getToggledStaffChat().contains(player.getUniqueId())) {
            event.setCancelled(true);

            StaffChatUtil.getInstance().broadcast(StaffChatUtil.getInstance().getFormattedMessage(player, event.getMessage()), "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(player, ChatColor.stripColor(TextUtil.colorize(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.staffchat-format")
                    .replace("%player_name%", player.getName())
                    .replace("%message%", event.getMessage()))
                    .replace("%server%", player.getServer().getInfo().getName())));
        }
    }

    @EventHandler
    public void onPlayerPostLogin(PostLoginEvent event) {
        if (event.getPlayer().hasPermission("betterstaffchat.messages.join") && !BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.join").equals("")) {
            BetterStaffChatBungeeCord.getInstance().getProxy().getScheduler().schedule(BetterStaffChatBungeeCord.getInstance(), () -> {
                String message = TextUtil.colorize(
                        BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.join")
                                .replace("%player_name%", event.getPlayer().getName())
                                .replace("%server%", event.getPlayer().getServer().getInfo().getName())
                );

                if (BetterStaffChatBungeeCord.getInstance().getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
                    message = message.replace("%luckperms_prefix%", LuckPermsUtil.getPrefix(event.getPlayer())).replace("%luckperms_suffix%", LuckPermsUtil.getSuffix(event.getPlayer()));
                }

                message = TextUtil.colorize(message);

                StaffChatUtil.getInstance().broadcast(message, "betterstaffchat.messages.read");
                StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.staffchat-join")
                        .replace("%player_name%", event.getPlayer().getName())
                        .replace("%server%", event.getPlayer().getServer().getInfo().getName()))));
            }, 1, TimeUnit.SECONDS);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (event.getPlayer().hasPermission("betterstaffchat.messages.leave") && !BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.leave").equals("")) {
            String message = TextUtil.colorize(
                    BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.leave")
                            .replace("%player_name%", event.getPlayer().getName())
                            .replace("%server%", event.getPlayer().getServer().getInfo().getName())
            );

            if (BetterStaffChatBungeeCord.getInstance().getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
                message = message.replace("%luckperms_prefix%", LuckPermsUtil.getPrefix(event.getPlayer())).replace("%luckperms_suffix%", LuckPermsUtil.getSuffix(event.getPlayer()));
            }

            message = TextUtil.colorize(message);

            StaffChatUtil.getInstance().broadcast(message, "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.staffchat-leave")
                    .replace("%player_name%", event.getPlayer().getName())
                    .replace("%server%", event.getPlayer().getServer().getInfo().getName()))));
        }
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        if (event.getPlayer().hasPermission("betterstaffchat.messages.switch") && !BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.switch").equals("")) {
            if (event.getFrom() == null) return;

            String message = TextUtil.colorize(
                    BetterStaffChatBungeeCord.getInstance().getConfig().getString("staffchat.switch")
                            .replace("%player_name%", event.getPlayer().getName())
                            .replace("%from%", event.getFrom().getName())
                            .replace("%to%", event.getPlayer().getServer().getInfo().getName())
            );

            if (BetterStaffChatBungeeCord.getInstance().getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
                message = message.replace("%luckperms_prefix%", LuckPermsUtil.getPrefix(event.getPlayer())).replace("%luckperms_suffix%", LuckPermsUtil.getSuffix(event.getPlayer()));
            }

            message = TextUtil.colorize(message);

            StaffChatUtil.getInstance().broadcast(message, "betterstaffchat.messages.read");
            StaffChatUtil.getInstance().discord(event.getPlayer(), ChatColor.stripColor(TextUtil.colorize(BetterStaffChatBungeeCord.getInstance().getConfig().getString("discord.discord-messages.staffchat-switch")
                    .replace("%player_name%", event.getPlayer().getName()))
                    .replace("%from%", event.getFrom().getName())
                    .replace("%to%", event.getPlayer().getServer().getInfo().getName())));
        }
    }
}
