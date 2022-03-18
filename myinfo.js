
let equiptypeArray=["camera","lens","tripod","cameraacc","monitor","lamplight","ledlight","lightacc","stand","studio"];
let equipNameArray=[];

fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/reservations/me?student_number=2017311456")
        .then(function(res){
            res.json()
        .then(function(json){
            console.log(json.reservations)
            document.getElementById('myid').innerHTML+=json.studentId
        })
    })

    function getTodayTime(){
        const now=new Date();
        const year=now.getFullYear();
        const month=now.getMonth()+1;
        const date=now.getDate();
    
        let nT=`${year}-${month >= 10 ? month : '0' + month}-${date >= 10 ? date : '0' + date}`;
        
        document.getElementById("lentalDate").value=nT;
        let nowTime=`${year}${month >= 10 ? month : '0' + month}${date >= 10 ? date : '0' + date}00`;
        return parseInt(nowTime);
    }

//장비조회호출해서 equipnamearray에 넣어두기
function getEquipName(){
    fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/equipments")
    .then(function(res){
        res.json()
        .then(function(json){
            for(i=0;i<json.length;i++){
                equipNameArray[i]=json[i].name
            }
        })
    })
}
    
getEquipName()
getMyLentalEquip(getTodayTime())

//내정보에서 ㅇ대여날짜선택했을 떄 숫자로바꾸기
function getMyLentalTime(){
    equipInitialize();
    let ldate=document.getElementById("lentalDate").value.replace(/\-/g,'')+"00";
    
    return parseInt(ldate);
    ;

}




function getMyLentalEquip(ldate){
    fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/reservations/me?student_number=2017311456")
    .then(function(res){
        res.json()
            .then(function(json){
                for(i=0;i<json.reservations.length;i++){
                    if(ldate>=json.reservations[i].startTime && ldate<json.reservations[i].endTime){
                        let type=equiptypeArray[(json.reservations[i].equipmentTypeId)-1]
                        document.querySelector("."+type).innerHTML+=
                        "<li> 대여번호: "+json.reservations[i].id + "<br>장비번호 : "+json.reservations[i].equipmentId+" 장비이름: "+equipNameArray[json.reservations[i].equipmentId-1]+"</li>";

                        //document.getElementById("myLentalList").append(json.reservations[i].equipmentId)
                    }
                }            

            })
    })
       
}

//어떻게 하는지 몰라서 장비리스트 초기화하기
function equipInitialize(){
    let lilist=document.querySelectorAll("li");
    lilist.forEach((ll)=>{
        ll.remove();
    })   
}