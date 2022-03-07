package eu.decent.menus.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface TabCompleteHandler {

	/**
	 * Handle Tab Complete.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return List of Tab Completed strings.
	 */
	List<String> handleTabComplete(CommandSender sender, String[] args);

}
