function loading(){	
	clubCheck(0);
	
}
function htmlString(id,title,topic_id,topic_title,joinNum,image){
	var html="";
	
	html+='<div class="clubItem" onclick="window.LS.goClubInfo('+id+');">'
	html+='<div class="clubHeaderImg"><img src="'+image+'"></div>';
	html+='<div class="clubInfo">';
	html+='<div class="clubTitle"><div class="clubTitleWord">'+title+'</div><div class="clubTitleNum">'+joinNum+'人参与</div></div>';
	html+='<div class="clubConIngo">'+topic_title+'</div>';
	html+='</div>';						
	html+='</div>';
	
	
	return html;
}
function moreHtml(){
	var html="";
    html+='<div class="itemMore">';
	html+='<a href="javascript:void(0);" onclick="window.LS.getMoreClubList();">查看各地俱乐部</a>';
	html+='</div>';
    $("#clubBox").append(html);
}

function htmlString2(topic_id,category,user_id,headicon,nickname,topic_title,images,replytotal,attenStatus,likeNum,LikeStatus,imgWidth,imgHeight){
	var html="";
	var imgMarginTop=-2;
	if(images!=undefined){
		imgMarginTop=marginTop(imgWidth,imgHeight);
	}
	html+='<div class="mainItem postsItem">';
	html+='<div class="mainItemTitle title2"></div>';
	html+='<div class="cardItem">';
	html+='<div class="cardItemBox">';
	html+='<div class="cardTitle"><div class="headerImg" onclick="window.LS.goUserHomePage('+user_id+')"><img src="'+headicon+'" /></div><div class="headerName" onclick="window.LS.goUserHomePage('+user_id+')">'+nickname+'</div>';
	if(attenStatus==1){
		/*html+='<div class="attentionBox acitve">已关注</div>';*/
	}else{
		html+='<div class="attentionBox" onclick="attentionClick($(this));window.LS.attention ('+user_id+')"></div>';/*关注*/
	}
	html+='</div>';
	html+='<div class="cardImg"  onclick="window.LS.goTopicInfo('+topic_id+','+category+')"><img src="'+images+'" style="margin-top:'+imgMarginTop+'px;"/></div>';
	html+='<div class="cardInfo">';
	html+='<div class="cardInfoTitle"  onclick="window.LS.goTopicInfo('+topic_id+','+category+')">'+topic_title+'</div>';
	html+='<div class="reviewNum">'+replytotal+'则评论 </div>';
	if(LikeStatus==1){
		html+='<div class="likeBox active"><span>'+likeNum+'</span></div>';
	}else{
		html+='<div class="likeBox" onclick="likeClick($(this));window.LS.like('+topic_id+');"><span>'+likeNum+'</span></div>';
	}
	html+='</div>';
	html+='</div>';
	html+='</div>';
	html+='</div>';
	
	return html;
}
function clubCheck(num){
	/*var userId=727681;*/
	var userId=window.LS.getUserId();
	imgShow=true;
	$.ajax({
		async: true,
		type:'post',
		url:'http://api.lis99.com/v4/club/officialClubList/'+num,
		dataType:'json',
		data:'user_id='+userId,
		timeout : 20000,
		success:function(msg){
			if(msg.status == 'OK'){
				var html="";
				var listsArr=msg.data.lists;
				$.each(listsArr,function(i){
					var id=listsArr[i].id;
					var title=listsArr[i].title;
					var topic_id=listsArr[i].topic_id;
					var topic_title=listsArr[i].topic_title;
					var joinNum=listsArr[i].joinNum;
					var image=listsArr[i].image;
					html+=htmlString(id,title,topic_id,topic_title,joinNum,image);
				});
				if(num==0){
					moreHtml();
				}
				$(".clubList").append(html);
				
			}else{
				
			}

		},
		complete : function(XMLHttpRequest,status){
			if(status=='timeout'){
				
			}
			$(".mainLoadingMain").hide();
			cardCheck(0);
		}
	});
	
}
var page=-1;
var noPage=false;
var imgShow=false;
function cardCheck(num){
	/*var userId=727681;*/
	var userId=window.LS.getUserId();
	num=page+1;
	$.ajax({
		async: true,
		type:'post',
		url:'http://api.lis99.com/v4/club/hottopics/'+num,
		data:'user_id='+userId,
		dataType:'json',
		timeout : 20000,
		success:function(msg){
			if(msg.status == 'OK'){
				page=page+1;
				var html="";
				var listsArr=msg.data.topicslist;
				$.each(listsArr,function(i){
					var topic_id=listsArr[i].topic_id
					var category=listsArr[i].category; //0:话题 1:活动 2:线上活动帖
					var user_id=listsArr[i].user_id
					var headicon=listsArr[i].headicon;
					var nickname=listsArr[i].nickname;
					
					var topic_title=listsArr[i].topic_title;
					var images=listsArr[i].images;
					
					var replytotal=listsArr[i].replytotal;
					var replytotal=listsArr[i].replytotal;
					var attenStatus=listsArr[i].attenStatus;
					var LikeStatus=listsArr[i].LikeStatus;
					var likeNum=listsArr[i].likeNum;
					
					var imgWidth=listsArr[i].width;
					var imgHeight=listsArr[i].height;
					
					html+=htmlString2(topic_id,category,user_id,headicon,nickname,topic_title,images,replytotal,attenStatus,likeNum,LikeStatus,imgWidth,imgHeight);
				});
				$(".mainContent").append(html);
				$(".postsItem").eq(0).addClass("postsItemFirst");
			}else{
				if(num==msg.data.totalpage){
					noPage=true;
					loadingImgNoHave("没有了");
				}
			}
	
		},
		complete : function(XMLHttpRequest,status){
			if(!noPage){
				imgShow=false;
			}else{
				imgShow=true;
			}
			
			if(status=='timeout'){
				
			}else if(status=='error'){
				
			}
			
		}
	});
	
	
	
}
function likeClick($thisObj){
	var nextTF=userIdHave();
	if(nextTF){
		$thisObj.addClass("active");
		$thisObj.attr("onclick","");
		var nowLikeNum=$thisObj.find("span").html();
		nowLikeNum=parseInt(nowLikeNum)+1;
		$thisObj.find("span").html(nowLikeNum);	
	}
	
}
function attentionClick($thisObj){
	var nextTF=userIdHave();
	if(nextTF){
		$thisObj.hide();
	}
}
/*var userIdSend=1111111;*/
var userIdSend=window.LS.getUserId();
function userIdHave(){
	if(userIdSend==undefined || userIdSend=="" || userIdSend==null){
		return false;
	}
	return true
}
function sendUserId(user_id){
	userIdSend=user_id;
}

