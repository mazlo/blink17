MATCH 
  (studyLabel)<-[:PREFLABEL]-
  (s:Study)-[:INGROUP]->
  (:StudyGroup)-[:PREFLABEL]->(studyGroupLabel), 
  (s)-[:PRODUCT]->
  (lds:LogicalDataSet)-[:PREFLABEL]->(datasetLabel), 
  (lds)-[:VARIABLE]->
  (variable)-[:REPRESENTATION]->
  (:ConceptScheme)-[:HASTOPCONCEPT]->(topConcept:Concept)
WHERE studyGroupLabel.en = 'CIS' AND studyLabel.en = '2008' AND datasetLabel.en = 'cross-sec' 
WITH topConcept
MATCH
  (topConcept:Concept)<-[:STATISTICSCATEGORY]-
  (:CategoryStatistics)-[:SPATIAL]->(country) 
RETURN DISTINCT country.label 
ORDER BY country.label;