var globalData = {
	server:"http://localhost:8666/iclock/",
	exit(){
		    window.sessionStorage.removeItem("token");	
			window.sessionStorage.removeItem("name");
			window.sessionStorage.removeItem("uid");
			window.location.href=globalData.pre+"index.html";
	}
};

