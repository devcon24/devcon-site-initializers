[Home](../../../README.md) / [Workshop](../README.md) 

# 10. Widget Templates

## Goal 

Learn how to define Widget Templates in a Site Initializer module.

## Context

In the context of this Workshop Demo you need to define Widget Templates for Site Navigation Menu widget in order to define custom structure and styling for header/footer navigation.

## Overview

Widget Templates are used to customize the layout of out-of-the-box widgets having such capability.

Using Widget Templates for Site Navigation Menu you can customize how the site menus (e.g. header and footer navigation) are rendered.

In the Site Initializer Widget Templates are defined in `ddm-templates` folder. For each Widget Template a subfolder must be created.

The subfolder should contain two files:
- `ddm-template.ftl` - a template file, sample:

        <#if entries?has_content>
            <div class="devcon-nav devcon-nav--footer">
                <#list entries as navItem>
                    <#assign nav_item_css_class = "devcon-nav__link" />
                    <#if navItem.isSelected()>
                        <#assign nav_item_css_class = "${nav_item_css_class} selected active" />
                    </#if>
                    <a href="${navItem.getURL()}" class="${nav_item_css_class}">
                        ${navItem.getName()}
                    </a>
                </#list>
            </div>
        </#if>

- `ddm-template.json` - a widget template descriptor, having the following structure:

      {
        "className": "com.liferay.portal.kernel.theme.NavItem",
        "ddmTemplateKey": "DEVCON_FOOTER_NAV",
        "name": "DevCon Footer Navigation",
        "resourceClassName": "com.liferay.portlet.display.template.PortletDisplayTemplate"
      }

  _**Note**: Here a widget template for Site Navigation portlet with `DEVCON_FOOTER_NAV` DDM Template Key is defined._

_Example in Liferay sources:_ https://github.com/liferay/liferay-portal/tree/master/workspaces/liferay-marketplace-workspace/client-extensions/liferay-marketplace-site-initializer/site-initializer/ddm-templates/navigation-header

## Practice

1. Copy [ddm-templates](../../../exercises/exercise-10/ddm-templates) folder from `exercise-10` to [site-initializer](../../../modules/devcon-site-initializer/src/main/resources/site-initializer).
2. Redeploy Site Initializer module and run Synchronization.
3. Make sure Widget Templates for header/footer navigation applied:

  ![01.png](images/01.png)

  _**Notes**: widget templates are applied "automatically" because the Header/Footer fragments pass the `displayStyle` value to portlet preferences of the embedded navigation widget. The styling is applied from the Theme CSS Client Extension deployed previously._ 

[<< 9. Pages Definition](../09-layouts/README.md) | 