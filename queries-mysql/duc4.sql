SELECT DISTINCT BINARY qt.en FROM variable v JOIN variable_question v_q ON v_q.variable_id=v.id JOIN question q ON q.id=v_q.question_id JOIN langString qt ON qt.id=q.questionText_id JOIN concept c ON c.id=v.concept_id WHERE c.notation REGEXP('.*employe*.*') ORDER BY qt.en;