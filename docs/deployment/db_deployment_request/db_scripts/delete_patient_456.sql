delete from ct_image
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 545);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from general_image where patient_pk_id = 545);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 545);

delete from general_image  
where patient_pk_id = 545;

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 545);

delete from general_series  
where patient_pk_id = 545;

delete from study 
where patient_pk_id = 545;

delete from patient 
where patient_pk_id = 545;





delete from ct_image
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 544);

delete from qa_status_history
where general_image_pk_id in (select image_pk_id from general_image where patient_pk_id = 544);

delete from group9_dicom_tags
where image_pk_id in (select image_pk_id from general_image where patient_pk_id = 544);

delete from general_image  
where patient_pk_id = 544;

delete from annotation  
where general_series_pk_id in (select general_series_pk_id from general_series where patient_pk_id = 544);

delete from general_series  
where patient_pk_id = 544;

delete from study 
where patient_pk_id = 544;

delete from patient 
where patient_pk_id = 544;