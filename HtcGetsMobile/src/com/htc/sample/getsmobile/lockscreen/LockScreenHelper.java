/**
 * 
 */
package com.htc.sample.getsmobile.lockscreen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.util.Log;

/**
 * Checks for the presence of the lock screen APIs and enables the right services.
 *
 */
public class LockScreenHelper {

	private static final String LOG_TAG = "LockScreenHelper";

	private static final String API_CLASS_NAME = "com.htc.lockscreen.fusion.open.SimpleIdleScreenService";

	private static final String LEGACY_API_CLASS_NAME = "com.htc.lockscreen.idlescreen.IdleScreenService";

	/**
	 * Enables any supported lock screen services.
	 * 
	 * @param context Context used to access the PackageManager
	 * @return boolean true if a supported lock screen service was enabled, false otherwise
	 */
	public static boolean enable(final Context context) {
		
		// Check if this version of HTC Sense offers the Lock Screen API
		if ( !isLockScreenOfferedBySenseVersion() ) {
			return false;
		}
		
		// Determine lock screen API to use.
		final Class<?> lockScreenBase = getSupportedLockScreenBase();
		
		// Enable appropriate lock screen services.
		boolean enabledAService = false;
		for ( final ServiceInfo service : getAllServices(context) ) {
			if ( !service.enabled && isLoadableAndAssignableTo(service.name, lockScreenBase) ) {
				enableService(context, service.name);
				enabledAService = true;
			}
		}
		
		return enabledAService;
	}
	
	private static boolean isLockScreenOfferedBySenseVersion() {
		final String senseVersion = getSenseVersion();
		Log.d(LOG_TAG, "isLockScreenOfferedBySenseVersion senseVersion = " + getSenseVersion());

		// Not a recent HTC Sense device.
		if ( null == senseVersion ) {
			return false;
		}

		// A lite version.
		if ( senseVersion.endsWith("a") ) {
			return false;
		}
		
		// Find the first number, e.g. the 4 in 4.0 or 3 in 3.5
	    final Pattern p = Pattern.compile("\\d+");
	    final Matcher m = p.matcher(senseVersion);
	    if ( !m.find() ) {
	    	return false;
	    }
	    
	    final String majorVersionString = m.group();
	    try {
			final int majorVersion = Integer.parseInt(majorVersionString);
			
			// Needs to be 4 or higher.
			return majorVersion >= 4;
			
		} catch (NumberFormatException e) {
			return false;
		}
	    
	}
	
	private static String getSenseVersion() {
		try {
            final Class<?> buildClass = Class.forName("com.htc.version.HtcBuild");            
            final Object buildObject = buildClass.newInstance();            
            final Method getStringValueMethod = buildClass.getMethod("getStringValue", String.class);
            final String senseVersionKeyArg = "htc.sense.version";            
            final String senseVersion = (String) getStringValueMethod.invoke(buildObject, senseVersionKeyArg);            
            return senseVersion;
            
        } catch (final ClassNotFoundException e) {
        	return null;
        } catch (IllegalArgumentException e) {
        	return null;
		} catch (IllegalAccessException e) {
        	return null;
		} catch (InvocationTargetException e) {
        	return null;
		} catch (NoSuchMethodException e) {
        	return null;
		} catch (InstantiationException e) {
        	return null;
		}
	}
	
	private static Class<?> getSupportedLockScreenBase() {

		try {
            return Class.forName(API_CLASS_NAME);
        } catch (final ClassNotFoundException e) {
        	// Do nothing. Try legacy preview version.
        }
		
        try {
            return Class.forName(LEGACY_API_CLASS_NAME);
        } catch (final ClassNotFoundException e) {
        	// No lock screen supported.
        	return null;
        }

	}
	
	private static ServiceInfo[] getAllServices(final Context context) {

		final PackageManager pm = (PackageManager) context.getPackageManager();
		try {			
			final ServiceInfo[] services = pm.getPackageInfo(context.getPackageName(), 
					PackageManager.GET_SERVICES | PackageManager.GET_DISABLED_COMPONENTS).services;
			if ( null != services ) {
				return services;
			}
		} catch (NameNotFoundException e) {
			// Couldn't read app's services.
		}		
		return new ServiceInfo[] {};
	}
	
	private static void enableService(final Context context, final String serviceName) {

		final PackageManager pm = (PackageManager) context.getPackageManager();
		final ComponentName componentName = new ComponentName(context.getPackageName(), serviceName);	
		Log.d(LOG_TAG, "Enabling service: " + componentName);	
		pm.setComponentEnabledSetting(componentName, 
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);			

	}
	
	private static boolean isLoadableAndAssignableTo(final String className, final Class<?> baseClass) {

		if ( null == baseClass ) {
			return false;
		}

		try {
			final Class<?> serviceClass = Class.forName(className);
			return baseClass.isAssignableFrom(serviceClass);
		} catch (ClassNotFoundException e) {
			// Class cannot be loaded.
			return false;
		}

	}

}
