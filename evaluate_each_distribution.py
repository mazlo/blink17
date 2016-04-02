import os

#
def run_evaluation( distribution, ptype, callback, ucs=["uc1","uc2","uc3","uc4"], weights=["50_50","60_40","70_30","80_20","90_10"] ):
  # extend the list if you want to have more than one thread
  for tps in ["1"]:

    print "[INFO] setting up thread pool to "+ tps;
    os.system( "sudo -u matthaeus -H sed -i 's/thread.pool.size=.*/thread.pool.size="+ tps +"/' application.properties ");

    if ( distribution == "equal" ):
      # one run. copy equalDistribution.txt to queries-folder
      print "[INFO] evaluation script started using 'equal distribution'"
      os.system( "sudo -u matthaeus cp -p distributions/equalDistribution.txt queries/." )
      callback()
    
    elif ( distribution == "probability" ):
      # multiple runs. create probabilityDistribution.txt file
      print "[INFO] evaluation script started using 'probability distribution'. I will evaluate all use cases"

      for uc in ucs:
        for weight in weights:
          print "[INFO] preparing evaluation of "+ uc +" with weights "+ weight
          os.system( "sudo -u matthaeus rm -f queries/*" )
          os.system( "sudo -u matthaeus cp -p distributions/each/dist_"+ uc +"_"+ weight +".txt queries/probabilityDistribution.txt" )
          print "[INFO] let's go"
          callback()
          print "[INFO] save away results"
          os.system( "sudo -u matthaeus cp -p `ls -t results_* | head -1` results_"+ ptype +"_"+ uc +"_"+ weight +".csv" )
          os.system( "sudo -u matthaeus cp -p `ls -t statistics_results_* | head -1` statistics_results_"+ ptype +"_"+ uc +"_"+ weight +".csv" )
    else:
      print "[ERROR] Missing parameter: equal or probability"

  return;
