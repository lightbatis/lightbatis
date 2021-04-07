function main () {
if (chd_his==1) { return { rstatu:2} }


    //参数库  B=β
_matrix(['gender','B1'],[
[1,0.04],
[2,0.12]
])



_matrix(['gender','B2'],[
[1,0.09],
[2,-0.74]
])



_matrix(['gender','B3'],[
[1,0],
[2,0]
])



_matrix(['gender','B4'],[
[1,0.42],
[2,-0.37]
])



_matrix(['gender','B5'],[
[1,0.66],
[2,0.22]
])



_matrix(['gender','B6'],[
[1,0.90],
[2,0.61]
])



_matrix(['gender','B7'],[
[1,-0.38],
[2,0.21]
])



_matrix(['gender','B8'],[
[1,0],
[2,0]
])



_matrix(['gender','B9'],[
[1,0.57],
[2,0.44]
])



_matrix(['gender','B10'],[
[1,0.74],
[2,0.56]
])



_matrix(['gender','B11'],[
[1,0.83],
[2,0.89]
])



_matrix(['gender','B12'],[
[1,0.85],
[2,0.63]
])



_matrix(['gender','B13'],[
[1,0.37],
[2,0.60]
])



_matrix(['gender','B14'],[
[1,0],
[2,0.45]
])



_matrix(['gender','B15'],[
[1,0],
[2,0]
])



_matrix(['gender','B16'],[
[1,-0.46],
[2,-0.54]
])



_matrix(['gender','B17'],[
[1,0.53],
[2,0.67]
])



_matrix(['gender','B18'],[
[1,0.73],
[2,0.58]
])




    //m 参数库
_matrix(['gender','m1'],[
[1,47.4],
[2,46.3]
])



_matrix(['gender','m2'],[
[1,0.36],
[2,0.46]
])



_matrix(['gender','m3'],[
[1,0.22],
[2,0.21]
])



_matrix(['gender','m4'],[
[1,0.13],
[2,0.11]
])



_matrix(['gender','m5'],[
[1,0.19],
[2,0.14]
])



_matrix(['gender','m6'],[
[1,0.10],
[2,0.08]
])



_matrix(['gender','m7'],[
[1,0.27],
[2,0.26]
])



_matrix(['gender','m8'],[
[1,0.42],
[2,0.41]
])



_matrix(['gender','m9'],[
[1,0.22],
[2,0.22]
])



_matrix(['gender','m10'],[
[1,0.06],
[2,0.07]
])



_matrix(['gender','m11'],[
[1,0.03],
[2,0.04]
])



_matrix(['gender','m12'],[
[1,0.26],
[2,0.24]
])



_matrix(['gender','m13'],[
[1,0.22],
[2,0.16]
])



_matrix(['gender','m14'],[
[1,0.14],
[2,0.33]
])



_matrix(['gender','m15'],[
[1,0.25],
[2,0.26]
])



_matrix(['gender','m16'],[
[1,0.33],
[2,0.41]
])



_matrix(['gender','m17'],[
[1,0.59],
[2,0.34]
])



_matrix(['gender','m18'],[
[1,0.06],
[2,0.15]
])




_matrix(['age','gender','p'],[
[_lessThan(35,age),1,147.4],
[_lessThan(35,age),2,25.4],
[_rangeClose(35,44,age),1,160.2],
[_rangeClose(35,44,age),2,135.9],
[_rangeClose(45,54,age),1,282.4],
[_rangeClose(45,54,age),2,219.2],
[_rangeClose(55,64,age),1,469.8],
[_rangeClose(55,64,age),2,454.1],
[_rangeClose(65,74,age),1,1020.9],
[_rangeClose(65,74,age),2,537.6],
[_rangeClose(75,84,age),1,1343.6],
[_rangeClose(75,84,age),2,1243.2],
[_atLeast(85,age),1,2093.5],
[_atLeast(85,age),2,2379.7]
])
















// 计算过程
sbp1=0
sbp2=0
sbp3=0
sbp4=0
sbp5=0
_matrix(['sbp','dbp','sbp1'],[
[_lessThan(120,sbp),_lessThan(80,dbp),1]
])


_matrix(['sbp','dbp','sbp2'],[
[_rangeCloseOpen(120,130,sbp),_rangeCloseOpen(80,85,dbp),1]
])


_matrix(['sbp','dbp','sbp3'],[
[_rangeCloseOpen(130,140,sbp),_rangeCloseOpen(85,90,dbp),1]
])


_matrix(['sbp','dbp','sbp4'],[
[_rangeCloseOpen(140,160,sbp),_rangeCloseOpen(90,100,dbp),1]
])


_matrix(['sbp','dbp','sbp5'],[
[_atLeast(160,sbp),_atLeast(100,dbp),1]
])


tsbp1=sbp1
tsbp2=sbp2
tsbp3=sbp3
tsbp4=sbp4
tsbp5=sbp5

tc1=0
tc2=0
tc3=0
tc4=0
tc5=0
_matrix(['tc','tc1'],[
[_lessThan(4.54,tc),1]
])


_matrix(['tc','tc2'],[
[_rangeCloseOpen(4.54,5.18,tc),1]
])


_matrix(['tc','tc3'],[
[_rangeCloseOpen(5.18,6.22,tc),1]
])


_matrix(['tc','tc4'],[
[_rangeCloseOpen(6.22,7.24,tc),1]
])


_matrix(['tc','tc5'],[
[_atLeast(7.24,tc),1]
])


hdl1=0
hdl2=0
hdl3=0
hdl4=0
hdl5=0

_matrix(['hdl','hdl1'],[
[_lessThan(0.91,hdl),1]
])


_matrix(['hdl','hdl2'],[
[_rangeCloseOpen(0.91,1.04,hdl),1]
])


_matrix(['hdl','hdl3'],[
[_rangeCloseOpen(1.04,1.29,hdl),1]
])


_matrix(['hdl','hdl4'],[
[_rangeCloseOpen(1.29,1.55,hdl),1]
])


_matrix(['hdl','hdl5'],[
[_atLeast(1.55,hdl),1]
])


_matrix(['smoke_status','msmoke_status'],[
[1,0],
[2,1],
[3,0]
])




_matrix(['diab_his','mdiab_his'],[
[1,1],
[2,0]
])





// 计算当前风险rrab_chd
r1=B1 * (age-m1)
r2=B2* (sbp1-m2)
r3=B3 * (sbp2-m3)
r4=B4 * (sbp3-m4)
r5=B5 * (sbp4-m5)
r6=B6 * (sbp5-m6)
r7=B7 * (tc1-m7)
r8=B8* (tc2-m8)
r9=B9* (tc3-m9)
r10=B10* (tc4-m10)
r11=B11 * (tc5-m11)
r12=B12 * (hdl1-m12)
r13=B13 * (hdl2-m13)
r14=B14* (hdl3-m14)
r15=B15 * (hdl4-m15)
r16=B16 * (hdl5-m16)
r17=B17* (msmoke_status-m17)
r18=B18* (mdiab_his-m18)
r_total=r1+ r2+ r3+ r4+ r5+ r6+ r7+ r8+ r9+ r10+ r11+ r12+ r13+ r14+ r15+ r16+ r17+ r18


if (gender==1) { rrab_huier=[1-(Math.pow(0.9895,Math.exp(r_total)))]*100*1000 }
if (gender==2) { rrab_huier=[1-(Math.pow(0.9961,Math.exp(r_total)))]*100*1000 }


//计算相对风险rr_chd
rr_huier=rrab_huier/p


//计算理想风险
sbp1=1
sbp2=0
sbp3=0
sbp4=0
sbp5=0

tc1=1
tc2=0
tc3=0
tc4=0
tc5=0

hdl1=0
hdl2=0
hdl3=0
hdl4=0
hdl5=1

msmoke_status=0


// 计算理想的相对风险rr_ideal_diab
r1=B1 * (age-m1)
r2=B2* (sbp1-m2)
r3=B3 * (sbp2-m3)
r4=B4 * (sbp3-m4)
r5=B5 * (sbp4-m5)
r6=B6 * (sbp5-m6)
r7=B7 * (tc1-m7)
r8=B8* (tc2-m8)
r9=B9* (tc3-m9)
r10=B10* (tc4-m10)
r11=B11 * (tc5-m11)
r12=B12 * (hdl1-m12)
r13=B13 * (hdl2-m13)
r14=B14* (hdl3-m14)
r15=B15 * (hdl4-m15)
r16=B16 * (hdl5-m16)
r17=B17* (msmoke_status-m17)
r18=B18* (mdiab_his-m18)
r_total=r1+ r2+ r3+ r4+ r5+ r6+ r7+ r8+ r9+ r10+ r11+ r12+ r13+ r14+ r15+ r16+ r17+ r18


if (gender==1) { rrab_ideal_huier=[1-(Math.pow(0.9895,Math.exp(r_total)))]*100*1000 }
if (gender==2) { rrab_ideal_huier=[1-(Math.pow(0.9961,Math.exp(r_total)))]*100*1000 }


//计算相对风险rr_chd
rr_ideal_huier=rrab_ideal_huier/p

if (rr_ideal_huier > rr_huier) { rr_huier=rr_ideal_huier }
if (rrab_ideal_huier>rrab_huier) { rrab_huier=rrab_ideal_huier }

//计算结果
return {rstatu:1,yy:{t1:tsbp1,t2:tsbp2,t3:tsbp3,t4:tsbp4,t5:tsbp5},nowData:{rr_huier:rr_huier,rrab_huier:rrab_huier},idealData:{rrab_ideal_huier:rrab_ideal_huier,rr_ideal_huier:rr_ideal_huier}}

}