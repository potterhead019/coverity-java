# Coverity Java Demo

A sample Java project configured for Coverity static analysis. This project contains intentional security vulnerabilities for demonstration and learning purposes.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Coverity Scan account (for cloud scanning) or Coverity Connect (for enterprise)

## Project Structure

```
coverity-java-demo/
├── .github/
│   └── workflows/
│       └── coverity-scan.yml    # GitHub Actions workflow for Coverity
├── .codesight/                  # CodeSight IDE configuration
├── src/
│   ├── main/java/com/demo/
│   │   └── Application.java     # Sample app with security vulnerabilities
│   └── test/java/com/demo/
│       └── ApplicationTest.java # Unit tests
├── coverity.yml                 # Coverity configuration
├── pom.xml                      # Maven build configuration
└── README.md                    # This file
```

## Quick Start

### Build the Project

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn package
```

### Run the Application

```bash
java -jar target/coverity-java-demo-1.0.0.jar
```

## Coverity Scan Setup

### Option 1: Coverity Scan (Free for Open Source)

1. **Register your project** at [scan.coverity.com](https://scan.coverity.com/)

2. **Get your project token** from the project settings page

3. **Configure GitHub Secrets** (for CI/CD):
   - `COVERITY_TOKEN`: Your project token from Coverity Scan
   - `COVERITY_EMAIL`: Email associated with your Coverity account

4. **Manual scan submission**:
   ```bash
   # Download Coverity Build Tool from scan.coverity.com
   
   # Run Coverity build
   cov-build --dir cov-int mvn clean compile -DskipTests
   
   # Create submission archive
   tar czvf coverity-scan.tar.gz cov-int
   
   # Submit to Coverity Scan
   curl --form token=<YOUR_TOKEN> \
        --form email=<YOUR_EMAIL> \
        --form file=@coverity-scan.tar.gz \
        --form version="1.0.0" \
        --form description="Manual build" \
        https://scan.coverity.com/builds?project=<YOUR_PROJECT>
   ```

### Option 2: Coverity Connect (Enterprise)

1. **Configure connection** to your Coverity Connect server

2. **Run analysis**:
   ```bash
   # Configure Coverity
   cov-configure --java
   
   # Build with Coverity
   cov-build --dir cov-int mvn clean compile
   
   # Analyze
   cov-analyze --dir cov-int --security --webapp-security
   
   # Commit defects to server
   cov-commit-defects --dir cov-int \
       --host <coverity-server> \
       --stream <stream-name> \
       --user <username> \
       --password <password>
   ```

### Option 3: CodeSight IDE Integration

This project includes CodeSight configuration for IDE-based scanning:

1. Install the CodeSight extension in your IDE
2. Open the project - configuration is auto-detected from `.codesight/`
3. Run analysis from the IDE toolbar

## GitHub Actions CI/CD

The project includes a GitHub Actions workflow that:

- Runs on push to main/master branches
- Runs on pull requests
- Runs weekly scheduled scans
- Submits results to Coverity Scan

### Workflow Triggers

| Event | Behavior |
|-------|----------|
| Push to main/master | Full Coverity scan + submission |
| Pull request | Local analysis with SpotBugs feedback |
| Weekly schedule | Full scan for trend tracking |
| Manual dispatch | On-demand full scan |

## Security Vulnerabilities (Intentional)

This demo contains the following intentional security issues for Coverity to detect:

| Vulnerability | Location | CWE |
|--------------|----------|-----|
| SQL Injection | `queryDatabase()` | CWE-89 |
| Command Injection | `processUserInput()` | CWE-78 |
| Path Traversal | `readFile()` | CWE-22 |
| Resource Leak | Multiple methods | CWE-772 |
| Null Pointer Dereference | `processData()` | CWE-476 |
| Hardcoded Credentials | `getConnection()` | CWE-798 |
| Insecure Random | `generateToken()` | CWE-330 |
| Information Exposure | Exception handlers | CWE-209 |

> ⚠️ **Warning**: Do not use this code in production. These vulnerabilities are for educational purposes only.

## Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report
```

## Configuration Files

### coverity.yml

Main Coverity configuration including:
- Build commands
- Enabled checkers
- Severity thresholds
- Exclude patterns

### .github/workflows/coverity-scan.yml

GitHub Actions workflow for automated Coverity scanning.

## Troubleshooting

### Common Issues

1. **"No files captured"** - Ensure Maven compiles successfully before Coverity build
   ```bash
   mvn clean compile  # Verify this works first
   ```

2. **"Token invalid"** - Verify your Coverity token in repository secrets

3. **"Build tool not found"** - Download and install Coverity Build Tool

### Logs and Debugging

```bash
# Verbose Coverity build
cov-build --dir cov-int mvn clean compile 2>&1 | tee coverity.log

# Check captured files
cat cov-int/build-log.txt
```

## License

This project is for educational and demonstration purposes only.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Submit a pull request

Coverity scans will run automatically on your PR.
