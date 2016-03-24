MATCH 
  (cs:ConceptScheme)-[:HASTOPCONCEPT]->
  (c:Concept)<-[:STATISTICSCATEGORY]-
  (cts:CategoryStatistics) 
WITH cs,SUM(cts.frequency) AS frequencySum 
MATCH 
  (cs:ConceptScheme)<-[:REPRESENTATION]-
  (v)<-[:VARIABLE]-(ss:SummaryStatistics)
WITH v,frequencySum,SUM(ss.numberOfCases) AS numberOfCasesSum 
RETURN v.id, frequencySum, numberOfCasesSum;