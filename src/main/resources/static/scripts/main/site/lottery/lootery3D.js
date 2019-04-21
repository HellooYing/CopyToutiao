// JavaScript Document
		var times=30;//抽奖次数
        var staffs = [];
        var wins = [];
		var init_fp=10,init_sp=20,init_tp=30;//123等奖初始数量
		var arrp = [0.1,0.3,0.4,0.2];//设置奖品及其中奖概率

        var rs, vi, ry;//三个参数   随意旋转，抖动效果，y轴旋转
        var camera, scene, renderer, controls;
        var objects = [];//对象
        var targets = {//目标
            table: [],//坐标数组
            sphere: [],
            helix: [],
            grid: [],
            bang: [],
        };
        (function() {//主函数
			//1、初始化奖品信息
			document.getElementById("m_p1").innerHTML=init_fp;
			document.getElementById("m_p2").innerHTML=init_sp;
			document.getElementById("m_p3").innerHTML=init_tp;
            //2、创建相机
            camera = new THREE.PerspectiveCamera(40, window.innerWidth / window.innerHeight, 1, 10000);
            camera.position.z = 3000;
            //创建场景
            scene = new THREE.Scene();
            //渲染
            renderer = new THREE.CSS3DRenderer();
            renderer.setSize(window.innerWidth, window.innerHeight);
            renderer.domElement.style.position = 'absolute';
			
			//>在<div comtainer>容器中添加<canvas>
            document.getElementById('container').appendChild(renderer.domElement);
            // 鼠标控制
            controls = new THREE.TrackballControls(camera, renderer.domElement);
            controls.rotateSpeed = 0.5;
            controls.minDistance = 500;
            controls.maxDistance = 6000;
            controls.addEventListener('change', render);
            //开始动画，从中心爆炸，仅在Y轴旋转
            startAnimate(targets.bang, true);
            animate();
			startLottery(true);
        })();//自调用,开始执行

        //添加键盘监听
        window.addEventListener('keydown', function(e) {
            switch (e.keyCode) {
                case 76: //l,幸运奖
                    startLottery(true);
                    break;
                case 83: //s,开始
                    startLottery(false);
                    break;
                case 32: //space,停并抽取人员
                    randomStaff();
                    lottery();
                    break;
                default:
                    break;
            }

        });
        window.addEventListener('resize', onWindowResize, false);//当窗口大小发生改变

        function startAnimate(target, rotateYOnly) {//开始动画
            init(target);
            if (rotateYOnly) {
                scene.rotation.x = 0;
                scene.rotation.z = 0;
                rotateY();
            } else {
                if (ry) cancelAnimationFrame(ry);
                rotateScene();
            }
        }

        //初始换场景
        function init(target) {
            if (objects.length) {
                transform(objects, target, 2000);
                return;
            }
            getStaff();
            // table
            for (var i = 0, l = staffs.length; i < l; i++) {
                let element = createStaffDom('element', staffs[i].NAME, staffs[i].NUM);
                var object = new THREE.CSS3DObject(element);//先创建好卡片，再添加到object中
                object.position.x = Math.random() * 4000 - 2000;
                object.position.y = Math.random() * 4000 - 2000;
                object.position.z = Math.random() * 4000 - 2000;
                scene.add(object);//场景中添加对象

                objects.push(object);//存放object

                // 表格需要坐标进行排序的
                var to = new THREE.Object3D();
                to.position.x = (staffs[i].p_x * 140) - 1330;
                to.position.y = -(staffs[i].p_y * 180) + 990;
                targets.table.push(to);

                //big bang
                var bo = new THREE.Object3D();//创建3D物体
                bo.position.x = object.position.x + (Math.random() * 2 - 1) * 2000;
                bo.position.y = object.position.y + (Math.random() * 2 - 1) * 1000;
                bo.position.z = object.position.z + (Math.random() * 2 - 1) * 2000;
                bo.rotation.x = (Math.random() * 2 - 1) * Math.PI;
                bo.rotation.y = (Math.random() * 2 - 1) * Math.PI;
                bo.rotation.z = (Math.random() * 2 - 1) * Math.PI;
                targets.bang.push(bo);
            }//for

            // sphere
            var vector = new THREE.Vector3();
            var spherical = new THREE.Spherical();//球体
            for (var i = 0, l = objects.length; i < l; i++) {
                var phi = Math.acos(-1 + (2 * i) / l);
                var theta = Math.sqrt(l * Math.PI) * phi;
                var object = new THREE.Object3D();
                spherical.set(800, phi, theta);
                object.position.setFromSpherical(spherical);
                vector.copy(object.position).multiplyScalar(2);
                object.lookAt(vector);
                targets.sphere.push(object);
            }

            // helix螺旋
            var vector = new THREE.Vector3();
            var cylindrical = new THREE.Cylindrical();
            for (var i = 0, l = objects.length; i < l; i++) {
                var theta = i * 0.225 + Math.PI;
                var y = -(i * 8) + 450;
                var object = new THREE.Object3D();
                // 参数一 圈的大小 参数二 左右间距 参数三 上下间距
                cylindrical.set(800, theta, y);
                object.position.setFromCylindrical(cylindrical);
                vector.x = object.position.x * 2;
                vector.y = object.position.y;
                vector.z = object.position.z * 2;
                object.lookAt(vector);
                targets.helix.push(object);
            }

            // grid
            for (var i = 0; i < objects.length; i++) {
                var object = new THREE.Object3D();
                object.position.x = ((i % 5) * 400) - 800; // 400 图片的左右间距  800 x轴中心点
                object.position.y = (-(Math.floor(i / 5) % 5) * 300) + 500; // 500 y轴中心点
                object.position.z = (Math.floor(i / 25)) * 200 - 800; // 300调整 片间距 800z轴中心点
                targets.grid.push(object);
            }

            transform(objects, target || targets.helix, 2000);
        }

        function startLottery(lucky) {
            if (lucky)
                startAnimate(targets.helix, lucky);
            else
                startAnimate(targets.sphere, lucky);
            $({
                s: 1
            }).animate({
                s: 0
            }, {
                duration: 600,
                step: function(n) {
                    $('.wins').css({
                        opacity: n,
                        transform: `scale(${n*2}) rotateY(${n*360*2}deg)`
                    });
                },
                complete: function() {
                    $('.wins').remove();
                }
            });
        }

        function lottery() {//开始抽奖，选中奖品或未中奖
			if(times<=0){
				alert("今日抽奖次数已用完，明日再来吧");
				startLottery(false);	
				return;
			}
			times--;
			var prizeLevel=probability(arrp);
			var chr;
			var m_p;//当前奖项数量
			var m_ps;//进度条的div
			var ini;//初始奖品数量
			switch(prizeLevel)
			{
				case 1:chr="一等奖";
					m_p=document.getElementById("m_p1");
					m_ps=document.getElementById("m_ps1");
					ini=init_fp;
					break;
				case 2:chr="二等奖";
					m_p=document.getElementById("m_p2");
					m_ps=document.getElementById("m_ps2");
					ini=init_sp;
					break;
				case 3:chr="三等奖";
					m_p=document.getElementById("m_p3");
					m_ps=document.getElementById("m_ps3");
					ini=init_tp;
					break;
				case 4:chr="没得奖";m_p=0;m_ps=0;break;
					
			}
            var w = window.innerWidth / 2 - 120 * 1.5;
            vibrate();
            bigBang();
			startLottery(false);	
            var t = setTimeout(function() {
				
                var element = createStaffDom('wins',chr, wins[0].NUM, 0.7);
                document.getElementById('container').appendChild(element);
                $({
                    s: 0
                }).animate({
                    s: 1
                }, {
                    duration: 1500,
                    easing: 'swing',
                    step: function(n) {
                        $(element).css({
                            opacity: `${n}`,
                            transform: `scale(${n*2}) rotateY(${n*360*2}deg)`
                        });
                    }
                });
                wins = [];//清空中奖者
				if(m_p!==0){//更改当前奖品数量
					var pNum = parseInt(m_p.innerHTML)-1;
					m_p.innerText=pNum;
					m_ps.setAttribute("style","width:"+100*pNum/ini+"%;")
				}
                clearTimeout(t);
            }, 2000);
        }

        function getStaff() {//获取人员名单及初始化位置
            for (var i = 0, l = 10; i < l; i++) {
                for (let j = 0, m = 20; j < m; ++j) {
                    let num = i * m + j;
                    staffs.push({
                        NAME: "",
                        NUM: num
                    });
                    staffs[num].p_x = (num) % 15 + 1;
                    staffs[num].p_y = Math.floor((num) / 15) + 1;
                }
            }
        }

        function randomStaff() {//随机产生一名中奖员工,可设置中奖概率
            let r = randomNum(0, staffs.length - 1);
			if(staffs[r].NAME==""){
				staffs[r].NAME="谢谢惠顾";
			}
            wins.push(staffs[r]);
			staffs.splice(r, 1);//从staffs中删除index=r
			if(staffs[r].NAME=="谢谢惠顾"){
				staffs[r].NAME="";
			}
        }

        function randomNum(a, b) {//获取a~b之间的一个随机数
            switch (arguments.length) {
                case 1:
                    return parseInt(Math.random() * a + 1);
                case 2:
                    return parseInt(Math.random() * (b - a + 1) + a);
                default:
                    return 0;
            }
        }

        function createStaffDom(className, prizeName, id, opacity) {//创建卡片信息
            opacity = opacity || Math.random() * 0.5 + 0.25;
            var element = document.createElement('div');//创建<div>
            element.className = className;
            element.style.backgroundColor = 'rgba(166,0,3,' + opacity + ')';

            var front = document.createElement('div');//卡片前面
            front.className = 'front';
            element.appendChild(front);

            var back = document.createElement('div');//中奖卡片背面
            back.className = 'back';
            element.appendChild(back);

            var p = document.createElement('p');//中奖的段落
            p.innerText = prizeName;
            front.appendChild(p);

            return element;
        }

        //图形变换
        function transform(origin, targets, duration) {//源，目标，持续时间
            TWEEN.removeAll();
            for (var i = 0; i < origin.length; i++) {
                var object = origin[i];
                var target = targets[i];
                new TWEEN.Tween(object.position)
                    .to({
                        x: target.position.x,
                        y: target.position.y,
                        z: target.position.z
                    }, Math.random() * duration + duration)
                    .easing(TWEEN.Easing.Exponential.InOut)
                    .start();
                new TWEEN.Tween(object.rotation)
                    .to({
                        x: target.rotation.x,
                        y: target.rotation.y,
                        z: target.rotation.z
                    }, Math.random() * duration + duration)
                    .easing(TWEEN.Easing.Exponential.InOut)
                    .start();
                new TWEEN.Tween(object.scale).to({
                        x: target.scale.x,
                        y: target.scale.y,
                        z: target.scale.z
                    }, Math.random() * duration + duration)
                    .easing(TWEEN.Easing.Exponential.InOut)
                    .start();
            }
            new TWEEN.Tween(this)
                .to({}, duration * 2)
                .onUpdate(render)
                .start();
        }

        //监听窗体大小变化
        function onWindowResize() {
            if (!camera) return;
            camera.aspect = window.innerWidth / window.innerHeight;
            camera.updateProjectionMatrix();
            renderer.setSize(window.innerWidth, window.innerHeight);
            render();
        }

        //维持球形
        function animate() {
            requestAnimationFrame(animate);
            TWEEN.update();
            controls.update();
            // 渲染循环
            render();
        }

        //rotate the scene
        function rotateScene() {//场景的旋转
            // 让场景通过x轴或者y轴旋转  & z
            scene.rotation.x += 0.05;
            scene.rotation.y += 0.2;
            scene.rotation.z += 0.01;
            rs = requestAnimationFrame(rotateScene);
            controls.update();
            // 渲染循环
            render();
        }

        //rotate the scene
        function rotateY() {
            // 让场景通过x轴或者y轴旋转  & z
            scene.rotation.y -= 0.001;
            ry = requestAnimationFrame(rotateY);
            controls.update();
            // 渲染循环
            render();
        }

        //抖动效果
        function vibrate() {
            scene.traverse(function(e) {
                e.rotation.x += ((Math.random() * 0.1 + 0.1) * (Math.random() >= 0.5 ? 1 : -1));
                e.rotation.y += ((Math.random() * 0.1 + 0.1) * (Math.random() >= 0.5 ? 1 : -1));
                e.rotation.z += ((Math.random() * 0.1 + 0.1) * (Math.random() >= 0.5 ? 1 : -1));
            });
            vi = requestAnimationFrame(vibrate);
        }

        //球形爆炸
        function bigBang() {
            let t1 = setTimeout(function() {
                cancelAnimationFrame(vi);
                cancelAnimationFrame(rs);
                transform(objects, targets.bang, 500);
                let t2 = setTimeout(function() {
                    rotateY();
                    clearTimeout(t2);
                }, 1000)
                clearTimeout(t1);
            }, 1500)

        }

        function render() {
            renderer.render(scene, camera);
        }
		function probability(m_arr)
		{
			let sum=0;
			let flag=0;
			for(var i=0;i<m_arr.length;i++){
				sum+=m_arr[i];
			}
			for(var i=0;i<m_arr;i++){
				m_arr[i]=m_arr[i]/sum;
			}
			sum = 0;
			var prob=randomNum(1,1000);
			for(var i=0;i<m_arr.length;i++){
				sum+=m_arr[i];
				flag = 1000*sum;
				if(prob<flag)
					return (i+1);
			}
		}
        //音乐设置
        var music = document.getElementById('m_music'),
            music_bool = true,
            music_init = function() {
                if (music_bool) {
                    music.play();
                    music_bool = false;                    
                } else {
                   music.pause();
				   music_bool = true;
                }
            }
		//获取随机颜色
		function getRandomColor(){
			return '#' + (function(h){
			return new Array(7 - h.length).join("0") + h
			})((Math.random() * 0x1000000 << 0).toString(16))
		}
		function changeState(m_id){
			if(m_id=="start"){
                randomStaff();
                lottery();
			}else{
				startLottery(true);//helix
			}
		}