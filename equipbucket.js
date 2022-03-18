function zzimcheck(){
    equipInitialize()
    fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/reservations/bucket/me?student_number=2017311456")
    .then(function(res){
        res.json()
        .then(function(json){
            for(i=0;i<json.softReservations.length;i++){
                let type=equiptypeArray[(json.softReservations[i].equipmentTypeId)-1]
                document.querySelector("."+type).innerHTML+=
                "<li>"+
                "<label><input type='checkbox' name='equip' value="+json.softReservations[i].equipmentId+","+json.softReservations[i].equipmentTypeId+
                ","+json.softReservations[i].startTime+","+json.softReservations[i].endTime+" />"+
                
                equipNameArray[json.softReservations[i].equipmentId-1]+" <br>"+
                json.softReservations[i].startTime/100 + " ~ " +
                forlentalendDate(json.softReservations[i].endTime/100) +
                "</label> </li>"
            }
            
        })
    })
}

function forlentalendDate(endDate){
    let end=new Date((endDate).toString().replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3'))
    let finalend=new Date(end.setDate(end.getDate()-1));
    let tyear=finalend.getFullYear();
    let tmonth=finalend.getMonth()+1;
    let tdate=finalend.getDate();

    let value=`${tyear}${tmonth >= 10 ? tmonth : '0' + tmonth}${tdate >= 10 ? tdate : '0' + tdate}00`
    console.log(parseInt(value));
    return parseInt(value)

}

function lental(){
    const query='input[name="equip"]:checked';
    const selectedEls=document.querySelectorAll(query);

    selectedEls.forEach((el)=>{
        let lentalvalue=el.value.split(",")
        let lentalEquipId=lentalvalue[0];
        let lentalTypeId=lentalvalue[1];
        let lentalStart=lentalvalue[2];
        let lentalEnd=lentalvalue[3];
        lentalpost(lentalEquipId,lentalTypeId,lentalStart,lentalEnd)
    });
    alert("대여완.")
    zzimcheck()
}

function lentalpost(lentalEquipId,lentalTypeId,lentalStart,lentalEnd){
    fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/reservations",{
        method:"POST",
        headers:{
            "Content-Type":"application/json",
        },
        body:JSON.stringify({
            endTime:lentalEnd,
            equipments: [{
                id:lentalEquipId,
                typeId:lentalTypeId
            }],
            startTime:lentalStart,
            studentId:"syzz1105",
        }),
    })
    .then((response)=>console.log(response));
}

function equipInitialize(){
    let lilist=document.querySelectorAll("li");
    lilist.forEach((ll)=>{
        ll.remove();
    })   
}

zzimcheck()

