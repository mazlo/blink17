MATCH (p:PeriodOfTime) WHERE NOT( p.startDate =~ '[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}' ) RETURN p;