package eu.decent.menus.api.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import eu.decent.menus.Config;
import eu.decent.menus.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class DecentCommand extends Command implements CommandBase {

	protected final Set<CommandBase> subCommands = Sets.newHashSet();
	protected final CommandInfo info;
	protected final String usage;

	public DecentCommand(String name, String usage) {
		super(name);
		this.info = getClass().getAnnotation(CommandInfo.class);
		this.usage = usage;
		if (info == null) {
			throw new RuntimeException(String.format("Command %s is not annotated with @CommandInfo.", name));
		}
		this.setAliases(Arrays.asList(info.aliases()));
	}

	protected Set<CommandBase> getSubCommands() {
		return subCommands;
	}

	@Override
	public CommandBase addSubCommand(@NotNull CommandBase commandBase) {
		if (subCommands.add(commandBase)) {
			return commandBase;
		}
		return null;
	}

	/**
	 * Handle the Command.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return Boolean whether the execution was successful.
	 */
	protected final boolean handle(CommandSender sender, String[] args) throws DecentCommandException {
		if (!CommandValidator.canExecute(sender, this)) {
			return true;
		}

		if (args.length != 0) {
			for (CommandBase subCommand : subCommands) {
				if (CommandValidator.isIdentifier(args[0], subCommand)) {
					final String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
					if (subCommandArgs.length < subCommand.getMinArgs()) {
						if (usage != null) {
							Common.tell(sender, usage);
						}
						return true;
					}
					return ((DecentCommand) subCommand).handle(sender, subCommandArgs);
				}
			}
		} else if (getMinArgs() > 0) {
			Config.tell(sender, Config.USAGE);
			return true;
		}

		return this.getCommandHandler().handle(sender, args);
	}

	/**
	 * Handle Tab Complete of the Command.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return List of tab completed Strings.
	 */
	protected final List<String> handeTabComplete(CommandSender sender, String[] args) {
		if (getPermission() != null && !sender.hasPermission(getPermission())) {
			return ImmutableList.of();
		}

		if (args.length == 1) {
			List<String> matches = Lists.newLinkedList();
			List<String> subs = subCommands.stream()
					.map(CommandBase::getName)
					.collect(Collectors.toList());
			subCommands.forEach(sub ->
					subs.addAll(Lists.newArrayList(sub.getAliases()))
			);

			StringUtil.copyPartialMatches(args[0], subs, matches);

			if (!matches.isEmpty()) {
				Collections.sort(matches);
				return matches;
			}
		} else if (args.length > 1) {
			for (CommandBase subCommand : subCommands) {
				if (CommandValidator.isIdentifier(args[0], subCommand)) {
					return ((DecentCommand) subCommand).handeTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
				}
			}
		}

		if (this.getTabCompleteHandler() == null) {
			return ImmutableList.of();
		}

		return this.getTabCompleteHandler().handleTabComplete(sender, args);
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
		try {
			return this.handle(sender, args);
		} catch (DecentCommandException e) {
			Common.tell(sender, e.getMessage());
			return true;
		}
	}

	@NotNull
	@Override
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
		return handeTabComplete(sender, args);
	}

	@Nullable
	@Override
	public String getPermission() {
		return info.permission();
	}

	@Override
	public boolean isPlayerOnly() {
		return info.playerOnly();
	}

	@Override
	public int getMinArgs() {
		return info.minArgs();
	}

	@NotNull
	@Override
	public String getUsage() {
		return info.usage();
	}

	@NotNull
	@Override
	public String getDescription() {
		return info.description();
	}

}
