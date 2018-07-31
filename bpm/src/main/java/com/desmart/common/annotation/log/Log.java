package com.desmart.common.annotation.log;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Log {

	 String description() default ""; //方法描述
	 
}
