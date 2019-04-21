//common-use
function refresh(){
    //刷新当前页面
    location.reload();
}

Date.prototype.Format = function(formatStr)
{
    var str = formatStr;
    var Week = ['日','一','二','三','四','五','六'];

    str=str.replace(/yyyy|YYYY/,this.getFullYear());
    str=str.replace(/yy|YY/,(this.getYear() % 100)>9?(this.getYear() % 100).toString():'0' + (this.getYear() % 100));
    str=str.replace(/MM/,this.getMonth()>9?(this.getMonth()+1).toString():'0' + (this.getMonth()+1));
    str=str.replace(/M/g,this.getMonth()+1);

    str=str.replace(/w|W/g,Week[this.getDay()]);

    str=str.replace(/dd|DD/,this.getDate()>9?this.getDate().toString():'0' + this.getDate());
    str=str.replace(/d|D/g,this.getDate());

    str=str.replace(/hh|HH/,this.getHours()>9?this.getHours().toString():'0' + this.getHours());
    str=str.replace(/h|H/g,this.getHours());
    str=str.replace(/mm/,this.getMinutes()>9?this.getMinutes().toString():'0' + this.getMinutes());
    str=str.replace(/m/g,this.getMinutes());

    str=str.replace(/ss|SS/,this.getSeconds()>9?this.getSeconds().toString():'0' + this.getSeconds());
    str=str.replace(/s|S/g,this.getSeconds());

    return str;
}
var myDate = new Date();
var startTime=myDate.Format("yyyy-MM-dd hh:mm");
myDate.setTime(myDate.getTime()+24*60*60*1000);
endTime=myDate.Format("yyyy-MM-dd hh:mm");

//index_page
function GetTimefromStartTimePicker(){
    var time=document.getElementById('startTimePicker').value.toString();
    var result;
    var reg = "T";  //[a-zA-Z]表示匹配字母，g表示全局匹配
    while (result = time.match(reg)) { //判断str.match(reg)是否没有字母了
        time = time.replace(result[0], ' '); //替换掉字母  result[0] 是 str.match(reg)匹配到的字母
    }
    startTime = time;
    document.getElementById('startTimeBox').value = time;
}

function GetTimefromEndTimePicker(){
    var time = document.getElementById('endTimePicker').value.toString();
    var result;
    var reg = "T";  //[a-zA-Z]表示匹配字母，g表示全局匹配
    while (result = time.match(reg)) { //判断str.match(reg)是否没有字母了
        time = time.replace(result[0], ' '); //替换掉字母  result[0] 是 str.match(reg)匹配到的字母
    }
    endTime = time;
    document.getElementById('endTimeBox').value = time;
}

function selectPeople(){
    var choice = document.getElementById('select_people').value;
    var res;
    switch(choice){
        case '1': res="滚动名片";break;
        case '2': res="3D球体";break;
        case '3': res="滚动数字";break;
        case '4': res="翻牌子";break;
    }
    document.getElementById('selectPeopleBox').value = res;
}

function selectPrize(){
    var choice = document.getElementById('select_prize').value;
    var res;
    switch(choice){
        case '1': res="刮奖";break;
        case '2': res="转盘";break;
        case '3': res="猜方格";break;
        case '4': res="翻牌";break;
    }
    document.getElementById('selectPrizeBox').value = res;
}

function isLimitNumOfPeople(){
    var state=document.getElementById('limitNumOfPeopleCB').value.toString();
}

//award_settings_page
var counter = 3;
function AddItems(){
    counter++;
    var newItems = '<div class="mdui-row-xs-4" id="item'+counter+'"><br><div class="mdui-col"><input id="award-name'+counter+'"class="mdui-textfield-input" type="text" placeholder="奖项名称" required/></div><div class="mdui-col"><input id="prize-name'+counter+'" class="mdui-textfield-input" type="text" placeholder="奖品名称"/></div><div class="mdui-col"><input id="prize-num'+counter+'" class="mdui-textfield-input" type="text" placeholder="奖品数量" required/></div><div class="mdui-col"><input id="winning-rate'+counter+'" class="mdui-textfield-input" type="text" placeholder="中奖概率" disabled/></div></div></div>';
    $("#prize-settings-area").append(newItems);
}
function CutItem(){
    if(counter>3){ 
        var str="#item"+(counter).toString();
        $(str).remove();
        counter--;
    }
    else{}
}

//offline-lottery-page
function startOfflineLottery(){
    window.open("./year/index.html");
}

//people-settings-page
function UnLimitedPeople(){
    if(document.getElementById('unlimitted').checked == true){
		document.getElementById('input-peoplenum').disabled = true;
	}
}
function LimittedPeople(){
	if(document.getElementById('limitted').checked == true){
		document.getElementById('input-peoplenum').disabled = false;
	}
}

