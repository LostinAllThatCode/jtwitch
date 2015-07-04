package org.gdesign.twitch.api.exception;

public class TwitchAPINoPermissionException extends Throwable {
	private static final long serialVersionUID = -7290924044524674999L;

	public TwitchAPINoPermissionException(String message) {
		super(message);
	}
}
