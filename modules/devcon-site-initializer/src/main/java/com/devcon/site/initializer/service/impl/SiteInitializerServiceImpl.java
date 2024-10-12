package com.devcon.site.initializer.service.impl;

import com.devcon.site.initializer.override.SynchronizeSiteInitializerMVCActionCommandOverride;
import com.devcon.site.initializer.service.SiteInitializerService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.PortletPreferenceValue;
import com.liferay.portal.kernel.service.PortletPreferenceValueLocalService;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

@Component(service = SiteInitializerService.class)
public class SiteInitializerServiceImpl implements SiteInitializerService {

    public static final String HEADER_MENU_DDM_TEMPLATE_KEY = "DEVCON_HEADER_NAV";
    public static final String HEADER_SITE_NAVIGATION_MENU_NAME = "DevCon Header Navigation";

    public static final String FOOTER_MENU_DDM_TEMPLATE_KEY = "DEVCON_FOOTER_NAV";
    public static final String FOOTER_SITE_NAVIGATION_MENU_NAME = "DevCon Footer Navigation";

    @Override
    public void verifySiteNavigationMenuIds(long groupId) {
        verifySiteNavigationMenuId(groupId, HEADER_MENU_DDM_TEMPLATE_KEY, HEADER_SITE_NAVIGATION_MENU_NAME);
        verifySiteNavigationMenuId(groupId, FOOTER_MENU_DDM_TEMPLATE_KEY, FOOTER_SITE_NAVIGATION_MENU_NAME);
    }

    private void verifySiteNavigationMenuId(long groupId, String ddmTemplateKey, String navigationMenuName) {
        String displayStylePreference = "ddmTemplate_" + ddmTemplateKey;
        DynamicQuery displayStyleQuery = portletPreferenceValueLocalService.dynamicQuery();
        displayStyleQuery.add(RestrictionsFactoryUtil.eq("name", "displayStyle"));
        displayStyleQuery.add(RestrictionsFactoryUtil.eq("smallValue", displayStylePreference));
        List<PortletPreferenceValue> displayStylePrefValues = portletPreferenceValueLocalService.dynamicQuery(displayStyleQuery);
        if (ListUtil.isNotEmpty(displayStylePrefValues)) {
            for (PortletPreferenceValue displayStylePrefValue : displayStylePrefValues) {
                long companyId = displayStylePrefValue.getCompanyId();
                long portletPreferencesId = displayStylePrefValue.getPortletPreferencesId();
                DynamicQuery navMenuQuery = portletPreferenceValueLocalService.dynamicQuery();
                navMenuQuery.add(RestrictionsFactoryUtil.eq("name", "siteNavigationMenuId"));
                navMenuQuery.add(RestrictionsFactoryUtil.eq("portletPreferencesId", portletPreferencesId));
                List<PortletPreferenceValue> navMenuPrefValues = portletPreferenceValueLocalService.dynamicQuery(navMenuQuery);
                if (ListUtil.isNotEmpty(navMenuPrefValues)) {
                    for (PortletPreferenceValue navMenuPrefValue : navMenuPrefValues) {
                        if (Validator.isBlank(navMenuPrefValue.getValue())) {
                            long siteNavigationMenuId = getSiteNavigationMenuId(groupId, navigationMenuName);
                            navMenuPrefValue.setValue(String.valueOf(siteNavigationMenuId));
                            navMenuPrefValue = portletPreferenceValueLocalService.updatePortletPreferenceValue(navMenuPrefValue);
                            _log.info(String.format("Updated PortletPreferenceValue #%d (%s=%s) for PortletPreference #%d.",
                                    navMenuPrefValue.getPortletPreferencesId(), "siteNavigationMenuId", siteNavigationMenuId, portletPreferencesId));
                        }
                    }
                } else {
                    long siteNavigationMenuId = getSiteNavigationMenuId(groupId, navigationMenuName);
                    long portletPreferenceValueId = counterLocalService.increment();
                    PortletPreferenceValue portletPreferenceValue = portletPreferenceValueLocalService.createPortletPreferenceValue(portletPreferenceValueId);
                    portletPreferenceValue.setCompanyId(companyId);
                    portletPreferenceValue.setPortletPreferencesId(portletPreferencesId);
                    portletPreferenceValue.setName("siteNavigationMenuId");
                    portletPreferenceValue.setValue(String.valueOf(siteNavigationMenuId));
                    portletPreferenceValue = portletPreferenceValueLocalService.updatePortletPreferenceValue(portletPreferenceValue);
                    _log.info(String.format("Saved PortletPreferenceValue #%d (%s=%s) for PortletPreference #%d.",
                            portletPreferenceValue.getPortletPreferenceValueId(), "siteNavigationMenuId", siteNavigationMenuId, portletPreferencesId));
                }
            }
        }
    }

    private long getSiteNavigationMenuId(long groupId, String navMenuName) {
        SiteNavigationMenu siteNavigationMenu = siteNavigationMenuLocalService.fetchSiteNavigationMenuByName(groupId, navMenuName);
        return siteNavigationMenu != null ? siteNavigationMenu.getSiteNavigationMenuId() : 0;
    }

    @Reference
    private CounterLocalService counterLocalService;
    @Reference
    private SiteNavigationMenuLocalService siteNavigationMenuLocalService;
    @Reference
    private PortletPreferenceValueLocalService portletPreferenceValueLocalService;

    private static final Log _log = LogFactoryUtil.getLog(SynchronizeSiteInitializerMVCActionCommandOverride.class);
}