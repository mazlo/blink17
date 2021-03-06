MATCH 
  (v:Variable)-[:REPRESENTATION]->
  (cs:ConceptScheme)-[:HASTOPCONCEPT]->
  (c:Concept)<-[:STATISTICSCATEGORY]-
  (cts:CategoryStatistics)-[:SPATIAL]->(country) 
WITH v,country,SUM(cts.frequency) AS frequencySum 
MATCH 
  (v)<-[:VARIABLE]-
  (ss:SummaryStatistics)-[:SPATIAL]->(country) 
WHERE ss.numberOfCases <> frequencySum 
RETURN v.id, frequencySum, ss.numberOfCases, country.label;