package com.tibco.automation.oiag.common.components.formbean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface FormElement {
	public enum Type {

		textbox, selectbox, checkbox, file,
		/**
		 * Radio button(s)
		 */
		optionbox, textarea, multiselectbox,
		/**
		 * HTML element other than form field
		 */
		text, toggletbox;
	}

	public Type fieldType() default Type.textbox;

	public String fieldLoc();
	
	public String attributeLoc() default  "";
	
	public String defaultValue() default "";
	public int defaultIntValue() default 0;

	public boolean required() default false;

	public int order() default Integer.MAX_VALUE;
}
