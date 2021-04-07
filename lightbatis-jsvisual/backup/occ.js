risk_level=0
(smoke_status,score_smoke_status)
1,0
2,10
3,0

(alcohol_status,score_drinking_status)
1,10
2,0
3,0

(betel_nuts_cons_sta,score_betel_nuts_cons_sta)
1,25
2,0

(oral_diseases,score_oral_diseases)
1,25
2,0

r1=10
r2=10
r3=25
r4=25

p1=0.281
p2=0.09
p3=0.019
p4=0.007

score_ave=r1*p1+r2*p2+r3*p3+r4*p4
score=score_smoke_status+score_drinking_status+score_betel_nuts_cons_sta+score_oral_diseases
rr=score/score_ave

if rr>=1.1 then risk_level=3 endif
if rr >=0.9 && rr<1.1 then risk_level=2 endif
if rr<0.9 then risk_level=1 endif

if risk_level==3 then risk_msg='高风险' endif
if risk_level==2 then risk_msg='中风险' endif
if risk_level==1 then risk_msg='低风险' endif
return {risk_level:risk_level,risk_msg:risk_msg}