package com.devcon.site.initializer.override;

import com.devcon.site.initializer.service.SiteInitializerService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
        property = {
                "service.ranking:Integer=100",
                "javax.portlet.name=com_liferay_site_initializer_extender_web_SiteInitializerPortlet",
                "mvc.command.name=/site_initializer/synchronize_site_initializer"
        },
        service = MVCActionCommand.class
)
public class SynchronizeSiteInitializerMVCActionCommandOverride extends BaseMVCActionCommand {

    public static final String DEVCON_SI_KEY = "com.devcon.site.initializer";

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        // Invoke Original Action
        mvcActionCommand.processAction(actionRequest, actionResponse);

        // Customization
        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = themeDisplay.getSiteGroupId();
        if (isDevConSite(groupId)) {
            // Save ThemeCSS CX for LayoutSet
            siteInitializerService.verifyLayoutSetThemeCSSClientExtension(groupId);
            // Save siteNavigationMenuId to Portlet Preferences
            siteInitializerService.verifySiteNavigationMenuIds(groupId);
        }
    }

    private boolean isDevConSite(long groupId) {
        Group group = groupLocalService.fetchGroup(groupId);
        UnicodeProperties unicodeProperties = group.getTypeSettingsProperties();
        String siteInitializerKey = unicodeProperties.get("siteInitializerKey");
        return DEVCON_SI_KEY.equals(siteInitializerKey);
    }

    @Reference
    private GroupLocalService groupLocalService;
    @Reference
    private SiteInitializerService siteInitializerService;

    @Reference(target = "(component.name=com.liferay.site.initializer.extender.web.internal.portlet.action.SynchronizeSiteInitializerMVCActionCommand)")
    private MVCActionCommand mvcActionCommand;
}