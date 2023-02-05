package com.github.bitsapling.sapling.objects.setting;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SiteBasicSettings implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String siteName;
    private String siteSubName;
    private String siteBaseURL;
    private String siteDescription;
    private List<String> siteKeywords;
    private boolean openRegistration;
    private boolean maintenanceMode;

}
