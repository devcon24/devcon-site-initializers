package com.devcon.site.initializer.service.impl;

import com.devcon.site.initializer.override.SynchronizeSiteInitializerMVCActionCommandOverride;
import com.devcon.site.initializer.service.SiteInitializerService;
import com.liferay.client.extension.model.ClientExtensionEntryRel;
import com.liferay.client.extension.service.ClientExtensionEntryRelLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.PortletPreferenceValue;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.PortletPreferenceValueLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.site.navigation.constants.SiteNavigationMenuPortletKeys;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldLocalService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(service = SiteInitializerService.class)
public class SiteInitializerServiceImpl implements SiteInitializerService {

    public static final String CX_TYPE = "themeCSS";
    public static final String CX_EXTERNAL_REFERENCE_CODE = "LXC:devcon-site-initializer-theme-css";

    // ---------------------------- Site Navigation Menu ---------------------------------------------------------------

    @Override
    public void verifySiteNavigationMenuIds(long groupId) {

        // Site Navigation Menu IDs Map
        Map<String, String> siteNavigationMenuIdMap = new HashMap<>();
        List<SiteNavigationMenu> siteNavigationMenus = siteNavigationMenuLocalService.getSiteNavigationMenus(groupId);
        for (SiteNavigationMenu siteNavigationMenu : siteNavigationMenus) {
            String menuName = siteNavigationMenu.getName();
            String placeholder = String.format("[$SITE_NAVIGATION_MENU_ID:%s$]", menuName);
            siteNavigationMenuIdMap.put(placeholder, String.valueOf(siteNavigationMenu.getSiteNavigationMenuId()));
        }

        // Master Pages
        List<LayoutPageTemplateEntry> layoutPageTemplateEntries = layoutPageTemplateEntryLocalService.getLayoutPageTemplateEntries(groupId);
        for (LayoutPageTemplateEntry layoutPageTemplateEntry : layoutPageTemplateEntries) {
            List<PortletPreferences> portletPreferences = portletPreferencesLocalService.getPortletPreferences(0, PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layoutPageTemplateEntry.getPlid());
            for (PortletPreferences portletPreference : portletPreferences) {
                String portletId = portletPreference.getPortletId();
                long companyId = portletPreference.getCompanyId();
                // Navigation Menu Portlet Preferences
                if (portletId.startsWith(SiteNavigationMenuPortletKeys.SITE_NAVIGATION_MENU)) {
                    // Group-Level Portlet Preferences
                    PortletPreferences sitePreferences = portletPreferencesLocalService.fetchPortletPreferences(groupId, PortletKeys.PREFS_OWNER_TYPE_LAYOUT, 0, portletId);
                    if (sitePreferences == null) {
                        sitePreferences = portletPreferencesLocalService.addPortletPreferences(
                                companyId,
                                PortletKeys.PREFS_OWNER_TYPE_LAYOUT,
                                0,
                                portletPreference.getPlid(),
                                portletPreference.getPortletId(),
                                null,
                                null
                        );
                        _log.info(String.format("Saved PortletPreferences #%d.", sitePreferences.getPortletPreferencesId()));
                    }
                    long portletPreferencesId = sitePreferences.getPortletPreferencesId();
                    // Update Portlet Preferences
                    List<PortletPreferenceValue> originalPrefValues = getPortletPreferenceValues(portletPreference.getPortletPreferencesId());
                    for (PortletPreferenceValue originalPrefValue : originalPrefValues) {
                        String preferenceValue = originalPrefValue.getValue();
                        if ("siteNavigationMenuId".equals(originalPrefValue.getName())) {
                            if (siteNavigationMenuIdMap.containsKey(preferenceValue)) {
                                String mappedValue = siteNavigationMenuIdMap.get(preferenceValue);
                                originalPrefValue.setValue(mappedValue);
                                originalPrefValue = portletPreferenceValueLocalService.updatePortletPreferenceValue(originalPrefValue);
                                _log.info(String.format("Updated siteNavigationMenuId PortletPreferenceValue from %s to %s.", preferenceValue, mappedValue));
                            }
                        }
                        PortletPreferenceValue sitePrefValue = getPortletPreferenceValue(portletPreferencesId, originalPrefValue.getName(), originalPrefValue.getValue());
                        if (sitePrefValue == null) {
                            sitePrefValue = portletPreferenceValueLocalService.createPortletPreferenceValue(counterLocalService.increment());
                            sitePrefValue.setCompanyId(companyId);
                            sitePrefValue.setPortletPreferencesId(portletPreferencesId);
                            sitePrefValue.setName(originalPrefValue.getName());
                            sitePrefValue.setValue(originalPrefValue.getValue());
                            sitePrefValue = portletPreferenceValueLocalService.updatePortletPreferenceValue(sitePrefValue);
                            _log.info(String.format("Saved PortletPreferenceValue #%d (%s=%s) for PortletPreference #%d.",
                                    sitePrefValue.getPortletPreferenceValueId(), sitePrefValue.getName(), sitePrefValue.getValue(), portletPreferencesId));
                        }
                    }
                }
            }
        }

        // Check remaining preferences
        DynamicQuery dynamicQuery = portletPreferenceValueLocalService.dynamicQuery();
        dynamicQuery.add(RestrictionsFactoryUtil.like("smallValue", "[$SITE_NAVIGATION_MENU_ID%"));
        List<PortletPreferenceValue> results = portletPreferenceValueLocalService.dynamicQuery(dynamicQuery);
        if (ListUtil.isNotEmpty(results)) {
            for (PortletPreferenceValue prefResult : results) {
                String value = prefResult.getValue();
                if (siteNavigationMenuIdMap.containsKey(value)) {
                    String mappedValue = siteNavigationMenuIdMap.get(value);
                    prefResult.setValue(mappedValue);
                    portletPreferenceValueLocalService.updatePortletPreferenceValue(prefResult);
                    _log.info(String.format("Updated siteNavigationMenuId PortletPreferenceValue from %s to %s.", value, mappedValue));
                }
            }
        }
    }

