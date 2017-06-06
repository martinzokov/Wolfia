/*
 * Copyright (C) 2017 Dennis Neufeld
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package space.npstr.wolfia.commands.util;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import space.npstr.wolfia.Config;
import space.npstr.wolfia.Wolfia;
import space.npstr.wolfia.commands.CommandParser;
import space.npstr.wolfia.commands.ICommand;
import space.npstr.wolfia.utils.App;

import java.util.function.Consumer;

/**
 * Created by npstr on 09.09.2016
 */
public class HelpCommand implements ICommand {

    public final static String COMMAND = "help";

    public HelpCommand() {
    }

    @Override
    public void execute(final CommandParser.CommandContainer commandInfo) {
        final MessageReceivedEvent e = commandInfo.event;
        final TextChannel channel = e.getTextChannel();
        final String help = String.format("Hi %s,\nyou can find %s's **documentation** and a **full list of commands** under\n<%s>"
                        + "\n\n**To invite the bot to your server please follow this link**:\n<%s>"
                        + "\n\nDrop by the Wolfia Lounge to play games, get support, leave feedback, and find like-minded individuals:\n<%s>"
                        + "\n\nCode open sourced on Github:\n<%s>"
                        + "\n\nCreated and hosted by Napster:\n<%s>",
                e.getAuthor().getName(), e.getJDA().getSelfUser().getName(), App.WEBSITE, App.INVITE_LINK, App.WOLFIA_LOUNGE_INVITE, "https://github.com/napstr/wolfia", "https://npstr.space");

        Consumer<Message> onSuccess = null;
        Consumer<Throwable> onFail = null;
        if (channel.canTalk()) {
            onSuccess = m -> Wolfia.handleOutputMessage(channel, "%s, sent you a PM with the help!", e.getAuthor().getAsMention());
            onFail = t -> Wolfia.handleOutputMessage(channel, "%s, cannot send you a PM with the help. Please unblock me or change your privacy settings.", e.getAuthor().getAsMention());
        }

        Wolfia.handlePrivateOutputMessage(e.getAuthor().getIdLong(), onSuccess, onFail, "%s", help);
    }

    @Override
    public String help() {
        return "```usage: " + Config.PREFIX + COMMAND + " (<command>)\nto see all available commands for this channel "
                + "or see the help for a specific command```";
    }
}
