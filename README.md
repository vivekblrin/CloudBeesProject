
# CloudBees Automation Project

## Overview
This project automates two key test scenarios using Java, Playwright, TestNG, and Allure Reporting:

- **CloudbeesHomePageTest**: Automates navigating and validating the CloudBees website documentation search functionality.
- **GitRepoTestingTest**: Automates Git operations like cloning, modifying, committing, and pushing using Java's `ProcessBuilder` (without any Git libraries).

---

## Project Structure
```
CloudBees/
├── src/test/java/com/org/cloudbees/
│   ├── CloudbeesHomePageTest.java
│   ├── GitRepoTestingTest.java
├── pom.xml
├── testng.xml
└── README.md
```

---

## Dependencies
- Java 10 or higher
- Maven
- Playwright for Java (`com.microsoft.playwright:playwright:1.17.1`)
- TestNG (`org.testng:testng:7.7.1`)
- Allure Reporting (`io.qameta.allure:allure-testng:2.21.0`)
- SLF4J API (`org.slf4j:slf4j-api:2.0.9`)
- Logback (`ch.qos.logback:logback-classic:1.4.14`)

---

## Setup Instructions

1. **Clone the Repository**  
   ```bash
   git clone <your-repository-url>
   cd CloudBees
   ```

2. **Install Dependencies**  
   Maven will automatically handle it through `pom.xml`.
   ```

3. **Run the Tests**  
   ```bash
   mvn clean test
   ```

4. **Generate Allure Report**  
   ```bash
   allure serve target/allure-results
   ```

---

## Tests Explained

### 1. CloudbeesHomePageTest
- Navigates to the CloudBees homepage.
- Interacts with the Products menu to navigate to CloudBees CD/RO.
- Verifies visibility of key elements.
- Opens the Documentation page in a new tab and performs a search.
- Navigates across search result pages.

### 2. GitRepoTestingTest
- **Test 1**: Clones a GitHub repo, adds a new file, commits, and pushes it.
- **Test 2**: Clones a repo, modifies an existing file (README.md), commits, and pushes it.

Both Git operations use Java's native `ProcessBuilder`, without any external Git libraries.

---

## Logging
- Using **SLF4J + Logback**.
- Console and file-based logging supported (logback.xml can be modified to add file appenders).

---

## Allure Reports
Each step inside the tests is wrapped with Allure steps for detailed reporting.
Simply run `allure serve target/allure-results` after the test run to view the report.

---

## Authors
- Vivek Swarup: vivekblrin

---

## License
This project is for demonstration purposes and does not carry a license.
