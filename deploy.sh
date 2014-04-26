#!/bin/sh
#
# Makes a version that is ready to deploy.
#
echo "Please enter a version number: "
read vnum
dir=mocha2mv-$vnum
mkdir $dir
cp -R src $dir/src
cp -R support $dir/support
cp build.xml $dir/build.xml

# Build the documentation
cd doc
pdflatex mocha2mv_summary.tex
cd ..
cp doc/mocha2mv_summary.pdf $dir/mocha2mv_summary.pdf

# Copy the installation script
cp install.sh $dir/install.sh

# Copy the VIS package
cp -R mocha $dir/mocha

# Now remove the CVS bloat
cd $dir
rm -r CVS
rm -r */CVS
rm -r */*/CVS
rm -r */*/*/CVS
rm -r */*/*/*/CVS

# Finally, create the tar.
echo "Now launch the following command:"
echo tar cvfz "$dir.tar.gz" "$dir/*"
