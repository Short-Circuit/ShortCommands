package com.shortcircuit.shortcommands.exceptions;


/**
 * @author ShortCircuit908
 */
public class NoPermissionException extends Exception {
	private static final long serialVersionUID = -2816353388681126986L;
	private final String required_permissions;

	public NoPermissionException(){
		this(null);
	}

	public NoPermissionException(String required_permissions) {
		super("I'm sorry, but you do not have permission to perform this command. Please contact " +
				"the server administrators if you believe this is in error.");
		this.required_permissions = required_permissions;
	}

	/**
	 * Gets the missing required permissions
	 *
	 * @return The missing permissions
	 */
	public String getRequiredPermissions() {
		return required_permissions;
	}
}
