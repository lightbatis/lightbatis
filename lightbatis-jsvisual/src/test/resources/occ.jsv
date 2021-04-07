if diab_his==1 then return { rstatu:2} endif
if fbg>=7.0 && diab_his==2 then return { rstatu:3} endif

//参数库  B=β,P=P
(gender,B11)
1,1
2,1

(gender,B12)
1,1.4
2,1.41

(gender,B21)
1,1
2,1

(gender,B22)
1,2.45
2,2.25

(gender,B23)
1,2.53
2,2.41

(gender,B31)
1,1
2,1

(gender,B32)
1,2.445
2,2.25

(gender,B33)
1,2.52
2,2.4

(gender,B41)
1,1
2,1

(gender,B42)
1,1.29
2,1.25

(gender,B51)
1,1
2,1

(gender,B52)
1,1.2
2,1.2

(gender,B61)
1,1
2,1

(gender,B62)
1,3.6
2,3.7

(gender,B71)
1,1
2,1

(gender,B72)
1,1.45
2,1.4

(gender,B81)
1,1
2,1

(gender,B82)
1,1.5
2,1.5

(gender,B91)
1,1
2,1

(gender,B81)
1,1
2,1

(gender,B92)
1,1.31
2,1.31

(gender,B101)
1,1
2,1

(gender,B102)
1,1.3
2,1.23

//P 参数库
(gender,P12)
1,0.06
2,0.06

(gender,P22)
1,0.114
2,0.112

(gender,P23)
1,0.041
2,0.049

(gender,P32)
1,0.101
2,0.09

(gender,P33)
1,0.021
2,0.019

(gender,P42)
1,0.307
2,0.4

(gender,P52)
1,0.407
2,0.405

(gender,P62)
1,0.015
2,0.015

(gender,P72)
1,0.119
2,0.179

(gender,P82)
1,0.038
2,0.0481

(gender,P92)
1,0.103
2,0.103

(gender,P102)
1,0.25
2,0.026

    //ASR参数库
(gender,ASR1)
1,0.4297
2,0.3455

(gender,ASR2)
1,3.4791
2,2.0612

(gender,ASR3)
1,8.9620
2,9.4139

(gender,ASR4)
1,11.5299
2,12.2051

(gender,ASR5)
1,11.5095
2,12.0034

//计算当前风险
(gender,hbp_his,mhbp_his)
1,1,1.45
1,2,1
2,1,1.4
2,2,1

(gender,diab_his_family,mdiab_his_family)
1,1,1.4
1,2,1
2,1,1.41
2,2,1

(gender,act,mact)
1,1,1
1,2,1
1,3,1.29
2,1,1
2,2,1
2,3,1.25

(gender,bmi,mbmi)
1,(-,24),1
1,[24,28),2.45
1,[28,+),2.53
2,(-,24),1
2,[24,28),2.25
2,[28,+),2.41

(gender,fbg,mfbg)
1,(-,6.1),1
1,[6.1,7.0),3.6
2,(-,6.1),1
2,[6.1,7.0),3.7

(gender,hdl,mhdl)
1,(-,1.04),1.31
1,[1.04,+),1
2,(-,1.04),1.31
2,[1.04,+),1

(gender,tg,mtg)
1,(-,1.7),1
1,[1.7,+),1.5
2,(-,1.7),1
2,[1.7,+),1.5

(gender,waist,mwaist)
1,(-,85),1
1,[85,95),2.445
1,[95,+),2.52
2,(-,80),1
2,[80,90),2.25
2,[90,+),2.4

(gender,smoke_status,msmoke_status)
1,1,1.3
1,2,1
1,3,1
2,1,1.23
2,2,1
2,3,1

(gender,vegefru,mvegefru)
1,1,1
1,2,1.2
2,1,1
2,2,1.2


// 计算相对风险rr_diab
M=mhbp_his*mdiab_his_family*mact*mbmi*mfbg*mhdl*mtg*mwaist*msmoke_status*mvegefru
N=(B12*P12+1-P12)*(B22*P22+1-P22)* (B23*P23+1-P23)* (B32* P32+1-P32)*(B33* P33+1-P33)*(B42*P42+1-P42)*(B52*P52+1-P52)*(B62* P62+1-P62)*(B72*P72+1-P72)*(B82*P82+1-P82) *(B92*P92+1-P92) *(B102*P102+1-P102)
rr_huier=M/N

//计算当前风险
if age<35 then rrab_huier=rr_huier*ASR1 endif
if age >= 35 && age<45 then rrab_huier=rr_huier*ASR2 endif
if age >= 45 && age<55 then rrab_huier=rr_huier*ASR3 endif
if age >= 55 && age<65 then rrab_huier=rr_huier*ASR4 endif
if age>=65 then rrab_huier=rr_huier*ASR5 endif

//开始理想风向
(gender,hbp_his,mhbp_his)
1,1,1.45
1,2,1
2,1,1.4
2,2,1

(gender,diab_his_family,mdiab_his_family)
1,1,1.4
1,2,1
2,1,1.41
2,2,1

mact=1

mbmi=1

mfbg=1

mhdl=1

mtg=1

mwaist=1

msmoke_status=1

mvegefru=1


// 计算相对风险rr_diab
M=mhbp_his*mdiab_his_family*mact*mbmi*mfbg*mhdl*mtg*mwaist*msmoke_status*mvegefru
N=(B12*P12+1-P12)*(B22*P22+1-P22)* (B23*P23+1-P23)* (B32* P32+1-P32)*(B33* P33+1-P33)*(B42*P42+1-P42)*(B52*P52+1-P52)*(B62* P62+1-P62)*(B72*P72+1-P72)*(B82*P82+1-P82) *(B92*P92+1-P92) *(B102*P102+1-P102)
rr_ideal_huier=M/N

//计算当前风险
if age<35 then rrab_ideal_huier=rr_ideal_huier*ASR1 endif
if age >= 35 && age<45 then rrab_ideal_huier=rr_ideal_huier*ASR2 endif
if age >= 45 && age<55 then rrab_ideal_huier=rr_ideal_huier*ASR3 endif
if age >= 55 && age<65 then rrab_ideal_huier=rr_ideal_huier*ASR4 endif
if age>=65 then rrab_ideal_huier=rr_ideal_huier* ASR5 endif

//计算结果
return {rstatu:1,nowData:{rr_huier:rr_huier,rrab_huier:rrab_huier},idealData:{rrab_ideal_huier:rrab_ideal_huier,rr_ideal_huier:rr_ideal_huier}}
