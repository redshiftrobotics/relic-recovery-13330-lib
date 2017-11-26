package org.redshiftrobotics.lib.config;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Xml;

import org.firstinspires.ftc.teamcode.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ariporad on 2017-11-25.
 */


public class ConfigurationManager {
    private class Config {
        @Nullable Config parent = null;

        String name;
        Map<String, String> strings;
        Map<String, Integer> ints;
        Map<String, Double> doubles;
        Map<String, Boolean> booleans;
        Map<String, Config> configs;

        Config(String name, Map<String, String> strings, Map<String, Integer> ints, Map<String, Double> doubles, Map<String, Boolean> booleans, Map<String, Config> configs) {
            this.name = name;
            this.strings = strings;
            this.ints = ints;
            this.doubles = doubles;
            this.booleans = booleans;
            this.configs = configs;

            Iterator<Config> configIterator = configs.values().iterator();

            while(configIterator.hasNext()) {
                configIterator.next().parent = this;
            }
        }
    }

    private static final int DEFAULT_INT = 0;
    private static final double DEFAULT_DOUBLE = 0.0;
    private static final String DEFAULT_STRING = null;
    private static final boolean DEFAULT_BOOLEAN = false;

    private static Map<String, Config> CONFIGS = null;

    private Config config;

    /***********************************************************************************************
     *                                         XML Parsing                                         *
     **********************************************************************************************/
    private Map<String, Config> readConfigFile(XmlResourceParser parser) throws XmlPullParserException, IOException {
        Map<String, Config> configs = new HashMap<>();

        parser.require(XmlResourceParser.START_TAG, null, "configFile");

        while (parser.next() != XmlResourceParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String tagname = parser.getName();

            if (tagname.equals("config")) {
                Config conf = readConfig(parser);
                configs.put(conf.name, conf);
            } else {
                skip(parser);
            }
        }

        return configs;
    }

    private Config readConfig(XmlResourceParser parser) throws XmlPullParserException, IOException {
        String name = parser.getAttributeValue(null, "name");
        Map<String, String> strings = new HashMap<>();
        Map<String, Integer> ints = new HashMap<>();
        Map<String, Double> doubles = new HashMap<>();
        Map<String, Boolean> booleans = new HashMap<>();
        Map<String, Config> configs = new HashMap<>();

        parser.require(XmlPullParser.START_TAG, null, "config");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String tagname = parser.getName();
            String subname = parser.getAttributeValue(null, "name");
            switch (tagname) {
                case "string":
                    strings.put(subname, readText(parser));
                    break;
                case "int":
                case "integer":
                    ints.put(subname, Integer.valueOf(readText(parser)));
                    break;
                case "double":
                    doubles.put(subname, Double.valueOf(readText(parser)));
                    break;
                case "bool":
                case "boolean":
                    booleans.put(subname, Boolean.valueOf(readText(parser)));
                    break;
                case "config":
                    Config conf = readConfig(parser);
                    configs.put(conf.name, conf);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new Config(name, strings, ints, doubles, booleans, configs);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    /***********************************************************************************************
     *                                       End XML Parsing                                      *
     **********************************************************************************************/

    private static ConfigurationManager sharedInstance = null;

    public static ConfigurationManager getSharedInstance() { return sharedInstance; }

    public static void setup(@NotNull String name) throws XmlPullParserException, IOException {
        sharedInstance = new ConfigurationManager(name);
    }

    private ConfigurationManager(@NotNull String name) throws XmlPullParserException, IOException {
        if (CONFIGS == null) {
            CONFIGS = readConfigFile(Resources.getSystem().getXml(R.xml.config));
        }

        if (!CONFIGS.containsKey(name)) throw new IllegalArgumentException("Unknown Config Name!");

        new ConfigurationManager(CONFIGS.get(name));
    }

    private ConfigurationManager(Config config) {
        this.config = config;
    }

    private <K, V> V getDefault(@NotNull Map<K, V> map, @NotNull K key, V defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    public ConfigurationManager getParent() {
        return new ConfigurationManager(config.parent);
    }

    public @NotNull ConfigurationManager getConfig(@NotNull String name) {
        if (!config.configs.containsKey(name)) throw new IllegalArgumentException("Unknown sub-config name!");
        return new ConfigurationManager(config.configs.get(name));
    }

    public String getString(String key) { return getString(key, DEFAULT_STRING); }
    public String getString(String key, String defaultValue) {
        return getDefault(config.strings, key, defaultValue);
    }


    public int getInt(String key) { return getInt(key, DEFAULT_INT); }
    public int getInt(String key, int defaultValue) {
        return getDefault(config.ints, key, defaultValue);
    }

    public double getDouble(String key) { return getDouble(key, DEFAULT_DOUBLE); }
    public double getDouble(String key, double defaultValue) {
        return getDefault(config.doubles, key, defaultValue);
    }

    public boolean getBoolean(String key) { return getBoolean(key, DEFAULT_BOOLEAN); }
    public boolean getBoolean(String key, boolean defaultValue) {
        return getDefault(config.booleans, key, defaultValue);
    }
}
