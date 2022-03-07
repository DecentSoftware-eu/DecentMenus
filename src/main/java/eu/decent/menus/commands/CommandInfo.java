package eu.decent.menus.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

	String[] aliases() default {};

	String permission();

	boolean playerOnly() default false;

	int minArgs() default 0;

	String usage();

	String description();

}
