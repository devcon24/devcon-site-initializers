[Home](../../../README.md) / [Workshop](../README.md) 

# 13. Page Content

## Goal 

Learn how you can define content for site pages in Site Initializer, hands-on practice on preparing content descriptors.

## Context

In the context of this Workshop Demo you'll need to define content in Site Initializer for Liferay DevCon 2024 site: Home page and Speakers page.

The home page should display custom fragments (Banner, Gallery, Venue, Ticket Prices, Sponsors), while the Speaker page should display a Collection of Objects.

## Overview

Similar to Master Pages, content for Pages (Layouts) is defined in `page-definition.json` descriptor file.

For Master Pages you can use the export feature, while it’s missing for individual pages. But to export the JSON structure for a page you can use the Fragment Composition feature.

The JSON for page definition should be cleaned up from environment-specific data, such values should be replaced with special placeholders.

## Practice

### 1. Define Additional Fragments

1.1. Copy [devcon-homepage-banner](../../../exercises/exercise-13/fragments/group/devcon/devcon-homepage-banner) and [devcon-ticket-prices](../../../exercises/exercise-13/fragments/group/devcon/devcon-ticket-prices) fragments from `exercise-13` to Site Initializer module.

1.2. Redeploy Site Initializer module and run Synchronization.

1.3. Make sure new fragments created.

### 2. Content Setup

2.1. Navigate to Site Builder → Pages and edit the Home page:

    ![01.png](images/01.png)

2.2. Add a wrapping container and an inner fixed-width container:

    ![02.png](images/02.png)

2.3. Add `DevCon Home Page Banner` fragment to the inner container:

    ![03.png](images/03.png)

2.4. Add `DevCon Ticket Prices` fragment below, and specify the values for editable elements:

    ![04.png](images/04.png)


[12. Customization](../12-customization/README.md) | 