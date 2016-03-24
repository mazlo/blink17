SELECT lo.label, c_cats.notation, ls_c.en, cats.frequency, cats.percentage 
FROM (
	SELECT cs.id AS conceptScheme_id
	FROM study s
	JOIN resource r_s ON r_s.id=s.id
	JOIN langString ls_s ON ls_s.id=r_s.prefLabel_id
	JOIN studyGroup sg ON sg.id=s.studyGroup_id
	JOIN resource r_sg ON r_sg.id=sg.id
	JOIN langString ls_sg ON ls_sg.id=r_sg.prefLabel_id
	JOIN study_logicalDataSet s_lds ON s_lds.study_id=s.id
	JOIN logicalDataSet lds ON lds.id=s_lds.logicalDataSet_id
	JOIN resource lds_r ON lds_r.id=lds.id
	JOIN langString ls_lds ON ls_lds.id=lds_r.prefLabel_id
	JOIN logicalDataSet_variable lds_v ON lds_v.logicalDataSet_id=s_lds.logicalDataSet_id
	JOIN variable v ON v.id=lds_v.variable_id
	JOIN concept c_v ON c_v.id=v.id
	JOIN conceptScheme cs ON cs.id=v.representation_id
	WHERE ls_sg.en = 'CIS' AND ls_s.en = '2008' AND ls_lds.en = 'cross-sec' AND c_v.notation = 'LARMAR' ) variable_conceptSchemes
JOIN conceptScheme_concept cs_c ON cs_c.conceptScheme_id=variable_conceptSchemes.conceptScheme_id
JOIN categoryStatistics_concept cats_c ON cats_c.concept_id=cs_c.hasTopConcept_id
JOIN categoryStatistics_location cats_lo ON cats_lo.categoryStatistics_id=cats_c.categoryStatistics_id
JOIN categorystatistics cats ON cats.id=cats_lo.categoryStatistics_id
JOIN location lo ON lo.id=cats_lo.location_id
JOIN concept c_cats ON c_cats.id=cs_c.hasTopConcept_id
JOIN resource r_c ON r_c.id=c_cats.id
JOIN langString ls_c ON ls_c.id=r_c.prefLabel_id 
WHERE lo.label = 'LV'
ORDER BY c_cats.notation;