MATCH (studyLabel)<-[:TITLE]-(s:Study)-[:PRODUCT]->(lds:LogicalDataSet)-[:VARIABLE]->(v:Variable) WHERE lds.isPublic = true AND NOT (v)-[:CONCEPT]->() AND NOT (v.notation =~ '(?i).*_F' OR v.notation =~ '(?i).*_I') RETURN studyLabel.en, v.notation;