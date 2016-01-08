MATCH (studyLabel)<-[:PREFLABEL]-(s:Study)-[:INGROUP]->(sg:StudyGroup)-[:PREFLABEL]->(studyGroupLabel), (s)-[:PRODUCT]->(lds:LogicalDataSet)-[:PREFLABEL]->(datasetLabel), (lds)-[:VARIABLE]->(variable) WHERE studyGroupLabel.en = 'SES' AND studyLabel.en = '2002' AND datasetLabel.en = 'cross-sec' AND variable.notation = 'B33' RETURN variable.id;