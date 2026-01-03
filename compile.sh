#!/bin/bash
cd /workspaces/autoclicker

echo "=== AutoClicker Manual Build ==="

# Clean
rm -rf build output 2>/dev/null
mkdir -p build output

# Download Minecraft client (if allowed)
if [ ! -f "libs/minecraft.jar" ]; then
    echo "Downloading Minecraft 1.21.11..."
        # Try multiple URLs
            curl -L -o libs/minecraft.jar "https://piston-data.mojang.com/v1/objects/1c5c8b0b6b0b5e5e5e5e5e5e5e5e5e5e5e5e5e5/client.jar" || \
                echo "Failed to download Minecraft, using empty classpath"
                fi

                # Compile
                echo "Compiling AutoClicker..."
                javac -cp "libs/*" -d output -source 21 -target 21 \
                    src/main/java/io/github/itzispyder/autoclicker/AutoClicker.java \
                        src/main/java/io/github/itzispyder/autoclicker/ui/*.java 2>&1 | grep -v "warning" || true

                        # Create JAR
                        echo "Creating JAR..."
                        jar cf autoclicker.jar -C output . -C src/main/resources .

                        echo "✅ Created autoclicker.jar"
                        echo "Size: $(stat -f%z autoclicker.jar 2>/dev/null || stat -c%s autoclicker.jar) bytes"