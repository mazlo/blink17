SELECT DISTINCT c.notation, ls_c.en FROM study s JOIN resource r_s ON r_s.id=s.id JOIN langString ls_s ON ls_s.id=r_s.prefLabel_id JOIN studyGroup sg ON sg.id=s.studyGroup_id JOIN resource r_sg ON r_sg.id=sg.id JOIN langString ls_sg ON ls_sg.id=r_sg.prefLabel_id JOIN study_logicalDataSet s_lds ON s_lds.study_id=s.id JOIN logicalDataSet lds ON lds.id=s_lds.logicalDataSet_id JOIN resource lds_r ON lds_r.id=lds.id JOIN langString ls_lds ON ls_lds.id=lds_r.prefLabel_id JOIN logicalDataSet_variable lds_v ON lds_v.logicalDataSet_id=s_lds.logicalDataSet_id JOIN variable v ON v.id=lds_v.variable_id JOIN concept c_v ON c_v.id=v.id JOIN conceptScheme cs ON cs.id=v.representation_id JOIN conceptScheme_concept cs_c ON cs_c.conceptScheme_id=cs.id JOIN concept c ON c.id=cs_c.hasTopConcept_id JOIN resource r_c ON r_c.id=c.id JOIN langString ls_c ON ls_c.id=r_c.prefLabel_id WHERE ls_sg.en = 'CIS' AND ls_s.en = '2008' AND ls_lds.en = 'cross-sec' AND c_v.notation = 'SUNI';