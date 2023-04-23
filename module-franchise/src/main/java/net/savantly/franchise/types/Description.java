package net.savantly.franchise.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Where;


@Property(editing = Editing.ENABLED, maxLength = Notes.MAX_LEN)
@PropertyLayout(named = "Description", multiLine = 2, hidden = Where.ALL_TABLES)
@Parameter(maxLength = Description.MAX_LEN)
@ParameterLayout(named = "Description", multiLine = 2)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    int MAX_LEN = 300;

}

