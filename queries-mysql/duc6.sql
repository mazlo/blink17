SELECT DISTINCT c.notation, lsc.en FROM variable v JOIN concept v_c ON v_c.id=v.id JOIN representation r ON r.id=v.representation_id JOIN conceptScheme_concept c_c ON c_c.conceptScheme_id=r.id JOIN concept c ON c.id=c_c.hasTopConcept_id JOIN resource r_c ON r_c.id=c.id JOIN langString lsc ON lsc.id=r_c.prefLabel_id LEFT JOIN variable_question v_q ON v_q.variable_id=v.id LEFT JOIN question q ON q.id=v_q.question_id LEFT JOIN langString qt ON qt.id=q.questionText_id WHERE BINARY v_c.notation = 'AGE';