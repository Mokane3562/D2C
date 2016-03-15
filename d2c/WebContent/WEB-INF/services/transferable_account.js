app.factory('transferable_account', [function(){
	return function(username, password, fname, lname, create_time){
		return {
			'username': username,
			'password': password,
			'fname': fname,
			'lname': lname,
			'create_time': create_time
		};
	};
}]);