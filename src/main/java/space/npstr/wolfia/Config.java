/*
 * Copyright (C) 2017 Dennis Neufeld
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package space.npstr.wolfia;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * /**
 * Created by npstr on 18.11.2016
 * <p>
 * Contains the config values loaded from disk
 */
public class Config {

    public static final String PREFIX = "w.";

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    //avoid a (in this case unnecessary) gettocalypse by making all the values public final
    public static final Config C;

    static {
        Config c;
        try {
            c = new Config();
        } catch (final IOException e) {
            c = null;
            log.error("Could not load config files!" + e);
        }
        C = c;
    }

    //config
    public final boolean isDebug;

    //sneaky sneaky
    public final String discordToken;
    public final String redisAuth;
    public final String errorLogWebHook;
    public final String jdbcUrl;

    @SuppressWarnings(value = "unchecked")
    public Config() throws IOException {

        final File sneakyFile = new File("sneaky.yaml");
        final File configFile = new File("config.yaml");

        final Yaml yaml = new Yaml();

        final Map<String, Object> config = (Map<String, Object>) yaml.loadAs(new FileReader(configFile), Map.class);
        final Map<String, Object> sneaky = (Map<String, Object>) yaml.load(new FileReader(sneakyFile));
        //change nulls to empty strings
        config.keySet().forEach((String key) -> config.putIfAbsent(key, ""));
        sneaky.keySet().forEach((String key) -> sneaky.putIfAbsent(key, ""));

        //config stuff
        this.isDebug = (boolean) config.getOrDefault("debug", false);

        //sneaky stuff
        final Map<String, String> tokens = (Map) sneaky.get("discordToken");
        if (tokens != null)
            if (this.isDebug)
                this.discordToken = tokens.getOrDefault("debug", "");
            else
                this.discordToken = tokens.getOrDefault("prod", "");
        else
            this.discordToken = "";

        final Map<String, String> redis = (Map) sneaky.get("redisAuth");
        if (redis != null)
            if (this.isDebug)
                this.redisAuth = redis.getOrDefault("debug", "");
            else
                this.redisAuth = redis.getOrDefault("prod", "");
        else
            this.redisAuth = "";

        this.errorLogWebHook = (String) sneaky.getOrDefault("errorLogWebHook", "");

        this.jdbcUrl = (String) sneaky.getOrDefault("jdbcUrl", "");
    }
}