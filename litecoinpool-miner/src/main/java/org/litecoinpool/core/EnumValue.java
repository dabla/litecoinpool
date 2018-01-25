package org.litecoinpool.core;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;

public interface EnumValue<VALUE> {
	VALUE getValue();
	
	public static <VALUE, ENUM extends Enum<ENUM> & EnumValue<VALUE>> ENUM valueOf(Class<ENUM> enumType, VALUE value) {
		return Functions.valueOf(enumType, value, absent());
	}
	
	public static <VALUE, ENUM extends Enum<ENUM> & EnumValue<VALUE>> ENUM valueOf(Class<ENUM> enumType, VALUE value, ENUM defaultValue) {
		return Functions.valueOf(enumType, value, fromNullable(defaultValue));
	}
	
	static class Functions {
		private static <VALUE, ENUM extends Enum<ENUM> & EnumValue<VALUE>> ENUM valueOf(Class<ENUM> enumType, VALUE value, Optional<ENUM> defaultValue) {
	    	if (value != null) {
		        for (ENUM enumConstant : enumType.getEnumConstants()) {
		            if (value.equals(enumConstant.getValue())) {
		                return enumConstant;
		            }
		        }
	    	}
	    	
	    	return defaultValue.or(throwIllegalArgumentException(enumType, value));
	    }

		private static <VALUE, ENUM> Supplier<ENUM> throwIllegalArgumentException(final Class<ENUM> enumType, final VALUE value) {
			return new Supplier<ENUM>() {
				@Override
				public ENUM get() {
					throw new IllegalArgumentException("Unexistant value " + value + " for " + enumType.getSimpleName());
				}
			};
		}
	}
}
