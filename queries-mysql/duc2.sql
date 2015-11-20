SELECT DISTINCT( lss.en ) FROM study s JOIN langstring lss on lss.id=s.title_id JOIN instrument i on i.study_id=s.id JOIN questionnaire_question qq on qq.questionnaire_id=i.id JOIN question q on q.id=qq.question_id JOIN langstring lsq on lsq.id=q.questionText_id WHERE lsq.en REGEXP( '.*hours.*work.*' );