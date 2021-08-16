package eu.vortexgg.rpg.util;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static void setField(Class<?> clazz, String field, Object value) {
	try {
	    Field fieldObject = clazz.getDeclaredField(field);
	    fieldObject.setAccessible(true);
	    fieldObject.set(clazz, value);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
