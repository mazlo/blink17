SELECT DISTINCT lssg.en, lsa.en FROM studyGroup sg JOIN langstring lssg ON lssg.id=sg.title_id JOIN langstring lsa ON lsa.id=sg.abstract_id JOIN study s ON s.studyGroup_id=sg.id JOIN study_logicalDataSet s_lds ON s_lds.study_id=s.id JOIN logicalDataSet_variable lds_v ON lds_v.logicalDataSet_id=s_lds.logicalDataSet_id JOIN concept c ON c.id=lds_v.variable_id WHERE c.notation = 'SEX' ORDER BY lssg.en;