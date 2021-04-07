```sql
mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' cowen | gzip > ./cowen.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' cowen20_test | gzip > ./cowen20_test.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' cowen_beta | gzip > ./cowen_beta.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' cowen_www | gzip > ./cowen_www.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' cowen_www_bak | gzip > ./cowen_www_bak.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' cowen_www_tmp | gzip > ./cowen_www_tmp.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_activity | gzip > ./db_activity.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_activity_test | gzip > ./db_activity_test.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_api_platform | gzip > ./db_api_platform.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_data_etl | gzip > ./db_data_etl.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_dcms | gzip > ./db_dcms.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_dcms_dev | gzip > ./db_dcms_dev.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_dcms_test | gzip > ./db_dcms_test.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_dcms_test_bak | gzip > ./db_dcms_test_bak.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_disease_h5_demo | gzip > ./db_disease_h5_demo.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_hbdc_collect_active | gzip > ./db_hbdc_collect_active.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_open_api | gzip > ./db_open_api.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_primary_doctor_ylyd | gzip > ./db_primary_doctor_ylyd.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_tcmebs | gzip > ./db_tcmebs.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_tcmebs_test | gzip > ./db_tcmebs_test.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_yl_cloud_test | gzip > ./db_yl_cloud_test.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_ylan_dcms | gzip > ./db_ylan_dcms.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_ylan_promo | gzip > ./db_ylan_promo.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_ylan_promo_test | gzip > ./db_ylan_promo_test.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' db_ylanyx_promotional | gzip > ./db_ylanyx_promotional.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' doc_annotate | gzip > ./doc_annotate.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' rds | gzip > ./rds.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' wellcare | gzip > ./wellcare.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' wellcare_test | gzip > ./wellcare_test.sql.gz
 mysqldump -h10.30.2.197 -uroot -p'xYwYmYsql!@#$14' yl_cloud_test | gzip > ./yl_cloud_test.sql.gz
```

```sql
gunzip < db_activity_test.sql.gz | mysql -hlocalhost -uroot -p'123456' db_activity_test
```

```shell
 docker run -it -v /Users/lifei/kewenspace/db:/home mysql /bin/bash

mysql -hlocalhost -uroot -p'123456' db_activity_test.sql
```