package com.devcon.site.initializer.service;

public interface SiteInitializerService {

    void verifyLayoutSetThemeCSSClientExtension(long groupId);

    void verifySiteNavigationMenuIds(long groupId);

    void verifyObjectImageFieldsMapping(long groupId);

}