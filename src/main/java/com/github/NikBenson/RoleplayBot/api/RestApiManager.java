package com.github.NikBenson.RoleplayBot.api;

import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
@PropertySource(value = "classpath:application.properties")
public class RestApiManager extends SpringBootServletInitializer implements JSONConfigured {
	private static final Class<RestApiManager> applicationClass = RestApiManager.class;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

	@RequestMapping("/")
	String test() {
		return "test";
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public @NotNull File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.API_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		System.out.println("test1");
		SpringApplication app = new SpringApplication(applicationClass);
		System.out.println("test2");
		app.run(json.toJSONString());
		System.out.println("test3");
	}
}
