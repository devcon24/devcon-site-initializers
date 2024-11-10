[Home](../../../README.md) / [Workshop](../README.md) 

# Bonus: Site Initializer Client Extensions

## Goal

Learn how you can initialize content using Site Initializer Client Extensions.

## Overview 

With [Site Initializer Client Extension](https://learn.liferay.com/w/dxp/liferay-development/importing-exporting-data/using-a-site-initializer-client-extension) you can create a site with predefined content like with OSGi Site Initializers.

This approach uses the same [BundleSiteInitializer](https://github.com/liferay/liferay-portal/blob/master/modules/apps/site-initializer/site-initializer-extender/site-initializer-extender/src/main/java/com/liferay/site/initializer/extender/internal/BundleSiteInitializer.java) under the hood for content processing, but it is SaaS-compatible due to its Client Extension nature.

With Site Initializer Client Extension a site is created automatically on deployment: the site name is specified in CX configuration. This also means that you can create only one site from such initializer.

Update support is updated by default: on a next deployment the content for specified site should be automatically updated.

## Practice

1. Delete the site created previously from Site Initializer.

2. Deploy [devcon-site-initializer-cx](../../../client-extensions/devcon-site-initializer-cx) module.

3. Navigate to created `DevCon CX` site (created automatically): http://localhost:8080/web/devcon-cx

![01.png](images/01.png)

_**Note**: customization introduced for OSGi Site Initializer is not applied here. To achieve the same state you'll need to perform the following steps manually (due to lack of such configuration in Site Initializer):_

_1) Select `DevCon Site Initializer Theme CSS` Theme CSS Client Extension for Layout Set;_

_2) Configure Site Navigation Menus for Header and Footer for `DevCon Main` Master Page;_

_3) Save mapping for Speaker Logo on Speakers page._


[<< 17. Objects](../17-objects/README.md)

###### © Vitaliy Koshelenko 2024 | All rights reserved