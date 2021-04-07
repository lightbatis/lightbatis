function main () {
if (stroke_his==1) { return {rstata:2} }

_matrix(['gender','B1'],[
[1,0.0408],
[2,0.0729]
])



_matrix(['gender','B2'],[
[1,0.0132],
[2,0.0161]
])



_matrix(['gender','B3'],[
[1,0.00019],
[2,0.00026]
])



_matrix(['gender','B4'],[
[1,0.786],
[2,0.4604]
])



_matrix(['gender','B5'],[
[1,0.7864],
[2,0.8055]
])



_matrix(['gender','B6'],[
[1,0.5224],
[2,0.5419]
])



_matrix(['gender','B7'],[
[1,0.7798],
[2,0.7173]
])



_matrix(['gender','B8'],[
[1,0.6429],
[2,0.6204]
])



_matrix(['age','gender','P'],[
[_lessThan(25,age),1,26.3],
[_lessThan(25,age),2,8.8],
[_rangeClose(25,34,age),1,35.6],
[_rangeClose(25,34,age),2,17.0],
[_rangeClose(35,44,age),1,67.6],
[_rangeClose(35,44,age),2,30.1],
[_rangeClose(45,54,age),1,205],
[_rangeClose(45,54,age),2,88.1],
[_rangeClose(55,64,age),1,459.7],
[_rangeClose(55,64,age),2,298.8],
[_atLeast(65,age),1,715.5],
[_atLeast(65,age),2,576.4]
])













_matrix(['hbp_medi','mhbp_medi'],[
[1,1],
[2,0]
])



sbp1=sbp

if (sbp<110 || sbp>200) { msbp=0 }
if (sbp>=110 && sbp<=200) { msbp=(mhbp_medi*(sbp-110)*(200-sbp)) }

_matrix(['cvd','mcvd'],[
[1,1],
[2,0]
])



_matrix(['lvh','mlvh'],[
[1,1],
[2,0]
])



_matrix(['smoke_status','msmoke_status'],[
[1,0],
[2,1],
[3,0]
])




_matrix(['af','maf'],[
[1,1],
[2,0]
])



_matrix(['diab_his','mdiab_his'],[
[1,1],
[2,0]
])



_matrix(['act','mact'],[
[1,2],
[2,1],
[3,0]
])




_matrix(['stroke_his_family','mstroke_his_family'],[
[1,1],
[2,0]
])



mbmi=bmi

R1= B1*age
R2= B2*sbp1
R3= B3*msbp
R4= B4*mcvd
R5= B5*mlvh
R6= B6*msmoke_status
R7= B7*maf
R8= B8*mdiab_his

if (gender==1) { P1=1-(Math.pow(0.9044,Math.exp(R1+R2+R3+R4+R5+R6+R7+R8-2.777))) }
if (gender==2) { P1=1-(Math.pow(0.9353,Math.exp(R1+R2+R3+R4+R5+R6+R7+R8-4.6766))) }

logit2 = Math.log(P1/(1-P1))

P2=1/(1+Math.exp(-logit2+0.1824*(mact-2)))

logit3=Math.log(P2/(1-P2))

P3=1/(1+Math.exp(-logit3-0.1*(mbmi-26)))

logit4=Math.log(P3/(1-P3))

P4=1/(1+Math.exp(-logit4-0.26*(0-0.2)))

logit5=Math.log(P4/(1-P4))

P5=1/(1+Math.exp(-logit5-0.59*(mstroke_his_family-0.05)))

rrab_huier=(1-Math.pow((1-P5),0.5))*1000

rr_huier=rrab_huier/P


//*计算理想风险*
if (sbp<110) { sbp1=sbp }
if (sbp>=110) { sbp1=110 }
msbp=0

msmoke_status=0

mact=2

if (bmi>=24) { mbmi=24 }
if (bmi<24) { mbmi=bmi }


R1= B1*age
R2= B2*sbp1
R3= B3*msbp
R4= B4*mcvd
R5= B5*mlvh
R6= B6*msmoke_status
R7= B7*maf
R8= B8*mdiab_his

if (gender==1) { P1=1-(Math.pow(0.9044,Math.exp(R1+R2+R3+R4+R5+R6+R7+R8-2.777))) }
if (gender==2) { P1=1-(Math.pow(0.9353,Math.exp(R1+R2+R3+R4+R5+R6+R7+R8-4.6766))) }

logit2 = Math.log(P1/(1-P1))

P2=1/(1+Math.exp(-logit2+0.1824*(mact-2)))

logit3=Math.log(P2/(1-P2))

P3=1/(1+Math.exp(-logit3-0.1*(mbmi-26)))

logit4=Math.log(P3/(1-P3))

P4=1/(1+Math.exp(-logit4-0.26*(0-0.2)))

logit5=Math.log(P4/(1-P4))

P5=1/(1+Math.exp(-logit5-0.59*(mstroke_his_family-0.05)))

rrab_ideal_huier=(1-Math.pow((1-P5),0.5))*1000

rr_ideal_huier=rrab_ideal_huier/P*1 //100%


return {rstatu:1,nowData:{rr:rr_huier,rrab:rrab_huier},idealData:{rrab:rrab_ideal_huier,rr:rr_ideal_huier}}

}