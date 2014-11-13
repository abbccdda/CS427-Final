insert into obstetricsvisit(obstetricsID, MID, visitDate, weeksPregnant, bloodPressure, fetalHeartRate, fundalHeightUterus)
values((select id from obstetrics where weeksPregnant = '08-3'), 1, "11/12/14", '08-3', '100/50', 60, 20.0);
