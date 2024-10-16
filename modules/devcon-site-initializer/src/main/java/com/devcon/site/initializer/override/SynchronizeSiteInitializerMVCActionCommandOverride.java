package com.devcon.site.initializer.override;

import com.devcon.site.initializer.service.SiteInitializerService;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.security.permission.PermissionCacheUtil;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.SiteInitializerFactory;
import com.liferay.site.initializer.SiteInitializerRegistry;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Callable;

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

        if (!FeatureFlagManagerUtil.isEnabled("LPS-165482")) {
            return;
        }

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = themeDisplay.getSiteGroupId();

        // ------ OnBeforeInitialize Customization
        if (isDevConSite(groupId)) {
            _log.info("SynchronizeSiteInitializerMVCActionCommand -> DevCon: Before Synchronize Site Initializer");
        }

        Callable<Group> callable = new GroupCallable(actionRequest);

        try {
            TransactionInvokerUtil.invoke(_transactionConfig, callable);
        }
        catch (Throwable throwable) {
            _log.error(throwable);
            SessionErrors.add(actionRequest, throwable.getClass());
            throw new Exception(throwable);
        }

        // ------ OnAfterInitialize Customization
        if (isDevConSite(groupId)) {
            _log.info("SynchronizeSiteInitializerMVCActionCommand -> DevCon: After Synchronize Site Initializer");
            // Save ThemeCSS CX for LayoutSet
            siteInitializerService.verifyLayoutSetThemeCSSClientExtension(groupId);
            // Save siteNavigationMenuId to Portlet Preferences
            siteInitializerService.verifySiteNavigationMenuIds(groupId);
            // Save mapping for Object Image Fields
            siteInitializerService.verifyObjectImageFieldsMapping(groupId);
        }

    }

    private boolean isDevConSite(long groupId) {
        try {
            Group group = _groupService.getGroup(groupId);
            UnicodeProperties unicodeProperties = group.getTypeSettingsProperties();
            String siteInitializerKey = unicodeProperties.get("siteInitializerKey");
            return DEVCON_SI_KEY.equals(siteInitializerKey);
        } catch (Exception e) {
            return false;
        }
    }

    private void _initialize(
            long groupId, InputStream inputStream, String siteInitializerKey)
            throws Exception {

        File tempFile = FileUtil.createTempFile(inputStream);
        File tempFolder = FileUtil.createTempFolder();

        FileUtil.unzip(tempFile, tempFolder);

        tempFile.delete();

        try {
            SiteInitializer siteInitializer = _siteInitializerFactory.create(
                    new File(tempFolder, "site-initializer"), siteInitializerKey);

            siteInitializer.initialize(groupId);
        }
        finally {
            tempFolder.delete();
        }
    }

    private Group _updateGroup(ActionRequest actionRequest) throws Exception {
        ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
                WebKeys.THEME_DISPLAY);

        Group group = _groupService.getGroup(themeDisplay.getSiteGroupId());

        UnicodeProperties unicodeProperties = group.getTypeSettingsProperties();

        String siteInitializerKey = unicodeProperties.get("siteInitializerKey");

        if (Validator.isNull(siteInitializerKey)) {
            return null;
        }

        UploadPortletRequest uploadPortletRequest =
                _portal.getUploadPortletRequest(actionRequest);

        try (InputStream inputStream = uploadPortletRequest.getFileAsStream(
                "siteInitializerFile")) {

            if (inputStream != null) {
                _initialize(
                        group.getGroupId(), inputStream, siteInitializerKey);

                return _groupService.getGroup(themeDisplay.getSiteGroupId());
            }
        }

        SiteInitializer siteInitializer =
                _siteInitializerRegistry.getSiteInitializer(siteInitializerKey);

        if (siteInitializer == null) {
            return null;
        }

        siteInitializer.initialize(group.getGroupId());

        return _groupService.getGroup(themeDisplay.getSiteGroupId());
    }

    private static final TransactionConfig _transactionConfig =
            TransactionConfig.Factory.create(
                    Propagation.REQUIRED, new Class<?>[] {Exception.class});

    @Reference
    private GroupService _groupService;

    @Reference
    private Portal _portal;

    @Reference
    private SiteInitializerFactory _siteInitializerFactory;

    @Reference
    private SiteInitializerRegistry _siteInitializerRegistry;

    private class GroupCallable implements Callable<Group> {

        @Override
        public Group call() throws Exception {
            try {
                return _updateGroup(_actionRequest);
            }
            catch (Exception exception) {
                PermissionCacheUtil.clearCache(
                        _portal.getUserId(_actionRequest));

                throw exception;
            }
        }

        private GroupCallable(ActionRequest actionRequest) {
            _actionRequest = actionRequest;
        }

        private final ActionRequest _actionRequest;

    }

    @Reference
    private SiteInitializerService siteInitializerService;

    private static final Log _log = LogFactoryUtil.getLog(SynchronizeSiteInitializerMVCActionCommandOverride.class);
}