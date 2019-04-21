var g_Interval = 1;
var g_PersonCount = 500;//参加抽奖人数
var g_Timer;
var running = false;
var users;

$.ajax({url:"/getUser",success:function(result){
		users=result.message
	}});
function beginRndNum(trigger){


	if(running){
		running = false;
		clearTimeout(g_Timer);		
		$(trigger).val("开始");
		$('#ResultNum').css('color','red');
	}
	else{
		running = true;
		$('#ResultNum').css('color','black');
		$(trigger).val("停止");
		beginTimer();
	}
}

function updateRndNum(){
	var index = Math.floor((Math.random()*users.length));
	$('#ResultNum').html(users[index].name);
}

function beginTimer(){
	g_Timer = setTimeout(beat, g_Interval);
}

function beat() {
	g_Timer = setTimeout(beat, g_Interval);
	updateRndNum();
}