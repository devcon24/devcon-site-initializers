[Home](../../../README.md) / [Workshop](../README.md) 

# 11. Navigation Menus

## Goal 

Learn how to define Site Navigation Menus in Site Initializer and apply them for Site Navigation Menu widgets.

## Context

In the context of this Workshop Demo you need to define custom navigation menus for Header and Footer, and apply them for appropriate Site Navigation Menu widgets.

## Overview

With Navigation Menus you can define custom menus for a site, and specify which elements are included into navigation and how they are organized.

In the Site Initializer the navigation menus are defined in `site-navigation-menus.json` descriptor file. 

You can define the navigation menus and menu items for each of the menus, sample:

```json
[
	{
		"menuItems": [
			{
				"friendlyURL": "/agenda",
				"externalReferenceCode": "devcon-header-agenda",
				"privateLayout": false,
				"type": "layout"
			},
			{
				"friendlyURL": "/faq",
				"externalReferenceCode": "devcon-header-faq",
				"privateLayout": false,
				"type": "layout"
			},
			{
				"friendlyURL": "/speakers",
				"externalReferenceCode": "devcon-header-speakers",
				"privateLayout": false,
				"type": "layout"
			}
		],
		"name": "DEVCON_HEADER_NAV_MENU"
	},
	{
		"menuItems": [
			{
				"friendlyURL": "/agenda",
				"externalReferenceCode": "devcon-footer-agenda",
				"privateLayout": false,
				"type": "layout"
			},
			{
				"friendlyURL": "/speakers",
				"externalReferenceCode": "devcon-footer-speakers",
				"privateLayout": false,
				"type": "layout"
			}
		],
		"name": "DEVCON_FOOTER_NAV_MENU"
	}
]
```

In the example above two navigation menus are created: `DEVCON_HEADER_NAV_MENU` and `DEVCON_FOOTER_NAV_MENU`. For each of the menus menu items are defined, using the following properties:
- `type`: the type of the navigation menu item;
    
    _**Note**: here we use only `layout` types, but there are other supported types as well, e.g. `node`, `url`, `display-page`._

- `friendlyURL`: the friendly URL of the referenced Layout;
- `privateLayout`: a flag indicating if menu item should be privately restricted;
- `externalReferenceCode`: unique menu item code.

  _**Note**: It's required to specify `externalReferenceCode` to avoid menu items duplication on synchronization._

_Example in Liferay sources:_ https://github.com/liferay/liferay-portal/tree/master/workspaces/liferay-marketplace-workspace/client-extensions/liferay-marketplace-site-initializer/site-initializer/ddm-templates/navigation-header

## Practice

1. Copy [site-navigation-menus.json](../../../exercises/exercise-11/site-navigation-menus.json) from `exercise-11` to [site-initializer](../../../modules/devcon-site-initializer/src/main/resources/site-initializer) folder.
2. Todo

[README.md](../10-widget-templates/README.md) | 