package org.stratum.protocol;

import org.litecoinpool.core.EnumValue;

public enum ErrorCode implements EnumValue<Integer> {
	UNKNOWN(20),
	JOB_NOT_FOUND(21),
	DUPLICATE_SHARE(22),
	LOW_DIFFICULTY_SHARE(23),
	UNAUTHORIZED_WORKER(24),
	NOT_SUBSCRIBED(25);
	
	private final Integer value;
	
	private ErrorCode(Integer value) {
		this.value = value;
	}
	
	public static ErrorCode errorCode(Integer value) {
    	return EnumValue.valueOf(ErrorCode.class, value, UNKNOWN);
    }
	
	@Override
	public Integer getValue() {
		return value;
	}
}
