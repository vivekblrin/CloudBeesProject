package com.org.cloudbees;


import com.microsoft.playwright.*;


import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.model.StepResult;
import io.qameta.allure.Allure;


import io.qameta.allure.model.Status;
import java.util.UUID;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;


import io.qameta.allure.*;
public class CloudbeesHomePageTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CloudbeesHomePageTest.class);


	@Test
	@Severity(SeverityLevel.CRITICAL)
    @Description("Navigate, search, and validate documentation on CloudBees website")
	public void verifyHomePage() {
	    try (Playwright playwright = Playwright.create()) {
	    	Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
	        BrowserContext context = browser.newContext();
	        Page page = context.newPage();
	        page.setDefaultTimeout(60000); 
	        step("Step 1: Navigate to CloudBees website", () -> {
	        // Step 1: Go to the website
	        page.navigate("https://www.cloudbees.com/");
	        logger.info("Step 1: Navigated to https://www.cloudbees.com/");
	        });

	        step("Step 2: Click Products > CloudBees CD/RO", () -> {
	        // Step 2: Click Products > CloudBees CD/RO
	        page.click("text=Products");
	        logger.info("Step 2: Clicked on 'Products'");
	          
	        //page.waitForSelector("[data-test='navbar.menuLink.products.otherProducts.cloudbeesCD']"); 
	        page.waitForNavigation(() -> {
	            page.click("[data-test='navbar.menuLink.products.otherProducts.cloudbeesCD']");
	        });
	        logger.info("Step 2: Clicked on 'CloudBees CD/RO' under Products");
	        });
	        
	        step("Step 3: Verify Cost Savings ($2m) is visible", () -> {
	        // Step 3: Verify Cost Savings ($2m) is visible
	        Locator costSavings = page.locator("text=$2m");
	        costSavings.waitFor();
	        assert costSavings.isVisible();
	        logger.info("Step 3: Verified that Cost Savings ($2m) is visible");
	        });

	        
	        step("Step 4: Click 'Auditors / Security'", () -> {
	        // Step 4: Scroll down and click Auditors / Security
	        page.click("button:has-text('Auditors / Security')");
	        logger.info("Step 4: Clicked on 'Auditors / Security'");
	        });
	        
	        step("Step 5: Verify  Generate single-click audit reports text under Release Governance", () -> {
	        // Step 5: Verify the text under Release Governance
	        Locator auditText = page.locator("text=Generate single-click audit reports");
	        auditText.waitFor();
	        assert auditText.isVisible();
	        logger.info("Step 5: Verified that text under Release Governance is visible");
	        });
	        

	        step("Step 6: Open Resources on top > Click Documentation step", () -> {
	        // Step 6: Click Resources > Documentation (opens in new tab)
	        	
	        	step("Step 6a: Clicked on 'Resources' ", () -> {
		        page.click("text=Resources");
	        	});
	        	
		        logger.info("Step 6: Clicked on 'Resources'");
	
		        // Wait for the new popup (new tab) to appear (no options, just the Runnable)
		        Page page1 = page.waitForPopup(() -> {
		            // Click action that triggers the popup (new tab)
		            page.click("[data-test=\"navbar.menuLink.resources.supportDocumentation.documentation\"]");
		            
		        });
	
		        logger.info("Step 6: Clicking Documentation openes a new tab");   
		        
		        step("Step 6b: Clicking Documentation openes a new tab", () -> {
		        page1.waitForLoadState();
		         });
		        
		        logger.info("Step 7: Waited for the new tab to load");
		        
		        page1.bringToFront();
		        
		        //step("Step 6c: Click search input on Documentation opens a new tab", () -> {
		        // Step 8: Wait for the actual search input and click it
		        Locator searchInput = page1.locator("input[placeholder='Search all CloudBees Resources']");
		        searchInput.waitFor(new Locator.WaitForOptions().setTimeout(10000));
		        
		        step("Step 6c:On  Click in the text field 'Search all CloudBees Resources'  new page is visible", () -> {
		        searchInput.click(new Locator.ClickOptions().setForce(true)); // Avoid overlay blocking
		        });
		        
		        logger.info("Step 8: On  Click in the text field 'Search all CloudBees Resources' new page is visible'");
		        //});
		        
		        step("Step 6d: Search for 'Installation'", () -> {
		        page1.fill("input[placeholder='Search']", "Installation");
		        logger.info("Step 9: Filled search query 'Installation'");
	
		        // Step 8: Press Enter after filling the search field
		        page1.press("input[placeholder='Search']", "Enter");
		        logger.info("Step 10: Pressed Enter to initiate search");
		        });
	
		        step("Step 6e: Navigate to page 2 and 3 of results", () -> {
		        // Navigate through search result pages
		        page1.click("[aria-label=\"Navigate to page 2 of search results\"]");
		        logger.info("Step 11: Clicked on page 2 of search results");
		        
		        page1.click("[aria-label=\"Navigate to page 3 of search results\"]");
		        logger.info("Step 12: Clicked on page 3 of search results");
		        
		        });
	        
	        });

	    }
	}
	
    private void step(String name, Runnable runnable) {
    	 String uuid = UUID.randomUUID().toString();
    	    Allure.getLifecycle().startStep(uuid, new StepResult().setName(name));
    	    try {
    	        runnable.run();
    	        Allure.getLifecycle().updateStep(uuid, step -> step.setStatus(Status.PASSED));
    	    } catch (Exception e) {
    	        Allure.getLifecycle().updateStep(uuid, step -> step.setStatus(Status.FAILED));
    	        throw e;
    	    } finally {
    	        Allure.getLifecycle().stopStep(uuid);
    	    }
    }
}
