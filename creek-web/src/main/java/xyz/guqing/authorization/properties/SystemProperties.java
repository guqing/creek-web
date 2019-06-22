package xyz.guqing.authorization.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.security.system")
public class SystemProperties {
    /**
     * Doc api disabled. (Default is true)
     */
    private boolean docDisabled = true;

    public boolean isDocDisabled() {
        return docDisabled;
    }

    public void setDocDisabled(boolean docDisabled) {
        this.docDisabled = docDisabled;
    }
}
