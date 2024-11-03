[Home](../../../README.md) / [Workshop](../README.md) 

# 15. Web Content, Structures and Templates

## Goal 

Learn how you can define Web Content Structures, Templates, Folders and Articles in Site Initializers.

## Context

In the context of this demo Web Content is used for Sponsors section on Home page.

A Sponsor structure should be created with required information, a list of Sponsor articles should be created and displayed in a Collection Display.  

## Overview

Using [Web Content](https://learn.liferay.com/w/dxp/content-authoring-and-management/web-content) you can define a data structure (Web Content Structure), template (Web Content Template), and individual content items of defined structure (Web Content Articles). 
Then you can use defined web content articles on site for displaying content - directly, or using fields mapping.

To define structured web content in Site Initializer you need to do the following steps:

**1. Define Web Content Structure**

Web Content Structure is defined in `[structure-name]-structure.xml` file inside `ddm-structures` folder.
In the descriptor file you need to specify the structure name and structure definition JSON:

```xml
<?xml version="1.0"?>
<root>
    <structure>
        <name>Structure Name</name>
        <description></description>
        <definition>
            <![CDATA[
                // Structure Definition JSON
            ]]>
        </definition>
    </structure>
</root>
```

**2. Define Web Content Template**

Web Content Templates are defined in `ddm-templates` folder. For each template a subfolder is created with the following files:
- `ddm-template.ftl` - the template file with Freemarker code for the template;
- `ddm-template.json` - the template descriptor, where you can specify the template name and key, and also the structure key, sample:
```json 
{
   "ddmStructureKey": "SPONSOR",
   "ddmTemplateKey": "SPONSOR",
   "name": "Sponsor"
}
```

**3. Define Web Content Folder and Articles**

Web Contents are defined inside `journal-articles` folder. 

Web Content Folder is defined in `[folder-name].metadata.json` file inside the `journal-articles` folder, sample:
```json
{
   "description": "Sponsors Journal Folder",
   "externalReferenceCode": "SPONSORSJOURNALFOLDER",
   "name": "Sponsors",
   "viewableBy": "Anyone"
}
```

For each Web Content Folder a subfolder inside `journal-articles` should be created. Web Content Articles are defined inside the subfolder using a pair of files:
- `<web-content>.json` - the web content descriptor, sample:
```json
{
  "articleId": "GOOGLE-CLOUD",
  "assetCategoryERCs": [
  ],
  "ddmStructureKey": "SPONSOR",
  "ddmTemplateKey": "SPONSOR",
  "folder": "Sponsors",
  "name": "Google Cloud"
}
```
- `<web-content>.xml` - the web content definition XML.




_Examples in Liferay sources:_
- Structure: https://github.com/liferay/liferay-portal/blob/master/modules/apps/site-initializer/site-initializer-masterclass/src/main/resources/site-initializer/ddm-structures/blog-structure.xml 
- Template: https://github.com/liferay/liferay-portal/tree/master/modules/apps/site-initializer/site-initializer-masterclass/src/main/resources/site-initializer/ddm-templates/blog-entry
- Folder: https://github.com/liferay/liferay-portal/blob/master/modules/apps/site-initializer/site-initializer-masterclass/src/main/resources/site-initializer/journal-articles/blog-entries.metadata.json
- Articles: https://github.com/liferay/liferay-portal/tree/master/modules/apps/site-initializer/site-initializer-masterclass/src/main/resources/site-initializer/journal-articles/blog-entries

## Practice

Todo

[<< 14. Documents](../14-documents/README.md) | [16. Collections >>](../16-collections/README.md)