function AllPeople(){
    document.getElementById('uploadbtn').disabled = true;
    document.getElementById('downloadbtn').disabled = true;
}
function SpecificObject(){
    document.getElementById('uploadbtn').disabled = false;
    document.getElementById('downloadbtn').disabled = false;
}

function UploadList(){
    document.getElementById('upload').click();
}
var zzexcel;
var num_of_people=0;
var people_list_for_ajax = null;
function sendfile(obj) {
    if(!obj.files) {
        return;
    }
    var f = obj.files[0];
    var reader = new FileReader();
    reader.readAsBinaryString(f);
    reader.onload = function(e) {
        var data = e.target.result;
            zzexcel = XLSX.read(data, {
                type: 'binary'
            });
        var jsonobj = XLSX.utils.sheet_to_json(zzexcel.Sheets[zzexcel.SheetNames[0]]);
        var items='<fieldset><legend>人员名单</legend><div class="mdui-row-xs-3"><div class="mdui-col" style="text-align:center; color:gray;">学号/工号</div><div class="mdui-col" style="text-align:center; color:gray;">姓名</div><div class="mdui-col" style="text-align:center; color:gray;">权重</div></div>';
        num_of_people = jsonobj.length; //记录人数
        document.getElementById('input-peoplenum').value=num_of_people;
        for(var i=1;i<=jsonobj.length;i++){
            items += '<div class="mdui-row-xs-3"><div class="mdui-col"><input class="mdui-textfield-input" type="text" id="id'+i+'" value="'+jsonobj[i-1].id+'" disabled/></div><div class="mdui-col"><input class="mdui-textfield-input" type="text" id="name'+i+'" value="'+jsonobj[i-1].name+'" disabled/></div><div class="mdui-col"><input class="mdui-textfield-input" type="text" id="weight'+i+'" value="0"/></div></div>'
        }
        items+='</fieldset>';
        $("#name-list-area").append(items);

        people_list_for_ajax = [];
        for(var i = 0; i < jsonobj.length; i++){
            people_list_for_ajax[i] = jsonobj[i].id;
        }
    }
}
function DownloadList(){

}
function ClearList(){
    $('#name-list-area').empty(); //可能sheetjs使用方法有问题，看戚子强发的网页！！
}

var firstPrizeAmount=0,secondPrizeAmount=0,thirdPrizeAmount=0,firstPrizeProbability=0,secondPrizeProbability=0,thirdPrizeProbability=0;
var firstPrizeName="", secondPrizeName="", thirdPrizeName="",firstAwardName="",secondAwardName="",thirdAwardName="";
function SavePeopleSettings(){
    if(document.getElementById('award-name1').value!=""
    &&document.getElementById('prize-num1').value!=""
    &&document.getElementById('award-name2').value!=""
    &&document.getElementById('prize-num2').value!=""
    &&document.getElementById('award-name3').value!=""
    &&document.getElementById('prize-num3').value!="") {
        firstPrizeName = document.getElementById('prize-name1').value;
        firstAwardName=document.getElementById('award-name1').value;;
        secondAwardName=document.getElementById('award-name2').value;;
        thirdAwardName=document.getElementById('award-name3').value;;
        firstPrizeAmount = parseInt(document.getElementById('prize-num1').value);
        secondPrizeName = document.getElementById('prize-name2').value;
        secondPrizeAmount = parseInt(document.getElementById('prize-num2').value);
        thirdPrizeName = document.getElementById('prize-name3').value;
        thirdPrizeAmount = parseInt(document.getElementById('prize-num3').value);
        var participantNum = num_of_people;
        if(document.getElementById('input-peoplenum').value!=""){
            participantNum = parseInt(document.getElementById('input-peoplenum').value);
            firstPrizeProbability = firstPrizeAmount/participantNum;
            document.getElementById('winning-rate1').value = firstPrizeProbability;
            secondPrizeProbability = secondPrizeAmount/participantNum;
            document.getElementById('winning-rate2').value = secondPrizeProbability;
            thirdPrizeProbability = thirdPrizeAmount/participantNum;
            document.getElementById('winning-rate3').value = thirdPrizeProbability;
        } else {
            alert("您还未填入活动参与者人数哦~");
            ShowPeopleSettingsPage();
        }
    } else {
        alert("您还未完成奖项设置哦~")
    }
}

//index-page
function CheckVip(){
    var isVip = true; //var isVip = getUserPrivilege(); 获取用户权限，查看该用户是否为Vip用户
    if(isVip == true){
        //该用户为Vip用户，允许用户自定义大屏幕背景
       $("#bg-list").hide();
       $('#upload-bg').show();
    }
    else{
        //用户为非Vip用户，弹出对话框，提醒充值
        document.getElementById('testbtn').click();
    }
}
function ShowDemoPct(){

    $('#upload-bg').hide();
    $('#bg-list').show();
    
}
function UploadBkg(){
    document.getElementById('upload-background').click();
}

