SELECT DISTINCT lo.label FROM ( SELECT cs_c.hasTopConcept_id FROM study s JOIN studyGroup sg ON sg.id=s.studyGroup_id JOIN resource rs ON rs.id=s.id JOIN langString lrs ON lrs.id=rs.prefLabel_id JOIN resource rsg ON rsg.id=sg.id JOIN langString lrsg ON lrsg.id=rsg.prefLabel_id JOIN study_logicalDataset slds ON slds.study_id=s.id JOIN logicalDataSet lds ON lds.id=slds.logicalDataSet_id JOIN resource rlds ON rlds.id=lds.id JOIN langString lrlds ON lrlds.id=rlds.prefLabel_id JOIN logicalDataSet_variable ldsv ON ldsv.logicalDataSet_id=slds.logicalDataSet_id JOIN variable v ON v.id=ldsv.variable_id JOIN conceptScheme cs ON cs.id=v.representation_id JOIN conceptScheme_concept cs_c ON cs_c.conceptScheme_id=cs.id WHERE lrsg.en = 'CIS' AND lrs.en = '2008' AND lrlds.en = 'cross-sec' ) vcons JOIN categoryStatistics_concept cats_c ON cats_c.concept_id=vcons.hasTopConcept_id JOIN categoryStatistics_location cats_l ON cats_l.categoryStatistics_id=cats_c.categoryStatistics_id JOIN categoryStatistics cats ON cats.id=cats_l.categoryStatistics_id JOIN location lo ON lo.id=cats_l.location_id ORDER BY lo.label