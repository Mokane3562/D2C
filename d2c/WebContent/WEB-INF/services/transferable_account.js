app.factory('transferable_account', [function(){
	return function(userName, password, firstName, lastName, createTime){
		return {
			'userName': userName,
			'password': password,
			'firstName': firstName,
			'lastName': lastName,
			'createTime': createTime
		};
	};
}]);