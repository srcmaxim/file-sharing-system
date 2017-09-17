package github.srcmaxim.filesharingsystem.util;

import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Property holder -- property container for env and properties.
 */
public class PropertyHolder {

    /**
     * Gets first value for given keys from environment or properties.
     *
     * @param env {@link Environment} for getting properties
     * @param keys for getting a value.
     * @return  first not null value for key from OS environment (first) or from properties (second).
     *          If no such value, {@link NullPointerException} will be thrown.
     */
    public static String getVariableOrException(Environment env, String...keys) {
        String variable = getVariableOrNull(env, keys);
        if (variable == null) {
            throw new NullPointerException("No such variable: " + Arrays.toString(keys) + " in OS environment variables or properties.");
        }
        return variable;
    }

    /**
     * Gets first value for given keys from environment or properties.
     *
     * @param env {@link Environment} for getting properties.
     * @param keys for getting a value.
     * @return  first not null value for key from OS environment (first) or from properties (second).
     *          If no such value, returns null.
     */
    public static String getVariableOrNull(Environment env, String...keys) {
        return getVariableOrDefault(null, env, keys);
    }

    /**
     * Gets first value for given keys from environment or properties.
     *
     * @param env {@link Environment} for getting properties
     * @param keys for getting a value.
     * @return  first not null value for key from OS environment (first) or from properties (second).
     *          If no such value, returns default value.
     */
    public static String getVariableOrDefault(String aDefault, Environment env, String...keys) {
        String value;
        for (String key : keys) {
            value = getEnvironmentVariable(key);
            if (value != null) {
                return value;
            }
            value = getProperty(env, key);
            if (value != null) {
                return value;
            }
        }
        return aDefault;
    }

    private static String getProperty(Environment env, String key) {
        try {
            return env.getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getEnvironmentVariable(String key) {
        try {
            return System.getenv(key);
        } catch (Exception e) {
            return null;
        }
    }

}
