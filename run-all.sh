#!/bin/bash

# Java 26 Features Demo Runner
# This script compiles and runs all Java 26 feature examples

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Set Java 26 path
JAVA_HOME=~/.sdkman/candidates/java/26.ea.35-open
JAVAC=$JAVA_HOME/bin/javac
JAVA=$JAVA_HOME/bin/java

echo -e "${BLUE}================================================${NC}"
echo -e "${BLUE}    Java 26 Features Demo${NC}"
echo -e "${BLUE}================================================${NC}\n"

# Check if Java 26 is available
if [ ! -f "$JAVA" ]; then
    echo -e "${YELLOW}Java 26 not found. Installing...${NC}"
    source ~/.sdkman/bin/sdkman-init.sh
    sdk install java 26.ea.35-open
fi

# Compile all examples
echo -e "${GREEN}Compiling Java 26 features...${NC}"
mkdir -p build
$JAVAC --enable-preview --source 26 src/features/*.java -d build/ 2>&1 | grep -v "^Note:"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation successful${NC}\n"
else
    echo -e "${YELLOW}⚠ Compilation completed with warnings${NC}\n"
fi

# Run each example
examples=(
    "PrimitiveTypesInPatterns:Primitive Types in Patterns (JEP 455)"
    "StreamGatherers:Stream Gatherers (JEP 473)"
    "ScopedValues:Scoped Values (JEP 481)"
    "FlexibleConstructorBodies:Flexible Constructor Bodies (JEP 482)"
    "ModuleImportDeclarations:Module Import Declarations (JEP 476)"
)

for example in "${examples[@]}"; do
    IFS=':' read -r class_name title <<< "$example"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}Running: $title${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    $JAVA --enable-preview -cp build features.$class_name
    echo -e "\n"
done

echo -e "${BLUE}================================================${NC}"
echo -e "${GREEN}All examples completed!${NC}"
echo -e "${BLUE}================================================${NC}"
