package utemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * µTemplate. A microscopic templating engine.
 * @author Fredrik Rambris (fredrik@rambris.com)
 */
public class Utemplate {
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[\\w.-]+$");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([\\w.-]+)}");
    private Map<String, String> data = new HashMap<>();
    private boolean showMissing = false;

    public Utemplate() {
    }

    public Utemplate(Map<String, String> data) {
        data.forEach((name, value) -> {
            if (!VALID_NAME_PATTERN.matcher(name).matches()) throw new TemplateException("Invalid name: " + name);
            data.put(name, value);
        });
    }

    /**
     * Set template variable
     * @param name
     * @param value
     * @return
     */
    public Utemplate with(String name, Object value) {
        if (value==null || !VALID_NAME_PATTERN.matcher(name).matches()) throw new TemplateException("Invalid name: " + name);
        data.put(name, value.toString().replaceAll("\\$", "\\\\\\$"));
        return this;
    }

    /**
     * Expand tags where there is no variable with [missing:name]
     * @return
     */
    public Utemplate showMissing() {
        this.showMissing = true;
        return this;
    }

    public Utemplate showMissing(boolean showMissing) {
        this.showMissing = showMissing;
        return this;
    }

    /**
     * Render given template
     * @param template
     * @return
     */
    public String render(String template) {
        var m = VARIABLE_PATTERN.matcher(template);
        return m.replaceAll(r -> {
            var name = r.group(1);
            var value = data.get(name);
            return value != null ? value : (showMissing ? "[missing:" + name + "]": "");
        });
    }

    public static class TemplateException extends RuntimeException {
        public TemplateException(String message) {
            super(message);
        }
    }
}
