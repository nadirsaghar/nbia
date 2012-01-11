create table study_temp as select study_pk_id from study where study_instance_uid = '9.9.9.9.9.9.99999.9.1.1.978.80402.1914.25390219.1.1';

create table series_temp as select general_series_pk_id from general_series where study_pk_id in (select * from study_temp);

create table image_temp as select image_pk_id from general_image where general_series_pk_id in (select * from series_temp);


delete from ct_image
where image_pk_id in (select image_pk_id from image_temp);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from image_temp);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from image_temp);

delete from general_image  
where general_series_pk_id in (select general_series_pk_id from series_temp);

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from series_temp);

delete from general_series  
where study_pk_id in (select study_pk_id from study_temp); 

delete from study 
where study_pk_id in (select study_pk_id from study_temp);

drop table study_temp;
drop table series_temp;
drop table image_temp;