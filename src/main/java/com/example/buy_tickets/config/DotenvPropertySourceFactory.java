package com.example.buy_tickets.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class DotenvPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        Properties props = new Properties();
        Path path = Path.of(resource.getResource().getURL().getPath());

        if (Files.exists(path)) {
            try {
                Files.lines(path)
                        .filter(line -> !line.trim().isEmpty())
                        .filter(line -> !line.trim().startsWith("#"))
                        .forEach(line -> {
                            int separator = line.indexOf('=');
                            if (separator > 0) {
                                String key = line.substring(0, separator).trim();
                                String value = line.substring(separator + 1).trim();
                                props.setProperty(key, value);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException("Unable to load .env file", e);
            }
        }

        return new PropertiesPropertySource(name != null ? name : "dotenv", props);
    }
}
