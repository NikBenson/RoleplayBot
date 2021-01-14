package com.github.NikBenson.RoleplayBot.configurations;

import java.util.HashMap;
import java.util.Map;

public class ModulesManager {
    private static Map<String, JSONConfigured> modules = new HashMap<>();

    public static void registerModule(String name, JSONConfigured module) {
        modules.put(name, module);
    }

    public static void activateModules(String... moduleNames) {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();

        for (String moduleName : moduleNames) {
            JSONConfigured module = modules.get(moduleName);
            configurationManager.registerConfiguration(module);
            try {
                configurationManager.load(module);
            } catch (Exception e) {
                System.out.println("could not load module: " + moduleName);
            }
        }
    }

    private ModulesManager() {}
}
