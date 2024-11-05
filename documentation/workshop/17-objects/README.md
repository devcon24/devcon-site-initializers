[Home](../../../README.md) / [Workshop](../README.md) 

# 17. Objects

## Goal 

Learn how you can define Objects and Object Entries in Site Initializer. Experience in displaying Objects in a Collection Display, and defining such content descriptors for Site Initializers.   

## Context

In the context of this demo Objects can be used to display a list of speakers on the Speaker page.
A Speaker Object can be defined, and a list of Object entries can be created and displayed in a Collection Display.

## Overview

[Objects](https://learn.liferay.com/w/dxp/liferay-development/objects) are used to speed up development process using no-code/low-code capabilities. 
Using Objects you can define data structures and entities, and use them in various Liferay applications and frameworks: Collection Displays and Forms, Permissions and Workflow, etc.

Object information in the Site Initializer can be defined with the following descriptors:

|          Data Type | Site Initializer Folder | Recommended File Name Pattern       | Example in Liferay                                                                                                                                                                                                                                               |
|-------------------:|------------------------:|-------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Object Definitions |      object-definitions | `<object-name>.json`                | [testray-build.json](https://github.com/liferay/liferay-portal/blob/master/workspaces/liferay-testray-workspace/client-extensions/liferay-testray-site-initializer/site-initializer/object-definitions/testray-build.json)                                       |
|     Object Entries |          object-entries | `<object-name>-entries.json`        | [1_testray-case-type.object-entries.json](https://github.com/liferay/liferay-portal/blob/master/workspaces/liferay-testray-workspace/client-extensions/liferay-testray-site-initializer/site-initializer/object-entries/1_testray-case-type.object-entries.json) |
|      Object Fields |           object-fields | `<object-name>-fields.json`         | [testray-build-to-tasks.json](https://github.com/liferay/liferay-portal/tree/master/workspaces/liferay-testray-workspace/client-extensions/liferay-testray-site-initializer/site-initializer/object-relationships)                                               |
|   Object Relations |    object-relationships | `<object-one>-to-<object-two>.json` | [build-case-results-object-fields.json](https://github.com/liferay/liferay-portal/blob/master/workspaces/liferay-testray-workspace/client-extensions/liferay-testray-site-initializer/site-initializer/object-fields/build-case-results-object-fields.json)      |

## Practice

Todo

[<< 16. Collections](../16-collections/README.md)