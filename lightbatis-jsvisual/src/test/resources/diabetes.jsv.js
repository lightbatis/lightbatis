function main () {
    if (diab_his==1) { return { rstatu:2} }
    if (fbg>=7.0 && diab_his==2) { return { rstatu:3} }

//参数库  B=β,P=P
    _matrix(['gender','B11'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B12'],[
        [1,1.4],
        [2,1.41]
    ])



    _matrix(['gender','B21'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B22'],[
        [1,2.45],
        [2,2.25]
    ])



    _matrix(['gender','B23'],[
        [1,2.53],
        [2,2.41]
    ])



    _matrix(['gender','B31'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B32'],[
        [1,2.445],
        [2,2.25]
    ])



    _matrix(['gender','B33'],[
        [1,2.52],
        [2,2.4]
    ])



    _matrix(['gender','B41'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B42'],[
        [1,1.29],
        [2,1.25]
    ])



    _matrix(['gender','B51'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B52'],[
        [1,1.2],
        [2,1.2]
    ])



    _matrix(['gender','B61'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B62'],[
        [1,3.6],
        [2,3.7]
    ])



    _matrix(['gender','B71'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B72'],[
        [1,1.45],
        [2,1.4]
    ])



    _matrix(['gender','B81'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B82'],[
        [1,1.5],
        [2,1.5]
    ])



    _matrix(['gender','B91'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B81'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B92'],[
        [1,1.31],
        [2,1.31]
    ])



    _matrix(['gender','B101'],[
        [1,1],
        [2,1]
    ])



    _matrix(['gender','B102'],[
        [1,1.3],
        [2,1.23]
    ])



//P 参数库
    _matrix(['gender','P12'],[
        [1,0.06],
        [2,0.06]
    ])



    _matrix(['gender','P22'],[
        [1,0.114],
        [2,0.112]
    ])



    _matrix(['gender','P23'],[
        [1,0.041],
        [2,0.049]
    ])



    _matrix(['gender','P32'],[
        [1,0.101],
        [2,0.09]
    ])



    _matrix(['gender','P33'],[
        [1,0.021],
        [2,0.019]
    ])



    _matrix(['gender','P42'],[
        [1,0.307],
        [2,0.4]
    ])



    _matrix(['gender','P52'],[
        [1,0.407],
        [2,0.405]
    ])



    _matrix(['gender','P62'],[
        [1,0.015],
        [2,0.015]
    ])



    _matrix(['gender','P72'],[
        [1,0.119],
        [2,0.179]
    ])



    _matrix(['gender','P82'],[
        [1,0.038],
        [2,0.0481]
    ])



    _matrix(['gender','P92'],[
        [1,0.103],
        [2,0.103]
    ])



    _matrix(['gender','P102'],[
        [1,0.25],
        [2,0.026]
    ])



//ASR参数库
    _matrix(['gender','ASR1'],[
        [1,0.4297],
        [2,0.3455]
    ])



    _matrix(['gender','ASR2'],[
        [1,3.4791],
        [2,2.0612]
    ])



    _matrix(['gender','ASR3'],[
        [1,8.9620],
        [2,9.4139]
    ])



    _matrix(['gender','ASR4'],[
        [1,11.5299],
        [2,12.2051]
    ])



    _matrix(['gender','ASR5'],[
        [1,11.5095],
        [2,12.0034]
    ])



//计算当前风险
    _matrix(['gender','hbp_his','mhbp_his'],[
        [1,1,1.45],
        [1,2,1],
        [2,1,1.4],
        [2,2,1]
    ])





    _matrix(['gender','diab_his_family','mdiab_his_family'],[
        [1,1,1.4],
        [1,2,1],
        [2,1,1.41],
        [2,2,1]
    ])





    _matrix(['gender','act','mact'],[
        [1,1,1],
        [1,2,1],
        [1,3,1.29],
        [2,1,1],
        [2,2,1],
        [2,3,1.25]
    ])







    _matrix(['gender','bmi','mbmi'],[
        [1,_lessThan(24,bmi),1],
        [1,_rangeCloseOpen(24,28,bmi),2.45],
        [1,_atLeast(28,bmi),2.53],
        [2,_lessThan(24,bmi),1],
        [2,_rangeCloseOpen(24,28,bmi),2.25],
        [2,_atLeast(28,bmi),2.41]
    ])







    _matrix(['gender','fbg','mfbg'],[
        [1,_lessThan(6.1,fbg),1],
        [1,_rangeCloseOpen(6.1,7.0,fbg),3.6],
        [2,_lessThan(6.1,fbg),1],
        [2,_rangeCloseOpen(6.1,7.0,fbg),3.7]
    ])





    _matrix(['gender','hdl','mhdl'],[
        [1,_lessThan(1.04,hdl),1.31],
        [1,_atLeast(1.04,hdl),1],
        [2,_lessThan(1.04,hdl),1.31],
        [2,_atLeast(1.04,hdl),1]
    ])





    _matrix(['gender','tg','mtg'],[
        [1,_lessThan(1.7,tg),1],
        [1,_atLeast(1.7,tg),1.5],
        [2,_lessThan(1.7,tg),1],
        [2,_atLeast(1.7,tg),1.5]
    ])





    _matrix(['gender','waist','mwaist'],[
        [1,_lessThan(85,waist),1],
        [1,_rangeCloseOpen(85,95,waist),2.445],
        [1,_atLeast(95,waist),2.52],
        [2,_lessThan(80,waist),1],
        [2,_rangeCloseOpen(80,90,waist),2.25],
        [2,_atLeast(90,waist),2.4]
    ])







    _matrix(['gender','smoke_status','msmoke_status'],[
        [1,1,1.3],
        [1,2,1],
        [1,3,1],
        [2,1,1.23],
        [2,2,1],
        [2,3,1]
    ])







    _matrix(['gender','vegefru','mvegefru'],[
        [1,1,1],
        [1,2,1.2],
        [2,1,1],
        [2,2,1.2]
    ])






// 计算相对风险rr_diab
    M=mhbp_his*mdiab_his_family*mact*mbmi*mfbg*mhdl*mtg*mwaist*msmoke_status*mvegefru
    N=(B12*P12+1-P12)*(B22*P22+1-P22)* (B23*P23+1-P23)* (B32* P32+1-P32)*(B33* P33+1-P33)*(B42*P42+1-P42)*(B52*P52+1-P52)*(B62* P62+1-P62)*(B72*P72+1-P72)*(B82*P82+1-P82) *(B92*P92+1-P92) *(B102*P102+1-P102)
    rr_huier=M/N

//计算当前风险
    if (age<35) { rrab_huier=rr_huier*ASR1 }
    if (age >= 35 && age<45) { rrab_huier=rr_huier*ASR2 }
    if (age >= 45 && age<55) { rrab_huier=rr_huier*ASR3 }
    if (age >= 55 && age<65) { rrab_huier=rr_huier*ASR4 }
    if (age>=65) { rrab_huier=rr_huier*ASR5 }

//开始理想风向
    _matrix(['gender','hbp_his','mhbp_his'],[
        [1,1,1.45],
        [1,2,1],
        [2,1,1.4],
        [2,2,1]
    ])





    _matrix(['gender','diab_his_family','mdiab_his_family'],[
        [1,1,1.4],
        [1,2,1],
        [2,1,1.41],
        [2,2,1]
    ])





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
    if (age<35) { rrab_ideal_huier=rr_ideal_huier*ASR1 }
    if (age >= 35 && age<45) { rrab_ideal_huier=rr_ideal_huier*ASR2 }
    if (age >= 45 && age<55) { rrab_ideal_huier=rr_ideal_huier*ASR3 }
    if (age >= 55 && age<65) { rrab_ideal_huier=rr_ideal_huier*ASR4 }
    if (age>=65) { rrab_ideal_huier=rr_ideal_huier* ASR5 }

//计算结果
    return {rstatu:1,nowData:{rr_huier:rr_huier,rrab_huier:rrab_huier},idealData:{rrab_ideal_huier:rrab_ideal_huier,rr_ideal_huier:rr_ideal_huier}}


}