# Maven Central Deployment Guide

This guide explains how to deploy Nexus Command modules to Maven Central using the new Central Publishing Maven Plugin.

## Prerequisites

### 1. Maven Central Account
- Create an account at [Maven Central](https://central.sonatype.com/)
- Generate a user token for authentication
- Note your namespace (should be `net.savantly` for this project)

### 2. GPG Key Setup
```bash
# Generate a new GPG key (if you don't have one)
gpg --gen-key

# List your keys to get the key ID
gpg --list-secret-keys --keyid-format LONG

# Export your public key to a keyserver
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID

# Export your private key (keep this secure!)
gpg --export-secret-keys YOUR_KEY_ID > private-key.asc

# Configure GPG for non-interactive use (important for CI/CD)
echo "pinentry-mode loopback" >> ~/.gnupg/gpg.conf
echo "allow-loopback-pinentry" >> ~/.gnupg/gpg-agent.conf

# Restart GPG agent
gpgconf --kill gpg-agent
```

### 3. Environment Variables
Set these environment variables in your CI/CD system or local environment:

```bash
export MAVEN_CENTRAL_USERNAME="your-username" # or use auth in maven settings xml
export MAVEN_CENTRAL_TOKEN="your-token"
export GPG_KEY_ID="your-gpg-key-id"
export GPG_PASSPHRASE="your-gpg-passphrase"
```

## Deployment Commands

### Deploy Snapshot Version
```bash
# Deploy all modules to Maven Central staging
mvn clean deploy -Prelease

# Or deploy specific modules
mvn clean deploy -Prelease -pl module-common-types,module-encryption
```

### Deploy Release Version
```bash
# 1. Update version to release version (remove -SNAPSHOT)
mvn versions:set -DnewVersion=3.4.0

# 2. Deploy to Maven Central
mvn clean deploy -Prelease

# 3. The plugin will automatically create a deployment bundle
# 4. Check the Central Portal for your deployment status
# 5. If autoPublish=false, manually publish from the portal

# 6. Tag the release
git tag -a v3.4.0 -m "Release version 3.4.0"
git push origin v3.4.0

# 7. Update to next development version
mvn versions:set -DnewVersion=3.4.1-SNAPSHOT
```

## Module-Specific Deployment

To deploy only specific modules:

```bash
# Deploy only core modules
mvn clean deploy -Prelease -pl module-common-types,module-audited-entity,module-encryption

# Deploy AI-related modules
mvn clean deploy -Prelease -pl module-ai,module-agents

# Deploy organization modules
mvn clean deploy -Prelease -pl module-organizations,module-franchise
```

## Configuration Details

### Central Publishing Plugin Configuration
- `publishingServerId`: Must match server ID in settings.xml
- `tokenAuth`: Uses token-based authentication (recommended)
- `autoPublish`: Set to `false` for manual review before publishing
- `waitUntil`: Waits until artifacts are published

### Required Metadata
All modules include:
- Source JAR (`maven-source-plugin`)
- Javadoc JAR (`maven-javadoc-plugin`)
- GPG signatures (`maven-gpg-plugin`)
- Required POM metadata (licenses, developers, SCM)

## Troubleshooting

### Common Issues

1. **GPG "Inappropriate ioctl for device" error**:
   ```bash
   # Fix GPG configuration for non-interactive use
   echo "pinentry-mode loopback" >> ~/.gnupg/gpg.conf
   echo "allow-loopback-pinentry" >> ~/.gnupg/gpg-agent.conf
   gpgconf --kill gpg-agent
   
   # Alternative: Use GPG_TTY environment variable
   export GPG_TTY=$(tty)
   ```

2. **GPG signing fails**: 
   - Ensure GPG key is properly configured and passphrase is correct
   - Check that GPG_PASSPHRASE environment variable is set
   - Verify key ID matches GPG_KEY_ID environment variable

3. **Authentication fails**: 
   - Verify Maven Central credentials and token
   - Check that MAVEN_CENTRAL_USERNAME and MAVEN_CENTRAL_TOKEN are set correctly

4. **Validation errors**: 
   - Check that all required POM metadata is present (description, licenses, developers, SCM)
   - Ensure source and javadoc JARs are being generated

5. **Namespace issues**: 
   - Ensure you have rights to publish under `net.savantly`
   - Verify namespace ownership in Maven Central portal

### Verification
After deployment, verify your artifacts at:
- Staging: https://central.sonatype.com/
- Published: https://search.maven.org/search?q=g:net.savantly

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Deploy to Maven Central
on:
  release:
    types: [published]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Import GPG key
        uses: crazy-max/ghaction-import-gpg@v6
        with:
          gpg_private_key: ${{ secrets.GPG_PRIVATE_KEY }}
          passphrase: ${{ secrets.GPG_PASSPHRASE }}
      
      - name: Deploy to Maven Central
        run: mvn clean deploy -Prelease
        env:
          MAVEN_CENTRAL_USERNAME: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
          MAVEN_CENTRAL_TOKEN: ${{ secrets.MAVEN_CENTRAL_TOKEN }}
          GPG_KEY_ID: ${{ secrets.GPG_KEY_ID }}
          GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
```

## Security Notes
- Never commit GPG private keys or tokens to version control
- Use environment variables or CI/CD secrets for sensitive data
- Consider using a dedicated GPG key for releases
- Regularly rotate your Maven Central tokens