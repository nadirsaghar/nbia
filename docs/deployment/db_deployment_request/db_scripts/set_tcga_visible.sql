

create table patient_temp as   
  select p.patient_pk_id 
  from study s, patient p 
  where s.patient_pk_id = p.patient_pk_id and p.trial_dp_pk_id = 1329496064;

update patient
set visibility = '1'
where trial_dp_pk_id = 1329496064;

update study
set visibility = '1'
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);

update general_series
set visibility = '1'
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);
  
update general_image
set visibility = '1', curation_timestamp=CURRENT_TIMESTAMP()
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);

drop table patient_temp;


create table patient_temp as   
  select p.patient_pk_id 
  from study s, patient p 
  where s.patient_pk_id = p.patient_pk_id and p.trial_dp_pk_id = 1334640643;

update patient
set visibility = '1'
where trial_dp_pk_id = 1334640643;

update study
set visibility = '1'
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);

update general_series
set visibility = '1'
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);
  
update general_image
set visibility = '1', curation_timestamp=CURRENT_TIMESTAMP()
where patient_pk_id in (
  select patient_pk_id 
  from patient_temp);

drop table patient_temp;

