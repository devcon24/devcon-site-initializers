[Home](../../../README.md) / [Workshop](../README.md) 

# 10. Widget Templates

## Goal 

Learn how to define Widget Templates in a Site Initializer module.

## Context

In the context of this Workshop Demo you need to define Widget Templates for Site Navigation Menu widget in order to define custom structure and styling for header/footer navigation.

## Overview

Widget Templates are used to customize the layout of out-of-the-box widgets having such capability.

Using Widget Templates for Site Navigation Menu you can customize how the site menus (e.g. header and footer navigation) are rendered.

Widget Templates in the Site Initializer are defined inside `ddm-templates` folder. For each Widget Template a subfolder must be created.

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
2. Analyze widget templates definition (`ddm-template.json`) and template (`ddm-template.ftl`) files.
3. Redeploy Site Initializer module and run Synchronization.
4. Navigate to Site Menu → Design → Templates → Widget Templates. Make sure `DevCon Header Navigation` and `DevCon Footer Navigation` templates created:

  ![01.png](images/01.png)

5. Navigate to Site Menu → Design → Page Templates → Masters and edit `DevCon Main` Master Page.
6. Navigate to header navigation configuration:
  
  ![02.png](images/02.png)
  
7. Choose `DevCon Header Navigation` display template and save configuration:

  ![03.png](images/03.png)

8. Select `DevCon Footer Navigation` for footer navigation in the same way:

  ![04.png](images/04.png)

  _**Note**: styling for navigation is applied from the Theme CSS Client Extension deployed previously._

9. Publish `DevCon Main` Master Page end export its definition.
10. 

[<< 9. Pages Definition](../09-layouts/README.md) | [11. Navigation Menus >>](../11-navigation-menus/README.md)