RNWFILE=$1
TEXFILE=`basename $RNWFILE .Rnw`.tex
if [ -z $R_HOME ]; then 
  R_HOME=`R RHOME`
fi
RPROG=$R_HOME/bin/R
RSCRIPT=$R_HOME/bin/Rscript

cp -f knitr/$RNWFILE $RNWFILE
$R_HOME/bin/Rscript -e "library(knitr); knit2pdf('$RNWFILE')"
cp -f knitr/`basename $RNWFILE .Rnw`-fake.Rnw $RNWFILE
