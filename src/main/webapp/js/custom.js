$(function(){
	get_user_list();
});

var get_user_list = function() {
	$.ajax({
		type:"get",
		url:'/do/admin/get_user_list',
		dataType:"json",
		success:function(data){
			$("#user_list").empty();
			for(var i = 0; i < data.length; i++){
				if(data[i].online){
					$("#user_list").append("<option value='" + data[i].user + "'>☺&nbsp;" + data[i].user + "</option>");
				}else{
					$("#user_list").append("<option value='" + data[i].user + "'>☻&nbsp;" + data[i].user + "</option>");
				}
			}
		}
	});
};

$(document).on("click", "#sendbtn", function(e) {
	var opts = {
		lines: 13, // The number of lines to draw
		length: 11, // The length of each line
		width: 5, // The line thickness
		radius: 17, // The radius of the inner circle
		corners: 1, // Corner roundness (0..1)
		rotate: 0, // The rotation offset
		color: '#FFF', // #rgb or #rrggbb
		speed: 1, // Rounds per second
		trail: 60, // Afterglow percentage
		shadow: false, // Whether to render a shadow
		hwaccel: false, // Whether to use hardware acceleration
		className: 'spinner', // The CSS class to assign to the spinner
		zIndex: 2e9, // The z-index (defaults to 2000000000)
		top: 'auto', // Top position relative to parent in px
		left: 'auto' // Left position relative to parent in px
	};
	var target = document.createElement("div");
	document.body.appendChild(target);
	var spinner = new Spinner(opts).spin(target);
	var overlay = iosOverlay({
		text: "Sending",
		spinner: spinner
	});

	send(overlay);
	
	return false;
});

var send = function(overlay){
	var user = $("#user_list").val();
	var shell = $("#message").val();
	var cmd_type = parseInt($("#cmd_type").val());
	var args = $("#args").val();
	var message = { CmdType: cmd_type, Message : shell };
	if(args != ''){
		message.Arg = new Array();
		message.Arg[0] = args;
	}
	$.ajax({
		type:"post",
		url:'/do/' + user + '/push',
		dataType:"json",
		data: { "data" : JSON.stringify(message)},
		complete : function(){
			overlay.update({
				icon: "img/check.png",
				text: "Success"
			});
			window.setTimeout(function() {
				overlay.hide();
			}, 1500);
		}
	});

};
