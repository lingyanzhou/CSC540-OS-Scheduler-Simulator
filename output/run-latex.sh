cd latex
for i in $(ls); do
    pdflatex -output-directory $i $i/$i
done
cd ..
