MATCH (studyLabel)<-[:PREFLABEL]-(s:Study)-[:INGROUP]->(:StudyGroup)-[:PREFLABEL]->(studyGroupLabel), (s)-[:PRODUCT]->(lds:LogicalDataSet)-[:PREFLABEL]->(datasetLabel), (lds)-[:VARIABLE]->(variable)-[:REPRESENTATION]->(:ConceptScheme)-[:HASTOPCONCEPT]->(concept:Concept)-[:PREFLABEL]->(conceptLabel) WHERE studyGroupLabel.en = 'CIS' AND studyLabel.en = '2008' AND datasetLabel.en = 'cross-sec' AND variable.notation = 'SUNI' RETURN DISTINCT concept.notation, conceptLabel.en;