[Home](../../../README.md) / [Workshop](../README.md) 

# 14. Documents

## Goal 

Learn how you can define Documents & Media files in Site Initializer, content descriptors preparation with documents, using placeholders.  

## Context

The Gallery and Venue sections on Home page should display appropriate images.
Those images can be Document & Media documents, initialized with the Site Initializer, and used for the fragments. 

## Overview

Documents in Site Initializer are defined inside `documents` folder. Site-scoped documents should be sored inside `group` subfolder, global documents - inside `company` one.

Inside the `group`/`company` subfolder the documents and folders are defined.

Upon initialization the same folders/documents structure should be created in Documents & Media.

_Example in Liferay sources:_ https://github.com/liferay/liferay-portal/tree/master/modules/apps/site-initializer/site-initializer-masterclass/src/main/resources/site-initializer/documents/group

## Practice

### 1. Documents Definition

1.1. Copy [documents](../../../exercises/exercise-14/documents) folder from `exercise-14` to [site-initializer](../../../modules/devcon-site-initializer/src/main/resources/site-initializer):

![01.png](images/01.png)

1.2. Redeploy Site Initializer module and run Synchronize. 

1.3. Navigate to Content & Data → Document and Media. Make sure `Gallery` and `Venue` Folders have been created with appropriate files inside.

![02.png](images/02.png)  

### 2. Additional Fragments Definition

2.1. Copy [devcon-gallery](../../../exercises/exercise-14/fragments/group/devcon/devcon-gallery) and [devcon-venue](../../../exercises/exercise-14/fragments/group/devcon/devcon-venue) fragments from `exercise-14` to [site-initializer](../../../modules/devcon-site-initializer/src/main/resources/site-initializer).

2.2. Redeploy Site Initializer module and run Synchronization. Make sure new fragments created.

### 3. Content Setup

3.1. Navigate to Site Builder → Pages and edit the Home page. 

3.2. Add `DevCon Gallery` and `DevCon Venue` fragments between `DevCon Home Page Banner` and `DevCon Ticket Prices`.

3.3. In the `DevCon Gallery` fragment for each editable image select an appropriate Documents & Media file using Mapping Source Selection and map to `File URL` field:

![03.png](images/03.png)
![04.png](images/04.png)
![05.png](images/05.png)

3.4. For the `DevCon Venue` fragment select the `venue-01.png` document using Direct Source Selection:

![06.png](images/06.png)

3.5. Publish the page.

### 4. Content Definition



[<< 13. Page Content](../13-page-content/README.md) |