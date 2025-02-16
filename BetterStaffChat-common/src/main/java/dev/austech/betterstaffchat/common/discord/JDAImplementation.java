/*
 * BetterStaffChat - JDAImplementation.java
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

package dev.austech.betterstaffchat.common.discord;

import dev.austech.betterstaffchat.common.util.AbstractStaffChatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public class JDAImplementation {
    private final JDA jda;
    @Getter(AccessLevel.PROTECTED) private final AbstractStaffChatUtil staffChatUtil;

    public JDAImplementation(JDA jda, AbstractStaffChatUtil staffChatUtil) {
        this.staffChatUtil = staffChatUtil;
        this.jda = jda;

        jda.addEventListener(new MessageListener(staffChatUtil));
    }

    public void sendMessage(String guild, String channel, String message) {
        jda.getGuildById(guild).getTextChannelById(channel).sendMessage(message).queue();
    }

    public void sendEmbed(String guild, String channel, DiscordWebhook.EmbedObject embed) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(embed.getColor());
        embedBuilder.setDescription(embed.getDescription());

        if (embed.getFooter() != null) {
            embedBuilder.setFooter(embed.getFooter().getText(), embed.getFooter().getIconUrl());
        }
        jda.getGuildById(guild).getTextChannelById(channel).sendMessage(embedBuilder.build()).queue();
    }

    public JDA asJda() {
        return this.jda;
    }
}