    private PortletPreferenceValue getPortletPreferenceValue(long portletPreferencesId, String name, String value) {
        DynamicQuery dynamicQuery = portletPreferenceValueLocalService.dynamicQuery();
        dynamicQuery.add(RestrictionsFactoryUtil.eq("portletPreferencesId", portletPreferencesId));
        dynamicQuery.add(RestrictionsFactoryUtil.eq("name", name));
        dynamicQuery.add(RestrictionsFactoryUtil.eq("smallValue", value));
        List<PortletPreferenceValue> results = portletPreferenceValueLocalService.dynamicQuery(dynamicQuery);
        return ListUtil.isNotEmpty(results) ? results.get(0) : null;
    }

    private List<PortletPreferenceValue> getPortletPreferenceValues(long portletPreferencesId) {
        DynamicQuery dynamicQuery = portletPreferenceValueLocalService.dynamicQuery();
        dynamicQuery.add(RestrictionsFactoryUtil.eq("portletPreferencesId", portletPreferencesId));
        return portletPreferenceValueLocalService.dynamicQuery(dynamicQuery);
    }

    // ---------------------------- Client Extension for LayoutSet ----------------------------

    @Override
    public void verifyLayoutSetThemeCSSClientExtension(long groupId) {
        Group group = groupLocalService.fetchGroup(groupId);
        if (group == null) {
            _log.warn("Group not found, groupId=" + groupId);
            return;
        }
        LayoutSet layoutSet = group.getPublicLayoutSet();
        if (layoutSet == null) {
            _log.warn("LayoutSet not found, groupId=" + groupId);
            return;
        }
        long classNameId = portal.getClassNameId(LayoutSet.class.getName());
        long classPK = layoutSet.getLayoutSetId();
        ClientExtensionEntryRel clientExtensionEntryRel = clientExtensionEntryRelLocalService.fetchClientExtensionEntryRel(classNameId, classPK, CX_TYPE);
        if (clientExtensionEntryRel == null) {
            try {
                clientExtensionEntryRel = clientExtensionEntryRelLocalService.addClientExtensionEntryRel(
                        group.getCreatorUserId(),
                        groupId,
                        classNameId,
                        classPK,
                        CX_EXTERNAL_REFERENCE_CODE,
                        CX_TYPE,
                        StringPool.BLANK,
                        new ServiceContext()
                );
                _log.info(String.format("Saved ClientExtensionEntryRel #%d for LayoutSet #%s (type=%s, code=%s)",
                        clientExtensionEntryRel.getClientExtensionEntryRelId(), layoutSet.getLayoutSetId(), CX_TYPE, CX_EXTERNAL_REFERENCE_CODE));
            } catch (Exception e) {
                _log.error("Failed to save ClientExtensionEntryRel, cause: " + e.getMessage());
            }
        }
    }

