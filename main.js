fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/equipments")
.then(function(res){
    res.json()
.then(function(json){
    for(i=0;i<json.length;i++){
        if(json[i].typeName=='카메라'){
            document.querySelector(".camera").innerHTML+=
            "<li>"+
            json[i].id + " " + json[i].name + "</li>";
        }else if(json[i].typeName=='렌즈'){
            document.querySelector(".lens").innerHTML+=
            "<li>"+
            json[i].id + " " + json[i].name + "</li>"; 
        }
       
    }

    })
})

function getTodayEquip(){
    const todayDate=new Date();
    const year=todayDate.getFullYear();
    const month=todayDate.getMonth()+1;
    const date=todayDate.getDate();

    var tdate=`${year}${month >= 10 ? month : '0' + month}${date >= 10 ? date : '0' + date}`
    return parseInt(tdate)    
}

/*
fetch("http://ec2-54-180-100-40.ap-northeast-2.compute.amazonaws.com:48080/equipments")
.then(response=>response.json())

.then(data=>{
    const container=document.querySelector(".items");
    
        container.innerHTML=data.map((item)=>createHTMLString(item)).join("");
    
    console.log(data[0])
});


function createHTMLString(item){
    return `
        <li class="item">
            <p class="itemdesc">${item.name}</p>
            </li>
    `;
}

*/


// const info=document.createElement("div");
//     info.textContent=data.map((item) =>item.name);
    
//     demo.appendChild(info);