//滚动加载

window.onscroll=function(){
	if(!imgShow){
		var scrollBottom=ifbottom();
		if(scrollBottom){
			loadingImgShow();
			cardCheck(0);
		}
	}
}
			function ifbottom(){
				 var scrollTop = 0;  
				 var clientHeight = 0;
				 var scrollHeight = 0;
				 if (document.documentElement && document.documentElement.scrollTop) {
				 	scrollTop = document.documentElement.scrollTop;
				 } else if (document.body) {
				 	scrollTop = document.body.scrollTop;
				 }
				 if (document.body.clientHeight && document.documentElement.clientHeight) {
				 	clientHeight = (document.body.clientHeight < document.documentElement.clientHeight) ? document.body.clientHeight: document.documentElement.clientHeight;
				 } else {
				 	clientHeight = (document.body.clientHeight > document.documentElement.clientHeight) ? document.body.clientHeight: document.documentElement.clientHeight;
				 }
				 scrollHeight = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
				 scrollHeight=scrollHeight-120;
				 
				 if (scrollTop + clientHeight >= scrollHeight) {
				 	return true;
				 } else {
				 	return false;
				 }
			}
			function loadingImgShow(){
				$(".wordClass").hide();
				var loadingImg=document.getElementById("loadingImgBox");
				loadingImg.style.display="block";
				imgShow=true;
			}
			function loadingImgHide(){
				var loadingImg=document.getElementById("loadingImgBox");
				loadingImg.style.display="none";
				
			}
			function loadingImgNoHave(wordObj){
				loadingImgHide();
				$(".wordClass").html(wordObj);
				$(".loadingImgClass").addClass("loadingImgNoHave");
				$(".wordClass").show();
			}

function marginTop(imgWidth,imgHeight){
	var windowWidth;
	if (document.body.clientHeight && document.documentElement.clientHeight) {
		windowWidth = (document.body.clientWidth < document.documentElement.clientWidth) ? document.body.clientWidth: document.documentElement.clientWidth;
	} else {
		windowWidth = (document.body.clientWidth > document.documentElement.clientWidth) ? document.body.clientWidth: document.documentElement.clientWidth;
	}
	var widthProportion=parseFloat(windowWidth)/parseFloat(imgWidth);
	imgHeight=parseInt(imgHeight*widthProportion);
	var marginTop=(imgHeight)-230;
	marginTop=marginTop/2;
	marginTop= - marginTop;
	return marginTop;
}