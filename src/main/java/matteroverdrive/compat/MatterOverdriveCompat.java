package matteroverdrive.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import matteroverdrive.compat.modules.CompatEnderIO;
import matteroverdrive.compat.modules.CompatExNihilo;
import matteroverdrive.compat.modules.CompatNEI;
import matteroverdrive.compat.modules.CompatThermalExpansion;
import matteroverdrive.compat.modules.computercraft.CompatComputerCraft;
import matteroverdrive.compat.modules.waila.CompatWaila;
import matteroverdrive.util.MOLog;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 *
 *
 * @author shadowfacts
 */
public class MatterOverdriveCompat {

	private static ArrayList<Class> modules = new ArrayList<Class>();

	//	Add additional modules that need to run on the Server and/or Client here
	public static void registerModules()
	{
		register(CompatThermalExpansion.class);
		register(CompatExNihilo.class);
		register(CompatEnderIO.class);
		register(CompatComputerCraft.class);
	}

	//Add additional modules that need to run ONLY on the Client
	public static void registerClientModules()
	{
		register(CompatNEI.class);
		register(CompatWaila.class);
	}

	private static boolean register(Class clazz)
	{
		if (clazz.isAnnotationPresent(Compat.class)) {
			Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
			if (Loader.isModLoaded(annotation.value())) {
				modules.add(clazz);
				return true;
			} else {
				MOLog.log(Level.INFO,"The mod %s was not loaded, skipping compatibility module.",annotation.value());
				return false;
			}
		}
		MOLog.log(Level.ERROR, "There was a problem register a compatibility module!");
		return false;
	}

	public static void preInit(FMLPreInitializationEvent event) {
		MOLog.log(Level.INFO,"Attempting to run pre-initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.PreInit.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						MOLog.log(Level.ERROR,e,"There was an error trying to invoke the pre-initialization method of the compatibility module for %1$s", annotation.value());
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void init(FMLInitializationEvent event) {
		MOLog.log(Level.INFO,"Attempting to run initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.Init.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						MOLog.log(Level.ERROR, e, "There was an error trying to invoke the initialization method of the compatibility module for %1$s", annotation.value());
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void postInit(FMLPostInitializationEvent event) {
		MOLog.log(Level.INFO,"Attempting to run post-initialization methods for all registered compatibility modules.");
		for (Class clazz : modules) {
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(Compat.PostInit.class) && Modifier.isStatic(m.getModifiers())) {
					try {
						m.invoke(null, event);
					} catch (ReflectiveOperationException e) {
						Compat annotation = (Compat)clazz.getAnnotation(Compat.class);
						MOLog.log(Level.ERROR, e, "There was an error trying to invoke the post-initialization method of the compatibility module for %1$s", annotation.value());
						e.printStackTrace();
					}
				}
			}
		}
	}

}
