function login(){
    alert("登录中...");
    var usrname = $("#usrname").val();
    var passwd = $("#passwd").val();
    var checkbox = $("#mgr").is(':checked');
    var json = {
        "usrname":usrname,
        "passwd":passwd
    };
    $.ajax({
        type:"get",
        url:"login.php",
        data:json,
        async:true,
        datatype:"json",
        success:function(res){
            console.log(res);
            if(res == 1){
                alert("登录成功");
                if(checkbox){
                    window.location.href = "../../Manager/award_settings.html";
                }else {
                    window.location.href = "index.html";
                }
            }else if(res == -1){
                alert("密码错误或用户名不存在");
            }else{
                alert("服务器正忙");
            }
        },
        error:function () {
            alert("系统异常，请稍后重试！");
        }
    });
}

function signup(){
    var usrname = $("#usrname").val();
    var name = $("#name").val();
    var phone = $("#phone").val();
    var email = $("#email").val();
    var passwd = $("#passwd").val();
    var passwd_re = $("#passwd_re").val();
    if(passwd_re !== passwd){
        alert("两次密码必须相同，请重试");
    }else if(name == '') {
        alert("昵称不能为空");
    }else if(email == ''){
        alert("电子邮箱不能为空");
    }else if(passwd == ''){
        alert("密码不能为空");
    }else if(usrname == ''){
        alert("用户名不能为空");
    }else{
        var json = {
            "usrname":usrname,
            "name":name,
            "email":email,
            "phone":phone,
            "passwd":passwd
        };
        $.ajax({
            type:"post",
            url:"signup.php",
            data:json,
            async:true,
            datatype:"json",
            success:function(res){
                console.log(res);
                if(res == 0){
                    alert("用户名已被注册，请重试");
                }else if(res == -1){
                    alert("格式错误，请重试");
                }else{
                    alert("注册成功");
                    window.location.href = "login.html";
                }
            },
            error:function () {
                alert("系统异常，请稍后重试！");
            }
        });
    }
}