    // ---------------------------- Fields Mapping for Object Image Fields ---------------------------------------------

    @Override
    public void verifyObjectImageFieldsMapping(long groupId) {
        DynamicQuery dynamicQuery = fragmentEntryLinkLocalService.dynamicQuery();
        dynamicQuery.add(RestrictionsFactoryUtil.like("editableValues", "%$OBJECT_FIELD:%"));
        List<FragmentEntryLink> fragmentEntryLinks = fragmentEntryLinkLocalService.dynamicQuery(dynamicQuery);
        Pattern objectFieldPlaceholderPattern = Pattern.compile("ObjectField_\\[\\$OBJECT_NAME:([a-zA-Z0-9]+)\\$]\\[\\$OBJECT_FIELD:([a-zA-Z0-9]+)\\$]#previewURL");
        for (FragmentEntryLink fragmentEntryLink : fragmentEntryLinks) {
            try {
                String editableValues = fragmentEntryLink.getEditableValues();
                Matcher matcher = objectFieldPlaceholderPattern.matcher(editableValues);
                if (matcher.find()) {
                    String externalReferenceCode = matcher.group(1);
                    ObjectDefinition objectDefinition = objectDefinitionLocalService
                            .fetchObjectDefinitionByExternalReferenceCode(externalReferenceCode, fragmentEntryLink.getCompanyId());
                    if (objectDefinition == null) {
                        _log.warn("ObjectDefinition not found, externalReferenceCode=" + externalReferenceCode);
                        continue;
                    }
                    String objectFieldName = matcher.group(2);
                    ObjectField objectField = objectFieldLocalService.fetchObjectField(objectDefinition.getObjectDefinitionId(), objectFieldName);
                    if (objectField == null) {
                        _log.warn("ObjectField not found, fieldName=" + objectFieldName);
                        continue;
                    }
                    String replacementValue = "ObjectField_" + objectField.getObjectFieldId() + "#previewURL";
                    String updatedEditableValues = matcher.replaceAll(replacementValue);
                    fragmentEntryLink.setEditableValues(updatedEditableValues);
                    fragmentEntryLinkLocalService.updateFragmentEntryLink(fragmentEntryLink);
                    _log.info(String.format("Updated editableValues for FragmentEntryLink #%d to: %s.", fragmentEntryLink.getFragmentEntryLinkId(), updatedEditableValues));
                }
            } catch (Exception e) {
                _log.error("Error: " + e.getMessage());
            }
        }
    }


    @Reference
    private Portal portal;
    @Reference
    private GroupLocalService groupLocalService;
    @Reference
    private ClientExtensionEntryRelLocalService clientExtensionEntryRelLocalService;

    @Reference
    private LayoutPageTemplateEntryLocalService layoutPageTemplateEntryLocalService;

    @Reference
    private CounterLocalService counterLocalService;
    @Reference
    private SiteNavigationMenuLocalService siteNavigationMenuLocalService;
    @Reference
    private PortletPreferencesLocalService portletPreferencesLocalService;
    @Reference
    private PortletPreferenceValueLocalService portletPreferenceValueLocalService;

    @Reference
    private ObjectFieldLocalService objectFieldLocalService;
    @Reference
    private ObjectDefinitionLocalService objectDefinitionLocalService;
    @Reference
    private FragmentEntryLinkLocalService fragmentEntryLinkLocalService;


    private static final Log _log = LogFactoryUtil.getLog(SynchronizeSiteInitializerMVCActionCommandOverride.class);
}