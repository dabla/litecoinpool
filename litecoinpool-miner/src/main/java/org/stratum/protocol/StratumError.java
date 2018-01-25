package org.stratum.protocol;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@JsonSerialize(using=StratumErrorSerializer.class)
@JsonDeserialize(using=StratumErrorDeserializer.class)
public class StratumError extends RuntimeException {
	private final ErrorCode errorCode;
	private final String traceback;
	
	public StratumError(ErrorCode errorCode, String message, String traceback) {
		super(message);
		this.errorCode = errorCode;
		this.traceback = traceback;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	public String getTraceback() {
		return traceback;
	}
}
