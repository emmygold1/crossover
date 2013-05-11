#include "search.h"

SEXP searchCOD(SEXP sS, SEXP pS, SEXP vS, SEXP designS, SEXP linkMS, SEXP tCCS, SEXP modelS, SEXP effFactorS, SEXP vRepS, SEXP balanceSS, SEXP balancePS, SEXP verboseS, SEXP nS) {
                 
  using namespace arma; //TODO Where should I place this?
  using namespace Rcpp;
  
  BEGIN_RCPP // Rcpp defines the BEGIN_RCPP and END_RCPP macros that should be used to bracket code that might throw C++ exceptions.
  
  bool verbose = is_true( any( LogicalVector(verboseS) ) );
  bool balanceS = is_true( any( LogicalVector(balanceSS) ) );
  bool balanceP = is_true( any( LogicalVector(balancePS) ) );
  int s = IntegerVector(sS)[0];
  int p = IntegerVector(pS)[0];
  int v = IntegerVector(vS)[0];
  int n = IntegerVector(nS)[0];
  vec vRep = as<vec>(vRepS);
  //TODO Perhaps using umat or imat for some matrices? (Can casting rcDesign(i,j) to int result in wrong indices.)
  mat design = as<mat>(designS);
  mat linkM = as<mat>(linkMS);
  mat tCC = as<mat>(tCCS); // t(C) %*% C
  
  // TODO Read random number generators and C!
  GetRNGstate();
  
  if (verbose) Rprintf("Starting search algorithm!\n");
  mat designOld, rcDesign, Ar;  
  double eOld = 0;
  double s1, s2;
  NumericVector rows, cols;
  for(int i=0; i<n; i++) {  
    designOld = design;
    rows = ceil(runif(2)*p)-1; 
    cols = ceil(runif(2)*s)-1;  
    while ( design(rows[0],cols[0]) == design(rows[1],cols[1]) ) {
      rows = ceil(runif(2)*p)-1; 
      cols = ceil(runif(2)*s)-1;  
    }
    double tmp = design(rows[0],cols[0]);
    design(rows[0],cols[0]) = design(rows[1],cols[1]);
    design(rows[1],cols[1]) = tmp;
    rcDesign = createRowColumnDesign(design, v);
    Ar = getInfMatrixOfDesign(rcDesign, v+v*v);
    
    s2 = 1; // We set this constant for the moment
    s1 = trace(pinv(trans(linkM) * Ar * linkM) * tCC) ;
    
    //if (verbose) Rprintf(S2/S1, " vs. ", eOld, " ");
    if (s2/s1 > eOld) {
      if (verbose) Rprintf("=> Accepting new matrix.\n");
      eOld = s2/s1; 
    } else {
      if (verbose) Rprintf("=> Keeping old matrix.\n");
      design = designOld;    
    }
  }
  PutRNGstate();
  //double ip = as_scalar(v.t() * v);
  return List::create(Named("design")=design, Named("eff")=s2/s1);
  
  END_RCPP
}

arma::mat createRowColumnDesign(arma::mat design, int v) {
  using namespace arma;
  mat rcDesign = design;
  for (unsigned i=1; i<rcDesign.n_rows; i++) {
    rcDesign.row(i) = design.row(i)*v+design.row(i-1);
  }
  //rowvec zeroRow = rowvec(rcDesign.n_cols);
  //rcDesign.insert_rows(0, zeroRow);
  //for (= rcDesign <- X + v*rbind(0, X[-dim(X)[1],])
  return rcDesign;
}

arma::mat getInfMatrixOfDesign(arma::mat rcDesign, int v) {
  using namespace arma;
  int p = rcDesign.n_rows;
  int s = rcDesign.n_cols;
  vec r = zeros<vec>(v);  
  mat NP = zeros<mat>(v, p);
  mat NS = zeros<mat>(v, s);
  int t;
  //rcDesign.print("RCDesign:");
  for (int i=0; i<p; i++) {
    for (int j=0; j<s; j++) {
      t = rcDesign(i,j)-1;      
      NP(t, i)++;
      NS(t, j)++;
      r(t)++;
    }
  }  
  //NP.print("NP:");
  //NS.print("NS:");
  //r.print("r:");
  mat A = diagmat(r) - (1/s)* NP * trans(NP) - (1/p)* NS * trans(NS) + (1/(p*s))* r * trans(r);
  return A; 
}