MATCH (studyLabel)<-[:PREFLABEL]-(s:Study)-[:INGROUP]->(:StudyGroup)-[:PREFLABEL]->(studyGroupLabel), (s)-[:PRODUCT]->(lds:LogicalDataSet)-[:PREFLABEL]->(datasetLabel), (lds)-[:VARIABLE]->(variable)-[:REPRESENTATION]->(:ConceptScheme)-[:HASTOPCONCEPT]->(c:Concept)<-[:STATISTICSCATEGORY]-(cs:CategoryStatistics)-[:SPATIAL]->(country), (c)-[:PREFLABEL]->(conceptLabel) WHERE studyGroupLabel.en = 'CIS' AND studyLabel.en = '2008' AND datasetLabel.en = 'cross-sec' AND variable.notation = 'LARMAR' AND country.label = 'LV' RETURN country.label, c.notation, conceptLabel.en, cs.frequency, cs.percentage ORDER BY c.notation;