package com.github.NikBenson.RoleplayBot.modules;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {
	ClassLoader classLoader;

	public ModuleLoader(File modulesPath) {
		classLoader = getClass().getClassLoader();

		loadAll(modulesPath);
	}

	private void loadAll(File modulesPath) {
		File[] modules = modulesPath.listFiles();
		if(modules != null) {
			for (File module : modules) {
				createClassLoader(module);
				tryLoad(module);
			}
		}
	}

	private void createClassLoader(File modulePath) {
		URL url = createJarURL(modulePath);
		classLoader = URLClassLoader.newInstance(new URL[]{url}, classLoader);
	}
	private URL createJarURL(File path) {
		try {
			return path.toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private void tryLoad(File file) {
		try {
			load(file);
		} catch (IOException e) {
			System.out.printf("Could not load %s%n", file.getName());
		}
	}
	private void load(File file) throws IOException {
		JarFile jarFile = new JarFile(file.getAbsolutePath());
		Enumeration<JarEntry> entries = jarFile.entries();
		while(entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			loadIfClass(entry);
		}

		jarFile.close();
	}
	private void loadIfClass(JarEntry entry) {
		if(!entry.isDirectory() && entry.getName().endsWith(".class")){
			loadEntry(entry);
		}
	}
	private void loadEntry(JarEntry entry) {
		String name = entry.getName();
		String className = name.substring(0, name.length() - 6);
		className = className.replace('/', '.');

		tryLoadClass(className);
	}
	private void tryLoadClass(String name) {
		try {
			loadClass(name);
		} catch (ClassNotFoundException e) {
			System.out.printf("Could not load %s%n", name);
			e.printStackTrace();
		} catch (NoClassDefFoundError ignored) {}
	}
	private void loadClass(String name) throws ClassNotFoundException, NoClassDefFoundError {
		Class<?> uncheckedClass = classLoader.loadClass(name);

		checkClassAndLoad(uncheckedClass);
	}
	private void checkClassAndLoad(Class<?> toCheck) {
		if(RoleplayBotModule.class.isAssignableFrom(toCheck)) {
			ModulesManager.registerModule((Class<? extends RoleplayBotModule>) toCheck);
		}
	}
}
