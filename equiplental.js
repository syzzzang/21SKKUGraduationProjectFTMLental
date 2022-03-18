
let equiptypeArray=["camera","lens","tripod","cameraacc","monitor","lamplight","ledlight","lightacc","stand","sound","studio"];

//처음켰을 때 오늘날짜랑 다음날날짜 받아서 starttime&&endtime문자열만들기
function getTodayTime(){
    const now=new Date();
    const year=now.getFullYear();
    const month=now.getMonth()+1;
    const date=now.getDate();

    const tomorrow=new Date(now.setDate(now.getDate()+1));
    const tyear=tomorrow.getFullYear();
    const tmonth=tomorrow.getMonth()+1;
    const tdate=tomorrow.getDate();

    let nT=`${year}-${month >= 10 ? month : '0' + month}-${date >= 10 ? date : '0' + date}`;
    let tT=`${tyear}-${tmonth >= 10 ? tmonth : '0' + tmonth}-${tdate >= 10 ? tdate : '0' + tdate}`;
    
    document.getElementById("startDate").value=nT
    document.getElementById("startDate").min=nT
    document.getElementById("endDate").value=tT
    document.getElementById("endDate").min=tT





    let nowTime=`${year}${month >= 10 ? month : '0' + month}${date >= 10 ? date : '0' + date}00`;
    let tomorrowTime=`${tyear}${tmonth >= 10 ? tmonth : '0' + tmonth}${tdate >= 10 ? tdate : '0' + tdate}00`;
    return "start_time="+nowTime+"&end_time="+tomorrowTime;
}
//장비대여 탭에서 날짜 클릭해서 버튼눌렀을때 starttime&&endtime문자열만들기
function getSubmitTime(){
    equipInitialize();
    console.log("submit타임+초기화")
    let sdate=document.getElementById("startDate").value.replace(/\-/g,'');
    let edate=document.getElementById("endDate").value.replace(/\-/g,'');
    console.log('start_time='+sdate+"00&end_time="+edate+"00")
    return 'start_time='+sdate+"00&end_time="+edate+"00";
    ;

}

//날짜문자열받아서 그 기간 동안 대여가능한 장비조회하기
function getEquip(edate){
    for(i=0;i<11;i++){
        let type=equiptypeArray[i];
        fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/reservations/types/"+(i+1)+"?"+edate)
        .then(function(res){
            res.json()
        .then(function(json){
             for(j=0;j<json.equipmentReservationStatuses.length;j++){
                if(json.equipmentReservationStatuses[j].isReserved==false){
                    document.querySelector("."+type).innerHTML+=
                    "<li>"+
                    "<label><input type='checkbox' name='equip' value="+json.equipmentReservationStatuses[j].equipment.id+","+json.equipmentReservationStatuses[j].equipment.typeId+" />"+
                    json.equipmentReservationStatuses[j].equipment.id + " " + json.equipmentReservationStatuses[j].equipment.name + "</label> </li>";
                }
            }
        
        })
        })
    }
}

function forzzimendDate(endDate){
    let end=new Date(endDate)
    let finalend=new Date(end.setDate(end.getDate()+1));
    let tyear=finalend.getFullYear();
    let tmonth=finalend.getMonth()+1;
    let tdate=finalend.getDate();

    let value=`${tyear}${tmonth >= 10 ? tmonth : '0' + tmonth}${tdate >= 10 ? tdate : '0' + tdate}00`
    console.log(parseInt(value));
    return parseInt(value)     
}

function zzim(){

    let sdate=parseInt(document.getElementById("startDate").value.replace(/\-/g,'')+"00");
    //let edate=parseInt(document.getElementById("endDate").value.replace(/\-/g,'')+"00");

    let edate=forzzimendDate(document.getElementById("endDate").value);

    const query='input[name="equip"]:checked';
    const selectedEls=document.querySelectorAll(query);

    selectedEls.forEach((el)=>{
        let zzimvalue=el.value.split(",")
        let zzimEquipId=zzimvalue[0];
        let zzimTypeId=zzimvalue[1];
        zzimpost(sdate, edate, zzimEquipId, zzimTypeId)
        //result+=zzimequipId+zzimTypeId+' ';
    });
    alert("찜했어용")

    getEquip(getSubmitTime())

}


function zzimpost(sdate, edate, zzimEquipId, zzimTypeId){
    fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/reservations/bucket",{
        method:"POST",
        headers:{
            "Content-Type":"application/json",
        },
        body:JSON.stringify({
            endTime: edate,
            equipment: {
                id:zzimEquipId,
                typeId:zzimTypeId
            },
            startTime:sdate,
            studentId:"syzz1105",
        }),
    })
    .then((response)=>console.log(response));

}




//어떻게 하는지 몰라서 장비리스트 초기화하기
function equipInitialize(){
    let lilist=document.querySelectorAll("li");
    lilist.forEach((ll)=>{
        ll.remove();
    })   
}


getEquip(getTodayTime())