function ShowBasicSettingsPage(){
    document.getElementById('basic-settings-page').style.display="block";
    document.getElementById('award-settings-page').style.display="none";
    document.getElementById('people-settings-page').style.display="none";
    document.getElementById('large-screen-lottery-page').style.display="none";
    document.getElementById('account-center-page').style.display="none";
    document.getElementById('activity-page').style.display="none";
}

function ShowAwardSettingsPage(){
    document.getElementById('award-settings-page').style.display="block";
    document.getElementById('basic-settings-page').style.display="none";
    document.getElementById('people-settings-page').style.display="none";
    document.getElementById('large-screen-lottery-page').style.display="none";
    document.getElementById('account-center-page').style.display="none";
    document.getElementById('activity-page').style.display="none";
}

function ShowPeopleSettingsPage(){
    document.getElementById('people-settings-page').style.display="block";
    document.getElementById('award-settings-page').style.display="none";
    document.getElementById('basic-settings-page').style.display="none";
    document.getElementById('large-screen-lottery-page').style.display="none";
    document.getElementById('account-center-page').style.display="none";
    document.getElementById('activity-page').style.display="none";
}

function ShowLargeScreenLotteryPage(){
    document.getElementById('large-screen-lottery-page').style.display="block";
    document.getElementById('people-settings-page').style.display="none";
    document.getElementById('award-settings-page').style.display="none";
    document.getElementById('basic-settings-page').style.display="none";
    document.getElementById('account-center-page').style.display="none";
    document.getElementById('activity-page').style.display="none";
}

function ShowAccountCenterPage(){
    document.getElementById('account-center-page').style.display="block";
    document.getElementById('large-screen-lottery-page').style.display="none";
    document.getElementById('people-settings-page').style.display="none";
    document.getElementById('award-settings-page').style.display="none";
    document.getElementById('basic-settings-page').style.display="none";
    document.getElementById('activity-page').style.display="none";
}

function ShowActivityPage(){
    document.getElementById('activity-page').style.display="block";
    document.getElementById('account-center-page').style.display="none";
    document.getElementById('large-screen-lottery-page').style.display="none";
    document.getElementById('people-settings-page').style.display="none";
    document.getElementById('award-settings-page').style.display="none";
    document.getElementById('basic-settings-page').style.display="none";
}

function checkData(){

    return true;
}
    
var is_add_activty_button_able = true;

function add(){
    if(is_add_activty_button_able){
        if(checkData()){
            is_add_activty_button_able = false;
            $.ajax({
                url : '/addActivity',
                type: 'POST',
                dataType: 'json',
                data: {
                    ActDescribe : document.getElementById('activ-name').value,
                    startTime : startTime,
                    endTime : endTime,
                    firstPrizeName : firstPrizeName,
                    firstAwardName : firstAwardName,
                    firstPrizeAmount : firstPrizeAmount,
                    firstPrizeProbability : firstPrizeProbability,
                    secondPrizeName : secondPrizeName,
                    secondAwardName : secondAwardName,
                    secondPrizeAmount : secondPrizeAmount,
                    secondPrizeProbability : secondPrizeProbability,
                    thirdPrizeName : thirdPrizeName,
                    thirdAwardName : thirdAwardName,
                    thirdPrizeAmount : thirdPrizeAmount,
                    thirdPrizeProbability : thirdPrizeProbability,
                    userList : people_list_for_ajax
                },
                success : function(res){
                    alert('上传成功');
                },
                error : function(){
                    alert('网络有些问题');
                },
                complete : function(){
                    is_add_activty_button_able = true;
                }
            })
            // $.ajax({
            //     url : '/addActivity',
            //     type : 'POST',
            //     dataType: 'json',
            //     data : {
            //         ActDescribe : document.getElementById('activ-name').value,
            //         startTime : startTime,
            //         endTime : endTime,
            //         firstPrizeName : firstPrizeName,
            //         firstPrizeAmount : firstPrizeAmount,
            //         firstPrizeProbability : firstPrizeProbability,
            //         secondPrizeName : secondPrizeName,
            //         secondPrizeAmount : secondPrizeAmount,
            //         secondPrizeProbability : secondPrizeProbability,
            //         thirdPrizeName : thirdPrizeName,
            //         thirdPrizeAmount : thirdPrizeAmount,
            //         thirdPrizeProbability : thirdPrizeProbability
            //         //userList : people_list_for_ajax
            //
            //     },
            //     success : function(res){
            //         alert('上传成功');
            //     },
            //     error : function(){
            //         alert('网络有些问题');
            //     },
            //     complete : function(){
            //         is_add_activty_button_able = true;
            //     }
            // });
        }
        
    }
    
}