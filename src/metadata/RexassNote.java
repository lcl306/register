package metadata;

import java.lang.annotation.Documented;

@Documented
public @interface RexassNote {
	
	String assignTo() default "[none]";
	int severity() default 0;

}
