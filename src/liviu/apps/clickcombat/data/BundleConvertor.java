package liviu.apps.clickcombat.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import liviu.apps.clickcombat.interfaces.BundleAnnotation;
import android.os.Bundle;
import android.util.Log;

public class BundleConvertor {
	
	public BundleConvertor() {}
	
	public Bundle toBundle(Object obj){				
		Bundle b 		= new Bundle();		
		Class<?> c 		= obj.getClass();
		
		if(obj instanceof Domain){			
			Domain 	d 		= (Domain)obj;
			Field[] fields 	= c.getDeclaredFields();
			
			for(Field f : fields){
				f.setAccessible(true);
				Annotation[] annotations = f.getAnnotations();
				for(Annotation a : annotations){
					if(a instanceof BundleAnnotation){
						BundleAnnotation annotation = (BundleAnnotation)a;	
						try {													
							if(f.getType().equals(String.class))
								b.putString(annotation.bundleKey(),((String)f.get(d)));
							else if(f.getType().equals(int.class) || f.getType().equals(Integer.class))
								b.putInt(annotation.bundleKey(), f.getInt(d));
							else if(f.getType().equals(long.class) || f.getType().equals(Long.class))
								b.putLong(annotation.bundleKey(), f.getLong(d));
							else if(f.getType().equals(Double.class) || f.getType().equals(double.class))
								b.putDouble(annotation.bundleKey(), f.getDouble(d));
							else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
								b.putBoolean(annotation.bundleKey(), f.getBoolean(d));						
						}
						catch (IllegalArgumentException e) {
							e.printStackTrace();
						}
						catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						Log.e("LObject", "member name: " + f.getName() + " " + annotation.bundleKey() + " type: " + f.getType());
					}
				}
			}
		}
		return b;
	}
}
