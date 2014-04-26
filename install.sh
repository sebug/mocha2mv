#!/bin/sh
#
# Compiles the java code, creates a jar and a runner
#
echo "Compiling mocha2mv"
echo "========================="
ant compile

echo "Building the jar"
echo "========================="
ant jar

echo "Building the jar launcher"
echo "========================="
launcher=mocha2mv
jar=`pwd`/mocha2mv.jar
location=/usr/local/bin
echo "#!/bin/sh" > $launcher
echo "# Runs the mocha2mv jar" >> $launcher
echo "#" >> $launcher
echo "java -jar \"$jar\" \$1 \$2" >> $launcher
chmod a+x $launcher
sudo cp $launcher $location/$launcher
echo "Installed $launcher in $location,"
echo "read the mocha2mv_summary.pdf file on how to install the VIS bindings."
