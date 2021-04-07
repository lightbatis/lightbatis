if ( waist < 85  &&  gender == 1){
    mwaist = 1;
    swaist={code:'0',label:'正常'}
}
if ( waist < 85  &&  gender == 2){
    mwaist = 1;
    swaist={code:'0',label:'正常'}
}
console.log('mwaist =' + mwaist);
console.log("result = " + ((85 <= waist) && (waist < 95)));
if ( (85 <= waist < 95)  &&  gender == 1){
    mwaist = 2.445;
    swaist={code:'4',label:'超重'}
}
if ( 80 <= waist < 90  &&  gender == 2){
    mwaist = 2.225;
    swaist={code:'4',label:'超重'}
}
console.log('mwaist =' + mwaist);
if ( waist >= 95  &&  gender == 1){
    mwaist = 2.52;
    swaist={code:'5',label:'肥胖'}
}
if ( waist >= 90  &&  gender == 2){
    mwaist = 2.4;
    swaist={code:'5',label:'肥胖'}
}
console.log('mwaist =' + mwaist);
