MATCH 
  (studyLabel)<-[:PREFLABEL]-
  (s:Study)-[:INGROUP]->
  (:StudyGroup)-[:PREFLABEL]->(studyGroupLabel), 
  (s)-[:PRODUCT]->
  (lds:LogicalDataSet)-[:PREFLABEL]->(datasetLabel), 
  (lds)-[:VARIABLE]->
  (variable)-[:REPRESENTATION]->(conceptScheme:ConceptScheme)
WHERE studyGroupLabel.en = 'CIS' AND studyLabel.en = '2008' AND datasetLabel.en = 'cross-sec' AND variable.notation = 'LARMAR'
WITH conceptScheme
MATCH
  (conceptScheme)-[:HASTOPCONCEPT]->
  (concept:Concept)<-[:STATISTICSCATEGORY]-
  (cs:CategoryStatistics)-[:SPATIAL]->(country), 
  (concept)-[:PREFLABEL]->(conceptLabel) 
WHERE country.label = 'LV'
WITH country, cs, concept, conceptLabel
RETURN country.label, concept.notation, conceptLabel.en, cs.frequency, cs.percentage 
ORDER BY concept.notation;