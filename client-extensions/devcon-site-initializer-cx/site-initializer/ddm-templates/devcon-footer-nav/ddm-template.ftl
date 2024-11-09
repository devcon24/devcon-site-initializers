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