package org.gdesign.twitch.api.exception;

public class TwitchAPIUnauthorizedAccessException extends Throwable {
	private static final long serialVersionUID = -6021704355810768502L;

	public TwitchAPIUnauthorizedAccessException(String message) {
		super(message);
	}
}
